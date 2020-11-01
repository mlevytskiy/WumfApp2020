package com.core.core_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinfo.appmonsta.AppInfoView
import com.bumptech.glide.RequestManager
import wumf.com.appsprovider2.AppContainer

class AppViewHolder(parent: ViewGroup, private val likes: Map<String, List<Int>>):
    RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false)) {

    fun bind(app: AppContainer, clickBlock: ((AppContainer, List<Int>)->Unit)?, glide: RequestManager) {
        (itemView as AppInfoView).setLikesHashMap(likes)
        itemView.glide = glide
        itemView.setModel(app)
        itemView.setOnClickListener { clickBlock?.invoke(app, likes.get(app.packageName)?: emptyList()) }
    }

}