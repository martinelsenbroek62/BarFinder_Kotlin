package com.lemust.ui.screens.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.details.dialog.BottomSheetFragment
import com.lemust.ui.screens.details.dialog.adapter.MenuItem
import com.lemust.ui.screens.profile.adapter.Item
import com.lemust.ui.screens.profile.adapter.ItemsAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.loader.view.*
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.content.Intent.CATEGORY_OPENABLE
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.support.v4.app.ActivityCompat.startActivityForResult
import com.lemust.LeMustApp
import com.myhexaville.smartimagepicker.ImagePicker
import com.myhexaville.smartimagepicker.OnImagePickedListener


class ProfileView(var fragment: BaseActivity, var root: View) : ProfileContract.View, BaseView(fragment!!) {


    override fun openGallery() {
        var imagePicker = ImagePicker(fragment, null, object : OnImagePickedListener {
            override fun onImagePicked(imageUri: Uri?) {
            }
        })
        imagePicker.choosePicture(true /*show camera intents*/);

    }


    override fun hideChangePasswordScreen() {
        root.tv_change_password.visibility = View.GONE
    }

    private var recyclerMusicTypes = root.findViewById<RecyclerView>(R.id.rv_profile_items)
    private var musicItemsItems = ArrayList<Item>()
    private var placeTypeAdapter: ItemsAdapter? = null
    private var bottomSheetFragment: BottomSheetFragment? = null


    init {
        initAction()
        initRecycler()
    }


    override fun changeTextInProgressBar(text: String) {
        root.title_loading.text = text
    }


    override fun updateBottomSheetItems(items: ArrayList<MenuItem>) {
        if (bottomSheetFragment != null) {
            bottomSheetFragment!!.updateResources(items)
        }
    }

    override fun resetUserPhoto() {
        Glide.with(fragment)
                .load(R.drawable.icon_avatar_default)
                .into(root.iv_avatar);

    }

    override fun onBottomSheetAction(): Observable<MenuItem> {
        return bottomSheetFragment!!.menuAdapter!!.clickListener
    }

    override fun setPhotoMenuItems(items: ArrayList<MenuItem>) {
        bottomSheetFragment = BottomSheetFragment.newInstance(items)
    }


    override fun setFirstName(name: String) {
        root.tv_first_name.text = name
    }

    override fun onOtherItemsAction(): Observable<Item> {
        return placeTypeAdapter!!.clickListener
    }

    override fun setItems(items: List<Item>) {
        musicItemsItems.clear()
        musicItemsItems.addAll(items)
        placeTypeAdapter!!.notifyDataSetChanged()
    }


    override fun setLastName(name: String) {
        root.tv_last_name.text = name
    }

    override fun isShowLoaderPhoto(isShow: Boolean) {
        fragment.runOnUiThread {
            if (isShow) {
                root.photo_loader.visibility = View.VISIBLE
            } else {
                root.photo_loader.visibility = View.GONE

            }
        }
    }


    private fun initRecycler() {
        var managerSubFilters = LinearLayoutManager(fragment!!, LinearLayoutManager.VERTICAL, false)
        recyclerMusicTypes.layoutManager = managerSubFilters
        placeTypeAdapter = ItemsAdapter(musicItemsItems)
        recyclerMusicTypes.adapter = placeTypeAdapter
        recyclerMusicTypes.isNestedScrollingEnabled = false;

    }

    override fun closePhotoDialog() {
        if (bottomSheetFragment != null)
            if (bottomSheetFragment!!.isAdded)
                bottomSheetFragment!!.dismiss()
    }

    override fun updateDialogResources() {
    }

    private fun initAction() {
        root.btn_back.setOnClickListener { fragment.onBackPressed() }
    }


    override fun setDefaultAvatar() {
        Glide.with(fragment)
                .load(R.drawable.icon_avatar_default)
                .into(root.iv_avatar);
    }


