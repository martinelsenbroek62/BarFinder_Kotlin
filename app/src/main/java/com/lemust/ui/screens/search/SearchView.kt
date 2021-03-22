package com.lemust.ui.screens.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.inputmethodservice.KeyboardView
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.google.gson.GsonBuilder
import com.jakewharton.rxbinding2.widget.RxTextView
import com.lemust.R
import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.ui.AppConst
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.main.MainActivity
import com.lemust.ui.screens.search.adapter.SearchAdapter
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.lemust.utils.Tools
import com.lemust.utils.onMainThread
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.view.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import timber.log.Timber
import java.util.concurrent.TimeUnit


class SearchView(var activity: BaseActivity, var root: View) : SearchContract.View , BaseView(activity!!){
    override fun getSearchText(): String {
return root.et_places.text.toString()
    }

    override fun showDialogException(e: Throwable): Observable<DialogModel.OnDialogResult> {

        var errorMessage = ErrorUtils(e, false,activity)
        errorMessage.parse()
            return showDialog(DialogModel().build(activity, errorMessage.titleError)
                    .showMessage(errorMessage.bodyError)
                    .showLastButton(activity.resources.getString(R.string.title_reload))
                    .showFirstButton(activity.resources.getString(R.string.title_cancel))
                    .single(true))

    }

    override fun showPlace(place: SearchItemDTO) {
        AppHelper.preferences.saveSearchedItem(place)
        activity.setResult(Activity.RESULT_OK);
        activity.finish()
    }

    private var recyclerPlaces = root.findViewById<RecyclerView>(R.id.rv_places)
    private var placeItems = ArrayList<SearchItemDTO>()
    private var placesAdapter: SearchAdapter? = null
    private var isVisibleRemoveTextIcon = false
    private var clearAction = PublishSubject.create<Any>()
    private var onScrollDown = PublishSubject.create<Any>()
    private var unregister: Unregistrar? = null

    override fun unregister() {
        if (unregister != null) {
            unregister!!.unregister()
        }
        onScrollDown.onComplete()
        clearAction.onComplete()
    }

    override fun setVisibleEmptyMessage(isVisible: Boolean) {
        if (isVisible) {
            root.empty_message.visibility = View.VISIBLE
        } else {
            root.empty_message.visibility = View.GONE

        }
    }

    override fun hideKeyboard() {
        try {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(root.et_places!!.windowToken, 0)
        } catch (e: Exception) {
            Timber.e("[search screen] [hideKeyboard] err: %s", e.message)

        }
        val view = activity!!.currentFocus
        if (view != null) {
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        activity!!.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }


    override fun onScrollDown(): Observable<Any> {
        return onScrollDown
    }

    init {
        initRecycler()

        root.iv_back.setOnClickListener { activity.finish() }
        root.et_places.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (isVisibleRemoveTextIcon)
                if (event.action === MotionEvent.ACTION_UP) {
                    if (root.et_places.compoundDrawables[DRAWABLE_RIGHT] != null)
                        if (event.rawX >= root.et_places.right - root.et_places.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                            root.et_places.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                            clearAction.onNext(Any())
                            root.et_places.setText(R.string.empty)
                            placesAdapter!!.clear()

                            return@OnTouchListener true
                        }
                }
            false
        })

        root.et_places.requestFocus()
        activity.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        unregister = KeyboardVisibilityEvent.registerEventListener(activity!!) {
            if (it) {
//                var layoutPar = FrameLayout.LayoutParams(  root.pb_map.layoutParams.width Tools.convertDpToPixel(230f, fragment).toInt())
                var layoutPar = FrameLayout.LayoutParams(root.pb_map.layoutParams.width, root.pb_map.layoutParams.width)

                val keyboardView = KeyboardView(activity, null)


                layoutPar.bottomMargin = keyboardView.height + Tools.convertDpToPixel(60f, activity!!).toInt()
                layoutPar.gravity = Gravity.CENTER
                root.pb_map.layoutParams = layoutPar
            } else {
                var layoutPar = FrameLayout.LayoutParams(root.pb_map.layoutParams.width, root.pb_map.layoutParams.width)
                layoutPar.bottomMargin = 0
                layoutPar.gravity = Gravity.CENTER

                root.pb_map.layoutParams = layoutPar

            }
        }


    }

    var firstVisibleInListview: Int = 0
    private fun initRecycler() {
        var managerSubFilters = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerPlaces.layoutManager = managerSubFilters
        placesAdapter = SearchAdapter(placeItems)
        recyclerPlaces.adapter = placesAdapter
        firstVisibleInListview = managerSubFilters.findFirstVisibleItemPosition()

        recyclerPlaces.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    onScrollDown.onNext(Any())
                } else {
                }

            }


        })

    }

    override fun setData(data: List<SearchItemDTO>) {
        placeItems.clear()
        placeItems.addAll(data)
        placesAdapter!!.notifyDataSetChanged()

    }

    override fun getPlacesName(): Observable<CharSequence> {
        return onMainThread(RxTextView.textChanges(root.findViewById(R.id.et_places))
                .skip(0).debounce(900, TimeUnit.MILLISECONDS))
    }

    override fun dismiss() {
        activity.finish()
    }

    override fun setVisibleLoader(isVisible: Boolean) {
        if (isVisible) {
            root.pb_map.visibility = View.VISIBLE
        } else {
            root.pb_map.visibility = View.GONE

        }
    }

    override fun setVisibleContent(isVisible: Boolean) {
        if (isVisible) {
            root.rv_places.visibility = View.VISIBLE
        } else {
            root.rv_places.visibility = View.GONE

        }

    }

    override fun isEnabledEditText(isEnabled: Boolean) {
        root.et_places.isEnabled = isEnabled
    }

    override fun onTouchItemEvent(): Observable<SearchItemDTO> {
        return placesAdapter!!.clickListener
    }


    override fun setVisibleRightEditTextIcon(isVisible: Boolean) {
        try {
            if (isVisible) {
                isVisibleRemoveTextIcon = true
                root.et_places.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        activity.resources.getDrawable(R.drawable.icn_clear_text, null), null)
            } else {
                isVisibleRemoveTextIcon = false

                root.et_places.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            }
        } catch (e: Exception) {
            System.err.print(e.stackTrace)
        }

    }

    override fun onClearAction(): Observable<Any> {
        return clearAction
    }
}