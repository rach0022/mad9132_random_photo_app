package com.example.randomwebimageapp

import android.content.Context
import android.gesture.Gesture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GestureDetectorCompat
import com.example.randomwebimageapp.databinding.ActivityMainBinding

/*
 * Created by Ravi Rachamalla on November 06, 2020
*/

// region MainActivity Class
// An Activity is a view (Screen) with a user Interface
class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    // region MainActivity Properties
    private val glideImage = GlideImage()

    // a property to detect gestures for the gesture listeners, we set it to null because some devices
    // may not have gestures
    var gestureDetector: GestureDetectorCompat? = null
    // endregion

    // region MainActivity Methods
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

            // clear out the cache
            glideImage.emptyCache(this)

            // set up the listeners for gesture inputs by the user
            // hook up the detector for double tap inputs
            gestureDetector = GestureDetectorCompat(this, this)
            gestureDetector?.setOnDoubleTapListener(this)


            //now we can write code where we know the internet is connected
            // set the click listener to the get image button
            binding.getImageButton.setOnClickListener(){
                glideImage.loadGlideImage(binding.imageView1, this, binding.progressBar)
            }

            // now lets run the get image button for the initial run
            binding.getImageButton.callOnClick()
        }
    }

    // override the onTouchEvent function to pass the touch to the gestureDector
    // to determine what type of gesture the touch was involved in
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        this.gestureDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    // extension method that contains a single string parameter called message
    // which will contain the text for the Toast.makeText method with a short notification time
    fun Context.toast(message: String){
        Toast.makeText(TheApp.context, message, Toast.LENGTH_SHORT).show()
    }

    //region Gesture Methods

    override fun onDown(p0: MotionEvent?): Boolean {
        toast("onDown")
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        TODO("Not yet implemented")
    }

    override fun onLongPress(p0: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    // endregion

    // A Private method to check the internet connection that will return false if not connected
//    private fun checkNetworkConnectivity(): Boolean {
//        return false
//    }
    // endregion
}

// endregion