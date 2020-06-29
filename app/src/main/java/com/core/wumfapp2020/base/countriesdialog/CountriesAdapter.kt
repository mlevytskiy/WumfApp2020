package com.core.wumfapp2020.base.countriesdialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.core.wumfapp2020.R

class CountriesAdapter(private val countries: ArrayList<Country>, private val bgColor1: Int, private val bgColor2: Int): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder? = null
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_dialog_country, parent, false)
            viewHolder = ViewHolder(view)
            view.setTag(viewHolder)
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.fill(countries[position], if (position%2 == 1) bgColor1 else bgColor2)
        return view
    }

    override fun getItem(position: Int): Any {
        return countries[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return countries.size
    }

    class ViewHolder(private val view: View) {

        private val flag: ImageView?
        private val name: TextView?

        init {
            flag = view.findViewById(R.id.flag)
            name = view.findViewById(R.id.country_name)
        }

        fun fill(country: Country, bgColor: Int) {
            flag?.setImageResource(country.flagId)
            name?.setText(country.name)
            view.setBackgroundColor(bgColor)
        }

    }
}