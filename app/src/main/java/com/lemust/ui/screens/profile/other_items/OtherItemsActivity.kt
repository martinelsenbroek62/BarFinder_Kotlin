package com.lemust.ui.screens.profile.other_items

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lemust.R
import com.lemust.repository.models.rest.user.get.UserOptionDTO
import com.lemust.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_place_type.view.*

class OtherItemsActivity : BaseActivity() {
    companion object {

        const val MUSIC_ALL_TYPES_KEY = "music_types"
        const val MUSIC_SELECTED_TYPES_KEY = "music_selected_types"
        const val ID_KEY = "id_key"

        fun start(context: BaseActivity, currentFavoriteMusic: ArrayList<UserOptionDTO>, currentSelectedFavoriteMusic: ArrayList<UserOptionDTO>,favoriteMusicResult: Int,id: Int) {
            var intent = Intent(context, OtherItemsActivity::class.java)
            intent.putExtra(ID_KEY,id )
            intent.putExtra(MUSIC_ALL_TYPES_KEY, currentFavoriteMusic)
            intent.putExtra(MUSIC_SELECTED_TYPES_KEY, currentSelectedFavoriteMusic)
            context.startActivityForResult(intent,favoriteMusicResult)
        }
    }

    override fun getDefaultFragmentsContainer(): Int? {
        return android.R.id.content
    }

    var view: OtherItemsContract.View? = null
    var presenter: OtherItemsContract.Presenter? = null
    var remainder: OtherItemsContract.Remainder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_type)

        var selectedMusicTypes = intent.getSerializableExtra(MUSIC_SELECTED_TYPES_KEY) as ArrayList<UserOptionDTO>
        var allMusicTypes = intent.getSerializableExtra(MUSIC_ALL_TYPES_KEY) as ArrayList<UserOptionDTO>
        var id = intent.getIntExtra(ID_KEY,-1)


        var root = findViewById<View>(android.R.id.content)
        setDefaultProgressLoader(root.pb_map)

        view = OtherItemsView(this, root)
        remainder = OtherItemsRemainder(this)

        presenter = OtherItemsPresenter(view as OtherItemsView, activityEventBus,remainder as OtherItemsContract.Remainder, selectedMusicTypes, allMusicTypes,id)
    }


    override fun onBackPressed() {
        remainder!!.generateBackPressed()
    }
}
