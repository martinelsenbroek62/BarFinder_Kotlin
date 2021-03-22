package com.lemust.ui.screens.details

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.jakewharton.rxbinding2.view.RxView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.BuildConfig
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.details.adapters.PlaceDetailsSubCategoryAdapter
import com.lemust.ui.screens.details.adapters.PlaceDetailsSubCategoryItem
import com.lemust.ui.screens.details.adapters.SlidingStatisticsAdapter
import com.lemust.ui.screens.details.dialog.BottomSheetFragment
import com.lemust.ui.screens.gallery.GalleryActivity
import com.lemust.ui.screens.gallery.GalleryActivity.Companion.PLACE_TITLE_KEY
import com.lemust.ui.screens.gallery.other.PhotoParamsHelper
import com.lemust.ui.screens.gallery.viwer.PhotoPreviewDialog
import com.lemust.ui.screens.main.MainActivity
import com.lemust.utils.AppDataHolder
import com.lemust.utils.GpsTracker
import com.lemust.utils.Tools
import com.steelkiwi.simplehistogram.TimeData
import com.tbruyelle.rxpermissions2.RxPermissions
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_place_details2.view.*
import kotlinx.android.synthetic.main.item_gallery.view.*
import locationprovider.davidserrano.com.LocationProvider
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PlaceDetailsView(var fragment: BaseFragment, var root: View) : PlaceDetailsContract.View, OnMapReadyCallback, BaseView(fragment.context!!) {
    override fun onPhotoPreviewClick(): Observable<Int> {
        return galleryItemActioan
    }

    var paramsHelper = PhotoParamsHelper(fragment.activity!!)
    var galleryItemActioan = PublishSubject.create<Int>()
    override fun isVisibleButtonShare(isVisibleBoolean: Boolean) {
        if (isVisibleBoolean) {
            root.btn_share.visibility = View.VISIBLE
        } else {
            root.btn_share.visibility = View.INVISIBLE

        }
    }

    override fun isVisibleGayFrendly(isVisible: Boolean) {
        if (isVisible) {
            root.iv_flag.visibility = View.VISIBLE
        } else {
            root.iv_flag.visibility = View.INVISIBLE

        }
    }

    override fun isShowProgressLoader(isShow: Boolean) {
        if (isShow) {
            root.progress_loader_details.visibility = View.VISIBLE
        } else {
            root.progress_loader_details.visibility = View.INVISIBLE

        }
    }

    override fun isClickableButtonShare(isClickable: Boolean) {
        root.btn_share.isClickable = isClickable
    }


    //  private var galleryRecycler = root.findViewById<RecyclerView>(R.id.image_recycler_view)
    // private var galleryAdapter: RecyclerPreviewImageAdapter? = null
    //private var galleryImages = ArrayList<ItemGalleryPreview>()


    private var subCategoryRecycler = root.findViewById<RecyclerView>(R.id.image_recycler_sub_category)
    private var subCategoryList = ArrayList<PlaceDetailsSubCategoryItem>()
    private var subCategoryAdapter: PlaceDetailsSubCategoryAdapter? = null

    private var mapReadyObservable = PublishSubject.create<Any>()!!
    private var currentDayObservable = PublishSubject.create<Int>()!!


    private var googleMap: GoogleMap? = null
    private var mPager: ViewPager? = null
    private var mPagerAdapter: SlidingStatisticsAdapter? = null
    private val statistics = ArrayList<ArrayList<TimeData>>()

    var bottomSheetFragment = BottomSheetFragment();


    init {
        initView()
        initRecycler()


        OverScrollDecoratorHelper.setUpOverScroll(root.place_details_scroll)
    }

    override fun onBackPressed(): Observable<Any> {
        return RxView.clicks(root.iv_back)
    }


    override fun onDestroy() {
        //  galleryAdapter!!.onClick.onComplete()
        mapReadyObservable.onComplete()
        currentDayObservable.onComplete()
    }

    override fun takeSnapshot(): Bitmap {

        val v = root as FrameLayout
        v.isDrawingCacheEnabled = true
        v.buildDrawingCache()

        var bm = Bitmap.createBitmap(
                v.getChildAt(0).getWidth(),
                v.getChildAt(0).getHeight(),
                Bitmap.Config.ARGB_8888);
        var c = Canvas(bm);
        c.drawColor(Color.TRANSPARENT);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);

        return bm


    }

    override fun isVisibleLoader(isVisible: Boolean) {
        if (isVisible) {
            root.dot_progress_bar.visibility = View.VISIBLE

        } else {
            root.dot_progress_bar.visibility = View.GONE

        }
    }

    override fun isVisibleButtonRepport(isVisible: Boolean) {
        if (isVisible) {
            root.tv_report_details.visibility = View.VISIBLE
        } else {
            root.tv_report_details.visibility = View.INVISIBLE

        }
    }

    override fun onPlaceDetailsReportAction(): Observable<Any> {
        return RxView.clicks(root.tv_report_details)
    }


    override fun setUserReportStatisticText(text: String) {
        root.tv_report_user_stat.text = text
    }

