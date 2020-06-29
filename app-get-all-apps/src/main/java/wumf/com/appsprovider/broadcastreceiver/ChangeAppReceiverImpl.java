package wumf.com.appsprovider.broadcastreceiver;

import android.content.Context;
import android.util.Log;

/**
 * Created by max on 09.09.16.
 */
public class ChangeAppReceiverImpl extends ChangeAppReceiver {

    @Override
    public void onAddApp(Context context, String pn) {
        Log.i("changeApp", "onAdd" + pn);
    }

    @Override
    public void onRemoveApp(Context context, String pn) {
        Log.i("changeApp", "onRemove" + pn);
    }

    @Override
    public void onUpdateApp(Context context, String pn) {
        Log.i("changeApp", "onUpdate" + pn);
    }

}
