package com.game.aries.jettpuzzlegame2.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.Button
import com.game.aries.jettpuzzlegame2.R

class BlinkingButton : Button {
    //ToDo 當按鈕有設定top,bottom,left,right時，蓋在上面的rect(0,0,w,h)會超出範圍

    private var mAttrs: AttributeSet
    private var autoRun = true

    constructor(context: Context, attrs: AttributeSet, defStyle : Int)  : super(context, attrs, defStyle){
        mAttrs = attrs
        init()
    }
    constructor(context: Context, attrs: AttributeSet)  : super(context, attrs){
        mAttrs = attrs
        init()
    }

    private fun init(){
        println("init")
        val attrString = context.theme.obtainStyledAttributes(mAttrs, R.styleable.BlinkingButton,0,0)
        autoRun = attrString.getBoolean(R.styleable.BlinkingButton_autoRunAnimator, true)

        initValueAnimator()
        mGradientPaint = Paint()
        mShiningPaint = Paint()
        mShiningPaint.style = Paint.Style.FILL

        if (autoRun){
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    mAnimating = true
                    valueAnimator.start()
                }
            })
        }
    }

    private var rect = Rect(0,0,1,1)
    private var mViewWidth = 0
    private var mViewHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        println("onMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = measuredWidth
        val h = measuredHeight
        rect.set(0,0,w,h)
    }

    private lateinit var mGradientPaint:Paint
    private lateinit var mShiningPaint:Paint
    private lateinit var mGradient: Shader
    private lateinit var mGradientMatrix: Matrix

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        println("onSizeChanged")
        super.onSizeChanged(w, h, oldw, oldh)
        if (mViewWidth == 0) {
            mViewWidth=measuredWidth
            mViewHeight=measuredHeight
            if (mViewWidth > 0) {
                setGradientPaint()
                rect.set(0,0,w,h)
            }
        }
    }

    private var mAnimating = false

    private fun setGradientPaint(){
        mGradientMatrix = Matrix()
        mGradientMatrix.setTranslate((-2 * mViewWidth).toFloat(), mViewHeight.toFloat())

        mGradient = LinearGradient(0f, 0f, mViewWidth.toFloat(), mViewHeight.toFloat(),
            intArrayOf(0x00ffffff, 0x55ffffff,0x00ffffff), floatArrayOf(0f, 0.9f, 1f), Shader.TileMode.CLAMP)
        mGradient.setLocalMatrix(mGradientMatrix)


        mGradientPaint.shader = mGradient
        mGradientPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)
    }

    var shiningColor = Color.rgb(255,255,255)
    var shiningAlpha = 0

    private fun setShiningMaskPaint(){
        mShiningPaint.color = Color.argb(shiningAlpha, Color.red(shiningColor), Color.green(shiningColor), Color.blue(shiningColor))
    }



    override fun onDraw(canvas: Canvas?) {
        //println("CustomView onDraw")
        super.onDraw(canvas)
        if (mAnimating && mGradientMatrix != null) {
            canvas?.drawRect(rect, mGradientPaint)
            if(shiningAlpha!=0){
                setShiningMaskPaint()
                canvas?.drawRect(rect, mShiningPaint)
            }
        }
    }

    private lateinit var valueAnimator: ValueAnimator
    private var mTranslateX = 0f
    private var mTranslateY = 0f


    private fun initValueAnimator(){
        valueAnimator = ValueAnimator.ofFloat(0f,1f)
                            .setDuration(6000)
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.addUpdateListener{
            val v  = it.animatedValue as Float
            mTranslateX = 4 * mViewWidth * v - mViewWidth * 2
            mTranslateY = mViewHeight * v
            mGradientMatrix.setTranslate(mTranslateX, mTranslateY)
            mGradient.setLocalMatrix(mGradientMatrix)
            invalidate()
        }
    }

    fun pause(){
        if (mAnimating) {
            mAnimating = false
            valueAnimator.cancel()
            invalidate()
        }
    }

    fun resume(){
        if (!mAnimating) {
            mAnimating = true
            initValueAnimator()
            valueAnimator.start()
        }
    }

}