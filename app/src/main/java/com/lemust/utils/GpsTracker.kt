package com.lemust.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import io.nlopez.smartlocation.SmartLocation
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class GpsTracker(private var context: Context) {
    private var isLocationDefined = false
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var subject = PublishSubject.create<LocationResult>()
    var delay = 10000L
    fun isAvailableLocation(): Boolean {
        return SmartLocation.with(context).location().state().locationServicesEnabled();
    }

    fun getCurrentLocation(delay: Long = 10000): Observable<LocationResult> {
        this.delay = delay
        SmartLocation.with(context).location()
                .oneFix()
                .start {
                    isLocationDefined = true
                    sendLocation(it)

                };

        handleLastUserPosition()





        return subject
    }

    private fun sendLocation(it: Location?) {
        subject.onNext(LocationResult(it))
        subject.onComplete()
    }

    private fun handleLastUserPosition() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isLocationDefined) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    fusedLocationClient.lastLocation.addOnSuccessListener {
                        if (it == null) {
                            sendNullLocation()
                        } else {
                            sendLocation(it)
                        }


                    } else {
                    sendNullLocation()
                }

            }

        }, delay);
    }

    private fun sendNullLocation() {
        subject.onNext(LocationResult())
        subject.onComplete()
    }


    class LocationResult(var location: Location? = null)

}
