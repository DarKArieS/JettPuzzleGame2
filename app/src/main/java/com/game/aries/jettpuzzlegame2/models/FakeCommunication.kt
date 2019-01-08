package com.game.aries.jettpuzzlegame2.models

class FakeCommunication{
    companion object {
        fun fakeCommunicate(callBack: ()->Unit, sleepTime :Long = 1000){
            MainModel.isCommunicating = true
            val myThread = Thread {
                Thread.sleep(sleepTime)
                println("FakeCommunication Done")
                callBack()
                MainModel.isCommunicating = false
            }
            myThread.start()
        }
    }
}