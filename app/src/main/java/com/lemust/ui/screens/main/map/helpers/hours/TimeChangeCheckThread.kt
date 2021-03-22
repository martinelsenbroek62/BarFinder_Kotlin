package com.lemust.ui.screens.main.map.helpers.hours

import io.reactivex.subjects.PublishSubject
import java.util.*

class TimeChangeCheckThread : Runnable {
    var changeHourAction = PublishSubject.create<Any>()
    var rightNow = Calendar.getInstance()
    var currentHour = rightNow.get(Calendar.HOUR_OF_DAY)
    var isRunning = true
    var isPause = false

    override fun run() {
        while (isRunning) {
            if (isPause) break
            Thread.sleep(1000)
            rightNow = Calendar.getInstance()
            if (currentHour != rightNow.get(Calendar.HOUR_OF_DAY)) {
                changeHourAction.onNext(Any())
                currentHour = rightNow.get(Calendar.HOUR_OF_DAY)

            }
        }

    }


    fun pause() {
        isPause = true

    }

    fun resume() {
        isPause = false

    }

    fun onDestroy(){
        changeHourAction.onComplete()
    }
}
