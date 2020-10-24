package com.example.randomwebimageapp

/* 
 * Created by Ravi Rachamalla on October 23, 2020
*/

class GlideImage {

    //properties of the glideimage class
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
}