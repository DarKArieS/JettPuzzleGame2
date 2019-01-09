package com.game.aries.jettpuzzlegame2.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.Button

class BlinkingButton: Button {

    constructor(context: Context, attrs: AttributeSet, defStyle : Int)  : super(context, attrs, defStyle)
    {
        println("Hi")
    }


}