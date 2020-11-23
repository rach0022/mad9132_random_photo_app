package com.example.randomwebimageapp

import AsyncStorageIO
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.gesture.Gesture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
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

    // for runtime permissions, can be any positive int value
    private val requestCode = 13

    // endregion

    // region MainActivity Methods
    // region LifeCycle Events
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
            //setup the permissions if we have an internet connection
            setupPermissions()

            // clear out the cache
            glideImage.emptyCache(this)

            // set up the listeners for gesture inputs by the user
            // hook up the detector for double tap inputs
            gestureDetector = GestureDetectorCompat(this, this)
            gestureDetector?.setOnDoubleTapListener(this)


            // load an image on startup
            val fileName = this.getString(R.string.last_image_file_name)
            val file = this.getFileStreamPath(fileName)
            if (file.exists()) {
                //file.delete() // use this for testing only
                glideImage.loadImageFromInternalStorage(binding.imageView1, this)
            } else {

                //sharedPreference.removeValue(R.string.last_url_key.toString())
                val lastUrl = sharedPreference.getValueString(getString(R.string.last_url_key))

                if (lastUrl != null) {
                    glideImage.loadGlideImage(binding.imageView1, this, binding.progressBar, lastUrl)
                } else {
                    glideImage.loadGlideImage(binding.imageView1, this, binding.progressBar)
                }
            }

        }
    }

    override fun onPause() {
        super.onPause()

        // save the application state to recover it after the app is paused
        // first get the image data (make sure we are displaying a non failed url image)
        if(binding.imageView1.drawable != null){
            val bitmap = binding.imageView1.drawable.toBitmap()
            val asyncStorageIO = AsyncStorageIO(bitmap, savingLastImage = true)
            asyncStorageIO.execute()
        }
    }
    // endregion

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

    // region Permissions
    private fun setupPermissions(){
        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            this.toast("Permission denied!")
            makeRequest()
        } else {
            this.toast("Permission Already Granted...")
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requestCode
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            this.requestCode -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.toast("Permission denied by user!")
                } else {
                    this.toast("Permission granted by user!")
                }
            }
        }
    }
//endregion

    // endregion
}

// endregion