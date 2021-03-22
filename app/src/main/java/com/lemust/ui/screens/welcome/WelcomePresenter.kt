package com.lemust.ui.screens.welcome

import android.content.Context
import com.lemust.R
import com.lemust.ui.screens.welcome.adapter.WelcomePagerItem
import com.lemust.utils.LocaleHelper
import com.squareup.otto.Bus


class WelcomePresenter(var view: WelcomeContract.View, var eventBus: Bus, var context: Context) : WelcomeContract.Presenter {
    var currentPager = 0
    var items = ArrayList<WelcomePagerItem>()

    init {
        initData()
        initListener()

    }

    private fun initListener() {
        view.onPositionAction().subscribe {
            currentPager = it
            if (currentPager == items.size - 1) {
                view.setVisibilityButtonSkip(false)
                view.changeButtonTitle(context.getString(R.string.title_got_it))
            } else {
                view.setVisibilityButtonSkip(true)
                view.changeButtonTitle(context.getString(R.string.title_next))


            }
        }
        view.onButtonAction().subscribe {
            if (currentPager == items.size - 1) {
                view.finishGotIt()
            }
            view.setPositionViewPager(currentPager + 1)

        }
        view.onSkipAction().subscribe {
            view.finishGotIt()

        }
    }


    private fun initData() {
        var item = WelcomePagerItem()

        when (LocaleHelper().getLanguage(context)) {
            "fr" -> {
                item.resourcesImg = R.drawable.img_onboarding_screen_1_fre

            }
            "ru" -> {
                item.resourcesImg = R.drawable.img_onboarding_screen_1_rus

            }
            "uk" -> {
                item.resourcesImg = R.drawable.img_onboarding_screen_1_ukr

            }
            else -> {
                item.resourcesImg = R.drawable.img_onboarding_screen_1_eng

            }
        }

        item.title = context.getString(R.string.tutorial_title_1)
        item.description = context.getString(R.string.text_onboarding1)
        items.add(item)

        item = WelcomePagerItem()
        item.resourcesImg = R.drawable.img_onboarding_screen_2
        item.title = context.getString(R.string.tutorial_title_3)
        item.description = context.getString(R.string.text_onboarding2)
        items.add(item)

        item = WelcomePagerItem()
        item.resourcesImg = R.drawable.img_onboarding_screen_3
        item.title = context.getString(R.string.tutorial_title_2)
        item.description = context.getString(R.string.text_onboarding_3)
        items.add(item)

        view.initViewPager(items)


    }


}