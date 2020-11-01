package com.core.core_adapters

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import wumf.com.appsprovider2.AppContainer
import wumf.com.appsprovider2.AppProvider


class AppsRecycleView(context: Context, attr: AttributeSet?) : RecyclerView(context, attr) {

    private val supervisor = SupervisorJob()
    private var scope = CoroutineScope(Dispatchers.IO + supervisor)
    private var appProvider: AppProvider? = null
    private var getAllAppsFromPhone = false
    private var itemListener: ((AppContainer, List<Int>)->Unit)? = null

    init {
        layoutManager = GridLayoutManager(getContext(), 3)

        var appsStr : String?

        context.theme.obtainStyledAttributes(
            attr,
            R.styleable.AppsRecycleView,
            0, 0
        ).apply {
            try {
                appsStr = getString(R.styleable.AppsRecycleView_items)
                getAllAppsFromPhone = getBoolean(R.styleable.AppsRecycleView_getAllAppsFromPhone, false)
            } finally {
                recycle()
            }

        }
        appProvider = AppProvider(context)
        if (getAllAppsFromPhone) {
            setPackages("", HashMap())
        }
        val spanCount = 3 // 3 columns
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.space_between_colums)
        val includeEdge = true
        addItemDecoration(GridSpacingItemDecoration(spanCount, spacingInPixels, includeEdge))
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) :
        ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    fun setPackages(packagesStr: String?, likes: Map<String, List<Int>>) {
        val packages = if (packagesStr?.isNotEmpty() ?: false) {
            packagesStr?.split(",") ?: emptyList()
        } else {
            emptyList()
        }
        setPackages(packages, likes)
    }

    fun setPackages(packages: List<String>) {
        setPackages(packages, emptyMap())
    }



    fun setPackages(packages: List<String>, likes: Map<String, List<Int>>) {
        var adapterX: AppsAdapter?  = adapter as AppsAdapter?
        if (!getAllAppsFromPhone && packages.isEmpty()) {
            adapterX?.apps?.clear()
            adapterX?.notifyDataSetChanged()
            return
        }
        if (adapterX?.apps?.size == packages.size) {
            val tmpList = adapterX.apps.filter {
                packages.contains(it.packageName)
            }
            if (tmpList.size == packages.size) {
                return
            }
        }
        adapterX = null
        val glide = Glide.with(this)
        startBgJob {
            appProvider?.getNextApps(
                updateBlock = { apps ->
                    adapterX?.notifyDataSetChanged() ?: run {
                        adapterX = AppsAdapter(apps, likes, itemListener, glide)
                        adapterX?.stateRestorationPolicy = Adapter.StateRestorationPolicy.ALLOW
                        setAdapter(adapterX)
                    }
                },
                packages = packages
            )
        }
    }

    fun setItemClick(block: (AppContainer, List<Int>) -> Unit) {
        val adapter = getAdapter() as AppsAdapter?
        itemListener = block
        adapter?.setItemClick(block)
        adapter?.notifyDataSetChanged()
    }

    private fun startBgJob(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = {
            block.invoke(this)
        })
    }
}