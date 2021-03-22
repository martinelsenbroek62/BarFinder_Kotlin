package com.lemust.ui.screens.reports.report_details

import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.report_details.Option
import com.lemust.repository.models.rest.report_details.PlaceDetailsReportDTO
import com.lemust.repository.models.rest.report_details.PlaceFilterField
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.reports.report_details.adapter.ModelItemsGenerator
import com.lemust.ui.screens.reports.report_details.adapter.Pricetem
import com.lemust.ui.screens.reports.report_details.adapter.TextItem
import com.lemust.ui.screens.reports.report_details.adapter.TypeItem
import com.lemust.utils.AppHelper
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody


class ReportDetailsPresenter(var view: ReportDetailsContract.View, var eventBus: Bus, var remainder: ReportDetailsContract.Remainder, var placeId: Int, var placeTypeIdKey: String) : ReportDetailsContract.Presenter {


    var currentProgress: Int = 0
    var saveDetailsDisposable: Disposable? = null
    var loadDetailsDisposable: Disposable? = null
    var placeDetailsReport: PlaceDetailsReportDTO? = null


    companion object {
        const val PRICE_ID = -1
        const val EDIT_ITEM_ID = -5
        const val FEATURED_ITEM_ID = -2
    }

    override fun onDestroy() {
        if (saveDetailsDisposable != null) {
            if (!saveDetailsDisposable!!.isDisposed) {
                saveDetailsDisposable!!.dispose()
            }
        }
        if (loadDetailsDisposable != null) {
            if (!loadDetailsDisposable!!.isDisposed) {
                loadDetailsDisposable!!.dispose()
            }

        }
    }

    init {

        loadData()
        initAction()
    }

    private fun initAction() {
        view.onSendReportAction().subscribe {
            handleEditPlaces()


        }
        view.onEditNameAction().subscribe {
            remainder.openEditNameScreen(placeDetailsReport!!.name!!)
        }
        remainder.onEditPlaceNameResultAction().subscribe {
            placeDetailsReport!!.name = it
            view.setPlaceName(it)
        }

        remainder.onNewItemResultAction().subscribe {
            if (placeDetailsReport != null) {
                var itemText = TextItem(it.newItemName, false, it.itemId, 1, Option(it.newItemName), TypeItem.TEXT_ITEM, isBackgroundFill = it.isBackgroundFill, isVisibleDivider = true)
                itemText.isCustomItem = true
                view.addNewItem(itemText, it.position + 1)
            } else {
                view.showToast("Please try again your last action")
            }
            // itemsData.add(adapterPosition, itemText)

        }
        view.onNewItemAction().subscribe {
            if (placeDetailsReport != null)
                remainder.openAddItemScreen(it)
            else
                view.showToast("Please try again your last action")
        }
    }