//    override fun onPhotoPreviewClick(): Observable<Int> {
//        return galleryAdapter!!.onClick;
//    }


    override fun showPhoto(list: ArrayList<Bitmap>, position: Int, title: String) {
        PhotoPreviewDialog().showDialog(fragment.childFragmentManager, list, position, title)
    }

    override fun onShareAction(): Observable<Any> {
        return RxView.clicks(root.btn_share)
    }


    override fun onGraphicContainer(): Observable<Any> {
        return RxView.clicks(root.tv_graphic_container)
    }


    override fun setFeatures(text: String) {
        root.tv_features_title.visibility = View.VISIBLE
        root.tv_features_content.visibility = View.VISIBLE
        root.tv_features_content.text = text
    }

    override fun setStaticsDay(day: String) {
        //TODO refactor this code ->move logic in presenter
        if (day.isEmpty()) {
            root.day_week.text = ""
            root.day_week_hour.text = ""
        } else {
            var dayName = day.substring(0, day.indexOf(" "))
            var dayPeriod = day.substring(day.indexOf(" "))
            root.day_week.text = dayName + " "
            root.day_week_hour.text = dayPeriod
        }
    }

    override fun setAboutPlace(description: String) {
        root.tv_about_place.text = description

    }

    override fun openGallery(images: ArrayList<Bitmap>, placeTitle: String) {
        var intent = Intent(fragment.context, GalleryActivity::class.java)
        intent.putExtra(PLACE_TITLE_KEY, placeTitle)
        AppDataHolder.savePhoto(images)
        fragment.activity!!.startActivity(intent)

    }


    override fun currentDayListener(): Observable<Int> {
        return currentDayObservable
    }


    override fun initPhotosPreview(items: ArrayList<Bitmap>) {
        if (items.isNotEmpty()) {
            root.tv_photos.visibility = View.VISIBLE
        } else {
            isVisibleLoader(false)
            root.photo_container.visibility = View.GONE
            root.tv_photos.visibility = View.GONE
            showToast("Sorry, the photo was not found")
        }

        when (items.size) {
            in 1..1 -> {
                root.img1.setImageBitmap(items[0])
                root.img3.visibility = View.GONE
                root.img4.visibility = View.GONE
            }
            in 1..2 -> {
                root.img3.visibility = View.GONE
                root.img4.visibility = View.GONE
                root.img1!!.layoutParams = paramsHelper!!.paramsB
                root.img2!!.layoutParams = paramsHelper!!.paramsB
                root.img1.setImageBitmap(items[0])
                root.img2.setImageBitmap(items[1])


            }
            in 2..3 -> {
                root.img1.setImageBitmap(items[0])
                root.img2.setImageBitmap(items[1])
                root.img3.setImageBitmap(items[2])
            }
            in 3..items.size -> {
                root.img1.setImageBitmap(items[0])
                root.img2.setImageBitmap(items[1])
                root.img3.setImageBitmap(items[2])
                root.img4.setImageBitmap(items[3])
            }
        }


    }

    private fun initPhotoViews() {
        root.img1!!.layoutParams = paramsHelper!!.paramsM
        root.img2!!.layoutParams = paramsHelper!!.paramsB
        root.img3!!.layoutParams = paramsHelper!!.paramsB
        root.img4!!.layoutParams = paramsHelper!!.paramsM


        root.img1.setOnClickListener { galleryItemActioan.onNext(0) }
        root.img2.setOnClickListener { galleryItemActioan.onNext(1) }
        root.img3.setOnClickListener { galleryItemActioan.onNext(2) }
        root.img4.setOnClickListener { galleryItemActioan.onNext(3) }

    }

    override fun getPermissionLocation(permission: PermissionListener) {
        (fragment.activity as BaseActivity).getPermissionLocation(permission)
    }

    override fun getLocation(): Observable<GpsTracker.LocationResult> {
        return GpsTracker(fragment.activity as BaseActivity).getCurrentLocation()
    }

    override fun openRout(to: LatLng, with: LatLng) {
        try {
            fragment.activity?.let {
                if (!it.isDestroyed) {
                    var intent = Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=${to.latitude},${to.longitude}&daddr=${with.latitude},${with.longitude}"));
                    it.startActivity(intent);
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun call(number: String) {
        var intent = Intent(Intent.ACTION_DIAL);
        intent.data = Uri.parse("tel:$number");
        fragment.startActivity(intent);
    }

    override fun onCallAction(): Observable<Any> {
        return RxView.clicks(root.btn_call)
    }

    override fun openWeb(number: String) {
        var browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(number));
        fragment.startActivity(browserIntent);
    }

    override fun onWebAction(): Observable<Any> {
        return RxView.clicks(root.btn_web)
    }

    override fun onDirectionsAction(): Observable<Any> {
        return RxView.clicks(root.btn_rout)

    }

    override fun onClickGallery(): Observable<Any> {
        return RxView.clicks(root.tv_gallery)
    }

    override fun setAddress(address: String) {
        root.tv_address.text = address
    }

    override fun setTime(times: String, currentTime: String) {
        root.tv_times.text = times
        root.tv_times_current.text = currentTime
    }

    override fun setPhone(phone: String) {
        root.tv_phone.text = phone
    }

    private fun initRecycler() {
        var managerSubCategory = LinearLayoutManager(fragment.context, LinearLayoutManager.VERTICAL, true)

        //galleryAdapter = RecyclerPreviewImageAdapter(galleryImages, fragment.activity!!, true)


        subCategoryAdapter = PlaceDetailsSubCategoryAdapter(subCategoryList)
        subCategoryRecycler.adapter = this.subCategoryAdapter
        subCategoryRecycler.isNestedScrollingEnabled = false;
        subCategoryRecycler.setLayoutManager(managerSubCategory)

//
//        val displayMetrics = DisplayMetrics()
//        fragment.activity!!.windowManager
//                .defaultDisplay
//                .getMetrics(displayMetrics)
////        val viewHeight = (displayMetrics.widthPixels / 3) * 2
//        // galleryRecycler.getLayoutParams().height = viewHeight

    }

    override fun mapIsReady(): Observable<Any> {
        return mapReadyObservable
    }

    override fun initMarker(iconId: Int, location: LatLng) {

        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))

        googleMap?.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(iconId))
                .position(location))

