package com.appinfo.appmonsta

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity.CENTER
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import wumf.com.appsprovider2.App
import wumf.com.appsprovider2.AppContainer
import wumf.com.appsprovider2.GooglePlayApp

const val NORMAL_MODE = 0
const val SMALL_MODE = 1

class AppInfoView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val textView: TextView
    private val imageView: ImageView
    private val whoLikesContainer: View
    private val whoLikesTxt: TextView
    var glide : RequestManager? = null
    var mode: Int = NORMAL_MODE
        set(value) {
            field = value
            if (field == SMALL_MODE) {
                setSmallMode(context)
            }
        }

    private var packagesLikes: Map<String, List<Int>>? = null

    init {
        orientation = VERTICAL
        gravity = CENTER
        setBackgroundResource(R.drawable.app_background)
        val view = View.inflate(getContext(), R.layout.view_app_info, this)
        textView = view.findViewById(R.id.text_view)
        imageView = view.findViewById(R.id.image_view)
        whoLikesContainer = view.findViewById(R.id.who_likes_container)
        whoLikesTxt = view.findViewById(R.id.who_likes_txt)
        if (mode == SMALL_MODE) {
            setSmallMode(context)
        }
    }

    fun setSmallMode(context: Context) {
        textView.visibility = GONE
        whoLikesContainer.visibility = GONE
        whoLikesTxt.visibility = GONE
        imageView.layoutParams.height = context.toPixels(30)
        imageView.layoutParams.width = context.toPixels(30)
        imageView.layoutParams = imageView.layoutParams
        imageView.setPadding(0,0,0,0)
        imageView.invalidate()
    }

    fun setModel(model: AppContainer?) {
        model?.app?.let {
            (glide ?: Glide.with(this)).clear(imageView)
            setModel(it)
        } ?:run {
            model?.gpApp?.let {
                Log.i("testrr", "setModel gpApp icon=${model.gpApp?.iconUrl}")
                setModel(it)
            } ?:kotlin.run {
                Log.i("testrr","setModel clearView")
                clearView()
            }
        }
    }

    fun clearView() {
        textView.setText("")
        (glide ?: Glide.with(this)).clear(imageView)
        imageView.setImageDrawable(null)
        imageView.setBackgroundColor(Color.LTGRAY)
        whoLikesContainer.visibility = View.GONE
    }

    fun setLikesHashMap(value: Map<String, List<Int>>) {
        packagesLikes = value
    }

    fun setModel(model: App) {
        textView.setText(model.name)
        setLikes(model.appPackage)
        textView.setTextColor(Color.BLACK)
        model.icon?.let {
            imageView.setImageDrawable(model.icon)
            imageView.setBackgroundColor(Color.TRANSPARENT)
        } ?:run {
            imageView.setImageDrawable(null)
            imageView.setBackgroundColor(Color.LTGRAY)
        }
    }

    fun setModel(model: GooglePlayApp) {
        if (model.name.isNullOrEmpty()) {
            textView.text = "Loading"
        } else {
            textView.text = model.name
        }
        setLikes(model.packageName)
        (glide ?: Glide.with(this)).load(model.iconUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.setBackgroundColor(Color.TRANSPARENT)
                    textView.setTextColor(Color.BLACK)
                    return false
                }
            })
            .into(imageView)
    }

    private fun setLikes(pkg: String) {
        val likes = packagesLikes?.get(pkg)
        likes?.let {
            whoLikesContainer.visibility = View.VISIBLE
            whoLikesTxt.text = "${likes.size}"
        } ?:run {
            whoLikesContainer.visibility = View.GONE
        }
    }

    fun Context.toPixels(dp: Int): Int {
        return (dp * (this.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}