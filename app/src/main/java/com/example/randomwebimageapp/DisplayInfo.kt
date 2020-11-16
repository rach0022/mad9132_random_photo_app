package com.example.randomwebimageapp

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager



/*
* Created by Tony Davidson on November 15, 2020
*/

class DisplayInfo {

    var width = 0
        private set
    var height = 0
        private set

    var realWidth = 0
        private set
    var realHeight = 0
        private set

    var density = 0.0f
        private set

    var isPortrait = true
        get () {
            return isOrientationLandscape()
        }
        private set

    private fun isOrientationLandscape() : Boolean{
        refreshDisplayResolution()
        return (height > width)  // false if landscape
    }

    private fun refreshDisplayResolution(){

        val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics
        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels
        density = displayMetrics.density

        val windowManager = TheApp.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size) // gets real size in pixels
        realWidth = size.x
        realHeight = size.y
    }

    init{
        refreshDisplayResolution()
    }
}