//        Handler().postDelayed({
//
//        }, 1500)


    }


    override fun initPlaceDetails(title: String, typePlace: String, description: String, rating: Float) {
        if (description != null)
            if (description.isNotEmpty())
                root.tv_short_description.visibility = View.VISIBLE

        root.tv_short_description.text = description
        root.tv_place_title.text = title
        root.tv_types_place.text = typePlace
        root.ratingBar.setRating(rating);


    }


    private fun initView() {
        // indicator = root.findViewById(R.id.indicator) as CirclePageIndicator
        val displayMetrics = DisplayMetrics()
        fragment.activity!!.windowManager
                .defaultDisplay
                .getMetrics(displayMetrics)

        mPager = root.findViewById(R.id.vp_statics) as ViewPager
        mPager!!.layoutParams.height = displayMetrics.heightPixels / 6
        mPagerAdapter = SlidingStatisticsAdapter(fragment.context!!, statistics)

        initPhotoViews()

    }


    override fun initViewPager(statics: ArrayList<ArrayList<TimeData>>) {
        this.statistics.clear()
        this.statistics.addAll(statics)
        mPager!!.adapter = mPagerAdapter

        root.vp_statics.visibility = View.GONE
        root.indicator.visibility = View.GONE
        var center = ((Int.MAX_VALUE / statics.size) / 2) * statics.size
        mPager!!.setCurrentItem(center + Tools.getTodayDayOfWeek(), false)
        root.indicator.visibility = View.VISIBLE
        root.vp_statics.visibility = View.VISIBLE

        mPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                var actualPosition = position % mPagerAdapter!!.getActualListCount()
                root.indicator.setCurrentSelectedItem(actualPosition)

            }

            override fun onPageSelected(position: Int) {
                var actualPosition = position % mPagerAdapter!!.getActualListCount()
                currentDayObservable.onNext(actualPosition)
            }
        })


    }

    override fun onMapReady(googleMap: GoogleMap?) {
        try {
            val displayMetrics = DisplayMetrics()
            fragment.activity!!.windowManager
                    .defaultDisplay
                    .getMetrics(displayMetrics)

            googleMap!!.setPadding((displayMetrics.widthPixels * 0.6).toInt(), 0, 0, Tools.convertDpToPixel(400F, fragment.context!!).toInt());


            val success = googleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(fragment.context, R.raw.style))
            if (!success) {
                Log.e(ContentValues.TAG, "Style parsing failed.")
            }
            root.mainMap.visibility = View.VISIBLE

        } catch (e: Resources.NotFoundException) {
            Log.e(ContentValues.TAG, "Can't find style. Error: ", e)
        }
        this.googleMap = googleMap

        mapReadyObservable.onNext(Any())
        googleMap!!.uiSettings.isMapToolbarEnabled = false;


        googleMap!!.setOnMapClickListener { }


