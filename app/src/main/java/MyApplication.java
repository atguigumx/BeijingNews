import android.app.Application;

import org.xutils.x;

/**
 * Created by shkstart on 2017/6/2.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
