package ttyy.com.common.dialogs.chose;

import android.content.Context;

import ttyy.com.common.dialogs.BaseDialog;
import ttyy.com.common.dialogs.BaseDialogBuilder;

/**
 * author: admin
 * date: 2017/05/15
 * version: 0
 * mail: secret
 * desc: ChoseDialogBuilder
 */

public class ChoseDialogBuilder extends BaseDialogBuilder {

    public ChoseDialogBuilder(Context context) {
        super(context);
    }

    @Override
    public ChoseDialogBuilder setWindowBackgroundColor(int color) {
        super.setWindowBackgroundColor(color);
        return this;
    }

    @Override
    public ChoseDialogBuilder setWindowWidth(int value) {
        super.setWindowWidth(value);
        return this;
    }

    @Override
    public ChoseDialogBuilder setWindowHeight(int value) {
        super.setWindowHeight(value);
        return this;
    }

    @Override
    public ChoseDialogBuilder setDim(float value) {
        super.setDim(value);
        return this;
    }

    @Override
    public ChoseDialogBuilder setRadius(int r) {
        super.setRadius(r);
        return this;
    }

    @Override
    public ChoseDialogBuilder setRadius(int tl, int tr, int br, int bl) {
        super.setRadius(tl, tr, br, bl);
        return this;
    }

    @Override
    public ChoseDialogBuilder setCancelable(boolean value) {
        super.setCancelable(value);
        return this;
    }

    @Override
    public ChoseDialogBuilder setCanceledOnTouchOutside(boolean value) {
        super.setCanceledOnTouchOutside(value);
        return this;
    }

    @Override
    public <T extends BaseDialog> T build() {
        return null;
    }

    @Override
    public void show() {

    }
}
