package com.lemust.ui.screens.profile

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.kbeanie.imagechooser.api.ChosenImage
import com.lemust.BuildConfig
import com.lemust.R
import com.lemust.repository.models.rest.user.get.UserFavoritePlaceTypeDTO
import com.lemust.repository.models.rest.user.get.UserOptionDTO
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.image_utils.ImagePickerManager
import com.lemust.ui.screens.profile.change_password.ChangePasswordActivity
import com.lemust.ui.screens.profile.days_go_out.DaysGoOutActivity
import com.lemust.ui.screens.profile.edit_user.EditUserActivity
import com.lemust.ui.screens.profile.favorites_place.FavoritePlaceActivity
import com.lemust.ui.screens.profile.location.SearchCityActivity
import com.lemust.ui.screens.profile.occupation.OccupationActivity
import com.lemust.ui.screens.profile.other_items.OtherItemsActivity
import com.lemust.ui.screens.profile.settings.SettingsActivity
import com.lemust.utils.RealPathUtil
import com.myhexaville.smartimagepicker.ImagePicker
import com.myhexaville.smartimagepicker.OnImagePickedListener
import id.zelory.compressor.Compressor
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.*
import java.util.*


class ProfileRemainder(var fragment: BaseActivity) : ProfileContract.Remainder, LifecycleObserver {

    companion object {
        const val LANGUAGE_CHANGED_KEY = "language_changed_key"
        const val IS_UPDATE_USER_DATA_KEY = "is_update_user_data"
        const val IS_UPDATE_USER_AND_SERVER_DATA_KEY = "is_update_user_and_server_data"
    }


    val DAYS_GO_OUT_RESULT = 4
    val FAVORITE_MUSIC_RESULT = 5
    val FAVORITE_PLACES_RESULT = 6
    val SEARCH_CITY_RESULT = 7
    val OCCUPATION_RESULT = 8
    val GALLERY_RESULT = 9
    val CAMERA_RESULT = 10
    val EDIT_USER_RESULT = 11
    val SETTING_RESULT = 12


    var onDaysGoOutResult = PublishSubject.create<Any>()
    var onMusicResult = PublishSubject.create<Any>()
    var onPlacesResult = PublishSubject.create<Any>()
    var onOccupationResult = PublishSubject.create<Any>()
    var onSearchResult = PublishSubject.create<Any>()
    var onNewImageReady = PublishSubject.create<ImageState>()
    var onUserEditReady = PublishSubject.create<Any>()
    var onSettingReady = PublishSubject.create<Boolean>()
    //    private var resume = PublishSubject.create<Any>()
    private var onBackPressed = PublishSubject.create<Any>()


    class ImageState(var path: String, var isStartLoad: Boolean, var errorLoad: String = "")


    var file: File? = null
    var uri: String? = null


    init {
        initAction()
    }


    override fun onSettingRewriteAction(): Observable<Boolean> {
        return onSettingReady
    }


    override fun onUserEditRewriteAction(): Observable<Any> {
        return onUserEditReady
    }

    override fun onNewImageAction(): Observable<ImageState> {
        return onNewImageReady
    }

    override fun onSearchRewriteAction(): Observable<Any> {
        return onSearchResult
    }

    override fun onOccupationRewriteAction(): Observable<Any> {
        return onOccupationResult
    }

    override fun closeScreen(isUpdateBottomScreen: Boolean) {
        if (isUpdateBottomScreen) {
            fragment.setResult(RESULT_OK, fragment.intent);
            fragment.finish();
        } else {
            fragment.setResult(RESULT_CANCELED, fragment.intent);
            fragment.finish();
        }
    }

    override fun onBackPressedAction(): Observable<Any> {
        return onBackPressed
    }


    override fun onFavoritePlacesRewriteAction(): Observable<Any> {
        return onPlacesResult
    }


