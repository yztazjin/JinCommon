package ttyy.com.common.dialogs.ask;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import ttyy.com.common.dialogs.BaseDialog;
import ttyy.com.common.dialogs.BaseDialogBuilder;

/**
 * author: admin
 * date: 2017/05/15
 * version: 0
 * mail: secret
 * desc: AskDialogBuilder
 */

public class AskDialogBuilder extends BaseDialogBuilder {

    private int titleTextColor;
    private int titleTextSize;
    private String titleStr;

    private int askTextColor;
    private int askTextSize;
    private String askStr;

    private int confirmTextColor;
    private int confirmTextSize;
    private String confirmStr;

    private int cancelTextColor;
    private int cancelTextSize;
    private String cancelStr;

    private View.OnClickListener confirmClickListener;
    private View.OnClickListener cancelClickListener;

    public AskDialogBuilder(Context context){
        super(context);

        titleTextColor = Color.parseColor("#333333");
        titleTextSize = 15;
        titleStr = "标题";

        askTextColor = titleTextColor;
        askTextSize = 15;
        askStr = "确定么?";

        confirmTextColor = titleTextColor;
        confirmTextSize = 15;
        confirmStr = "确定";

        cancelTextColor = titleTextColor;
        cancelTextSize = 15;
        cancelStr = "取消";
    }

    @Override
    public AskDialogBuilder setWindowBackgroundColor(int color) {
        super.setWindowBackgroundColor(color);
        return this;
    }

    @Override
    public AskDialogBuilder setWindowWidth(int value) {
        super.setWindowWidth(value);
        return this;
    }

    @Override
    public AskDialogBuilder setWindowHeight(int value) {
        super.setWindowHeight(value);
        return this;
    }

    @Override
    public AskDialogBuilder setDim(float value) {
        super.setDim(value);
        return this;
    }

    @Override
    public AskDialogBuilder setRadius(int r) {
        super.setRadius(r);
        return this;
    }

    @Override
    public AskDialogBuilder setRadius(int tl, int tr, int br, int bl) {
        super.setRadius(tl, tr, br, bl);
        return this;
    }

    public AskDialogBuilder setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    public AskDialogBuilder setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public AskDialogBuilder setTitleStr(String titleStr) {
        this.titleStr = titleStr;
        return this;
    }

    public AskDialogBuilder setAskTextColor(int askTextColor) {
        this.askTextColor = askTextColor;
        return this;
    }

    public AskDialogBuilder setAskTextSize(int askTextSize) {
        this.askTextSize = askTextSize;
        return this;
    }

    public AskDialogBuilder setAskStr(String askStr) {
        this.askStr = askStr;
        return this;
    }

    public AskDialogBuilder setConfirmTextColor(int confirmTextColor) {
        this.confirmTextColor = confirmTextColor;
        return this;
    }

    public AskDialogBuilder setConfirmTextSize(int confirmTextSize) {
        this.confirmTextSize = confirmTextSize;
        return this;
    }

    public AskDialogBuilder setConfirmStr(String confirmStr) {
        this.confirmStr = confirmStr;
        return this;
    }

    public AskDialogBuilder setCancelTextColor(int cancelTextColor) {
        this.cancelTextColor = cancelTextColor;
        return this;
    }

    public AskDialogBuilder setCancelTextSize(int cancelTextSize) {
        this.cancelTextSize = cancelTextSize;
        return this;
    }

    public AskDialogBuilder setCancelStr(String cancelStr) {
        this.cancelStr = cancelStr;
        return this;
    }

    public AskDialogBuilder setConfirmClickListener(View.OnClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
        return this;
    }

    public AskDialogBuilder setCancelClickListener(View.OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
        return this;
    }

    public int getAskTextColor() {
        return askTextColor;
    }

    public int getAskTextSize() {
        return askTextSize;
    }

    public String getAskStr() {
        return askStr;
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

    public int getCancelTextColor() {
        return cancelTextColor;
    }

    public int getCancelTextSize() {
        return cancelTextSize;
    }

    public String getCancelStr() {
        return cancelStr;
    }

    public View.OnClickListener getConfirmClickListener() {
        return confirmClickListener;
    }

    public View.OnClickListener getCancelClickListener() {
        return cancelClickListener;
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
    public AskDialog build() {
        return new AskDialog(this);
    }

    @Override
    public void show() {
        build().show();
    }
}
