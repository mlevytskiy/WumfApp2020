package com.core.core_adapters

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            0, 0).apply {
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
    }

    fun showLoadedState() {
        val pkg = "a,b,c,d,e"
        setPackages(pkg, emptyMap())
    }

    fun setPackages(packagesStr: String?, likes: Map<String, List<Int>>) {
        val packages = if (packagesStr?.isNotEmpty() ?: false) {
            packagesStr?.split(",") ?: emptyList()
        } else {
            emptyList()
        }
        if (!getAllAppsFromPhone && packages.isEmpty()) {
            val adapter = getAdapter() as AppsAdapter?
            adapter?.apps?.clear()
            adapter?.notifyDataSetChanged()
            return
        }
        startBgJob {
            var adapter: AppsAdapter? = null
            appProvider?.getNextApps(
                updateBlock = { apps ->
                    adapter?.notifyDataSetChanged() ?: run {
                        adapter = AppsAdapter(apps, likes, itemListener)
                        setAdapter(adapter)
                    }
                },
                packages = packages
            )
        }
    }

    fun setItemClick(block: (AppContainer, List<Int>)->Unit ) {
        val adapter = getAdapter() as AppsAdapter?
        itemListener = block
        adapter?.setItemClick(block)
    }

    private fun startBgJob(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = {
            block.invoke(this)
        })
    }

}