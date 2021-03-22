package com.lemust

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.lemust.repository.api.ApiLeMust
import com.lemust.repository.api.RestManagerImpl
import io.fabric.sdk.android.Fabric
import io.paperdb.Paper


class LeMustApp : MultiDexApplication() {

    companion object {
        lateinit var instance: LeMustApp
    }

    val api: ApiLeMust by lazy { RestManagerImpl().api }

    override fun onCreate() {
        super.onCreate()
        Paper.init(this);



//
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
////        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
////                .setDefaultFontPath("fonts/font.ttf")
////                .setFontAttrId(R.attr.fontPath)
////                .build()
////        )
                Fabric.with(this, Crashlytics())

        instance = this

    }
}