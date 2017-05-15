package ttyy.com.common.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.WindowManager;

/**
 * author: admin
 * date: 2017/05/15
 * version: 0
 * mail: secret
 * desc: BaseDialogBuilder
 */

public abstract class BaseDialogBuilder {

    private int windowWidth;
    private int windowHeight;
    private float dim;

    private int[] radius;// tl tr br bl

    private int windowBackgroundColor;

    private boolean cancelable;
    private boolean canceledOnTouchOutside;

    private Context context;

    public BaseDialogBuilder(Context context){
        this.context = context;
        windowWidth = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.7f);
        windowHeight = WindowManager.LayoutParams.WRAP_CONTENT;
        windowBackgroundColor = Color.WHITE;
        dim = 0.7f;

        cancelable = true;
        canceledOnTouchOutside = true;

        radius = new int[]{8,8,8,8};
    }

    public Context getContext(){
        return context;
    }

    public int getWindowWidth(){
        return windowWidth;
    }

    public BaseDialogBuilder setWindowWidth(int value){
        windowWidth = value;
        return this;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public BaseDialogBuilder setWindowHeight(int value){
        windowHeight = value;
        return this;
    }

    public float getDim(){
        return dim;
    }

    public BaseDialogBuilder setDim(float value){
        dim = value;
        return this;
    }

    public int getWindowBackgroundColor(){
        return windowBackgroundColor;
    }

    public BaseDialogBuilder setWindowBackgroundColor(int color){
        windowBackgroundColor = color;
        return this;
    }

    protected Drawable getWindowBackgroundColorDrawable(){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(windowBackgroundColor);

        drawable.setCornerRadii(new float[]{
                radius[0],radius[0],
                radius[1],radius[1],
                radius[2],radius[2],
                radius[3],radius[3]});

        return drawable;
    }

    public int[] getRadius(){
        return radius;
    }

    public BaseDialogBuilder setRadius(int r){
        radius[0] = r;
        radius[1] = r;
        radius[2] = r;
        radius[3] = r;
        return this;
    }

    public BaseDialogBuilder setRadius(int tl, int tr, int br, int bl){
        radius[0] = tl;
        radius[1] = tr;
        radius[2] = br;
        radius[3] = bl;
        return this;
    }

    public BaseDialogBuilder setCancelable(boolean value){
        cancelable = value;
        return this;
    }

    public boolean getCancelable(){
        return cancelable;
    }

    public BaseDialogBuilder setCanceledOnTouchOutside(boolean value){
        canceledOnTouchOutside = value;
        return this;
    }

    public boolean getCanceledOnTouchOutside(){
        return canceledOnTouchOutside;
    }

    public abstract <T extends BaseDialog> T build();

    public abstract void show();
}
