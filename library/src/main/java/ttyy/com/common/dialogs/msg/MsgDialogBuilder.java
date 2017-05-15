package ttyy.com.common.dialogs.msg;

import android.content.Context;

import ttyy.com.common.dialogs.BaseDialog;
import ttyy.com.common.dialogs.BaseDialogBuilder;

/**
 * author: admin
 * date: 2017/05/15
 * version: 0
 * mail: secret
 * desc: MsgDialogBuilder
 */

public class MsgDialogBuilder extends BaseDialogBuilder {

    public MsgDialogBuilder(Context context) {
        super(context);
    }

    @Override
    public MsgDialogBuilder setWindowBackgroundColor(int color) {
        super.setWindowBackgroundColor(color);
        return this;
    }

    @Override
    public MsgDialogBuilder setWindowWidth(int value) {
        super.setWindowWidth(value);
        return this;
    }

    @Override
    public MsgDialogBuilder setWindowHeight(int value) {
        super.setWindowHeight(value);
        return this;
    }

    @Override
    public MsgDialogBuilder setDim(float value) {
        super.setDim(value);
        return this;
    }

    @Override
    public MsgDialogBuilder setRadius(int r) {
        super.setRadius(r);
        return this;
    }

    @Override
    public MsgDialogBuilder setRadius(int tl, int tr, int br, int bl) {
        super.setRadius(tl, tr, br, bl);
        return this;
    }

    @Override
    public MsgDialogBuilder setCancelable(boolean value) {
        super.setCancelable(value);
        return this;
    }

    @Override
    public MsgDialogBuilder setCanceledOnTouchOutside(boolean value) {
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
