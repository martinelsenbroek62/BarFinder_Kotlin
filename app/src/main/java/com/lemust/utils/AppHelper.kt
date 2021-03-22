package com.lemust.utils

import android.annotation.SuppressLint
import android.content.Context
import com.lemust.repository.api.ApiLeMust
import com.lemust.repository.api.RestManagerImpl
import com.lemust.repository.preferences.PreferenceManager
import com.lemust.repository.preferences.PreferenceManagerImpl

    
@SuppressLint("StaticFieldLeak")
object AppHelper {
    lateinit var context: Context
    var locale = LocaleHelper()
//    var userCredentials = UserCredentials()
    val api: ApiLeMust by lazy { RestManagerImpl().api }
    val preferences: PreferenceManager = PreferenceManagerImpl()
}