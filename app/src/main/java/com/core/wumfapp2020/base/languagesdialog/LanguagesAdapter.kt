package com.core.wumfapp2020.base.languagesdialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.core.wumfapp2020.R
import wumf.com.detectphone.Country

class LanguagesAdapter(private val languages: Array<String>, private val bgColor1: Int, private val bgColor2: Int): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder? = null
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_dialog_language, parent, false) as TextView
            viewHolder = ViewHolder(view)
            view.setTag(viewHolder)
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.fill(languages[position], if (position%2 == 1) bgColor1 else bgColor2)
        return view
    }

    override fun getItem(position: Int): Any {
        return languages[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return languages.size
    }

    class ViewHolder(private val view: TextView) {

        fun fill(language: String, bgColor: Int) {
            view.text = language
            view.setBackgroundColor(bgColor)
        }

    }
}