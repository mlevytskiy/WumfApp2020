package com.core.wumfapp2020.base

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.appinfo.appmonsta.AppInfoView
import com.core.wumfapp2020.DynamicApp
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.countriesdialog.CountriesAdapter
import com.core.wumfapp2020.databinding.DialogAppBinding
import com.core.wumfapp2020.databinding.DialogAppInMyCollectionBinding
import com.core.wumfapp2020.databinding.DialogCheckAppInGooglePlayBinding
import com.core.wumfapp2020.di.AppComponent
import wumf.com.appsprovider2.AppContainer
import wumf.com.detectphone.Country


var dialog : AlertDialog? = null //last dialog
fun showCheckAppIfExistOnGooglePlayDialog(context: Context, appContainer: AppContainer, addPkgInMemory: ()->Unit,
                                          moveToAddToMyCollectionScreen: ()->Unit): DialogInterface {
    val dialog = createDialogBuilder(context)
        .setView(createCheckAppIfExistOnGooglePlayDialogView(context, appContainer, addPkgInMemory, moveToAddToMyCollectionScreen, { dialog?.dismiss() } ))
        .show()

    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
    com.core.wumfapp2020.base.dialog = dialog
    return dialog
}

private fun createCheckAppIfExistOnGooglePlayDialogView(context: Context, appContainer: AppContainer, addPkgInMemory: ()->Unit,
                                                        moveToAddToMyCollectionScreen: ()->Unit, dismissDialog: ()->Unit): View {
    val binding = DialogCheckAppInGooglePlayBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as @org.jetbrains.annotations.NotNull LayoutInflater)
    val viewModel = getAppComponent().checkAppInGooglePlayFactory.create(appContainer, addPkgInMemory, moveToAddToMyCollectionScreen, dismissDialog)
    binding.viewModel = viewModel
    viewModel.check()
    return binding.root
}

private fun getAppComponent(): AppComponent = DynamicApp.instance?.component!!

private fun createDialogBuilder(context: Context): AlertDialog.Builder {
    return AlertDialog.Builder(context)
        .setTitle("")
        .setNegativeButton(R.string.close) { dialog, _ -> dialog.dismiss() }
}

fun showCountriesDialog(context: Context, countries: ArrayList<Country>, checkedItem: Int, select: (Country)->Unit): DialogInterface {
    val dialog = createDialogBuilder(context).setView(createCountriesView(context, countries, select)).show()
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
    return dialog
}
private fun createCountriesView(context: Context, countries: ArrayList<Country>, select: (Country)->Unit): View {
    val li = LayoutInflater.from(context)
    val listView = li.inflate(R.layout.dialog_countries, null) as ListView
    val gray = context.resources.getColor(R.color.observatory_alpha_6)
    val adapter = CountriesAdapter(countries, Color.WHITE, gray)
    listView.adapter = adapter
    listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ -> select(countries[position]) }
    return listView
}

fun showLanguageDialog(context: Context, arrayId: Int) {
    //
}

fun showAppDialogFromMyCollection(appContainer: AppContainer, context: Context, showInGPBlock: ()->Unit, removeFromMyCollection: ()->Unit) : DialogInterface {
    val dialog = createDialogBuilder(context).setView(createAppDialogViewFromMyCollection(appContainer, context, showInGPBlock, removeFromMyCollection)).show()
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
    return dialog
}

fun createAppDialogViewFromMyCollection(appContainer: AppContainer, context: Context,
                        showInGPBlock: ()->Unit, removeFromMyCollection: ()->Unit): View {
    val binding = DialogAppInMyCollectionBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as @org.jetbrains.annotations.NotNull LayoutInflater)
    val viewModel = getAppComponent().appInMyCollectionViewModelFactory.create(appContainer, showInGPBlock, removeFromMyCollection)
    binding.viewModel = viewModel
    return binding.root
}

fun showAppDialog(appContainer: AppContainer, context: Context, showInGPBlock: ()->Unit, showPeopleWhoLikesBlock: ()->Unit,
                  peopleWhoLikes: List<Int>): DialogInterface {
    val dialog = createDialogBuilder(context).setView(createAppDialogView(appContainer, context, showInGPBlock, showPeopleWhoLikesBlock, peopleWhoLikes)).show()
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
    return dialog
}

fun createAppDialogView(appContainer: AppContainer, context: Context,
                        showInGPBlock: ()->Unit, showPeopleWhoLikesBlock: ()->Unit,
                        peopleWhoLikes: List<Int>): View {
    val binding = DialogAppBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as @org.jetbrains.annotations.NotNull LayoutInflater)
    val viewModel = getAppComponent().appViewModelFactory.create(appContainer, showPeopleWhoLikesBlock, showInGPBlock, peopleWhoLikes.size)
    binding.viewModel = viewModel
    return binding.root
}

fun showSimpleDialog(context: Context, arrayId: Int, checkedItem: Int, select: (Array<String>, Int)->Unit, cancel: (DialogInterface)->Unit,
                     defCountryName: String ) {
    val array = context.resources.getStringArray(arrayId)
    array[1] = String.format(array[1], defCountryName)
    createDialogBuilder(context)
        .setSingleChoiceItems(array, checkedItem, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, position: Int) {
                dialog?.dismiss()
                select(array, position)
            }
        })
        .setNegativeButton(R.string.close, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                cancel(dialog)
            }
        })
        .show()
}