    private fun initAction() {
        fragment.onActivityResultListener.subscribe {

            imagePicker.handleActivityResult(it.resultCode, it.requestCode, it.data);

            when (it.requestCode) {
                DAYS_GO_OUT_RESULT -> {
                    onDaysGoOutResult.onNext(Any())
                }
                FAVORITE_MUSIC_RESULT -> {
                    onMusicResult.onNext(Any())

                }
                FAVORITE_PLACES_RESULT -> {
                    onPlacesResult.onNext(Any())

                }
                OCCUPATION_RESULT -> {
                    onOccupationResult.onNext(Any())
                }
                SEARCH_CITY_RESULT -> {
                    onSearchResult.onNext(Any())
                }

//                GALLERY_RESULT -> {
//                    handleGalleryResult(it)
//                }
//
//                CAMERA_RESULT -> {
//                    handleCameraResult(it)
//                }
                EDIT_USER_RESULT -> {
                    onUserEditReady.onNext(Any())
                }
                SETTING_RESULT -> {
                    handleSettingResult(it)
                }
            }


        }
    }

    private fun handleSettingResult(it: BaseActivity.OnActivityResult) {

        if (it.data != null)
            if (it.data!!.hasExtra(LANGUAGE_CHANGED_KEY)) {
                if (it.data!!.getBooleanExtra(LANGUAGE_CHANGED_KEY, false)) {
                    onSettingReady.onNext(true)
                } else {
                    onSettingReady.onNext(false)

                }
            }

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        onDaysGoOutResult.onComplete()
        onMusicResult.onComplete()
        onPlacesResult.onComplete()
        onOccupationResult.onComplete()
        onSearchResult.onComplete()
        onUserEditReady.onComplete()
        onNewImageReady.onComplete()
        onBackPressed.onComplete()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
//        resume.onNext(Any())

    }


    override fun openCamera() {
        Dexter.withActivity(fragment)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        imagePicker.openCamera();

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {/* ... */
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {/* ... */
                        token.continuePermissionRequest();

                    }
                }).check()


    }

    var imagePicker = ImagePicker(fragment, null, OnImagePickedListener { imageUri ->
        onNewImageReady.onNext(ImageState("", true))
        Thread(Runnable {


            try {
                var realPath = RealPathUtil.getPathFromUri(fragment, imageUri)!!

                var rotation = getImageOrientation(realPath);
                var file = resaveBitmap(realPath, rotation);
                onNewImageReady!!.onNext(ImageState(file.path, false))

            } catch (e: Exception) {
                e.printStackTrace()
                var msg = fragment.resources.getString(R.string.title_error_load_photo)
                if (e.localizedMessage != null)
                    msg = e.localizedMessage


                Handler(Looper.getMainLooper()).post {

                    onNewImageReady.onNext(ImageState("", false, msg))
                }

            } catch (e: Error) {
                Handler(Looper.getMainLooper()).post {
                    onNewImageReady.onNext(ImageState("", false, e.localizedMessage))
                }
            }

        }).start()
    })


    override fun showGallery() {
        Dexter.withActivity(fragment)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest();

                    }

                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (it.areAllPermissionsGranted()) {
                                try {
                                    imagePicker.choosePicture(false);
                                } catch (e: Throwable) {
                                    System.out.print(e.localizedMessage)
                                }
                            }

                            if (it.isAnyPermissionPermanentlyDenied) {


                            }
                        }


                    }

                }).check()


