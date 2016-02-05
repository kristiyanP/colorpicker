package petrov.kristiyan.colorpicker;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

public class ColorPicker {


    private OnChooseColorListener onChooseColorListener;

    public interface OnChooseColorListener {
        void onChooseColor(int position,int color);
    }
    // ALLOWS YOU TO SET LISTENER && INVOKE THE OVERIDING METHOD
    // FROM WITHIN ACTIVITY
    public void setOnChooseColorListener(OnChooseColorListener listener) {
        onChooseColorListener = listener;
    }


    private ArrayList<ColorPal> colors;
    private ColorViewAdapter colorViewAdapter;
    private int position = 0;
    private TypedArray ta;
    private Activity activity;
    private int columns;
    private String title;
    private int gravity = Gravity.CENTER;
    private int marginLeft, marginRight, marginTop, marginBottom;
    private int tickColor;
    private int marginButtonLeft, marginButtonRight, marginButtonTop, marginButtonBottom;
    private int buttonWidth, buttonHeight;
    private int buttonDrawable;
    private String negativeText = "CANCEL", positiveText = "OK";
    private boolean roundButton;
    private ArrayList<Drawable> drawables;
    private ArrayList<ImageView> images;

    /**
     * Constructor
     * @param activity Activity calling
     */
    public ColorPicker(Activity activity) {
        this.activity = activity;
        columns = 3;
    }

    /**
     * Set buttons color using a resource array of colors example : check in library  res/values/array.xml
     * @param resId Array resource
     * @return this
     */
    public ColorPicker setColors(int resId) {
        ta = activity.getResources().obtainTypedArray(resId);
        colors = new ArrayList<>();
        for (int i = 0; i < ta.length(); i++) {
            colors.add(new ColorPal(ta.getColor(i, 0), false));
        }
        return this;
    }

    /**
     * Set default colors defined in array.xml of the library
     * @return this
     */
    private ColorPicker setColors() {
        ta = activity.getResources().obtainTypedArray(R.array.default_colors);
        colors = new ArrayList<>();
        for (int i = 0; i < ta.length(); i++) {
            colors.add(new ColorPal(ta.getColor(i, 0), false));
        }
        return this;
    }

    /**
     * Set buttons from an arraylist of Hex values
     * @param colorsHexList List of hex values of the colors
     * @return this
     */
    public ColorPicker setColors(ArrayList<String> colorsHexList) {
        colors = new ArrayList<>();
        for (int i = 0; i < colorsHexList.size(); i++) {
            colors.add(new ColorPal(colorsHexList.indexOf(i), false));
        }
        return this;
    }

    /**
     * Set buttons color  Example : Color.RED,Color.BLACK
     * @param colorsList list of colors
     * @return this
     */
    public ColorPicker setColors(int... colorsList) {
        colors = new ArrayList<>();
        for (int i = 0; i < colorsList.length; i++) {
            colors.add(new ColorPal(colorsList[i], false));
        }
        return this;
    }

    /**
     * Set the Background of the buttons with drawables ( the order is important! )
     * @param backgroundButton Drawables
     * @return this
     */
    private ColorPicker setBackgroundButtonDrawables(ArrayList<Drawable> backgroundButton) {
        this.drawables = backgroundButton;
        return this;
    }

    /**
     * Set the Background of the buttons with images ( the order is important! )
     * @param backgroundButton Images
     * @return this
     */
    private ColorPicker setBackgroundButtonImages(ArrayList<ImageView> backgroundButton) {
        this.images = backgroundButton;
        return this;
    }

    /**
     * Show the Material Dialog
     */
    public void show() {
        if (colors == null || colors.isEmpty())
            setColors();
        View view = activity.getLayoutInflater().inflate(R.layout.color_palette_layout, null);
        if (title != null) {
            TextView titleView = (TextView) view.findViewById(R.id.title);
            titleView.setText(title);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.color_palette);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, columns);
        recyclerView.setLayoutManager(gridLayoutManager);
        colorViewAdapter = new ColorViewAdapter(colors);
        recyclerView.setAdapter(colorViewAdapter);

