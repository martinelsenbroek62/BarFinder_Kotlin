package com.lemust.ui.screens.details;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Objects;

public class ApplicationSelectorReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("INFORMATION", "Received intent after selection: "+intent.getExtras().get(Intent.EXTRA_CHOSEN_COMPONENT));    }
}
