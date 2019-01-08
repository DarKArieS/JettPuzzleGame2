package com.game.aries.jettpuzzlegame2.models

/*
 * Main Model (global object)
 *
 * This class stores and refreshes the information used in this game.
 *
 */

object MainModel {
    var isLoading = false
    var isTransiting = false
    var isCommunicating = false

    enum class MainActivityLifeCycle {
        RUNNING, PAUSE
    }

    var mainActivityStatus = MainActivityLifeCycle.RUNNING

    fun checkIsLoading():Boolean{
        return (isLoading || isCommunicating || isTransiting)
    }

    init{
        // Todo: get information from SP/SQL/Backend

    }

    fun fakeCommunicate(callBack: ()->Unit){
        FakeCommunication.fakeCommunicate(callBack)
    }

}