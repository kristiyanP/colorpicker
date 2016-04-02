package petrov.kristiyan.colorpicker;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class ColorPicker {


    private OnChooseColorListener onChooseColorListener;
    private OnFastChooseColorListener onFastChooseColorListener;
    private OnButtonListener onNegativeButtonListener, onPositiveButtonListener;


    public interface OnChooseColorListener {
        void onChooseColor(int position, int color);
    }

    public interface OnFastChooseColorListener {
        void setOnFastChooseColorListener(int position, int color);
    }

    public interface OnButtonListener {
        void onClick(View v);
    }

    private ArrayList<ColorPal> colors;
    private ColorViewAdapter colorViewAdapter;
    private boolean fastChooser;
    private TypedArray ta;
    private Activity activity;
    private int columns;
    private String title;
    private int marginLeft, marginRight, marginTop, marginBottom;
    private int tickColor;
    private int marginButtonLeft, marginButtonRight, marginButtonTop, marginButtonBottom;
    private int buttonWidth, buttonHeight;
    private int buttonDrawable;
    private String negativeText, positiveText;
    private boolean roundButton;
    private boolean dismiss;
    private boolean fullheight;
    private CustomDialog dialog;
    private RecyclerView recyclerView;
    private int default_color;
    private int paddingTitleLeft, paddingTitleRight, paddingTitleBottom, paddingTitleTop;


    /**
     * Constructor
     *
     * @param activity Activity calling
     */
    public ColorPicker(Activity activity) {
        this.activity = activity;
        this.dismiss = true;
        this.marginButtonLeft = this.marginButtonTop = this.marginButtonRight = this.marginButtonBottom = dip2px(5);
        this.title = "Choose the color";
        this.negativeText = "CANCEL";
        this.positiveText = "OK";
        this.default_color = 0;
        this.columns = 5;
    }

    /**
     * Set buttons color using a resource array of colors example : check in library  res/values/colorpicker-array.xml
     *
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
     * Set default colors defined in colorpicker-array.xml of the library
     *
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
     *
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
     *
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
     * Show the Material Dialog
     */
    public void show() {
        if (colors == null || colors.isEmpty())
            setColors();
        View view = activity.getLayoutInflater().inflate(R.layout.color_palette_layout, null, false);

        TextView titleView = (TextView) view.findViewById(R.id.title);
        if (title != null) {
            titleView.setText(title);
            titleView.setPadding(paddingTitleLeft, paddingTitleTop, paddingTitleRight, paddingTitleBottom);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.color_palette);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, columns);
        recyclerView.setLayoutManager(gridLayoutManager);
        if (fastChooser)
            colorViewAdapter = new ColorViewAdapter(colors, onFastChooseColorListener);
        else
            colorViewAdapter = new ColorViewAdapter(colors);
        if (fullheight) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.BELOW, titleView.getId());
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            recyclerView.setLayoutParams(lp);
        }

        recyclerView.setAdapter(colorViewAdapter);

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
        if (roundButton) {
            this.setButtonDrawable(R.drawable.round_button);
        }
        if (buttonDrawable != 0) {
            colorViewAdapter.setButtonDrawable(buttonDrawable);
        }

        if (default_color != 0) {
            colorViewAdapter.setDefaultColor(default_color);
        }

        if (!fastChooser || onNegativeButtonListener != null || onPositiveButtonListener != null) {

            Button positiveButton = (Button) view.findViewById(R.id.positive);
            Button negativeButton = (Button) view.findViewById(R.id.negative);

            positiveButton.setText(positiveText + "");
            negativeButton.setText(negativeText + "");

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!fastChooser)
                        onChooseColorListener.onChooseColor(colorViewAdapter.getColorPosition(), colorViewAdapter.getColorSelected());
                    onPositiveButtonListener.onClick(v);
                    if (dismiss)
                        dialog.dismiss();
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNegativeButtonListener.onClick(v);
                    if (dismiss)
                        dialog.dismiss();
                }
            });

        }
        dialog = new CustomDialog(activity, view);
        dialog.show();
        //Keep dialog open when rotate
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }


    /**
     * Define the number of columns by default value= 3
     *
     * @param c Columns number
     * @return this
     */
    public ColorPicker setColumns(int c) {
        columns = c;
        return this;
    }

    /**
     * Define the title of the Material Dialog
     *
     * @param title Title
     * @return this
     */
    public ColorPicker setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Define the margins of the Material Dialog in PIXEL
     *
     * @param left   left
     * @param top    top
     * @param right  right
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
     *
     * @param color Color
     * @return this
     */
    public ColorPicker setButtonsTickColor(int color) {
        this.tickColor = color;
        return this;
    }

    /**
     * Set a single drawable for all buttons example : you can define a different shape ( then round or square )
     *
     * @param drawable
     * @return this
     */
    public ColorPicker setButtonDrawable(int drawable) {
        this.buttonDrawable = drawable;
        return this;
    }

    /**
     * Set the buttons size in PIXEL
     *
     * @param width  width
     * @param height height
     * @return this
     */
    public ColorPicker setButtonSize(int width, int height) {
        this.buttonWidth = width;
        this.buttonHeight = height;
        return this;
    }

    /**
     * Set the Margin between the buttons in PIXEL default is 10
     *
     * @param left   left
     * @param top    top
     * @param right  right
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
     *
     * @param text text
     * @return this
     */
    public ColorPicker setPositiveButton(String text, OnButtonListener listener) {
        this.positiveText = text;
        this.onPositiveButtonListener = listener;
        return this;
    }

    /**
     * Set the negative button in the Material Dialog usable also with fastChooser
     *
     * @param text text
     * @return this
     */
    public ColorPicker setNegativeButton(String text, OnButtonListener listener) {
        this.negativeText = text;
        this.onNegativeButtonListener = listener;
        return this;
    }

    /**
     * Set round button
     *
     * @param roundButton true if you want a round button
     * @return this
     */
    public ColorPicker setRoundButton(boolean roundButton) {
        this.roundButton = roundButton;
        return this;
    }

    /**
     * set a fast listener ( it shows a dialog without buttons and the event fires as soon you select a color )
     *
     * @param listener
     * @return
     */
    public ColorPicker setFastChooser(OnFastChooseColorListener listener) {
        this.fastChooser = true;
        this.onFastChooseColorListener = listener;
        return this;
    }

    /**
     * set a listener for the color picked
     *
     * @param listener
     */
    public void setOnChooseColorListener(OnChooseColorListener listener) {
        onChooseColorListener = listener;
    }

    /**
     * set if to dismiss the dialog or not on button click, by default is set to true
     */
    public ColorPicker setDismissOnButtonClick(boolean dismiss) {
        this.dismiss = dismiss;
        return this;
    }

    /**
     * set Match_parent to RecyclerView
     *
     * @return
     */
    public ColorPicker setDialogFullHeight() {
        this.fullheight = true;
        return this;
    }

    /**
     * Choose the color to be selected by default
     *
     * @param color int
     * @return
     */
    public ColorPicker setDefaultColor(int color) {
        this.default_color = color;
        return this;
    }

    /**
     * getDialog if you need more options
     *
     * @return
     */
    public CustomDialog getDialog() {
        return dialog;
    }

    /**
     * dismiss the dialog
     */
    public void dismissDialog() {
        if (dialog != null) {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            };
            handler.postDelayed(runnable, 250);
        }
    }

    private ColorPicker setTitlePadding(int left, int top, int right, int bottom) {
        paddingTitleLeft = left;
        paddingTitleRight = right;
        paddingTitleTop = top;
        paddingTitleBottom = bottom;
        return this;
    }

    private int dip2px(float dpValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
