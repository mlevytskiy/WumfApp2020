package wumf.com.appsprovider2

import android.content.pm.ResolveInfo

class AppContainer(val packageName: String, val app: App? = null,
                   var gpApp: GooglePlayApp? = null, val resolveInfo: ResolveInfo? = null)