package wumf.com.appsprovider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import wumf.com.appsprovider.util.FileGenerator;
import wumf.com.appsprovider.util.SaveIconUtils;

/**
 * Created by max on 17.12.16.
 */

public class AppProvider {

    public final static AppProvider instance = new AppProvider();

    private HList appsHList = new HList();
    private List<App> apps = new ArrayList<App>();
    private OnChangeLastInstalledAppsListener listener = new OnChangeLastInstalledAppsListener() {
        @Override
        public void changedTop6(List<App> apps) {

        }

        @Override
        public void changedAll(List<App> apps) {

        }
    };

    private PackageManager pm;
    private SaveIconUtils saveIconUtils;
    private FileGenerator fileGenerator;

    private AppProvider() { }

    public AppProvider setListener(OnChangeLastInstalledAppsListener listener) {
        this.listener = listener;
        return this;
    }

    public AppProvider setMyPackageName(String value) {
        appsHList.setMyAppPackageName(value);
        return this;
    }

    public AppProvider setContext(Context context) {
        this.pm = context.getPackageManager();
        saveIconUtils = new SaveIconUtils(context);
        fileGenerator = new FileGenerator(context);
        return this;
    }

    public AppProvider initBaseInfo() {
        List<ResolveInfo> resolveInfos = getResolveInfos();
        Map<String, ResolveInfo> map = new HashMap<>();
        List<App> apps = resolveInfoToApp(resolveInfos, map);
        listener.setMap(map);

        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App app1, App app2) {
                if (app1.installDate == app2.installDate) {
                    return 0;
                }

                if (app1.installDate > app2.installDate) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        initFullAppInfo(apps, new Runnable() {
            @Override
            public void run() {
                listener.changedTop6(appsHList.getTop6Apps());
                listener.changedAll(appsHList.getAllApps());
            }
        });

        appsHList.setAllApps(apps);

        return this;
    }

    public void updateAlreadySharedApps(List<String> appPackages) {
        appsHList.setNewHiddenApps(appPackages);
        listener.changedTop6(appsHList.getTop6Apps());
        listener.changedAll(appsHList.getAllApps());
    }

    public void initFullAppInfo(final List<App> apps, final Runnable finishInit) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (App app : apps) {
                    ResolveInfo ri = listener.getMap().get(app.appPackage);
                    app.name = ri.loadLabel(pm).toString();
                    app.iconWithBadQuality = loadAndSaveIconInFile(pm, ri);
                    app.icon = loadAndSaveIconInFileGoodQuality(pm, ri);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                AppProvider.this.apps = apps;
                finishInit.run();
            }
        }.execute();

    }

    public List<ResolveInfo> getResolveInfos() {

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<android.content.pm.ResolveInfo> appList = pm.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA);

        return appList;
    }

    private boolean isSystemPackage(ResolveInfo ri) {
        return ( (ri.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0 );
    }

    private boolean isInstalledFromGooglePlay(ResolveInfo ri) {
        return TextUtils.equals(pm.getInstallerPackageName(ri.activityInfo.packageName), "com.android.vending");
    }

    private List<App> resolveInfoToApp(List<ResolveInfo> list, Map<String, ResolveInfo> map) {
        List<App> result = new ArrayList<>();
        long systemInstallDate = -1;
        int i = 0;
        for (ResolveInfo resolveInfo : list) {
            if (map.containsKey(resolveInfo.activityInfo.packageName)) { //TODO: fixme
                continue; //this package already exists
            }
            map.put(resolveInfo.activityInfo.packageName, resolveInfo);
            if ( isSystemPackage(resolveInfo) ) {
                systemInstallDate = getInstallDate(resolveInfo.activityInfo.packageName);
            } else {
                if ( isInstalledFromGooglePlay(resolveInfo) ) {
                    result.add(resolveInfoToApp(resolveInfo));
                }
            }
        }

        systemInstallDate = systemInstallDate + TimeUnit.MINUTES.toMillis(30);

        for (App app : new ArrayList<>(result)) {
            if (app.installDate < systemInstallDate) {
                result.remove(app); //remove also some system apps
            }
        }

        Collections.sort(result, new Comparator<App>() {
            @Override
            public int compare(App app1, App app2) {
                if (app1.installDate == app2.installDate) {
                    return 0;
                }

                if (app1.installDate > app2.installDate) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        return result;
    }

    public ArrayList<App> findAppsByPackages(List<String> packageNames, List<ResolveInfo> list) {
        ArrayList<App> result = new ArrayList<>();
        for (ResolveInfo resolveInfo : list) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (packageNames.contains(packageName)) {
                result.add(resolveInfoToApp(resolveInfo));
            }
        }
        return result;
    }

    public App findAppByPackage(String packageStr) {
        for (App app: apps) {
            if (app.appPackage.equals(packageStr)) {
                return app;
            }
        }
        return null;
//        return resolveInfoToApp(listener.getMap().get(packageStr));
//        for (ResolveInfo resolveInfo : list) {
//            String packageName = resolveInfo.activityInfo.packageName;
//            if (packageStr.equals(packageName)) {
//                return resolveInfoToApp(resolveInfo);
//            }
//        }
    }

    private App resolveInfoToApp(ResolveInfo resolveInfo) {
        String pack = getPackageName(resolveInfo);
        String mainAct = getMainActivityName(resolveInfo);
        long id = Objects.hash(pack, mainAct);
        return new App(id, resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name,
                null, getInstallDate(resolveInfo.activityInfo.packageName));
    }

    private long getInstallDate(String packageName) {
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String loadAndSaveIconInFileGoodQuality(PackageManager pm, ResolveInfo resolveInfo) {
        return loadAndSaveIconInFileImpl(resolveInfo, true);
    }

    private String loadAndSaveIconInFile(PackageManager pm, ResolveInfo resolveInfo) {
        return loadAndSaveIconInFileImpl(resolveInfo, false);
    }

    private String loadAndSaveIconInFileImpl(ResolveInfo resolveInfo, boolean isGoodQuality) {
        String pack = getPackageName(resolveInfo);
        String actName = getMainActivityName(resolveInfo);
        File file;
        if (isGoodQuality) {
            file = fileGenerator.generatePngImage(pack, actName);
        } else {
            file = fileGenerator.generateImage(pack, actName);
        }
        if (!file.exists()) {
            Drawable drawable = resolveInfo.loadIcon(pm);
            if (isGoodQuality) {
                saveIconUtils.saveInFileInWithGoodQuality(file, drawable);
            } else {
                saveIconUtils.saveInFile(file, drawable);
            }
        }
        return file.getAbsolutePath();
    }

    private String getPackageName(ResolveInfo resolveInfo) {
        return resolveInfo.activityInfo.packageName;
    }

    private String getMainActivityName(ResolveInfo resolveInfo) {
        return resolveInfo.activityInfo.name.substring(resolveInfo.activityInfo.name.lastIndexOf('.'));
    }

}
