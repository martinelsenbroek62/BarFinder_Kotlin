package com.lemust.ui.screens.left_menu.localization

import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.ui.screens.left_menu.localization.adapter.LanguageItem
import com.lemust.utils.AppDataHolder
import com.lemust.utils.AppHelper
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.util.DisplayMetrics
import java.util.*


class LanguagePresenter(var view: LanguageContract.View) : LanguageContract.Presenter {
    var selectedLanguage: LanguageItem? = null

    init {
        loadData()
        initAction()
    }

    private fun initAction() {
        view.onClickAction().subscribe {
            selectedLanguage = it
//            apply()

        }
        view.onApplyAction().subscribe {

            //if (NetworkTools.isOnline()) {
            apply()
        }
    }

    private fun apply() {
        if (selectedLanguage != null) {
            AppHelper.locale.setLocale(view.getContext(), selectedLanguage!!.languageCode);
            view.changeLanguage(selectedLanguage!!.languageCode)
            loadData()
            AppDataHolder.citiesLanguageChanged = true
            AppHelper.preferences.setLanguageChangedForProfile(true)
            AppHelper.preferences.setLanguageChangedForMap(true)
            view.finish()
        }
        //            } else{
        //                eventBus.post(MainActivity.ShowNoInternetDialog())
        //            }
        view.dismiss()
    }




    private fun loadData() {
        val languages = view.getContext().resources.getStringArray(R.array.languages)
        val language_codes = view.getContext().resources.getStringArray(R.array.languages_codes)
        var languageItems = ArrayList<LanguageItem>()
        languages.forEachIndexed { index, s ->
            languageItems.add(LanguageItem(language_codes[index], s, false))
        }

        languageItems.filter { it.languageCode == AppHelper.locale.getLanguage(view.getContext()) }.map { it.isSelected = true }
        view.setLanguages(languageItems)

    }

}