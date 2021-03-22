package com.lemust.ui.screens.left_menu


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.screens.main.MainActivity


class LeftMenuFragment : BaseFragment() {
    var view: LeftMenuContract.View? = null
    var presenter: LeftMenuContract.Presenter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_left_menu
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as BaseActivity).activityEventBus.register(this)


        var root = inflater.inflate(R.layout.fragment_left_menu, container, false)
        view = LeftMenuView(activity as MainActivity, root)
        presenter = LeftMenuPresenter(view as LeftMenuView)

        lifecycle.addObserver(view as LeftMenuView)


        return root;



        return root
    }

    companion object {


        fun newInstance(): LeftMenuFragment {
            val fragment = LeftMenuFragment()
//            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
//            args.putString(ARG_PARAM2, param2)
//            fragment.arguments = args
            return fragment
        }
    }


    override fun onDestroyView() {
        (activity as BaseActivity).activityEventBus.unregister(this)
        super.onDestroyView()
    }
}// Required empty public constructor