    private fun handleEditPlaces() {

        var priceElements = view.getItems().filter { it.id == PRICE_ID && it.typeItem != TypeItem.TITLE }

        var checkedPrice = priceElements.filter { (it as Pricetem).isChecked }


        var price: Int? = null
        if (checkedPrice.isNotEmpty())
            price = (checkedPrice[0] as Pricetem).obj as Int

        placeDetailsReport!!.googlePriceLevel = price

        var textItems = view.getItems().filter { it.typeItem == TypeItem.TEXT_ITEM }
        var custom = textItems.filter { (it as TextItem).isCustomItem }


        custom.forEach { custom ->
            placeDetailsReport!!.placeFilterField!!.filter { custom.id == it.id }.map { it.options.add((custom as TextItem).obj as Option) }
        }


        var priceFeatured = view.getItems().filter { it.id == FEATURED_ITEM_ID && it.typeItem == TypeItem.TEXT_ITEM }.map { it as TextItem }

        priceFeatured.forEach { priceFeatured ->
            placeDetailsReport!!.placeFilterField!!.filter {
                priceFeatured.childId == it.id
            }.map {
                it.data = priceFeatured.isChecked
            }
        }


        AppHelper.api.savePlaceDetailsItems(placeId, placeTypeIdKey, placeDetailsReport!!).subscribe(object : Observer<ResponseBody> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                if (saveDetailsDisposable != null) {
                    if (!saveDetailsDisposable!!.isDisposed) {
                        saveDetailsDisposable!!.dispose()
                    }
                }
                saveDetailsDisposable = d
            }

            override fun onNext(new: ResponseBody) {
                view.isShowProgressLoader(false)

                view.showPositiveDialogOkCallback(view.getViewContext().getString(R.string.thanks_for_you_update), view.getViewContext().getString(R.string.thanks_update)).subscribe {
                    remainder.dismiss()

                }
            }

            override fun onError(e: Throwable) {
                view.isShowProgressLoader(false)

                Handler(Looper.getMainLooper()).post {
                    view.showDialog(DialogModel().build(remainder.getContext(), remainder.getContext().resources.getString(R.string.no_internet_connection))
                            .showLastButton(remainder.getContext().resources.getString(R.string.title_reload))
                            .showFirstButton(remainder.getContext().resources.getString(R.string.title_ok))
                            .single(true)).subscribe {
                        if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                            it.dialog.dismiss()

                        }
                        if (DialogModel.State.SECOND_BUTTON == it.clicked) {
                            handleEditPlaces()
                            it.dialog.dismiss()



                        }
                    }
                }
            }
        })


    }


    private fun loadData() {
        view.isShowProgressLoader(true)
        view.isVisibleContent(false)
        AppHelper.api.getPlaceDetailsItems(placeId, placeTypeIdKey).subscribe(object : Observer<PlaceDetailsReportDTO> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                if (loadDetailsDisposable != null) {
                    if (!loadDetailsDisposable!!.isDisposed) {
                        loadDetailsDisposable!!.dispose()
                    }
                }
                loadDetailsDisposable = d
            }

            override fun onNext(t: PlaceDetailsReportDTO) {
                // testPost(t)
                placeDetailsReport = t
                generateItems(t)
                view.isShowProgressLoader(false)
                view.isVisibleContent(true)
            }

            override fun onError(e: Throwable) {
                view.isShowProgressLoader(false)

                Handler(Looper.getMainLooper()).post {
                    view.showDialog(DialogModel().build(remainder.getContext(), remainder.getContext().resources.getString(R.string.no_internet_connection))
                            .showLastButton(remainder.getContext().resources.getString(R.string.title_reload))
                            .showFirstButton(remainder.getContext().resources.getString(R.string.title_cancel))
                            .single(true)).subscribe {
                        if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                            it.dialog.dismiss()
                            remainder.dismiss()

                        }
                        if (DialogModel.State.SECOND_BUTTON == it.clicked) {
                            it.dialog.dismiss()
                            loadData()


                        }
                    }
                }
            }
        })
    }


    private fun generateItems(t: PlaceDetailsReportDTO) {
        var generator = ModelItemsGenerator()
        var featured = ArrayList<PlaceFilterField>()

        generator.addEditPlaceNameItem(t.name!!, EDIT_ITEM_ID)

        generator.addPriceCategory(t.googlePriceLevel, PRICE_ID,remainder.getContext().resources.getString(R.string.title_price))



        t.placeFilterField!!.forEach {
            if (it.filterFieldType == "choice") {
                generator.addHeader(it.name, it.id)
                addTextItems(it.options, generator)
                generator.addButton(it.name)
            } else if (it.filterFieldType == "bool") {
                featured.add(it)

            }

        }

        if (featured.isNotEmpty()) {
            generator.addHeader(view.getViewContext().resources.getString(R.string.title_features_report), FEATURED_ITEM_ID)
            addFeatureItems(featured, generator)
        }






        view.setDetails(generator.list)


    }

    private fun addFeatureItems(featured: ArrayList<PlaceFilterField>, generator: ModelItemsGenerator) {
        var changeBackgroundIndicator = 0
        for (i in 0 until featured.size - 1) {
            changeBackgroundIndicator++
            generator.addTextItem(featured[i].name, featured[i].id, featured[i].data as Boolean, featured[i], false, changeBackgroundIndicator % 2 == 1, true)
        }
        changeBackgroundIndicator++
        generator.addTextItem(featured[featured.size - 1].name, featured[featured.size - 1].id, featured[featured.size - 1].data as Boolean, featured[featured.size - 1], false, changeBackgroundIndicator % 2 == 1, true)


    }

    private fun addTextItems(options: List<Option>, generator: ModelItemsGenerator) {
        var changeBackgroundIndicator = 0
        for (i in 0 until options.size - 1) {
            changeBackgroundIndicator++
            generator.addTextItem(options[i].option!!, options[options.size - 1].id!!, options[i].isSelected!!, options[i], false, changeBackgroundIndicator % 2 == 1)
        }
        changeBackgroundIndicator++
        generator.addTextItem(options[options.size - 1].option!!, options[options.size - 1].id!!, options[options.size - 1].isSelected!!, options[options.size - 1], true, changeBackgroundIndicator % 2 == 1)


    }

}