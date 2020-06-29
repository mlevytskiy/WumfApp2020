package wumf.com.appsprovider;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 16.12.16.
 */

public class HList extends ArrayList<AppContainer> {

    private List<AppContainer> allItems = new ArrayList<>();
    private String myAppPackageName;

    public void setMyAppPackageName(String value) {
        myAppPackageName = value;
    }

    public List<App> getAllApps() {
        List<App> apps = new ArrayList<>();
        for (AppContainer container : allItems) {
            if (!container.isHidden) {
                apps.add(container.app);
            }
        }
        return apps;
    }

    public List<App> getTop6Apps() {
        List<App> apps = new ArrayList<>();
        int itemIndex = 0;
        for (AppContainer container : allItems) {
            if (!container.isHidden) {
                if (itemIndex < 6) {
                    apps.add(container.app);
                } else {
                    break;
                }
                itemIndex++;
            }
        }
        return apps;
    }

    public void setAllApps(List<App> apps) {
        allItems.clear();
        for (App app : apps) {
            AppContainer container = new AppContainer();
            container.app = app;
            container.isHidden = TextUtils.equals(app.appPackage, myAppPackageName);
            allItems.add(container);
        }
    }

    public void setNewHiddenApps(List<String> appPackages) {
        for (wumf.com.appsprovider.AppContainer item : allItems) {
            item.isHidden = appPackages.contains(item.app.appPackage);
            if (TextUtils.equals(item.app.appPackage, myAppPackageName)) {
                item.isHidden = true;
            }
        }
    }

}
