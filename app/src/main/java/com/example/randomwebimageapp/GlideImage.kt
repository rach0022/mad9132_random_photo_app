package com.example.randomwebimageapp

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/* 
 * Created by Ravi Rachamalla on October 23, 2020
*/

class GlideImage {

    //properties of the glide image class
    private val listOfImageUrls = mutableListOf<String>(
        "https://placebear.com/800/600",
        "https://placekitten.com/800/600",
        "https://placedog.net/800/600",
        "https://source.unsplash.com/collection/9435244/800x600",
        "https://source.unsplash.com/collection/9572295/800x600",
        "https://source.unsplash.com/collection/181581/800x600",
        "https://source.unsplash.com/collection/8930154/800x600",
        "https://picsum.photos/800/600",
        "https://www.placecage.com/800/600",
        "https://fillmurray.com/800/600",
        "https://media.wired.com/photos/593250d94dc9b45ccec5d2fb/master/w_2560%2Cc_limit/270915898_3b84f9d176_o.jpg",
        "https://asc-csa.gc.ca/images/recherche/tiles/ab472982-ce3b-4b22-9a96-70057e484b7e.jpg",
        "https://asc-csa.gc.ca/images/recherche/tiles/218639e3-24f3-4289-a701-f5b55adae1e1.jpg",
        "https://asc-csa.gc.ca/images/recherche/tiles/d4baade1-3ef5-4569-aa6f-05c419424c3c.jpg"
    )

    private var listCounter = 0

    var lastURL = ""
        private set

    // Initialization method will run every time this object is instantiated
    init {
        // Shuffle (randomize) the this.listOfImageUrls
        this.listOfImageUrls.shuffle()
    }

    // GlideImage Class Methods:
    // Will use the getRandomImageURL function as the default value for url
    fun loadGlideImage(
        imageView: ImageView,
        context: Activity,
        progressBar: ProgressBar,
        url: String = this.getRandomImageURL()
    ){
        // first show the progress bar
        progressBar.visibility = View.VISIBLE
        // set up the context for the Glide bump tech library
        // so we can access the image view controls
        // we can chain on dot operators in a promise like flow to load our image
        Glide.with(context) // our context = the Activity
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // load the images into a cache for easier recall
            .into(imageView) // load the image into the image view

        this.lastURL = url // in case we use a different url, we should save the last used url

        // lastly hide the progress bar once the image is loaded
        progressBar.visibility = View.INVISIBLE
    }

    private fun getRandomImageURL(): String {
        // first set last url to the currently available url
        this.lastURL = this.listOfImageUrls[this.listCounter]

        // increment list counter
        this.listCounter ++

        // lets do a conditional check on list counter
        if(this.listCounter == this.listOfImageUrls.size){
            // shuffle the list first
            this.listOfImageUrls.shuffle()
            // reset the list counter
            this.listCounter = 0
        }
        return this.lastURL
    }
}