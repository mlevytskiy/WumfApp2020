package wumf.com.appsprovider2

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wumf.com.appsprovider2.AppProvider.QueryType.*

const val GET_APPS_AMOUNT = 18

class AppProvider(context: Context) {

    private val innerAppsProvider: InnerAppsProvider
    private val gpAppProvider: GooglePlayAppsProvider

    init {
        innerAppsProvider = InnerAppsProvider(context)
        gpAppProvider = GooglePlayAppsProvider(context)
    }

    suspend fun getNextApps(
        packages: List<String> = emptyList(), queryType: QueryType = ANY_APP,
        nextAppsAmount: Int = GET_APPS_AMOUNT, updateBlock: (MutableList<AppContainer>) -> Unit
    ) = withContext(Dispatchers.IO) {
        when(queryType) {
            INNER_APP-> {
                var currentIndex = 0
                innerAppsProvider.prepareForSaveDrawablesInFiles()
                val allApps = innerAppsProvider.getAllAppsBaseInfo(packages)
                withContext(Dispatchers.Main) {
                    updateBlock(allApps)
                }
                while (currentIndex < allApps.size) {
                    currentIndex = innerAppsProvider.getNextApps(
                        allApps = allApps,
                        currentIndex = currentIndex,
                        amount = nextAppsAmount
                    )
                    withContext(Dispatchers.Main) {
                        updateBlock(allApps)
                    }
                }
                innerAppsProvider.saveAllDrawablesInFiles()
            }
            GOOGLE_PLAY -> {
                var currentIndex = 0
                val allApps = gpAppProvider.wrapPackages(packages)
                withContext(Dispatchers.Main) {
                    updateBlock(allApps)
                }
                while (currentIndex < allApps.size) {
                    currentIndex = gpAppProvider.getNextApps(
                        allApps = allApps,
                        currentIndex = currentIndex,
                        amount = nextAppsAmount
                    )
                    withContext(Dispatchers.Main) {
                        updateBlock(allApps)
                    }
                }
            }
            ANY_APP -> {
                var currentIndex = 0
                innerAppsProvider.prepareForSaveDrawablesInFiles()
                var allApps = innerAppsProvider.getAllAppsBaseInfo(packages)
                withContext(Dispatchers.Main) {
                    updateBlock(allApps)
                }
                while (currentIndex < allApps.size) {
                    currentIndex = innerAppsProvider.getNextApps(
                        allApps = allApps,
                        currentIndex = currentIndex,
                        amount = nextAppsAmount
                    )
                    withContext(Dispatchers.Main) {
                        updateBlock(allApps)
                    }
                }

                currentIndex = 0
                allApps = gpAppProvider.filterOnlyGPApps(allApps)
                while (currentIndex < allApps.size) {
                    currentIndex = gpAppProvider.getNextApps(
                        allApps = allApps,
                        currentIndex = currentIndex,
                        amount = nextAppsAmount
                    )
                    withContext(Dispatchers.Main) {
                        updateBlock(allApps)
                    }
                }
            }
        }
    }

    enum class QueryType {
        GOOGLE_PLAY,
        INNER_APP,
        ANY_APP
    }

}