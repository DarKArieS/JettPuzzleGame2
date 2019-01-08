package com.game.aries.jettpuzzlegame2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {

    private var onPause = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Thread{
            //Todo: Do first time data updating here!
            Thread.sleep(500)
            while(onPause){
                Thread.sleep(500)
            }
            this.runOnUiThread {
                val act2 = Intent(this, MainActivity::class.java)
                startActivity(act2)
                // remove this activity from back stack
                this.finish()
            }
        }.start()
    }
    // lock back button when loading
    override fun onBackPressed() {}

    override fun onPause() {
        onPause = true
        super.onPause()
    }

    override fun onResume() {
        onPause = false
        super.onResume()
    }
}
