package kr.android.shoppinglistapp_room.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kr.android.shoppinglistapp_room.model.LocationData
import kr.android.shoppinglistapp_room.viewmodel.LocationViewModel

class LocationUtil (var context: Context) {

    private val _fusedLocationProviderClient : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    //to request location
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(locationViewModel: LocationViewModel){

        //how to request location
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000
        ).build()

        //what to do with the requested location
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location = LocationData(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                    locationViewModel.updateLocation(location)
                }
            }
        }

        //triggering the location request
        _fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

    }

    //to check location permission
    fun checkPermission(context: Context) : Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

}