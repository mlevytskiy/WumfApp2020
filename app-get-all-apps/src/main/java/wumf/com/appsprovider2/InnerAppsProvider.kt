package wumf.com.appsprovider2

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.graphics.drawable.Drawable
import wumf.com.appsprovider2.Util.getDrawableFromFile
import wumf.com.appsprovider2.Util.getFile
import wumf.com.appsprovider2.Util.resizeDrawable
import wumf.com.appsprovider2.Util.saveInFile
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class InnerAppsProvider(context: Context) {

    private val pm: PackageManager
    private val resolveInfos: List<ResolveInfo>
    private val resources: Resources
    private var filesDir: File

    private val tempMap = HashMap<File, Drawable>()

    init {
        pm = context.getPackageManager()
        filesDir = context.filesDir
        resources = context.resources
        //получить список resolveInfos с сиистемы
        resolveInfos = getResolveInfos()
    }

    fun prepareForSaveDrawablesInFiles() {
        tempMap.clear()
    }

    fun saveAllDrawablesInFiles() {
        tempMap.entries.forEach{ it ->
            saveInFile(it.key, it.value)
        }
        tempMap.clear()
    }

    fun getAllAppsBaseInfo(packages: List<String>): ArrayList<AppContainer> {
        val result = ArrayList<AppContainer>()

        if (packages.isEmpty()) {
            resolveInfos.forEach {
                val appContainer = getAppContainer(it)
                result.add(appContainer)
            }
            return result
        }

        val tempPackages = ArrayList<String>(packages)
        resolveInfos.forEach {
            val pack = getPackageName(it)
            if (tempPackages.contains(pack)) {
                val appContainer = getAppContainer(it)
                result.add(appContainer)
                tempPackages.remove(pack)
            }
        }
        tempPackages.forEach {
            result.add(AppContainer(it, null, null, null))
        }
        return result
    }

    private fun getAppContainer(resolveInfo: ResolveInfo): AppContainer {
        val app =  resolveInfoToApp(resolveInfo);
        return AppContainer(app.appPackage, app, null, resolveInfo)
    }

    fun getNextApps(amount: Int = 6, allApps: List<AppContainer>, currentIndex: Int): Int {

        val nextIndex = Math.min(allApps.size, currentIndex + amount)
        if (nextIndex == currentIndex) {
            return allApps.size //end
        }
        allApps.sortedBy { it.app?.installDate }

        for (i in currentIndex until nextIndex) {
            fillApp(allApps[i])
        }

        return nextIndex

    }

    private fun fillApp(appContainer: AppContainer) {
        appContainer.resolveInfo?.let {
            val fileImage = getFile(getPackageName(it), getMainActivityName(it), filesDir)
            appContainer.app?.name = it.loadLabel(pm).toString()
            appContainer.app?.icon = if (fileImage.exists()) {
                getDrawableFromFile(fileImage)
            } else {
                val drawable = resizeDrawable(resources, it.loadIcon(pm))
                tempMap.put(fileImage, drawable)
                drawable
            }
        }
    }

    fun getResolveInfos(): List<ResolveInfo> {

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        return pm.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA)
    }

    fun sortResolveInfos(resolveInfos: List<ResolveInfo>) {

    }

    private fun resolveInfoToApp(resolveInfo: ResolveInfo): App {
        val pack = getPackageName(resolveInfo)
        val mainAct = getMainActivityName(resolveInfo)
        val id = Objects.hash(pack, mainAct).toLong()
        return App(
            id, resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name,
            null, getInstallDate(resolveInfo.activityInfo.packageName)
        )
    }

    private fun getPackageName(resolveInfo: ResolveInfo): String {
        return resolveInfo.activityInfo.packageName
    }

    private fun getMainActivityName(resolveInfo: ResolveInfo): String {
        return resolveInfo.activityInfo.name.substring(resolveInfo.activityInfo.name.lastIndexOf('.'))
    }

    private fun getInstallDate(packageName: String): Long {
        try {
            val packageInfo = pm.getPackageInfo(packageName, 0)
            return packageInfo.firstInstallTime
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return 0
    }

}