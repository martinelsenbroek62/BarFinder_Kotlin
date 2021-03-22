package com.lemust.ui.screens.sharing

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.BuildConfig
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.gallery.viwer.PhotoPreviewDialog
import com.lemust.ui.screens.main.map.MainRemainder.Companion.RESULT_CODE_RESET
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_sharing.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class SharingView(var fragment: BaseActivity, var root: View) : SharingContract.View, BaseView(fragment!!) {

  var buttonSendIsActive=true
    override fun isShowProgressLoader(isShow: Boolean) {
        if(isShow){
            root.pb_map.visibility=View.VISIBLE
        }else{
            root.pb_map.visibility=View.GONE

        }
    }



    override fun onResetAction(): Observable<Any> {
        return RxView.clicks(root.tv_reset)
    }

    override fun finish() {
        fragment.finish()
    }

    override fun onSentAction(): Observable<Any> {
        return onSentAction
    }

    val SHARE_RESULT_KEY = 21
    var onSentAction = PublishSubject.create<Any>()

    init {
        initAction()
    }

    private fun initAction() {
        root.iv_back.setOnClickListener {
            fragment.onBackPressed()
        }
        fragment.onActivityResultListener.subscribe {
            when (it.requestCode) {
                SHARE_RESULT_KEY -> {
                    buttonSendIsActive=true
                        onSentAction.onNext(Any())
                }
            }
        }
    }

    override fun setPreviewScreenshot(img: Bitmap) {
        root.iv_map_preview.setImageBitmap(img)
    }

    override fun onApplyAction(): Observable<Any> {
        return RxView.clicks(root.tv_apply)
    }

    override fun cancel() {
        fragment.setResult(RESULT_CODE_RESET, fragment.intent);
        fragment.finish();

    }


    override fun onPhotoAction(): Observable<Any> {
        return RxView.clicks(root.iv_map_preview)
    }

    override fun showPhoto(img: Bitmap) {
        var list = ArrayList<Bitmap>()
        list.add(img)
        PhotoPreviewDialog().showDialog(fragment.supportFragmentManager, list, 0, "");

    }
    override fun shareApplication(img: Bitmap) {
        val shareBody = "https://le-must.com/download/"
        if(buttonSendIsActive)
        Dexter.withActivity(fragment)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        var share = Intent(Intent.ACTION_SEND);
                        share.setType("*/*");
                        share.putExtra(Intent.EXTRA_TEXT, shareBody);

                        var bytes = ByteArrayOutputStream();
                        img!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        var f = File(Environment.getExternalStorageDirectory().path + "" + File.separator + "temporary_file.jpg");
                        try {
                            f.createNewFile();
                            var fo = FileOutputStream(f);
                            fo.write(bytes.toByteArray());
                        } catch (e: IOException) {
                            e.printStackTrace();
                        }

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            val photoURI = FileProvider.getUriForFile(fragment, BuildConfig.APPLICATION_ID + ".com.lemust.ui.screens.sharing", f)
                            share.putExtra(Intent.EXTRA_STREAM, photoURI);

                        }else{
                            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
//
                        }

                        var shareIntent=Intent.createChooser(share, "Share Image")
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        buttonSendIsActive=false
                        fragment.startActivityForResult(shareIntent,SHARE_RESULT_KEY);

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {/* ... */
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {/* ... */
                        token.continuePermissionRequest();

                    }
                }).check()

    }
}