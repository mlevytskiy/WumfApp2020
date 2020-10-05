package com.core.wumfapp2020

import android.graphics.Typeface
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.TextUtils
import android.text.method.TransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout.VERTICAL
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DataBindingAdapters {

    @JvmStatic
    @BindingAdapter("filePath")
    fun setImage(view: ImageView, filePath: String?) {
        filePath?.let {
            val imgFile = File(filePath)
            if(imgFile.exists()) {
                view.setImageURI(Uri.fromFile(imgFile))
            }
        }
    }

    @JvmStatic
    @BindingAdapter("animatedDrawable")
    fun bindAnimation(imageView: ImageView, isStart: Boolean) {
        val animatedDrawable = imageView.drawable as Animatable
        if (isStart) animatedDrawable.start() else animatedDrawable.stop()
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun bindVisibility(view: View, visibility: Boolean) {
        view.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibilityAnimated", "inAnim", "outAnim")
    fun bindVisibilityAnimated(view: View, visibilityAnimated: Boolean, inAnim: Animation?, outAnim: Animation?) {
        if (visibilityAnimated && view.visibility != View.VISIBLE) {
            if (inAnim != null) {
                view.startAnimation(inAnim)
            }
            view.visibility = View.VISIBLE
        } else if (!visibilityAnimated && view.visibility == View.VISIBLE) {
            if (outAnim != null) {
                outAnim.setAnimationListener(object : SimpleAnimationListener() {
                    override fun onAnimationEnd(animation: Animation) {
                        view.visibility = View.GONE
                    }
                })
                view.startAnimation(outAnim)
            } else {
                view.visibility = View.GONE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageViewResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("android:background")
    fun setBgResId(view: View, imageResId: Int) {
        view.setBackgroundResource(imageResId)
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("transformationMethod")
    fun setTransformationMethod(view: EditText, method: TransformationMethod?) {
        if (method != null) {
            view.transformationMethod = method
            view.setSelection(view.length())
        }
    }

    @JvmStatic
    @BindingAdapter("drawableStartResId")
    fun drawableStartResId(textView: TextView, resId: Int) {
        val drawables = textView.compoundDrawables
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            if (resId != 0) ContextCompat.getDrawable(textView.context, resId) else null,
            drawables[1],
            drawables[2],
            drawables[3]
        )
    }

    @JvmStatic
    @BindingAdapter("drawableEndResId")
    fun drawableEndResId(textView: TextView, resId: Int) {
        val drawables = textView.compoundDrawables
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawables[0],
            drawables[1],
            if (resId != 0) ContextCompat.getDrawable(textView.context, resId) else null,
            drawables[3]
        )
    }

    @JvmStatic
    @BindingAdapter("drawableBottomResId")
    fun drawableBottomResId(textView: TextView, resId: Int) {
        val drawables = textView.compoundDrawables
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawables[0],
            drawables[1],
            drawables[2],
            if (resId != 0) ContextCompat.getDrawable(textView.context, resId) else null
        )
    }

    @JvmStatic
    @BindingAdapter("textResId")
    fun setTextResId(textView: TextView, resource: Int) {
        if (resource != 0) {
            textView.text = textView.context.getText(resource)
        } else {
            textView.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("app:textSpannable")
    fun setTextSpannable(textView: TextView, textSpannable: Spannable) {
        textView.text = textSpannable
    }

    @JvmStatic
    @BindingAdapter("textHtml")
    fun setTextHtml(textView: TextView, text: String) {
        if (!TextUtils.isEmpty(text)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                textView.text = Html.fromHtml(text)
            }
        } else {
            textView.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter("textHtmlRes")
    fun setTextHtmlRes(textView: TextView, @StringRes resId: Int) {
        val text: String = textView.context.resources.getString(resId)
        if (!TextUtils.isEmpty(text)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                textView.text = Html.fromHtml(text)
            }
        } else {
            textView.text = ""
        }
    }

//    @JvmStatic
//    @BindingAdapter("requestFocus")
//    fun requestFocus(et: EditText, request: Boolean) {
//        if (request) {
//            et.post {
//                et.requestFocus()
//                et.setSelection(et.text.length)
//                et.showKeyboard()
//            }
//        }
//    }

    @JvmStatic
    @BindingAdapter("disableTouch")
    fun disableTouch(v: View, disable: Boolean) {
        if (disable) {
            v.setOnClickListener { }
            v.setOnTouchListener { view, motionEvent -> true }
        }
    }

    @JvmStatic
    @BindingAdapter("setOnEditorActionListener")
    fun setOnEditorActionListener(et: EditText, listener: TextView.OnEditorActionListener) {
        et.setOnEditorActionListener(listener)
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun setPageAdapter(viewPager: ViewPager, adapter: PagerAdapter) {
        viewPager.offscreenPageLimit = adapter.count - 1
        viewPager.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("hasFixedSize")
    fun setRecyclerViewFixedSize(recyclerView: RecyclerView, fixedSize: Boolean) {
        recyclerView.setHasFixedSize(fixedSize)
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun <VH : RecyclerView.ViewHolder?> setRecyclerViewAdapter(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<VH>
    ) {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("decoratorDrawable")
    fun setVerticalDecorator(recyclerView: RecyclerView, drawable: Int) {
        val itemDecorator = DividerItemDecoration(recyclerView.context, VERTICAL)

        itemDecorator.setDrawable(ContextCompat.getDrawable(recyclerView.context, drawable)!!)
        recyclerView.addItemDecoration(itemDecorator)
    }

    @JvmStatic
    @BindingAdapter("pageMargin")
    fun setPageMargin(viewPager: ViewPager, spacing: Float) {
        viewPager.pageMargin = spacing.toInt()
    }

    @JvmStatic
    @BindingAdapter("isBold")
    fun setTypeface(tv: TextView, isBold: Boolean) {
        tv.setTypeface(null, if (isBold) Typeface.BOLD else Typeface.NORMAL)
    }

    @JvmStatic
    @BindingAdapter("currentPagePosition")
    fun bindCurrentPosition(view: ViewPager, position: Int) {
        view.post {
            try {
                view.currentItem = position
            } catch (ignore: IllegalStateException) {
            }
        }
    }

    @JvmStatic
    @BindingAdapter("currentPagePosition", "smoothScroll")
    fun bindCurrentPosition(view: ViewPager, position: Int, smoothScroll: Boolean) {
        view.post {
            try {
                if (smoothScroll) {
                    view.currentItem = position
                } else {
                    view.setCurrentItem(position, false)
                }
            } catch (ignore: IllegalStateException) {
            }
        }
    }

    @JvmStatic
    @BindingAdapter("viewAnimation")
    fun bindAnimation(view: View, anim: Animation?) {
        anim?.let {
            if (!it.hasEnded()) view.startAnimation(it)
        }
    }

    @JvmStatic
    @BindingAdapter("activated")
    fun bindActivated(view: ImageView, activated: Boolean) {
        view.isActivated = activated
    }

    @JvmStatic
    @BindingAdapter("animatedDrawable")
    fun bindAnimatedDrawable(imageView: ImageView, animDrawable: Drawable) {
        imageView.setImageDrawable(animDrawable)
        imageView.post { (animDrawable as? AnimationDrawable)?.start() }
    }

    @JvmStatic
    @BindingAdapter("layoutFullscreen")
    fun layoutFullscreen(view: View, set: Boolean) {
        if (set) {
            view.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    @JvmStatic
    @BindingAdapter("setStatusBarHeight")
    fun setStatusBarHeight(view: View, set: Boolean) {
        if (set) {
            view.setOnApplyWindowInsetsListener { v, insets ->
                v.layoutParams.height = insets.systemWindowInsetTop
                insets
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:layout_marginTop")
    fun setStatusBarHeight(view: androidx.appcompat.widget.Toolbar, marginTop: Int) {
        val params = view.layoutParams
        if (params is ViewGroup.MarginLayoutParams) {
            params.topMargin = marginTop
            view.layoutParams = params
        }
    }


    @JvmStatic
    @BindingAdapter("setStatusBarMarginTop")
    fun setStatusBarMarginTop(view: View, set: Boolean) {
        if (set) {
            view.setOnApplyWindowInsetsListener { v, insets ->
                val params = v.layoutParams
                if (params is ViewGroup.MarginLayoutParams) {
                    params.topMargin = insets.systemWindowInsetTop
                    v.layoutParams = params
                }
                insets
            }
        }
    }

    @JvmStatic
    @BindingAdapter("items", "item_layout")
    fun <T> setStuzoRecyclerViewAdapter(recyclerView: RecyclerView, items: List<T>?, itemLayoutResId: Int) = recyclerView.adapter?.let {
        @Suppress("UNCHECKED_CAST")
        (recyclerView.adapter as CommonRecyclerAdapter<T, CommonViewHolder>).setItems(items)
    } ?: run {
        recyclerView.adapter = CommonRecyclerAdapter(items ?: emptyList(), itemLayoutResId, CommonViewHolder::class)
    }

    class CommonRecyclerAdapter<T, VH : CommonAbstractViewHolder>(
        items: List<T>,
        private val mItemViewId: Int,
        private val vhkClass: KClass<VH>
    ) : RecyclerView.Adapter<VH>() {
        private val myItems = ArrayList<T>().apply { addAll(items) }

        fun setItems(items: List<T>?) {
            myItems.clear()
            items?.let { myItems.addAll(it) }
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, i: Int): VH {
            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                mItemViewId,
                parent,
                false
            )
            return vhkClass.primaryConstructor?.call(binding) ?: error("Can't create ViewHolder")
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            if (position < myItems.size) {
                holder.bind(myItems[position])
            }
        }

        override fun getItemCount(): Int = myItems.size
    }

    @JvmStatic
    @BindingAdapter("scrollToBottom")
    fun scrollToBottom(scroll: ScrollView, scrollToBottom: Boolean) {
        if (scrollToBottom) {
            scroll.post {
                scroll.fullScroll(View.FOCUS_DOWN)
            }
        }
    }


    class CommonViewHolder constructor(private val binding: ViewDataBinding) : CommonAbstractViewHolder(binding) {

        override fun bind(item: Any?) {
            item?.let {
                binding.setVariable(BR.viewModel, it)
                binding.executePendingBindings()
            }
        }
    }

    abstract class CommonAbstractViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: Any?)
    }
}