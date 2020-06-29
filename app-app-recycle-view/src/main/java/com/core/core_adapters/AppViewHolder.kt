package com.core.core_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinfo.appmonsta.AppInfoView
import wumf.com.appsprovider2.AppContainer

class AppViewHolder(parent: ViewGroup, private val likes: Map<String, List<Int>>):
    RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false)) {

    fun bind(app: AppContainer, clickBlock: ((AppContainer, List<Int>)->Unit)?) {
        (itemView as AppInfoView).setLikesHashMap(likes)
        itemView.setModel(app)
        itemView.setOnClickListener { clickBlock?.invoke(app, likes.get(app.packageName)?: emptyList()) }
    }

}