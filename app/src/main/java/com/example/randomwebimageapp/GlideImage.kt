package com.example.randomwebimageapp

import android.app.Activity
import android.widget.ImageView

/* 
 * Created by Ravi Rachamalla on October 23, 2020
*/

class GlideImage() {

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
        "http://fillmurray.com/800/600"
    )

    private var listCounter = 0

    var lastURL = ""
        private set

    // Initialization method will run every time this object is instantiated
    init {
        // Shuffle (randomize) the self.listOfImageUrls
        this.listOfImageUrls.shuffle()
    }

    // GlideImage Class Methods:
    fun loadGlideImage(imageView: ImageView, activity: Activity, imageName: String ){

    }

    fun getRandomImageUrl(): String {
        return ""
    }
}