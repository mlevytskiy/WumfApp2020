package wumf.com.appsprovider;

import android.content.pm.ResolveInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by max on 02.09.16.
 */
public abstract class OnChangeLastInstalledAppsListener {

    private Map<String, ResolveInfo> map;

    void setMap(Map<String, ResolveInfo> map) {
        this.map = map;
    }

    Map<String, ResolveInfo> getMap() {
        return map;
    }

    public abstract void changedTop6(List<App> apps);

    public abstract void changedAll(List<App> apps);

}
