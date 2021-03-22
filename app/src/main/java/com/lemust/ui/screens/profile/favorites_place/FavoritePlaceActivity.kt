package com.lemust.ui.screens.profile.favorites_place

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.repository.models.rest.user.get.UserFavoritePlaceTypeDTO
import com.lemust.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_favorite.view.*

class FavoritePlaceActivity : BaseActivity() {
    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    var view: FavoritePlaceContract.View? = null
    var presenter: FavoritePlaceContract.Presenter? = null
    var remainder: FavoritePlaceContract.Remainder? = null


    companion object {

        const val MUSIC_ALL_TYPES_KEY = "music_types"
        const val MUSIC_SELECTED_TYPES_KEY = "music_selected_types"

        fun start(context: BaseActivity, selectedMusicTypes: ArrayList<UserFavoritePlaceTypeDTO>, allMusicTypes: ArrayList<UserFavoritePlaceTypeDTO>, favoritE_PLACES_RESULT: Int) {
            var intent = Intent(context, FavoritePlaceActivity::class.java)
            intent.putExtra(MUSIC_ALL_TYPES_KEY, allMusicTypes)
            intent.putExtra(MUSIC_SELECTED_TYPES_KEY, selectedMusicTypes)
            context.startActivityForResult(intent,favoritE_PLACES_RESULT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)


        var selectedMusicTypes = intent.getSerializableExtra(MUSIC_SELECTED_TYPES_KEY) as ArrayList<UserFavoritePlaceTypeDTO>
        var allMusicTypes = intent.getSerializableExtra(MUSIC_ALL_TYPES_KEY) as ArrayList<UserFavoritePlaceTypeDTO>


        var root = findViewById<View>(android.R.id.content)
        setDefaultProgressLoader(root.pb_map)

        view = FavoritePlaceView(this, root)
        remainder = FavoritePlaceRemainder(this)

        presenter = FavoritePlacePresenter(view as FavoritePlaceView, activityEventBus, remainder as FavoritePlaceRemainder,selectedMusicTypes, allMusicTypes)

    }

    override fun onBackPressed() {
        remainder!!.generateBackPressed()
    }
}
