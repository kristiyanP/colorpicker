package petrov.kristiyan.colorpicker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class ColorViewAdapter extends RecyclerView.Adapter<ColorViewAdapter.ViewHolder> {

    private ColorPicker.OnFastChooseColorListener onFastChooseColorListener;
    private ArrayList<ColorPal> mDataset;
    private int colorPosition = -1;
    private int colorSelected;
    private int marginLeft, marginRight, marginTop, marginBottom;
    private int tickColor = Color.WHITE;
    private int marginButtonLeft = 0, marginButtonRight = 0, marginButtonTop = 3, marginButtonBottom = 3;
    private int buttonWidth = -1, buttonHeight = -1;
    private int buttonDrawable;
    private WeakReference<CustomDialog> mDialog;


    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public AppCompatButton colorItem;

        public ViewHolder(View v) {
            super(v);
            //buttons settings
            colorItem = v.findViewById(R.id.color);
            colorItem.setTextColor(tickColor);
            colorItem.setBackgroundResource(buttonDrawable);
            colorItem.setOnClickListener(this);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) colorItem.getLayoutParams();
            layoutParams.setMargins(marginButtonLeft, marginButtonTop, marginButtonRight, marginButtonBottom);
            if (buttonWidth != -1)
                layoutParams.width = buttonWidth;
            if (buttonHeight != -1)
                layoutParams.height = buttonHeight;

            //relative layout settings
            LinearLayout linearLayout = v.findViewById(R.id.linearLayout);
            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) linearLayout.getLayoutParams();
            lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        }

        @Override
        public void onClick(View v) {
            if (colorPosition != -1 && colorPosition != getLayoutPosition()) {
                mDataset.get(colorPosition).setCheck(false);
                notifyItemChanged(colorPosition);
            }
            colorPosition = getLayoutPosition();
            colorSelected = (int) v.getTag();
            mDataset.get(getLayoutPosition()).setCheck(true);
            notifyItemChanged(colorPosition);

            if (onFastChooseColorListener != null && mDialog != null) {
                onFastChooseColorListener.setOnFastChooseColorListener(colorPosition, colorSelected);
                dismissDialog();
            }
        }
    }

    private void dismissDialog() {
        if(mDialog == null)
            return;
        Dialog dialog = mDialog.get();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public int getColorSelected() {
        return colorSelected;
    }

    public int getColorPosition() {
        return colorPosition;
    }

    public ColorViewAdapter(ArrayList<ColorPal> myDataset, ColorPicker.OnFastChooseColorListener onFastChooseColorListener, WeakReference<CustomDialog> dialog) {
        mDataset = myDataset;
        mDialog = dialog;
        this.onFastChooseColorListener = onFastChooseColorListener;
    }

    public ColorViewAdapter(ArrayList<ColorPal> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.palette_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int color = mDataset.get(position).getColor();

        int textColor = ColorUtils.isWhiteText(color) ? Color.WHITE : Color.BLACK;

        if (mDataset.get(position).isCheck()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                holder.colorItem.setText("✔");
            } else {

                holder.colorItem.setText(Html.fromHtml("&#x2713;"));
            }
        } else
            holder.colorItem.setText("");

        holder.colorItem.setTextColor(tickColor == Color.WHITE ? textColor : tickColor);
        if (buttonDrawable != 0) {
            holder.colorItem.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else {
            holder.colorItem.setBackgroundColor(color);
        }
        holder.colorItem.setTag(color);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void setMargin(int left, int top, int right, int bottom) {
        this.marginBottom = bottom;
        this.marginLeft = left;
        this.marginRight = right;
        this.marginTop = top;
    }

    public void setDefaultColor(int color) {
        for (int i = 0; i < mDataset.size(); i++) {
            ColorPal colorPal = mDataset.get(i);
            if (colorPal.getColor() == color) {
                colorPal.setCheck(true);
                colorPosition = i;
                notifyItemChanged(i);
            }
        }
    }

    public void setTickColor(int color) {
        this.tickColor = color;
    }

    public void setColorButtonMargin(int left, int top, int right, int bottom) {
        this.marginButtonLeft = left;
        this.marginButtonRight = right;
        this.marginButtonTop = top;
        this.marginButtonBottom = bottom;
    }

    public void setColorButtonSize(int width, int height) {
        this.buttonWidth = width;
        this.buttonHeight = height;
    }

    public void setColorButtonDrawable(int drawable) {
        this.buttonDrawable = drawable;
    }

}