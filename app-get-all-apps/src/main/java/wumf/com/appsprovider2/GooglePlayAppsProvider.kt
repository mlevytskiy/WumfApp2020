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
    companion object {

        private val appSerializer = GPAppSerializer()
        private val kryo = Kryo()

        fun fillAppFromGooglePlay(pkgName: String): GooglePlayApp {
            val app = GooglePlayApp(pkgName)
            fillApp(app, null)
            return app
        }

        private fun fillApp(app: GooglePlayApp, filesDir: File?) {
            val file = filesDir?.let { Util.getFile(app.packageName, filesDir) } ?: run { null }
            if (file?.exists() == true) {
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
                        url.contains("googleusercontent.com/")
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

    private val resources: Resources = context.resources
    private var filesDir: File = context.filesDir

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

    fun fillApp(appContainer: AppContainer) {
        appContainer.gpApp?.let {
            fillApp(it, filesDir)
        } ?:run {
            val gpApp =  GooglePlayApp((appContainer.packageName))
            appContainer.gpApp = gpApp
            fillApp(gpApp, filesDir)
        }
    }

}