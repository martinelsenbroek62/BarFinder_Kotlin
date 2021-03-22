package com.lemust.utils

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import com.lemust.LeMustApp
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.dialog_preview_place.view.*
import java.util.*

class PhotoTools {

    internal fun initPhotoByGooglePLaceId(placeId: String, view: View) {
        val mGeoDataClient: GeoDataClient = Places.getGeoDataClient(LeMustApp.instance)

        val photoResponse = mGeoDataClient.getPlacePhotos(placeId)
        photoResponse.addOnCompleteListener { task ->
            try {
                if (task.isSuccessful) {
                    val photos = task.result
                    val photoMetadataBuffer = photos.photoMetadata
                    if (photoMetadataBuffer != null) {
                        if (photoMetadataBuffer.count > 0) {

                            val photoMetadata = photoMetadataBuffer.get(0)


                            val photoResponse = mGeoDataClient.getPhoto(photoMetadata)

                            photoResponse.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val photo = task.result
                                    view.iv_place.setImageBitmap(photo.bitmap)
                                } else {
                                    Log.d("photo_load_err", "photo not load")
                                }
                            }
                            photoMetadataBuffer.release()

                        }
                    }
                } else {
                    Log.d("photo_load_err", "photo not load")
                }
            } catch (e: SecurityException) {
                e.printStackTrace();

            }

        }


    }

    var countGoogleAvailablePhotos = 0;
    var loadedPhotos = ArrayList<Bitmap>()
    var photosLoadedAction = PublishSubject.create<PhotosResult>()

    fun getGooglePhotos(placeId: String, placeId1: Int): Observable<PhotosResult> {

        loadHooglePhotos(placeId, placeId1)

        return onMainThread(photosLoadedAction)
    }

    private fun loadHooglePhotos(placeId: String, placeId1: Int) {
        loadedPhotos.clear()
        val mGeoDataClient: GeoDataClient = Places.getGeoDataClient(LeMustApp.instance, null)
        val photoResponse = mGeoDataClient.getPlacePhotos(placeId)
        photoResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                try {
                    val photos = task.result
                    val photoMetadataBuffer = photos.photoMetadata
                    countGoogleAvailablePhotos = photoMetadataBuffer.count
                    if (photoMetadataBuffer.count == 0) {
                        photosLoadedAction.onNext(PhotosResult(ArrayList()))
                        photosLoadedAction.onComplete()

                        //
                    }

                    for (i in 0 until photoMetadataBuffer.count) {
                        val photoMetadata = photoMetadataBuffer.get(i)
                        val photoResponse = mGeoDataClient.getPhoto(photoMetadata)
                        photoResponse.addOnCompleteListener { task ->
                            try {
                                if (task.isSuccessful) {

                                    val photo = task.result

                                    loadedPhotos.add(photo.bitmap)
                                } else {
                                    countGoogleAvailablePhotos--
                                }
                            } catch (e: Exception) {
                                countGoogleAvailablePhotos--
                            }

                            if (loadedPhotos.size == countGoogleAvailablePhotos) {
                                photosLoadedAction.onNext(PhotosResult(loadedPhotos))
                                photosLoadedAction.onComplete()


                                //                                view.initPhotosPreview(loadedPhotos)
                                //                                view.isVisibleMainContent(true)
                                //                                view.isVisibleButtonRepport(true)


                            }
                        }

                    }

                    photoMetadataBuffer.release()

                } catch (e: Exception) {

                }
            } else {
                Log.d("photo_load_err", "photo not load")
            }
        }
    }


    class PhotosResult(var images: ArrayList<Bitmap>)


}