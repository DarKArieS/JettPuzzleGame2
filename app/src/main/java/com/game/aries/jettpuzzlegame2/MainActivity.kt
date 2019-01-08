package com.game.aries.jettpuzzlegame2

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import androidx.navigation.findNavController
import com.game.aries.jettpuzzlegame2.models.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading_layout.view.*
import android.view.ViewGroup



class MainActivity : AppCompatActivity() {
    lateinit var vg : ViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAnimator()
        vg = window.decorView.rootView as ViewGroup
    }

    lateinit var loadingView: View
    private lateinit var transitionAnimEnter: ObjectAnimator
    private lateinit var transitionAnimExit: ObjectAnimator
    private fun initAnimator(){
        loadingView = this.layoutInflater.inflate(R.layout.loading_layout, null)

        transitionAnimEnter = ObjectAnimator
            .ofFloat(loadingView, "alpha", 0f, 1f)
            .setDuration(800)

        transitionAnimEnter.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                MainModel.isTransiting = true
                val params = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
                vg.addView(loadingView,params)
                (vg.loadingAnimator.background as AnimationDrawable).start()
            }
            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        transitionAnimExit = ObjectAnimator
            .ofFloat(loadingView, "alpha", 1f, 0f)
            .setDuration(800)

        transitionAnimExit.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                (vg.loadingAnimator.background as AnimationDrawable).stop()
                vg.removeView(loadingView)
                MainModel.isTransiting = false
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    fun showLoadingView(loadingString:String="Connecting"){
        if(loadingString!=null){
            loadingView.loadingTextView.text = loadingString
        }
        transitionAnimEnter.start()
    }

    fun hideLoadingView(){
        transitionAnimExit.start()
    }

    override fun onBackPressed() {

        // TodoDone: Bug: 在非最後一個fragment時按下返回，在動畫中使用多工鍵再切回app時會直接重啟app

        if(!MainModel.checkIsLoading()){
            val navController = this.findNavController(R.id.fragmentHost)
            val transition = Transition()

            if(navController.graph.startDestination == navController.currentDestination?.id){
                transition.transitionMessage = "Closing"
            }

            transition.navigation = {
                //if(!navController.navigateUp())super.onBackPressed()
                super.onBackPressed()
            }
            transition.communication={callback -> FakeCommunication.fakeCommunicate(callback,1000)}
            transition.commit(this)
        }
    }

    override fun onPause() {
        MainModel.mainActivityStatus = MainModel.MainActivityLifeCycle.PAUSE
        super.onPause()
        println("onPause")
    }

    override fun onResume() {
        MainModel.mainActivityStatus = MainModel.MainActivityLifeCycle.RUNNING
        super.onResume()
        println("onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("MainActivity on Destroy")
    }


}
