package com.example.randomwebimageapp

/* 
 * Created by Ravi Rachamalla on October 23, 2020
*/

class GlideImage {

    //properties of the glideimage class
    private val listOfImageUrls = mutableListOf<String>(
            "https://placebear.com/800/600",
            "https://placekitten.com/800/600",
            "https://placedog.com/800/600"
    )

    private var listCounter = 0

    var lastURL = ""
        private set
}