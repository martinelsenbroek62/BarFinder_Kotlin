package com.lemust.ui.screens.auth.sign_up


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment


class SignUpFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var param1: String? = null
    private var param2: String? = null

    var view: SignUpContract.View? = null
    var presenter: SignUpContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var root = inflater.inflate(R.layout.fragment_sign_out, container, false)
        view = SignUpView(this, root)
        presenter = SignUpPresenter(view as SignUpView, (activity as BaseActivity).activityEventBus,this)
        return root;
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SignUpFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