    override fun updateResources() {
        root.tv_personal_info.text = fragment.resources.getString(R.string.title_personal_info)
        root.tv_date_of_birth.text = fragment.resources.getString(R.string.title_date_of_birth)
        root.tv_birth_day.text = fragment.resources.getString(R.string.title_not_set)
        root.tv_location.text = fragment.resources.getString(R.string.title_living_in)
        root.tv_occupation_header.text = fragment.resources.getString(R.string.title_occupation)
        root.tv_occupation.text = fragment.resources.getString(R.string.title_not_set)
        root.tv_personal_tastes.text = fragment.resources.getString(R.string.title_personal_tastes)

        root.tv_days_go_out.text = fragment.resources.getString(R.string.title_not_set)
        root.tv_place_types_profile.text = fragment.resources.getString(R.string.title_not_set)
        root.tv_user_location.text = fragment.resources.getString(R.string.title_not_set)
        root.tv_place_type.text = fragment.resources.getString(R.string.title_place_type_like)
        root.tv_days_open_to_go_out.text = fragment.resources.getString(R.string.title_days_open_to_go_out)
        root.tv_change_password_title.text = fragment.resources.getString(R.string.title_change_password)


        root.tv_title_first_name.text = fragment.resources.getString(R.string.title_first_name)
        root.tv_title_last_name.text = fragment.resources.getString(R.string.title_last_name)


    }


    override fun onEditFirstName(): Observable<Any> {
        return RxView.clicks(root.item_first_name)
    }

    override fun onEditLastName(): Observable<Any> {
        return RxView.clicks(root.item_last_name)
    }


    override fun onSettingsAction(): Observable<Any> {
        return RxView.clicks(root.btn_setting)
    }

    override fun setUserAvatar(img: String) {
        Glide.with(LeMustApp.instance)
                .load((img)).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                        root.iv_avatar.setImageDrawable(resource)
                        root.invalidate()

                        return true
                    }
                }).submit()

    }

    override fun setUserAvatar(img: Bitmap) {
        Glide.with(LeMustApp.instance)
                .load(img).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                        root.iv_avatar.setImageDrawable(resource)
                        root.invalidate()

                        return true
                    }
                }).submit()

    }



    override fun onImageAvatarAction(): Observable<Any> {
        return RxView.clicks(root.iv_avatar)
    }

    override fun showAvatarDialog() {
        if (bottomSheetFragment != null)
            if (!bottomSheetFragment!!.isAdded)
                bottomSheetFragment!!.show(fragment.supportFragmentManager, bottomSheetFragment!!.tag);
    }

    override fun setUserLocation(location: String) {
        root.tv_user_location.text = location
    }

    override fun setOccupation(days: String) {
        root.tv_occupation.text = days
    }

    override fun onLocationAction(): Observable<Any> {
        return RxView.clicks(root.item_location)
    }

    override fun setDaysGoOut(days: String) {
        root.tv_days_go_out.text = days
    }


    override fun onOccupationAction(): Observable<Any> {
        return RxView.clicks(root.item_occupation)
    }


    override fun onChangeActionPassword(): Observable<Any> {
        return RxView.clicks(root.tv_change_password)
    }


    override fun setBirthDate(date: String) {
        root.tv_birth_day.text = date
    }


    override fun onUserBirthDateAction(): Observable<Any> {
        return RxView.clicks(root.item_birth_day)
    }

    override fun showDateDialog(date: String?): Observable<DatePickerFragment.Date> {
        var dialog = DatePickerFragment()
        dialog.showDialog(fragment.supportFragmentManager, date)
        return dialog.dateEvent!!
    }

    override fun setUserSelectedPlaceType(title: String) {
        root.tv_place_types_profile.text = title
    }

    override fun onPlaceTypeAction(): Observable<Any> {
        return RxView.clicks(root.item_place_type)
    }


    override fun onDaysGoOutAction(): Observable<Any> {
        return RxView.clicks(root.item_days_go_out)
    }


    override fun isShowProgressLoader(isShow: Boolean) {
        (fragment).showDefaultProgressLoader(isShow)

    }


}