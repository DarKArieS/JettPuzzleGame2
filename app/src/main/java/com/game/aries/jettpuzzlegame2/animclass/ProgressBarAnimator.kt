package com.game.aries.jettpuzzlegame2.animclass

import android.content.res.Resources
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.LayerDrawable
import android.os.Handler
import android.widget.ProgressBar

class ProgressBarAnimator(private val progressBar: ProgressBar, private val handlerUI: Handler) {
    private var isDelayUpdating = false
    private var isShining = false

    fun delayUpdateProgress(delayTime: Long){
        if (!isDelayUpdating){
            isDelayUpdating = true
            Thread{
                Thread.sleep(delayTime)
                for (i in 1..9){
                    progressBar.progress =
                            (progressBar.progress +
                                    (progressBar.secondaryProgress - progressBar.progress)* i /10)
                    Thread.sleep(50)
                }
                progressBar.progress = progressBar.secondaryProgress
                isDelayUpdating = false
            }.start()
        }
    }

    private var shiningAnimStepMax = 20
    private var shiningAnimStepNode = 3
    private var shiningAnimStep = 0
    private val progressBarDrawable = (progressBar.progressDrawable as LayerDrawable)
        .findDrawableByLayerId(Resources.getSystem().getIdentifier("progress","id","android"))

    // to update the running thread, we need them:
    private var mIsDarkerShining = true
    private var mShiningTime : Long = 1000
    private var mShiningDegree = 0.25f

    /*
    isDarker: choose your shining type
    shiningDegree: 0f ~ 0.5f
    for shining light,
    */
    fun progressShining(isDarkerShining:Boolean = false, shiningTime: Long = 1000, shiningDegree: Float = 0.25f){
        shiningAnimStep = 0
        mShiningTime = shiningTime/shiningAnimStepMax
        mShiningDegree = shiningDegree
        mIsDarkerShining = isDarkerShining
        if(!isShining){
            isShining = true
            Thread{
                while(shiningAnimStep<shiningAnimStepMax){
                    val step : Float = when(mIsDarkerShining){
                        true-> when(shiningAnimStepMax < shiningAnimStepNode){
                            true -> 1 - (mShiningDegree * (shiningAnimStep.toFloat())/shiningAnimStepNode)
                            false -> 1 - mShiningDegree +
                                    (mShiningDegree * ((shiningAnimStep-shiningAnimStepNode).toFloat())/(shiningAnimStepMax-shiningAnimStepNode))
                        }
                        false-> when(shiningAnimStepMax < shiningAnimStepNode){
                            true -> 1 + (mShiningDegree * (shiningAnimStep.toFloat())/shiningAnimStepNode)
                            false -> 1 + mShiningDegree -
                                    (mShiningDegree * ((shiningAnimStep-shiningAnimStepNode).toFloat())/(shiningAnimStepMax-shiningAnimStepNode))
                        }
                    }

                    val colorFilter = ColorMatrixColorFilter(
                        floatArrayOf(
                            1f*step,0f,0f,0f,0f,
                            0f,1f*step,0f,0f,0f,
                            0f,0f,1f*step,0f,0f,
                            0f,0f,0f,1f,0f
                        )
                    )
                    handlerUI.post{progressBarDrawable.colorFilter = colorFilter}
                    shiningAnimStep ++
                    Thread.sleep(mShiningTime)
                }

                if(shiningAnimStep>=shiningAnimStepMax){
                    handlerUI.post{progressBarDrawable.clearColorFilter()}
                    isShining = false
                }
            }.start()
        }
    }

    fun update(progressIncrement: Int, trueProgress: Int){
        if(isDelayUpdating) progressBar.incrementProgressBy(progressIncrement)
        else progressBar.progress = trueProgress
        progressBar.secondaryProgress = trueProgress
    }

}