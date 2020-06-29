package wumf.com.appsprovider2

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.graphics.Canvas
import java.io.*


private val ICON_SIZE_LDPI = 36
private val ICON_SIZE_MDPI = 48
private val ICON_SIZE_TVDPI = 64
private val ICON_SIZE_HDPI = 72
private val ICON_SIZE_XHDPI = 96
private val ICON_SIZE_XXHDPI = 144

object Util {

    private var fileGenerator: FileGenerator? = null
    private var height = 0

    private fun calculateIconHeight(density: Int): Int {
        return if (density == DisplayMetrics.DENSITY_LOW) {
            ICON_SIZE_LDPI
        } else if (density == DisplayMetrics.DENSITY_MEDIUM) {
            ICON_SIZE_MDPI
        } else if (density == DisplayMetrics.DENSITY_TV) {
            ICON_SIZE_TVDPI
        } else if (density == DisplayMetrics.DENSITY_HIGH) {
            ICON_SIZE_HDPI
        } else if (density == DisplayMetrics.DENSITY_XHIGH) {
            ICON_SIZE_XHDPI
        } else {
            ICON_SIZE_XXHDPI
        }
    }

    fun getFile(packageName: String, mainActivityName: String, file: File): File {
        return fileGenerator?.pngImage(packageName, mainActivityName) ?:run {
            fileGenerator = FileGenerator(file)
            return@run fileGenerator!!.pngImage(packageName, mainActivityName)
        }
    }

    fun getFile(packageName: String, file: File): File {
        return fileGenerator?.generate(packageName, "json") ?:run {
            fileGenerator = FileGenerator(file)
            return@run fileGenerator!!.generate(packageName, "json")
        }
    }

    @SuppressLint("NewApi")
    fun resizeDrawable(resources: Resources, image: Drawable): Drawable {
        if (height == 0) {
            height = calculateIconHeight(resources.displayMetrics.densityDpi)
        }
        if (image.intrinsicHeight >= height) {
            val b = convertDrawableToBitmap(drawable = image)
            val bitmapResized = Bitmap.createScaledBitmap(b, height, height, false)
            return BitmapDrawable(resources, bitmapResized)
        } else {
            return image
        }
    }

    fun saveInFile(file: File, drawable: Drawable) {
        val bitmap = convertDrawableToBitmap(drawable)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            file.setReadable(false)
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)
            out.flush()
            file.setReadable(true)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    //do nothing
                }

            }
        }
    }

    fun getDrawableFromFile(file: File): Drawable? {
        return Drawable.createFromPath(file.absolutePath)
    }

    private fun convertDrawableToBitmap(drawable: Drawable): Bitmap {

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bounds = drawable.bounds
        val width = if (!bounds.isEmpty) bounds.width() else drawable.intrinsicWidth
        val height = if (!bounds.isEmpty) bounds.height() else drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(
            if (width <= 0) 1 else width, if (height <= 0) 1 else height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        drawable.bounds = bounds;
        return bitmap
    }

    private class FileGenerator(private val fileDir: File) {

        fun pngImage(packageName: String, mainActivityName: String): File {
            return generate("$packageName$mainActivityName", "png")
        }

        fun generate(str: String, extension: String)= File(fileDir, "$str.$extension")

    }

}
