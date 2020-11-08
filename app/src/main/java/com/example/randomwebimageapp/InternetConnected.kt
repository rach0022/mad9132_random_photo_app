/*
 * Created by Ravi Rachamalla on October 19, 2020
*/
package com.example.randomwebimageapp

// An interface to global information about the app environment
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/*
 * Created by Ravi Rachamalla on November 06, 2020
*/

// region InternetConnected Class
class InternetConnected(val context: Context) {
    // region InternetConnected Methods
    // A methods to check the phones network connectivity status
    fun checkNetworkConnectivity() : Boolean {
        //for Marshmallow API 23 and + use this style of check
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return postMarshmallowInternetCheck(connectivityManager)
    }

    // A private method to tell us if we are Marshmallow or Up API
    private fun postMarshmallowInternetCheck(connectivityManager: ConnectivityManager): Boolean{
        //get a ref to the network
        val network = connectivityManager.activeNetwork

        //get a ref to the network connection:
        val connection = connectivityManager.getNetworkCapabilities(network)

        //check if a network has 'transport' which means that the network allows you to travel
        return connection != null && (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
    // endregion
}
// endregion