package petrov.kristiyan.colorpicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static petrov.kristiyan.colorpicker.ColorUtils.dip2px;
import static petrov.kristiyan.colorpicker.ColorUtils.getDimensionDp;


public class ColorPicker {

    private OnChooseColorListener onChooseColorListener;
    private OnFastChooseColorListener onFastChooseColorListener;

    public interface OnChooseColorListener {
        void onChooseColor(int position, int color);

        void onCancel();
    }

    public interface OnFastChooseColorListener {
        void setOnFastChooseColorListener(int position, int color);

        void onCancel();
    }

    public interface OnButtonListener {
        void onClick(View v, int position, int color);
    }

    private ArrayList<ColorPal> colors;
    private ColorViewAdapter colorViewAdapter;
    private boolean fastChooser;
    private TypedArray ta;
    private WeakReference<Activity> mContext;
    private int columns;
    private String title;
    private int marginLeft, marginRight, marginTop, marginBottom;
    private int tickColor;
    private int marginColorButtonLeft, marginColorButtonRight, marginColorButtonTop, marginColorButtonBottom;
    private int colorButtonWidth, colorButtonHeight;
    private int colorButtonDrawable;
    private String negativeText, positiveText;
    private boolean roundColorButton;
    private boolean dismiss;
    private boolean fullHeight;
    private WeakReference<CustomDialog> mDialog;
    private RecyclerView recyclerView;
    private RelativeLayout colorpicker_base;
    private LinearLayout buttons_layout;
    private int default_color;
    private int paddingTitleLeft, paddingTitleRight, paddingTitleBottom, paddingTitleTop;
    private View dialogViewLayout;
    private boolean disableDefaultButtons;
    private AppCompatButton positiveButton, negativeButton;

    /**
     * Constructor
     */
    public ColorPicker(Activity context) {
        dialogViewLayout = LayoutInflater.from(context).inflate(R.layout.color_palette_layout, null, false);
        colorpicker_base = dialogViewLayout.findViewById(R.id.colorpicker_base);
        recyclerView = dialogViewLayout.findViewById(R.id.color_palette);
        buttons_layout = dialogViewLayout.findViewById(R.id.buttons_layout);
        positiveButton = dialogViewLayout.findViewById(R.id.positive);
        negativeButton = dialogViewLayout.findViewById(R.id.negative);

        this.mContext = new WeakReference<>(context);
        this.dismiss = true;
        this.marginColorButtonLeft = this.marginColorButtonTop = this.marginColorButtonRight = this.marginColorButtonBottom = 5;
        this.title = context.getString(R.string.colorpicker_dialog_title);
        this.negativeText = context.getString(R.string.colorpicker_dialog_cancel);
        this.positiveText = context.getString(R.string.colorpicker_dialog_ok);
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
        if (mContext == null)
            return this;

        Context context = mContext.get();
        if (context == null)
            return this;

        ta = context.getResources().obtainTypedArray(resId);
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
            colors.add(new ColorPal(Color.parseColor(colorsHexList.get(i)), false));
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
        for (int aColorsList : colorsList) {
            colors.add(new ColorPal(aColorsList, false));
        }
        return this;
    }

    /**
     * Choose the color to be selected by default
     *
     * @param color int
     * @return this
     */
    public ColorPicker setDefaultColorButton(int color) {
        this.default_color = color;
        return this;
    }

    /**
     * Show the Material Dialog
     */
    public void show() {
        if (mContext == null)
            return;

        Activity context = mContext.get();
        if (context == null)
            return;

        if (colors == null || colors.isEmpty())
            setColors();

        AppCompatTextView titleView = dialogViewLayout.findViewById(R.id.title);
        if (title != null) {
            titleView.setText(title);
            titleView.setPadding(
                    dip2px(paddingTitleLeft, context), dip2px(paddingTitleTop, context),
                    dip2px(paddingTitleRight, context), dip2px(paddingTitleBottom, context));
        }
        mDialog = new WeakReference<>(new CustomDialog(context, dialogViewLayout));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, columns);
        recyclerView.setLayoutManager(gridLayoutManager);
        if (fastChooser)
            colorViewAdapter = new ColorViewAdapter(colors, onFastChooseColorListener, mDialog);
        else
            colorViewAdapter = new ColorViewAdapter(colors);

