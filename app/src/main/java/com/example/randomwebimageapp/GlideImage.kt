package com.example.randomwebimageapp

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/* 
 * Created by Ravi Rachamalla on November 6th, 2020
*/

// region GlideImage Class
class GlideImage {

    //private val for the randomSiteIdentifier
    private val randomSiteIdentifier = "<RANDOM>" //NOTE < and > are illegal url characters+
    private val appScreenWidth = "<appscreenwidth>"
    private val appScreenHeight = "<appscreenheight>"

    private var width = 300
    private var height = 400


    // region GlideImage Properties
    // listOfImageUrls, will be initialized with a string list containing https secured links to
    // either place holder image sites or single images
    private val listOfImageUrls = mutableListOf<String>(
            "https://placebear.com/${appScreenWidth}/${appScreenHeight}",
            "https://placekitten.com/${appScreenWidth}/${appScreenHeight}$randomSiteIdentifier",
            "https://placedog.net/${appScreenWidth}/${appScreenHeight}$randomSiteIdentifier",
            "https://source.unsplash.com/collection/9435244/${appScreenWidth}x${appScreenHeight}$randomSiteIdentifier",
            "https://source.unsplash.com/collection/9572295/${appScreenWidth}x${appScreenHeight}$randomSiteIdentifier",
            "https://source.unsplash.com/collection/181581/${appScreenWidth}x${appScreenHeight}$randomSiteIdentifier",
            "https://source.unsplash.com/collection/8930154/${appScreenWidth}x${appScreenHeight}$randomSiteIdentifier",
            "https://picsum.photos/${appScreenWidth}/${appScreenHeight}$randomSiteIdentifier",
            "https://www.placecage.com/${appScreenWidth}/${appScreenHeight}$randomSiteIdentifier",
            "https://media.wired.com/photos/593250d94dc9b45ccec5d2fb/master/w_2560%2Cc_limit/270915898_3b84f9d176_o.jpg",
            "https://asc-csa.gc.ca/images/recherche/tiles/ab472982-ce3b-4b22-9a96-70057e484b7e.jpg",
            "https://asc-csa.gc.ca/images/recherche/tiles/218639e3-24f3-4289-a701-f5b55adae1e1.jpg",
            "https://asc-csa.gc.ca/images/recherche/tiles/d4baade1-3ef5-4569-aa6f-05c419424c3c.jpg"
    )

    // list counter, keeps track of index location of the list, when it reaches the list size
    // it will be reset to zero
    private var listCounter = 0

    // lastURL will contain the last url used to generate the user
    var lastURL = ""
        private set

    // our instance of Display Info to get the real width and height
    private val displayInfo = DisplayInfo()

    // diskCacheStrategy
    private var diskCacheStrategy = DiskCacheStrategy.ALL

    // endregion

    // region GlideImage Methods
    // Initialization method will run every time this object is instantiated
    init {
        // Shuffle (randomize) the this.listOfImageUrls
        this.listOfImageUrls.shuffle()
    }

    // Will use the getRandomImageURL function as the default value for url
    fun loadGlideImage(
            imageView: ImageView,
            context: Activity,
            progressBar: ProgressBar,
            url: String = this.getRandomImageURL()
    ) {
        // first show the progress bar
        progressBar.visibility = View.VISIBLE

        //check if the device in is Portrait mode
        val portrait = displayInfo.isPortrait

        this.width = displayInfo.realWidth
        this.height = displayInfo.realHeight

        // check if we are in portrait mode
        if(portrait){ //swap width and height
//            var temp = height
//            height = width
//            width = temp
            width = height.also{height = width}
        }

        // log the url in LOGCAT before the url is changed (after modifying whats in the tags)
        Log.d("urldata", url)
        var updatedURL = url

        if(url.contains(randomSiteIdentifier)){
            // lets not cache random images
            diskCacheStrategy = DiskCacheStrategy.NONE
            updatedURL =  updatedURL.replace(randomSiteIdentifier, "")
        } else {
            // set the diskCacheStrategy to all if its not random
            diskCacheStrategy = DiskCacheStrategy.ALL
        }

        // Now that we have checked if we have a random image or not and changed our diskCacheStrategy
        // accordingly then we can replace the width and height identifiers with the values of width & height
        // depending on if in portrait or not. Log the updated url in the debug log to confirm proper replacements
        updatedURL =  updatedURL.replace(appScreenWidth, "$width")
        updatedURL =  updatedURL.replace(appScreenHeight, "$height")
        Log.d("urldata", updatedURL)

        // set up the context for the Glide bump tech library
        // so we can access the image view controls
        // we can chain on dot operators in a promise like flow to load our image
        Glide.with(context) // our context = the Activity
                .load(updatedURL)
                .diskCacheStrategy(this.diskCacheStrategy) // load the images into a cache for easier recall
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        // hide hte progress bar and display the url that failed in a toast message
                        progressBar.visibility = View.GONE
                        context.toast("glide image load failed: $updatedURL")
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        // hide the progress bar and set the imageView to the resource returned
                        progressBar.visibility = View.GONE
//                        context.toast("Glide Load Success")
                        imageView.setImageDrawable(resource)
                        // only save the last url if it is successful
                        lastURL = updatedURL // in case we use a different url, we should save the last used url
                        return false
                    }

                })
                .into(imageView) // load the image into the image view
    }

    private fun getRandomImageURL(): String {
        // first set last url to the currently available url
        this.lastURL = this.listOfImageUrls[this.listCounter]

        // increment list counter
        this.listCounter++

        // lets do a conditional check on list counter
        if (this.listCounter == this.listOfImageUrls.size) {
            // shuffle the list first
            this.listOfImageUrls.shuffle()
            // reset the list counter
            this.listCounter = 0
        }
        return this.lastURL
    }

    //method to empty the cache using our nested class AsyncGlide
    fun emptyCache(context: Context) {
        val asyncGlide = AsyncGlide(context)
        asyncGlide.execute()
    }

    // extension method that contains a single string parameter called message
    // which will contain the text for the Toast.makeText method with a short notification time
    fun Context.toast(message: String) {
        Toast.makeText(TheApp.context, message, Toast.LENGTH_SHORT).show()
    }

    // private nested class AsyncGlide, will be used to reset the the disk cache upon app load, among other things
    private inner class AsyncGlide(val context: Context) : AsyncTask<Any, Any, Any>() {
        override fun doInBackground(vararg params: Any?): Any? {
            Glide.get(context).clearDiskCache()
            return null
        }

        // we override the onPostExecute function to display a toast message to the user
        // that the image class was deleted
        override fun onPostExecute(result: Any?) {
            super.onPostExecute(result)
//            context.toast("Image cache deleted")
        }
    }

    // endregion
}
// endregion