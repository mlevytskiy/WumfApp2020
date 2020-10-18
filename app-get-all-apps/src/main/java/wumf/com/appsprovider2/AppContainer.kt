package wumf.com.appsprovider2

import android.content.pm.ResolveInfo
import java.io.Serializable

class AppContainer(val packageName: String = "", val app: App? = null,
                   var gpApp: GooglePlayApp? = null, val resolveInfo: ResolveInfo? = null) : Serializable