//
//        googleMap!!.setOnMapLoadedCallback {
//
//
//            val view = root.findViewById(R.id.fl_map) as FrameLayout
//            view.visibility = View.GONE
//            view.isDrawingCacheEnabled = true
//            view.buildDrawingCache()
//            val bm = view.drawingCache
//            root.fl_map.visibility = View.INVISIBLE
//            root.iv_map.setImageBitmap(bm)
//            root.iv_map.visibility = View.VISIBLE
//        }
    }

//    override fun isVisibleReportUserStatistic(isVisible: Boolean) {
//        if (isVisible) {
//            root.tv_report_user_stat.visibility = View.VISIBLE
//        } else {
//            root.tv_report_user_stat.visibility = View.GONE
//
//        }
//    }

    override fun isVisibleStatisticsView(isVisible: Boolean) {
        if (isVisible) {
            root.indicator.visibility = View.VISIBLE
            root.vp_statics.visibility = View.VISIBLE
            root.tv_is_empty_statistic.visibility = View.GONE
        } else {
            //root.view_statistics_container.visibility = View.GONE
            root.tv_is_empty_statistic.visibility = View.VISIBLE
            root.indicator.visibility = View.GONE
            root.vp_statics.visibility = View.GONE
            //root.tv_address_title.visibility = View.GONE

        }
    }

    override fun isVisibleAddress(isVisible: Boolean) {
        if (isVisible) {
            root.tv_address.visibility = View.VISIBLE
            root.tv_address_title.visibility = View.VISIBLE
        } else {
            root.tv_address_title.visibility = View.GONE
            root.tv_address.visibility = View.GONE

        }
    }

    override fun setSubCategory(to: ArrayList<PlaceDetailsSubCategoryItem>) {
        subCategoryList.addAll(to)
        subCategoryAdapter!!.notifyDataSetChanged()
    }


    override fun isVisibleTimeWork(isVisible: Boolean) {
        if (isVisible) {
            root.tv_times.visibility = View.GONE
            root.tv_times_current.visibility = View.VISIBLE
            root.tv_graphic_container.visibility = View.VISIBLE
        } else {
            root.tv_times_current.visibility = View.GONE
            root.tv_times.visibility = View.GONE
            root.tv_graphic_container.visibility = View.GONE

        }
    }


    override fun isVisiblePhone(isVisible: Boolean) {
        if (isVisible) {
            root.tv_phone_title.visibility = View.VISIBLE
            root.tv_phone.visibility = View.VISIBLE
        } else {
            root.tv_phone_title.visibility = View.GONE
            root.tv_phone.visibility = View.GONE

        }
    }

    override fun isShowGraphic() {
        var isShow = root.tv_times.isVisible
        if (!isShow) {
            root.tv_times.visibility = View.VISIBLE
            root.tv_times_current.visibility = View.GONE

            root.iv_graphic.setImageResource(R.drawable.icn_details_arrow_up)
        } else {
            root.tv_times.visibility = View.GONE
            root.tv_times_current.visibility = View.VISIBLE

            root.iv_graphic.setImageResource(R.drawable.icn_details_arrow_down)


        }
    }

    override fun isVisibleAboutPlace(isVisible: Boolean) {
        if (isVisible) {
            root.tv_about_place.visibility = View.VISIBLE
        } else {
            root.tv_about_place.visibility = View.GONE

        }
    }

    override fun isVisibleMainContent(isVisible: Boolean) {
        if (isVisible) {

            root.btn_divider.visibility = View.VISIBLE
            root.main_content.visibility = View.VISIBLE
        } else {
            root.btn_divider.visibility = View.GONE
            root.main_content.visibility = View.GONE

        }
    }

    override fun isVisiblePhoneButton(isVisible: Boolean) {
        if (isVisible) {
            root.btn_call.visibility = View.VISIBLE
        } else {
            root.btn_call.visibility = View.GONE

        }
    }

    override fun isVisibleDirectionButton(isVisible: Boolean) {
        if (isVisible) {
            root.btn_rout.visibility = View.VISIBLE
        } else {
            root.btn_rout.visibility = View.GONE

        }
    }

    override fun isVisibleWebButton(isVisible: Boolean) {
        if (isVisible) {
            root.btn_web.visibility = View.VISIBLE
        } else {
            root.btn_web.visibility = View.GONE

        }
    }

    override fun isVisibleRatingBar(isVisible: Boolean) {
        if (isVisible) {
            root.ratingBar.visibility = View.VISIBLE
        } else {
            root.ratingBar.visibility = View.INVISIBLE

        }
    }

    override fun setPriceLevel(level: Int) {
        root.view_price_level.setRating(level)
    }

    override fun isVisiblePriceLevel(isVisible: Boolean) {
        if (isVisible) {
            root.view_price_level.visibility = View.VISIBLE
        } else {
            root.view_price_level.visibility = View.INVISIBLE

        }
    }


    override fun isAboutPlaceTextView(isVisible: Boolean) {
        if (isVisible) {
            root.tv_about_pace_container.visibility = View.VISIBLE
        } else {
            root.tv_about_pace_container.visibility = View.GONE

        }

    }


    override fun isVisibleFrequentationContainer(isVisible: Boolean) {
        if (isVisible) {
            root.frequentation_container.visibility = View.VISIBLE
        } else {
            root.frequentation_container.visibility = View.GONE

        }
    }

    override fun showGPSEnabledSettingDialog() {
        Handler(Looper.getMainLooper()).post {
            showDialog(DialogModel().build(fragment.activity!!, fragment.activity!!.getString(R.string.title_GPS_settings))
                    .showMessage(fragment.activity!!.getString(R.string.title_PS_is_not_enabled))
                    .showLastButton(fragment.activity!!.resources.getString(R.string.title_setting))
                    .showFirstButton(fragment.activity!!.resources.getString(R.string.title_cancel))
                    .single(true)).subscribe {
                if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                    dialog!!.dismiss()

                }
                if (DialogModel.State.SECOND_BUTTON == it.clicked) {
                    dialog!!.dismiss()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    fragment.activity!!.startActivityForResult(intent, MainActivity.ON_ACTIVITY_RESULT_LOCALE)


                }
            }
        }


    }


    override fun shareLink(placeId: Int, cityId: Int, placeTypeId: String) {
//        val shareBody = "http://static.steel.kiwi/lemust/download/"
//        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
//        sharingIntent.type = "text/plain"
////            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
//        fragment.startActivity(Intent.createChooser(sharingIntent, "Share link"))

        val shareBody = "https://le-must.com/share/?placeId=$placeId&cityId=$cityId&placeTypeId=$placeTypeId"
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
        fragment.startActivity(Intent.createChooser(sharingIntent, "Share link"))

    }


    override fun shareLinkAndMap(placeId: Int, cityId: Int, placeTypeId: String) {
        val shareBody = "https://le-must.com/share/?placeId=$placeId&cityId=$cityId&placeTypeId=$placeTypeId"
        Dexter.withActivity(fragment.activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
//                        AppDataHolder.isScreenshotReady.subscribe {
                        var share = Intent(Intent.ACTION_SEND);
                        share.setType("*/*");
                        share.putExtra(Intent.EXTRA_TEXT, shareBody);

                        var bytes = ByteArrayOutputStream();
                        takeSnapshot()!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        var f = File(Environment.getExternalStorageDirectory().path + "" + File.separator + "temporary_file.jpg");
                        try {
                            f.createNewFile();
                            var fo = FileOutputStream(f);
                            fo.write(bytes.toByteArray());
                        } catch (e: IOException) {
                            e.printStackTrace();
                        }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            val photoURI = FileProvider.getUriForFile(fragment.context!!, BuildConfig.APPLICATION_ID + ".com.lemust.ui.screens.sharing", f)
                            share.putExtra(Intent.EXTRA_STREAM, photoURI);

                        } else {
                            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
//
                        }
                        fragment.startActivityForResult(Intent.createChooser(share, "Share Image"), 777);

//                        val receiver = Intent(fragment.context, ApplicationSelectorReceiver::class.java)
//                        var pendingIntent = PendingIntent.getBroadcast(fragment.context, 0, receiver, PendingIntent.FLAG_ONE_SHOT);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                            val chooser = Intent.createChooser(share, "Share Image", pendingIntent.intentSender)
                        //  fragment!!.startActivityForResult(share,777);

//                        } else {
//                        }


                        //  }
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {/* ... */
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {/* ... */
                        token.continuePermissionRequest();

                    }
                }).check()


    }


    override fun showBottomSheetDialog() {
        bottomSheetFragment!!.show(fragment.childFragmentManager, bottomSheetFragment!!.tag);
    }


    override fun hideBottomSheetDialog() {
        bottomSheetFragment!!.dismiss()
    }

//    override fun onSharePlaceDetailsAction(): Observable<Any> {
//        return bottomSheetFragment!!.placeDetailsAction
//    }
//
//    override fun onSharePlaceDetailsAndMapAction(): Observable<Any> {
//        return bottomSheetFragment!!.placeDetailsAndMapAction
//    }

    override fun onReportStatisticAction(): Observable<Any> {
        return RxView.clicks(root.tv_report_user_stat)
    }
}