package com.example.randomwebimageapp

import android.app.Application
import android.content.Context

/* 
 * Created by Ravi Rachamalla on November 06, 2020
*/
class TheApp : Application(){
    override fun onCreate(){
        super.onCreate()
        context = applicationContext
    }

    // an unnamed companion object will be available to this object
    companion object {
        // late init means this variable will be initialized later, if this is used before
        // init the application will crash, lateinit only works on vars
        lateinit var context: Context
            private set
    }

}