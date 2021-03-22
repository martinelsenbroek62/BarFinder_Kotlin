package com.lemust.ui.screens.main


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.hairdresser.services.socials.facebook.FacebookAuthHelper
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.ui.AppConst
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.navigation.NavigationController
import com.lemust.ui.screens.auth.AuthActivity
import com.lemust.ui.screens.filters.base.FiltersFragment
import com.lemust.ui.screens.left_menu.LeftMenuFragment
import com.lemust.ui.screens.main.map.MainFragment
import com.lemust.utils.AppDataHolder
import com.lemust.utils.AppHelper
import com.lemust.utils.SystemUtils
import com.squareup.otto.Subscribe
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject


class MainActivity : BaseActivity(), OldPlayServicesFragment.CallbackOldServices {
    var onBackPressedAction = PublishSubject.create<Any>()
    var onTakeScreenAction = PublishSubject.create<Any>()
    var onFilterExeptionAction = PublishSubject.create<Throwable>()
    val googleApi = GoogleApiAvailability.getInstance()
    var mServiceAvailabilityCode: Int = 0


    var isSelectCurrentTime = true
    var isActivityRecreated = true

    val ON_ACTIVITY_APP_SERVICE_RESULT = 3
    var LINK_TO_GOOGLE_PLAY_SERVICES: String? = null

    var oldServicesFragment: OldPlayServicesFragment? = null


    companion object {
        val ON_ACTIVITY_RESULT_LOCALE = 2
        val ON_ACTIVITY_RESULT_PLACE_DETAILS = 4
        fun start(context: Context, isViaDeeplink: Boolean = false) {
            // var classNameMap = MainActivity::class.java.name
            //  var mapIsOpened = false;
            //   val activityManager = LeMustApp.instance.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            // val taskList = activityManager.appTasks


//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                taskList.filter { it.taskInfo.baseActivity.className == classNameMap }.map { mapIsOpened = true }
//            } else {
//                val taskList = activityManager.getRunningTasks(10)
//                taskList.filter { it.topActivity.className == classNameMap }.map { mapIsOpened = true }
//            }


//            if (mapIsOpened) {
//               Log.d("Sdsds","dsdsds")
//            }else{
            var intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
//            }


//            if (!isViaDeeplink && mapIsOpened) {
//                //                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
//                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION;
//                context.startActivity(intent)
//
//            }

        }
    }


    public lateinit var navigation: NavigationController

    lateinit var leftContainer: FrameLayout
    lateinit var rightContainer: FrameLayout
    lateinit var mainContainer: FrameLayout
    lateinit var touchContainer: FrameLayout
    var onLanguageAction = BehaviorSubject.create<Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtils.setTransparentForStatusBar(this)
        Log.d("activity_test", "activity start")

        setContentView(R.layout.activity_base_navigation_activityu)
        leftContainer = findViewById(R.id.fl_left_container)
        rightContainer = findViewById(R.id.fl_right_container)
        mainContainer = findViewById(R.id.fl_base_container)
        touchContainer = findViewById(R.id.fl_touch_layout)

        navigation = NavigationController(leftContainer, rightContainer, mainContainer, touchContainer, windowManager)

        hideKeyboard()
        var user = AppHelper.preferences.getUser()






        if (user == null) {
            FacebookAuthHelper.disconnectFromFacebook()
            AppDataHolder.skipCurrentCity()
            AppHelper.preferences.clearToken()
            AppDataHolder.actualUserPhoto = ""


            var intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }





        mServiceAvailabilityCode = googleApi.isGooglePlayServicesAvailable(this)
        LINK_TO_GOOGLE_PLAY_SERVICES = "play.google.com/store/apps/details?id=com.google.android.gms&hl=" + AppHelper.locale.getLanguage(this)