        if (fullHeight) {
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
            colorViewAdapter.setTickColor(tickColor);
        }
        if (marginColorButtonBottom != 0 || marginColorButtonLeft != 0 || marginColorButtonRight != 0 || marginColorButtonTop != 0) {
            colorViewAdapter.setColorButtonMargin(
                    dip2px(marginColorButtonLeft, context), dip2px(marginColorButtonTop, context),
                    dip2px(marginColorButtonRight, context), dip2px(marginColorButtonBottom, context));
        }
        if (colorButtonHeight != 0 || colorButtonWidth != 0) {
            colorViewAdapter.setColorButtonSize(dip2px(colorButtonWidth, context), dip2px(colorButtonHeight, context));
        }
        if (roundColorButton) {
            setColorButtonDrawable(R.drawable.round_button);
        }
        if (colorButtonDrawable != 0) {
            colorViewAdapter.setColorButtonDrawable(colorButtonDrawable);
        }

        if (default_color != 0) {
            colorViewAdapter.setDefaultColor(default_color);
        }

        if (disableDefaultButtons) {
            positiveButton.setVisibility(View.GONE);
            negativeButton.setVisibility(View.GONE);
        }

        positiveButton.setText(positiveText);
        negativeButton.setText(negativeText);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChooseColorListener != null && !fastChooser)
                    onChooseColorListener.onChooseColor(colorViewAdapter.getColorPosition(), colorViewAdapter.getColorSelected());
                if (dismiss) {
                    dismissDialog();
                    if (onFastChooseColorListener != null) {
                        onFastChooseColorListener.onCancel();
                    }
                }
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dismiss)
                    dismissDialog();
                if (onChooseColorListener != null)
                    onChooseColorListener.onCancel();
            }
        });

        if (mDialog == null) {
            return;
        }

        Dialog dialog = mDialog.get();

        if (dialog != null && !context.isFinishing()) {
            dialog.show();
            //Keep mDialog open when rotate
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
        }

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
     * Set tick color
     *
     * @param color Color
     * @return this
     */
    public ColorPicker setColorButtonTickColor(int color) {
        this.tickColor = color;
        return this;
    }

    /**
     * Set a single drawable for all buttons example : you can define a different shape ( then round or square )
     *
     * @param drawable Resource
     * @return this
     */
    public ColorPicker setColorButtonDrawable(int drawable) {
        this.colorButtonDrawable = drawable;
        return this;
    }

    /**
     * Set the buttons size in DP
     *
     * @param width  width
     * @param height height
     * @return this
     */
    public ColorPicker setColorButtonSize(int width, int height) {
        this.colorButtonWidth = width;
        this.colorButtonHeight = height;
        return this;
    }

    /**
     * Set the Margin between the buttons in DP is 10
     *
     * @param left   left
     * @param top    top
     * @param right  right
     * @param bottom bottom
     * @return this
     */
    public ColorPicker setColorButtonMargin(int left, int top, int right, int bottom) {
        this.marginColorButtonLeft = left;
        this.marginColorButtonTop = top;
        this.marginColorButtonRight = right;
        this.marginColorButtonBottom = bottom;
        return this;
    }

    /**
     * Set round button
     *
     * @param roundButton true if you want a round button
     * @return this
     */
    public ColorPicker setRoundColorButton(boolean roundButton) {
        this.roundColorButton = roundButton;
        return this;
    }

    /**
     * set a fast listener ( it shows a mDialog without buttons and the event fires as soon you select a color )
     *
     * @param listener OnFastChooseColorListener
     * @return this
     */
    public ColorPicker setOnFastChooseColorListener(OnFastChooseColorListener listener) {
        this.fastChooser = true;
        buttons_layout.setVisibility(View.GONE);
        this.onFastChooseColorListener = listener;
        dismissDialog();
        return this;
    }

    /**
     * set a listener for the color picked
     *
     * @param listener OnChooseColorListener
     */
    public ColorPicker setOnChooseColorListener(OnChooseColorListener listener) {
        onChooseColorListener = listener;
        return this;
    }

    /**
     * Add a  Button
     *
     * @param text     title of button
     * @param button   button to be added
     * @param listener listener
     * @return this
     */
    public ColorPicker addListenerButton(String text, Button button, final OnButtonListener listener) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, colorViewAdapter.getColorPosition(), colorViewAdapter.getColorSelected());
            }
        });
        button.setText(text);
        if (button.getParent() != null)
            buttons_layout.removeView(button);
        buttons_layout.addView(button);
        return this;
    }

    /**
     * add a new Button using default style
     *
     * @param text     title of button
     * @param listener OnButtonListener
     * @return this
     */
    public ColorPicker addListenerButton(String text, final OnButtonListener listener) {
        if (mContext == null)
            return this;

        Context context = mContext.get();
        if (context == null)
            return this;


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(dip2px(10, context), 0, 0, 0);
        Button button = new Button(context);
        button.setMinWidth(getDimensionDp(R.dimen.action_button_min_width, context));
        button.setMinimumWidth(getDimensionDp(R.dimen.action_button_min_width, context));
        button.setPadding(
                getDimensionDp(R.dimen.action_button_padding_horizontal, context) + dip2px(5, context), 0,
                getDimensionDp(R.dimen.action_button_padding_horizontal, context) + dip2px(5, context), 0);
        button.setBackgroundResource(R.drawable.button);
        button.setTextSize(getDimensionDp(R.dimen.action_button_text_size, context));
        button.setTextColor(ContextCompat.getColor(context, R.color.black_de));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, colorViewAdapter.getColorPosition(), colorViewAdapter.getColorSelected());
            }
        });
        button.setText(text);
        if (button.getParent() != null)
            buttons_layout.removeView(button);

        buttons_layout.addView(button);
        button.setLayoutParams(params);
        return this;
    }

    /**
     * set if to dismiss the mDialog or not on button listener click, by default is set to true
     *
     * @param dismiss boolean
     * @return this
     */
    public ColorPicker setDismissOnButtonListenerClick(boolean dismiss) {
        this.dismiss = dismiss;
        return this;
    }

    /**
     * set Match_parent to RecyclerView
     *
     * @return this
     */
    public ColorPicker setDialogFullHeight() {
        this.fullHeight = true;
        return this;
    }

    /**
     * getmDialog if you need more options
     *
     * @return CustomDialog
     */
    public
    @Nullable
    CustomDialog getmDialog() {
        if (mDialog == null)
            return null;
        return mDialog.get();
    }

    /**
     * getDialogViewLayout is the view inflated into the mDialog
     *
     * @return View
     */
    public View getDialogViewLayout() {
        return dialogViewLayout;
    }

    /**
     * getDialogBaseLayout which is the RelativeLayout that contains the RecyclerView
     *
     * @return RelativeLayout
     */
    public RelativeLayout getDialogBaseLayout() {
        return colorpicker_base;
    }

    /**
     * get the default PositiveButton
     *
     * @return Button
     */
    public Button getPositiveButton() {
        return positiveButton;
    }

    /**
     * get the default NegativeButton
     *
     * @return Button
     */
    public Button getNegativeButton() {
        return negativeButton;
    }

    /**
     * dismiss the mDialog
     */
    public void dismissDialog() {
        if (mDialog == null)
            return;

        Dialog dialog = mDialog.get();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * disables the postive and negative buttons
     *
     * @param disableDefaultButtons boolean
     * @return this
     */
    public ColorPicker disableDefaultButtons(boolean disableDefaultButtons) {
        this.disableDefaultButtons = disableDefaultButtons;
        return this;
    }

    /**
     * set padding to the title in DP
     *
     * @param left   dp
     * @param top    dp
     * @param right  dp
     * @param bottom dp
     * @return this
     */
    public ColorPicker setTitlePadding(int left, int top, int right, int bottom) {
        paddingTitleLeft = left;
        paddingTitleRight = right;
        paddingTitleTop = top;
        paddingTitleBottom = bottom;
        return this;
    }

    /**
     * Set default colors defined in colorpicker-array.xml of the library
     *
     * @return this
     */
    private ColorPicker setColors() {
        if (mContext == null)
            return this;

        Context context = mContext.get();
        if (context == null)
            return this;

        ta = context.getResources().obtainTypedArray(R.array.default_colors);
        colors = new ArrayList<>();
        for (int i = 0; i < ta.length(); i++) {
            colors.add(new ColorPal(ta.getColor(i, 0), false));
        }
        return this;
    }

    private ColorPicker setMargin(int left, int top, int right, int bottom) {
        this.marginLeft = left;
        this.marginRight = right;
        this.marginTop = top;
        this.marginBottom = bottom;
        return this;
    }

}
