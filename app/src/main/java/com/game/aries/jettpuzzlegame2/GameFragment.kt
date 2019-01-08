package com.game.aries.jettpuzzlegame2


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.game.aries.jettpuzzlegame2.models.GameTimer
import kotlinx.android.synthetic.main.fragment_game.view.*
import android.os.Handler
import com.game.aries.jettpuzzlegame2.animclass.ProgressBarAnimator

class GameFragment : Fragment(), GameTimer.TimerBarController {
    private lateinit var rootView:View
    private lateinit var timer : GameTimer
    private val handlerUI = Handler()
    private lateinit var timeBarAnimator: ProgressBarAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_game, container, false)

        (activity as MainActivity).supportActionBar?.hide()

        timeBarAnimator = ProgressBarAnimator(rootView.timerProgressBar, handlerUI)

        // setup timer
        val timeThreshold = 40f
        timer = GameTimer(this)
        timer.secondsCount = timeThreshold
        timer.maxTimeInSeconds = timeThreshold
        rootView.timerProgressBar.max = (timeThreshold*100).toInt() // timer bar resolution: 0.01 second
        rootView.timerProgressBar.progress = (timeThreshold*100).toInt()
        rootView.timerProgressBar.secondaryProgress = (timeThreshold*100).toInt()

        timer.startTimer()

        // setup button experiment
        rootView.gameOption1.setOnClickListener {
            addTime(3f)
        }
        rootView.gameOption2.setOnClickListener {
            timer.stopTimer()
        }

        rootView.gameOption3.setOnClickListener {
//            addTime(-3f)
            minusTime(3f)
        }

        rootView.gameOption4.setOnClickListener {
            //            addTime(-3f)
            timer.startTimer()
        }

        //
        rootView.gameOption1.background

        return rootView
    }

    private fun initAnimator(){


    }

    private fun addTime(second:Float){
        timer.secondsCount+=second
        if (timer.secondsCount>timer.maxTimeInSeconds) timer.secondsCount = timer.maxTimeInSeconds

        // animation
        timeBarAnimator.progressShining(false,500,0.7f)
        timeBarAnimator.animateUpdatingDelayed(500)
    }

    private fun minusTime(second:Float){
        timer.secondsCount-=second

        // animation
        timeBarAnimator.progressShining(true)
        timeBarAnimator.animateUpdatingDelayed(0)
    }

    override fun timerOnUpdate() {
        timeBarAnimator.update(-1,(timer.secondsCount*100).toInt())

        val strTmp="%.2f".format(timer.secondsCount)
        rootView.timeTextView.text = strTmp
    }

    override fun timesUp() {
        rootView.timeTextView.text = "time's up"
    }

    override fun onDestroy() {
        (activity as MainActivity).supportActionBar?.show()
        super.onDestroy()
    }
}
