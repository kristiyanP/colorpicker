package petrov.kristiyan.colorpicker_sample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author kristiyan
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

}
