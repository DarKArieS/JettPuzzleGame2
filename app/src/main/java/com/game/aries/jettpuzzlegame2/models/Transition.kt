package com.game.aries.jettpuzzlegame2.models

import com.game.aries.jettpuzzlegame2.MainActivity
import android.os.Handler


class Transition {
    var navigation: (()->Unit)? = null
    var customCallback: (()->Unit)? = null
    var communication: (( callback: ()->Unit )->Unit)? = null
    var transitionMessage:String = "Connecting"
    val handler = Handler()

    fun commit(mainActivity: MainActivity){
        //TodoDone: Bug: communicating中onStop(例如按下多工鍵)，navigate會被吃掉
        val fullCallBack = {
            var handlerStatus = handler.post{
                if(customCallback!=null) customCallback!!()
            }

            //navigation navigate protecting
            while(MainModel.mainActivityStatus == MainModel.MainActivityLifeCycle.PAUSE){
                Thread.sleep(500)
            }

            handlerStatus = handler.post{
                if(navigation!=null) navigation!!()
                mainActivity.hideLoadingView()
            }
        }

        //mainActivity.runOnUiThread{mainActivity.showLoadingView()}
        mainActivity.showLoadingView(transitionMessage)
        communication!!(fullCallBack)
    }
}