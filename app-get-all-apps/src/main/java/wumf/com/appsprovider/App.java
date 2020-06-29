package wumf.com.appsprovider;

/**
 * Created by max on 02.09.16.
 */
public class App {

    public final long id;
    public final String appPackage;
    public String name;
    public String icon;
    public String iconWithBadQuality;
    public final long installDate;

    public App(long id, String appPackage, String name, String icon, long installDate) {
        this.id = id;
        this.appPackage = appPackage;
        this.name = name;
        this.icon = icon;
        this.installDate = installDate;
    }

}