//        ImagePickerManager.getInstance().chooseImage(fragment, object : ImagePickerManager.OnImagePickedListener {
//            override fun onChosenImageReady(chosenImage: ChosenImage?) {
//                onNewImageReady.onNext(ImageState("", true))
//                Thread(Runnable {
//                                            try {
//
//                    var rotation = getImageOrientation(chosenImage!!.filePathOriginal);
//                    var file = resaveBitmap(chosenImage.filePathOriginal, rotation);
//
//                    onNewImageReady.onNext(ImageState(file.absolutePath, false))
//                                            }catch (e:Throwable){
//                                                System.out.print(e.localizedMessage)
//                                            }
//                }).start()
//
////                onNewImageReady.onNext(chosenImage!!.filePathOriginal)
//
//            }
//
//        })


    }

    private fun checkRotationFromCamera(bitmap: Bitmap, pathToFile: String, rotate: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotate.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    fun getImageOrientation(imagePath: String): Int {
        var rotate = 0
        val exif = ExifInterface(imagePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
        }


        return rotate
    }

    val MAX_IMAGE_SIZE = 1600 * 1200
    private fun resaveBitmap(filePath: String, rotation: Int): File {
        var outStream: OutputStream? = null
        var file = File(filePath)

        try {

            var newBitmap=Compressor(fragment).setMaxWidth(180)
                    .setMaxHeight(180)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).absolutePath)
                    .compressToBitmap(file);
            var bitmap = BitmapFactory.decodeFile(file.absolutePath)
            bitmap = checkRotationFromCamera(bitmap, file.absolutePath, rotation)
            bitmap = Bitmap.createScaledBitmap(bitmap, newBitmap.width, newBitmap.height, false)
            outStream = FileOutputStream(file.absolutePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outStream)


           // file=Compressor(fragment).compressToFile(file);




            outStream!!.flush()
            outStream!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return file
    }


    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val debugTag = "MemoryInformation"
        // Image nin islenmeden onceki genislik ve yuksekligi
        val height = options.outHeight
        val width = options.outWidth
        Log.d(debugTag, "image height: $height---image width: $width")
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }
        Log.d(debugTag, "inSampleSize: $inSampleSize")
        return inSampleSize
    }

    override fun getContext(): Context {
        return fragment.baseContext
    }

    override fun openChangePasswordScreen() {
        fragment.startActivity(Intent(fragment, ChangePasswordActivity::class.java))
    }

    override fun openOccupation(map: HashMap<String, String>?) {
        OccupationActivity.start(fragment, map!!, OCCUPATION_RESULT)
    }

    override fun openPlaceTypeScreen(selectedMusicTypes: ArrayList<UserFavoritePlaceTypeDTO>, allMusicTypes: ArrayList<UserFavoritePlaceTypeDTO>) {
        FavoritePlaceActivity.start(fragment, selectedMusicTypes, allMusicTypes, FAVORITE_PLACES_RESULT)
    }

    override fun openDaysGoOutScreen() {
        fragment.startActivityForResult(Intent(fragment, DaysGoOutActivity::class.java), DAYS_GO_OUT_RESULT)
    }

    override fun openLocation() {
        fragment.startActivityForResult(Intent(fragment, SearchCityActivity::class.java), SEARCH_CITY_RESULT)
    }
//
//    override fun openEditUserScreen(isFirstName:Boolean) {
//        EditUserActivity.start(fragment,isFirstName)
////        fragment.startActivityForResult(Intent(fragment, EditUserActivity::class.java), EDIT_USER_RESULT)
//    }


    override fun openEditFirstName() {
        EditUserActivity.start(fragment, true, EDIT_USER_RESULT)
    }

    override fun openEditLastName() {
        EditUserActivity.start(fragment, false, EDIT_USER_RESULT)
    }

    override fun openSettings() {
        fragment.startActivityForResult(Intent(fragment, SettingsActivity::class.java), SETTING_RESULT)
    }


    override fun openTypesMusicScreen(currentFavoriteMusic: ArrayList<UserOptionDTO>, currentSelectedFovoriteMusic: ArrayList<UserOptionDTO>, id: Int) {
        // fragment.startActivity(Intent(fragment, OtherItemsActivity::class.java))
        OtherItemsActivity.start(fragment, currentFavoriteMusic, currentSelectedFovoriteMusic, FAVORITE_MUSIC_RESULT, id)
    }

    override fun onDaysGoOutRewriteAction(): Observable<Any> {
        return onDaysGoOutResult
    }

    override fun onFavoriteMusicRewriteAction(): Observable<Any> {
        return onMusicResult
    }


    override fun onBackPressed() {
        onBackPressed.onNext(Any())
    }


}

