package wumf.com.appsprovider2

import android.graphics.drawable.Drawable
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by max on 02.09.16.
 */
class App(
    val id: Long,
    val appPackage: String,
    var name: String,
    var icon: Drawable?,
    val installDate: Long)
