package petrov.kristiyan.colorpicker;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialog;
import android.view.View;
import android.view.Window;

public class CustomDialog extends AppCompatDialog {
    private View view;

    public CustomDialog(Context context, View layout) {
        super(context);
        view = layout;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
    }
}