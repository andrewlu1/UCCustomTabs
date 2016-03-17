package cn.uc.uccustomtabs;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.db.DbManagerImpl;
import org.xutils.x;

/**
 * Created by Administrator on 2016/3/15.
 */
public class App extends Application {
    public void onCreate() {
        super.onCreate();
        _instance = this;
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }

    private static App _instance;

    public static App getApplication() {
        return _instance;
    }

}