        if(drawables != null && drawables.size() != colors.size()){
            Log.e("ERROR SIZE","color size does not match background size!!! drawable size = "+drawables.size() + " colors size = " +colors.size() );
            return ;
        }
        if(images != null && images.size() != colors.size()){
            Log.e("ERROR SIZE","colors size does not match background size!!! images size = "+images.size() + " colors size = " +colors.size() );
            return ;
        }
        if (gravity != Gravity.CENTER) {
            colorViewAdapter.setGravity(gravity);
        }

        if (marginBottom != 0 || marginLeft != 0 || marginRight != 0 || marginTop != 0) {
            colorViewAdapter.setMargin(marginLeft, marginTop, marginRight, marginBottom);
        }
        if (tickColor != 0) {
            colorViewAdapter.setButtonsTickColor(tickColor);
        }
        if (marginButtonBottom != 0 || marginButtonLeft != 0 || marginButtonRight != 0 || marginButtonTop != 0) {
            colorViewAdapter.setButtonMargin(marginButtonLeft, marginButtonTop, marginButtonRight, marginButtonBottom);
        }
        if (buttonHeight != 0 || buttonWidth != 0) {
            colorViewAdapter.setButtonSize(buttonWidth, buttonHeight);
        }
        if (buttonDrawable != 0) {
            colorViewAdapter.setButtonDrawable(buttonDrawable);
        }
        if (roundButton) {
            this.setButtonDrawable(R.drawable.round_button);
        }
        //create Material Dialog
        final MaterialDialog mMaterialDialog = new MaterialDialog(activity);
        mMaterialDialog
                .setPositiveButton(positiveText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position = colorViewAdapter.getColorPosition();
                        onChooseColorListener.onChooseColor(colorViewAdapter.getColorPosition(),colorViewAdapter.getColorSelected());
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton(negativeText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                }).setView(view);
        mMaterialDialog.show();
    }

    /**
     * Define the number of columns by default value= 3
     * @param c Columns number
     * @return this
     */
    public ColorPicker setColumns(int c) {
        columns = c;
        return this;
    }

    /**
     * Define the title of the Material Dialog
     * @param title Title
     * @return this
     */
    public ColorPicker setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Define the gravity of the columns example : Gravity.CENTER, Gravity.LEFT , Gravity.RIGHT ...
     * @param gravity Gravity
     * @return this
     */
    public ColorPicker setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * Define the margins of the Material Dialog in PIXEL
     * @param left left
     * @param top top
     * @param right right
     * @param bottom bottom
     * @return this
     */
    public ColorPicker setMargin(int left, int top, int right, int bottom) {
        this.marginLeft = left;
        this.marginRight = right;
        this.marginTop = top;
        this.marginBottom = bottom;
        return this;
    }

    /**
     * Set tick color
     * @param color Color
     * @return this
     */
    public ColorPicker setButtonsTickColor(int color) {
        this.tickColor = color;
        return this;
    }

    /**
     * Set a single drawable for all buttons example : you can define a different shape ( then round or square )
     * @param drawable
     * @return this
     */
    public ColorPicker setButtonDrawable(int drawable) {
        this.buttonDrawable = drawable;
        return this;
    }

    /**
     * Set the buttons size in PIXEL
     * @param width width
     * @param height height
     * @return this
     */
    public ColorPicker setButtonSize(int width, int height) {
        this.buttonWidth = width;
        this.buttonHeight = height;
        return this;
    }

    /**
     * Set the Margin between the buttons in PIXEL
     * @param left left
     * @param top top
     * @param right right
     * @param bottom bottom
     * @return
     */
    public ColorPicker setButtonMargin(int left, int top, int right, int bottom) {
        this.marginButtonLeft = left;
        this.marginButtonTop = top;
        this.marginButtonRight = right;
        this.marginButtonBottom = bottom;
        return this;
    }

    /**
     * Set title of the positive button in the Material Dialog
     * @param text text
     * @return this
     */
    public ColorPicker setPositiveButtonText(String text) {
        this.positiveText = text;
        return this;
    }

    /**
     * Set title of the negative button in the Material Dialog
     * @param text text
     * @return this
     */
    public ColorPicker setNegativeButtonText(String text) {
        this.negativeText = text;
        return this;
    }

    /**
     * Set round button
     * @param roundButton true if you want a round button
     * @return this
     */
    public ColorPicker setRoundButton(boolean roundButton) {
        this.roundButton = roundButton;
        return this;
    }

}
