package com.example.randomwebimageapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.randomwebimageapp.databinding.ActivityMainBinding

/*
 * Created by Ravi Rachamalla on November 06, 2020
*/

// An Activity is a view (Screen) with a user Interface
class MainActivity : AppCompatActivity() {
    private val glideImage = GlideImage()

    //The initialization function, what happens after we load
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //call the super class
//        setContentView(R.layout.activity_main) // sets up the user interface

        // using the view binding
        // making our UI available
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            // set the click listener to the get image button
            binding.getImageButton.setOnClickListener(){
                glideImage.loadGlideImage(binding.imageView1, this, binding.progressBar)
            }

            // now lets run the get image button for the initial run
            binding.getImageButton.callOnClick()
        }
    }

    // A Private method to check the internet connection that will return false if not connected
//    private fun checkNetworkConnectivity(): Boolean {
//        return false
//    }
}