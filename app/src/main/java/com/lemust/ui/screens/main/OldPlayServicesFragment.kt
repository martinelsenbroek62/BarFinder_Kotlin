package com.lemust.ui.screens.main


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.lemust.R
import kotlinx.android.synthetic.main.fragment_old_play_services.view.*


class OldPlayServicesFragment : Fragment() {
    val googleApi = GoogleApiAvailability.getInstance()

    private var currentServicesState: Int? = null
    private var currentCheckUpdated = 0
    private var maxCheckUpdated = 20
    private var delayCheckUpdate: Long = 500
    private var listener: CallbackOldServices? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentServicesState = it.getInt(ARG_SERVICES_STATE)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var root = inflater.inflate(R.layout.fragment_old_play_services, container, false)
        root.btn_update_services.setOnClickListener {
            listener!!.fragmentResultAction(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED)
        }

        if (currentServicesState == ConnectionResult.SERVICE_UPDATING) {
            enableUpdatingState(root)
        }
        return root
    }


    companion object {
        private const val ARG_SERVICES_STATE = "arg_services_state"
        fun newInstance(servicesState: Int) =
                OldPlayServicesFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SERVICES_STATE, servicesState)
                    }
                }

    }


    fun setUpdatingState() {
        currentServicesState = ConnectionResult.SERVICE_UPDATING
        enableUpdatingState(view!!)

    }

    private fun enableUpdatingState(view: View) {
        Log.d("updating_test","enableUpdatingState")
        view.tv_update_services.text = "Services installing..."
        view.btn_update_services.visibility = View.INVISIBLE
        view!!.pb_old_services.visibility = View.VISIBLE
        Handler().postDelayed(run, delayCheckUpdate)

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CallbackOldServices) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    var run = object : Runnable {
        override fun run() {
            currentCheckUpdated++

            var serviceAvailabilityCode = googleApi.isGooglePlayServicesAvailable(context)

            when (serviceAvailabilityCode) {
                ConnectionResult.SUCCESS -> {
                                            view!!.pb_old_services.visibility = View.GONE
                    view!!.tv_update_services.text = "App starting..."
                    Log.d("updating_test","enableUpdatingState SUCCESS : ")

                    listener!!.fragmentResultAction(ConnectionResult.SUCCESS)
                }
                ConnectionResult.SERVICE_UPDATING -> {
                    Log.d("updating_test","enableUpdatingState SERVICE_UPDATING : ")

//                    if (currentCheckUpdated < maxCheckUpdated) {
                        Handler().postDelayed(this, delayCheckUpdate)
                        Log.d("updating_test","enableUpdatingState SERVICE_UPDATING : POst")

//                    } else {
                        Log.d("updating_test","enableUpdatingState SERVICE_UPDATING : SERVICE_VERSION_UPDATE_REQUIRED")
//                        view!!.pb_old_services.visibility = View.GONE
//                        view!!.btn_update_services.visibility = View.VISIBLE
//                        listener!!.fragmentResultAction(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED)


                   // }

                }
            }


        }
    }

    interface CallbackOldServices {
        fun fragmentResultAction(code: Int)

    }
}
