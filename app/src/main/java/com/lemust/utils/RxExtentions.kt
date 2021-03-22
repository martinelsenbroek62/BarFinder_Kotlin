package com.lemust.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

    

inline fun <T> onMainThread(observable: Observable<T>): Observable<T> = observable
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(AndroidSchedulers.mainThread())

inline fun <T> onBackgroundThread(observable: Observable<T>): Observable<T> = observable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())