package com.game.aries.jettpuzzlegame2.models

import android.os.Handler

class GameTimer(private var timerController: GameTimer.TimerController){
    interface TimerController{
        fun timerOnUpdate()
        fun timesUp()
    }

    var secondsCount = 40f
    var maxTimeInSeconds = secondsCount
    private var isStarted = false
    private var isStopCalled = false

    private lateinit var timerThread: Thread
    private var uiHandler = Handler()
    private lateinit var runnable: Runnable

    fun start() {
        if(!isStarted){
            isStarted = true

            timerThread = Thread{
                while(secondsCount>=0 && !isStopCalled){
                    Thread.sleep(10) // 0.01 second
                    secondsCount -= 0.01f
                }
                if(!isStopCalled){
                    uiHandler.post{
                        timerController.timesUp()
                        stop()
                    }
                }
            }

            runnable = Runnable {
                timerController.timerOnUpdate()
                uiHandler.postDelayed(runnable, 10)
            }

            isStopCalled = false
            timerThread.start()
            uiHandler.postDelayed(runnable, 10)
        }
    }

    fun stop() {
        isStopCalled = true
        isStarted = false
        uiHandler.removeCallbacks(runnable)
    }
}