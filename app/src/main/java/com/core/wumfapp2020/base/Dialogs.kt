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
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.countriesdialog.CountriesAdapter
import com.core.wumfapp2020.base.countriesdialog.Country
import wumf.com.appsprovider2.AppContainer

fun showCountriesDialog(context: Context, countries: ArrayList<Country>, checkedItem: Int, select: (Country)->Unit, cancel: (DialogInterface)->Unit): DialogInterface {
    val dialog = AlertDialog.Builder(context)
        .setTitle("")
        .setView(createCountriesView(context, countries, select))
        .setNegativeButton(R.string.close, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                cancel(dialog)
            }
        }).show()

    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))

    return dialog
}
private fun createCountriesView(context: Context, countries: ArrayList<Country>, select: (Country)->Unit): View {
    val li = LayoutInflater.from(context)
    val listView = li.inflate(R.layout.dialog_countries, null) as ListView
    val gray = context.resources.getColor(R.color.observatory_alpha_6)
    val adapter = CountriesAdapter(countries, Color.WHITE, gray)
    listView.adapter = adapter
    listView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            select(countries[position])
        }

    })
    return listView
}

fun showLanguageDialog(context: Context, arrayId: Int) {
    //
}

fun showAppDialog(appContainer: AppContainer, context: Context, showInGPBlock: ()->Unit, showPeopleWhoLikesBlock: ()->Unit,
                  peopleWhoLikes: List<Int>): DialogInterface {
    val dialog = AlertDialog.Builder(context)
        .setTitle("")
        .setView(createAppDialogView(appContainer, context, showInGPBlock, showPeopleWhoLikesBlock, peopleWhoLikes))
        .setNegativeButton(R.string.close, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                //?
            }
        }).show()

    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))

    return dialog
}

fun createAppDialogView(appContainer: AppContainer, context: Context,
                        showInGPBlock: ()->Unit, showPeopleWhoLikesBlock: ()->Unit,
                        peopleWhoLikes: List<Int>): View {
    //Todo: should be rewrite using databinding
    val li = LayoutInflater.from(context)
    val view = li.inflate(R.layout.dialog_app, null)
    val appInfo = view.findViewById<AppInfoView>(R.id.app_info)
    appInfo.setModel(appContainer)
    val showInGP = view.findViewById<View>(R.id.show_in_google_play)
    showInGP.setOnClickListener { showInGPBlock() }
    val showPeopleWhoLike = view.findViewById<View>(R.id.show_people_who_likes)
    showPeopleWhoLike.setOnClickListener { showPeopleWhoLikesBlock() }
    val whoLikesTxt = view.findViewById<TextView>(R.id.who_likes_txt2)
    whoLikesTxt.text = "${peopleWhoLikes.size}"
    return view
}


fun showSimpleDialog(context: Context, arrayId: Int, checkedItem: Int, select: (Array<String>, Int)->Unit, cancel: (DialogInterface)->Unit,
                     defCountryName: String ) {
    val array = context.resources.getStringArray(arrayId)
    array[1] = String.format(array[1], defCountryName)
    AlertDialog.Builder(context)
        .setTitle("")
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