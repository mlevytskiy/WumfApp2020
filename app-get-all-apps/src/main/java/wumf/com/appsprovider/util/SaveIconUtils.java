package wumf.com.appsprovider.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by max on 30.03.16.
 */
public class SaveIconUtils {

    private static final int ICON_SIZE_LDPI = 36;
    private static final int ICON_SIZE_MDPI = 48;
    private static final int ICON_SIZE_TVDPI = 64;
    private static final int ICON_SIZE_HDPI = 72;
    private static final int ICON_SIZE_XHDPI = 96;
    private static final int ICON_SIZE_XXHDPI = 144;
    private final int iconHeight;

    public SaveIconUtils(Context context) {
        this(context.getResources().getDisplayMetrics().densityDpi);
    }

    public SaveIconUtils(int density) {
        this.iconHeight = calculateIconHeight(density);
    }

    public void saveInFileInWithGoodQuality(File file, Drawable drawable) {
        Bitmap bitmap = drawableToBitmapWithGoodQuality(drawable);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            file.setReadable(false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            file.setReadable(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }
    }

    public Drawable resize(Drawable image, Resources resources) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, iconHeight, iconHeight, false);
        return new BitmapDrawable(resources, bitmapResized);
    }

    public void saveInFile(File file, Drawable drawable) {
        Bitmap bitmap = drawableToBitmap(drawable);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            file.setReadable(false);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 30, out);
            out.flush();
            file.setReadable(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    //does nothing
                }
            }
        }

    }

    private int calculateIconHeight(int density) {
        if (density == DisplayMetrics.DENSITY_LOW) {
            return ICON_SIZE_LDPI;
        } else if (density == DisplayMetrics.DENSITY_MEDIUM) {
            return ICON_SIZE_MDPI;
        } else if (density == DisplayMetrics.DENSITY_TV) {
            return ICON_SIZE_TVDPI;
        } else if (density == DisplayMetrics.DENSITY_HIGH) {
            return ICON_SIZE_HDPI;
        } else if (density == DisplayMetrics.DENSITY_XHIGH) {
            return ICON_SIZE_XHDPI;
        } else {
            return ICON_SIZE_XXHDPI;
        }
    }

    private Bitmap drawableToBitmapWithGoodQuality(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if((bitmap = bitmapDrawable.getBitmap()) != null) {
                if (bitmapDrawable.getIntrinsicHeight() > iconHeight ||
                        bitmapDrawable.getIntrinsicWidth() > iconHeight) {
                    bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), iconHeight, iconHeight, false);
                } else {
                    //does nothing
                }
            }
        } else {
            if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return bitmap;
    }

    private Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if((bitmap = bitmapDrawable.getBitmap()) != null) {
                if (bitmapDrawable.getIntrinsicHeight() > iconHeight ||
                        bitmapDrawable.getIntrinsicWidth() > iconHeight) {
                    bitmap = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), iconHeight, iconHeight, false);
                } else {
                    //does nothing
                }
            }
        } else {
            if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        Bitmap b = Bitmap.createBitmap(iconHeight, iconHeight,  Bitmap.Config.ARGB_8888);
        b.eraseColor(Color.WHITE);

        Canvas canvas = new Canvas(b);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return b;
    }
}
