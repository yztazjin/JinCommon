package ttyy.com.common.dialogs.msg;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import ttyy.com.common.dialogs.BaseDialogBuilder;
import ttyy.com.common.dialogs.ask.AskDialog;

/**
 * author: admin
 * date: 2017/05/15
 * version: 0
 * mail: secret
 * desc: MsgDialogBuilder
 */

public class MsgDialogBuilder extends BaseDialogBuilder {

    private int titleTextColor;
    private int titleTextSize;
    private String titleStr;

    private int msgTextColor;
    private int msgTextSize;
    private String msgStr;

    private int confirmTextColor;
    private int confirmTextSize;
    private String confirmStr;

    private View.OnClickListener confirmClickListener;

    public MsgDialogBuilder(Context context){
        super(context);

        titleTextColor = Color.parseColor("#333333");
        titleTextSize = 15;
        titleStr = "标题";

        msgTextColor = titleTextColor;
        msgTextSize = 15;
        msgStr = "这是一个消息框";

        confirmTextColor = titleTextColor;
        confirmTextSize = 15;
        confirmStr = "确定";

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

    public MsgDialogBuilder setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    public MsgDialogBuilder setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public MsgDialogBuilder setTitleStr(String titleStr) {
        this.titleStr = titleStr;
        return this;
    }

    public MsgDialogBuilder setMsgTextColor(int askTextColor) {
        this.msgTextColor = askTextColor;
        return this;
    }

    public MsgDialogBuilder setMsgTextSize(int askTextSize) {
        this.msgTextSize = askTextSize;
        return this;
    }

    public MsgDialogBuilder setMsgStr(String askStr) {
        this.msgStr = askStr;
        return this;
    }

    public MsgDialogBuilder setConfirmTextColor(int confirmTextColor) {
        this.confirmTextColor = confirmTextColor;
        return this;
    }

    public MsgDialogBuilder setConfirmTextSize(int confirmTextSize) {
        this.confirmTextSize = confirmTextSize;
        return this;
    }

    public MsgDialogBuilder setConfirmStr(String confirmStr) {
        this.confirmStr = confirmStr;
        return this;
    }

    public MsgDialogBuilder setConfirmClickListener(View.OnClickListener clickListener) {
        this.confirmClickListener = clickListener;
        return this;
    }

    public int getMsgTextColor() {
        return msgTextColor;
    }

    public int getMsgTextSize() {
        return msgTextSize;
    }

    public String getMsgStr() {
        return msgStr;
    }

    public int getConfirmTextColor() {
        return confirmTextColor;
    }

    public int getConfirmTextSize() {
        return confirmTextSize;
    }

    public String getConfirmStr() {
        return confirmStr;
    }

    public View.OnClickListener getConfirmClickListener() {
        return confirmClickListener;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public int getTitleTextSize() {
        return titleTextSize;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    @Override
    public MsgDialog build() {
        return new MsgDialog(this);
    }

    @Override
    public void show() {
        build().show();
    }
}
