package wumf.com.appsprovider.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by max on 21.04.16.
 */
public abstract class ChangeAppReceiver extends BroadcastReceiver {

    @Override
    public final void onReceive(Context context, Intent intent) {
        String packageName = intent.getData().getEncodedSchemeSpecificPart();
        if (TextUtils.equals(context.getApplicationContext().getPackageName(), packageName)) {
            //ignore
            return;
        }

        switch (intent.getAction()) {
            case Intent.ACTION_PACKAGE_REPLACED:
            case Intent.EXTRA_REPLACING:
                onUpdateApp(context, packageName);
                break;
            case Intent.ACTION_PACKAGE_ADDED:
                onAddApp(context, packageName);
                break;
            case Intent.ACTION_PACKAGE_REMOVED:
                onRemoveApp(context, packageName);
                break;
        }

    }

    public abstract void onAddApp(Context context, String pn);
    public abstract void onRemoveApp(Context context, String pn);
    public abstract void onUpdateApp(Context context, String pn);

}
