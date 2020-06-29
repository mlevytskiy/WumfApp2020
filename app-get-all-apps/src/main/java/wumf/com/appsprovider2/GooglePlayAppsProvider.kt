package wumf.com.appsprovider2

import android.content.Context
import android.content.res.Resources
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.jsoup.Jsoup
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception

class GooglePlayAppsProvider(context: Context) {

    private val resources: Resources
    private var filesDir: File
    private val appSerializer = GPAppSerializer()
    private val kryo = Kryo()

    init {
        filesDir = context.filesDir
        resources = context.resources
    }

    fun filterOnlyGPApps(allApps: ArrayList<AppContainer>): ArrayList<AppContainer> {
        val result = ArrayList<AppContainer>()
        allApps.forEach {
            if (it.app == null) {
                result.add(it)
            }
        }
        return result
    }

    fun wrapPackages(packages: List<String>): MutableList<AppContainer> {
        val result = ArrayList<AppContainer>()
        packages.forEach {
            result.add(AppContainer(packageName = it, gpApp = GooglePlayApp(it)))
        }
        return result
    }

    fun getNextApps(amount: Int = 6, allApps: List<AppContainer>, currentIndex: Int): Int {
        val nextIndex = Math.min(allApps.size, currentIndex + amount)
        if (currentIndex == nextIndex) {
            return allApps.size //end
        }
        allApps.sortedBy { it.app?.installDate }

        for (i in currentIndex until nextIndex) {
            fillApp(allApps[i])
        }

        return nextIndex
    }

    private fun fillApp(appContainer: AppContainer) {
        appContainer.gpApp?.let {
            fillApp(it)
        } ?:run {
            val gpApp =  GooglePlayApp((appContainer.packageName))
            appContainer.gpApp = gpApp
            fillApp(gpApp)
        }
    }

    private fun fillApp(app: GooglePlayApp) {
        val file = Util.getFile(app.packageName, filesDir)
        if (file.exists()) {
            val input = Input(FileInputStream(file))
            val appFromFile = kryo.readObject(input, GooglePlayApp::class.java, appSerializer)
            app.name = appFromFile.name
            app.iconUrl = appFromFile.iconUrl
            input.close()
        } else {
            val url = "https://play.google.com/store/apps/details?id=" + app.packageName
            try {
                val doc = Jsoup.connect(url).get()
                val metaElements = doc.select("meta[property=og:title]")
                var title = ""
                metaElements.singleOrNull()?.let {
                    title = it.attr("content")
                }

                val images = doc.select("img[src]")
                val appIcon = images.filter {
                    val url = it.absUrl("src")
                    url.startsWith("https://lh3.googleusercontent.com/")
                }.find {
                    (it.attr("alt").contentEquals("Cover art"))
                }?.absUrl("src")
                appIcon?.let {
                    app.iconUrl = it
                    app.name = title
                } ?:run {
                    app.iconUrl = ""
                    app.name = title
                }
                val out = Output(FileOutputStream(file))
                kryo.writeObject(out, app, appSerializer)
                out.close()
            } catch (e: Exception) {
                //does nothing
            }
        }

    }



}