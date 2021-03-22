package com.lemust.ui.screens.auth.sign_in


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment


class SignInFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_sign_in
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var view: SingInContract.View? = null
    var presenter: SingInContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_sign_in, container, false)

        var root = inflater.inflate(R.layout.fragment_sign_in, container, false)
        view = SignInView(this, root)
        presenter = SignInPresenter(view as SignInView, (activity as BaseActivity).activityEventBus,this)


        return root;
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SignInFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