        activityEventBus.register(this)
        handleCriticalOldServices()
        isActivityRecreated = false


    }

    private fun handleCriticalOldServices() {
        when (mServiceAvailabilityCode) {
            ConnectionResult.SUCCESS -> {
                initScreens()

            }
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> {
                if (googleApi.isUserResolvableError(mServiceAvailabilityCode)) {
                    oldServicesFragment = OldPlayServicesFragment.newInstance(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED)
                    //   addFragmentInContentContainer(oldServicesFragment!!)

                }

            }
            ConnectionResult.SERVICE_UPDATING -> {
                oldServicesFragment = OldPlayServicesFragment.newInstance(ConnectionResult.SERVICE_UPDATING)
                //   addFragmentInContentContainer(oldServicesFragment!!)


            }


        }
    }

    override fun fragmentResultAction(status: Int) {
        when (status) {
            ConnectionResult.SUCCESS -> {
                showBaseContent()
            }
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> {
                startActivityForResult(Intent(Intent.ACTION_VIEW, Uri.parse("https://$LINK_TO_GOOGLE_PLAY_SERVICES")), ON_ACTIVITY_APP_SERVICE_RESULT)
            }

        }
    }


    private fun initScreens() {
        Log.d("tset_life_circle", "-- initScreens")


//        addFragmentInLeftContainer(LeftMenuFragment.newInstance())
//        addFragmentInContentContainer(MainFragment.newInstance(false))
//        addFragmentInRightContainer(FiltersFragment.newInstance())
    }


    override fun onRestart() {
        super.onRestart()

        Log.d("tset_life_circle", "ON onRestart")
    }

    override fun onStop() {
        Log.d("tset_life_circle", "ON Stop")
        super.onStop()
    }

//
//    @Subscribe
//    fun onEvent(event: HideFiltersScreen) {
//        // if (event.isAnimat)
//        //  hideMenu()
//        //  else
//        //  hideMenuWithoutAnimated()
//    }


    override fun onPause() {
        super.onPause()
        isSelectCurrentTime = true
    }

    override fun onResume() {
        Log.d("tset_life_circle", "ON onResume")
        val view = getCurrentFocus()
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        }
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        super.onResume()
    }


    override fun onDestroy() {
        isSelectCurrentTime = true



        activityEventBus.unregister(this)

        onBackPressedAction.onComplete()
        super.onDestroy()
        System.gc()
        // reset()
    }

//    @Subscribe
//    fun onEvent(event: ShowFilters) {
//        // showRightMenu()
//    }
//
//    @Subscribe
//    fun onEvent(event: ShowLeftMenu) {
//        // showLeftMenu()
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ON_ACTIVITY_RESULT_PLACE_DETAILS -> {
                isSelectCurrentTime = false
            }
            ON_ACTIVITY_APP_SERVICE_RESULT -> {
                mServiceAvailabilityCode = googleApi.isGooglePlayServicesAvailable(this)
                when (mServiceAvailabilityCode) {
                    ConnectionResult.SUCCESS -> {
                        showBaseContent()
                    }
                    ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> {


                    }
                    ConnectionResult.SERVICE_UPDATING -> {
                        if (oldServicesFragment != null) {
                            if (oldServicesFragment!!.isAdded) {
                                oldServicesFragment!!.setUpdatingState()
                            }
                        }

                    }
                }

            }
        }
    }

    private fun showBaseContent() {
        //  removeContentFragment()
        initScreens()
    }


    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (!navigation.isVisibleMenu()) {
            handleAppExit()
        } else {
            onBackPressedAction.onNext(Any())
        }
    }

    private fun handleAppExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }


    override fun getDefaultFragmentsContainer(): Int? {
        return R.id.fl_right_container
    }


    public class InitFilters(var cityId: Int, var placeTypeId: Int)
    //    public class IsLocationEnabled(var isEnabled: Boolean)
//    public class HideFiltersScreen(var isAnimat: Boolean = true)
//    public class ShowLeftMenu()
    public class FilterChangedToServer()

    //    public class ShowPreviewDialog()
    //public class ShowNoInternetDialog()
    class UpdateResources()
//    class ShowFilters
    //  class OnDialogDismissed

}
