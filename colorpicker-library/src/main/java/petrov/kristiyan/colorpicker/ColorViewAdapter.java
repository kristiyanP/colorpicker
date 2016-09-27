package petrov.kristiyan.colorpicker;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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
    private CustomDialog dialog;


    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public Button colorItem;

        public ViewHolder(View v) {
            super(v);
            //buttons settings
            colorItem = (Button) v.findViewById(R.id.color);
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
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
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

            if (onFastChooseColorListener != null && dialog!= null) {
                onFastChooseColorListener.setOnFastChooseColorListener(colorPosition, colorSelected);
                dismissDialog();
            }
        }
    }
    private void dismissDialog() {
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

    public int getColorSelected() {
        return colorSelected;
    }

    public int getColorPosition() {
        return colorPosition;
    }

    public ColorViewAdapter(ArrayList<ColorPal> myDataset, ColorPicker.OnFastChooseColorListener onFastChooseColorListener,CustomDialog dialog) {
        mDataset = myDataset;
        this.dialog = dialog;
        this.onFastChooseColorListener = onFastChooseColorListener;
    }

    public ColorViewAdapter(ArrayList<ColorPal> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public ColorViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.palette_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mDataset.get(position).isCheck()) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                holder.colorItem.setText("✔");
            }
            else {
                holder.colorItem.setText(Html.fromHtml("&#x2713;"));
            }
        }
        else
            holder.colorItem.setText("");
        if (buttonDrawable != 0) {
            holder.colorItem.getBackground().setColorFilter(mDataset.get(position).getColor(), PorterDuff.Mode.SRC_IN);
        } else {
            holder.colorItem.setBackgroundColor(mDataset.get(position).getColor());
        }
        holder.colorItem.setTag(mDataset.get(position).getColor());
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