package com.example.randomwebimageapp

import android.content.Context
import android.gesture.Gesture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
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
    private var gestureDetector: GestureDetectorCompat? = null

    // the binding property, we set it to the main activity in the oncreate method
    private lateinit var binding: ActivityMainBinding

    // a variable to check if our device is in full screen or not
    private var showingSystemUI = true

    // endregion

    // region MainActivity Methods
    //The initialization function, what happens after we load
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //call the super class
//        setContentView(R.layout.activity_main) // sets up the user interface

        // using the view binding
        // making our UI available
        this.binding = ActivityMainBinding.inflate(layoutInflater)
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


            // load an image on startup
            glideImage.loadGlideImage(binding.imageView1, this, binding.progressBar)

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

    //region Gesture Event Methods

    override fun onDown(p0: MotionEvent?): Boolean {
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return true
    }


    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
        glideImage.emptyCache(this)
        this.toast("Cleared cache")
    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        // We want the user to swipe from left to right and display the next image
        // if the x value of is positive, or if the x value 2 is less then the x value of the starting point
        // we can also determine velocity but we
        // p0!!. the !!. is a cheat to bypass if the value is null
        if (p0.x < p1.x){
//        if((p1.x - p0.x) < 0)
            glideImage.loadGlideImage(binding.imageView1, this, binding.progressBar)
        }
        return true
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        return true
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        this.showingSystemUI = if(showingSystemUI) {
            hideSystemUI()
            false
        } else {
            showSystemUI()
            true
        }
        return true
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        return true
    }

    // endregion

    // region for Hide/ Show System UI
    private fun hideSystemUI(){
        // bitwise operation of doing an or operation on each
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI(){
        window.decorView.systemUiVisibility = ( View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
    // endregion
    // endregion
}

// endregion