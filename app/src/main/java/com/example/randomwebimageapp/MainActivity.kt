package com.example.randomwebimageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

// An Activity is a view (Screen) with a user Interface
class MainActivity : AppCompatActivity() {

    //The initialization function, what happens after we load
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //call the super class
        setContentView(R.layout.activity_main) // sets up the user interface

        //we need to pass it a context, the context being this
        val internetConnected: InternetConnected = InternetConnected(this)


        //now lets check if we are connected to the internet and then display an error message
        if (!internetConnected.checkNetworkConnectivity()){
            AlertDialog.Builder(this)
                .setTitle(R.string.message_title)
                .setMessage(R.string.message_text)
                .setNegativeButton(R.string.quit){_, _ -> finishAffinity()}
                .show()
        } else {
            //now we can write code where we know the internet is connected

        }
    }
}