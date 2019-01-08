package com.game.aries.jettpuzzlegame2.animclass

import android.content.res.Resources
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.LayerDrawable
import android.os.Handler
import android.widget.ProgressBar

class ProgressBarAnimator(private val progressBar: ProgressBar, private val handlerUI: Handler) {
    private var isAnimatingUpdatingDelayed = false
    private var isAnimatingShining = false
    private var mTrueProgress = 0

    fun animateUpdatingDelayed(delayTime: Long){
        if (!isAnimatingUpdatingDelayed){
            isAnimatingUpdatingDelayed = true
            Thread{
                Thread.sleep(delayTime)
                for (i in 1..9){
                    progressBar.progress =
                            (progressBar.progress +
                                    (progressBar.secondaryProgress - progressBar.progress)* i /10)
                    Thread.sleep(50)
                }
                progressBar.progress = progressBar.secondaryProgress
                isAnimatingUpdatingDelayed = false
            }.start()
        }
    }

    private var animMaxStepShining = 20
    private var animStepShining = 0
    private val progressBarDrawable = (progressBar.progressDrawable as LayerDrawable)
        .findDrawableByLayerId(Resources.getSystem().getIdentifier("progress","id","android"))

    private var mIsDarker = true
    private var mShiningTime : Long = 1000
    private var mShiningDegree = 0.25f

    /*
    isDarker: choose your shining type
    shiningDegree: 0f ~ 0.5f
    for shining light,
    */
    fun progressShining(isDarker:Boolean = false, shiningTime: Long = 1000, shiningDegree: Float = 0.25f){
        animStepShining = 0
        mShiningTime = shiningTime/20
        mShiningDegree = shiningDegree
        mIsDarker = isDarker
        if(!isAnimatingShining){
            isAnimatingShining = true
            Thread{
                while(animStepShining<animMaxStepShining){
                    val step : Float = when(mIsDarker){
                        true-> when(animMaxStepShining < 3){
                            true -> 1 - (mShiningDegree * (animStepShining.toFloat())/3)
                            false -> 1 - mShiningDegree + (mShiningDegree * ((animStepShining-3).toFloat())/17)
                        }
                        false-> when(animMaxStepShining < 3){
                            true -> 1 + (mShiningDegree * (animStepShining.toFloat())/3)
                            false -> 1 + mShiningDegree - (mShiningDegree * ((animStepShining-3).toFloat())/17)
                        }
                    }
                    println(step)

                    val colorFilter = ColorMatrixColorFilter(
                        floatArrayOf(
                            1f*step,0f,0f,0f,0f,
                            0f,1f*step,0f,0f,0f,
                            0f,0f,1f*step,0f,0f,
                            0f,0f,0f,1f,0f
                        )
                    )
                    // To avoid thread conflicting OxO
                    handlerUI.post{
                        //progressBar.progressDrawable.
                        //setColorFilter(Color.argb(255*(10-animCountTimeBarAnimatingDecrease)/10,255,255,255),porterDuffMode)
                        //progressMask.setColorFilter(Color.argb(255*(10-animCountTimeBarAnimatingDecrease)/10,255,50,50),porterDuffMode)
                        progressBarDrawable.colorFilter = colorFilter
                    }
                    animStepShining ++
                    Thread.sleep(mShiningTime)
                }

                if(animStepShining>=animMaxStepShining){
                    progressBarDrawable.clearColorFilter()
                    isAnimatingShining = false
                }
            }.start()
        }
    }

    fun update(progressIncrement: Int, trueProgress: Int){
        mTrueProgress = trueProgress
        if(isAnimatingUpdatingDelayed) progressBar.incrementProgressBy(progressIncrement)
        else progressBar.progress = trueProgress
        progressBar.secondaryProgress = trueProgress
    }

}