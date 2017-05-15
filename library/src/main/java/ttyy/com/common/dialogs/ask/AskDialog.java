package ttyy.com.common.dialogs.ask;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ttyy.com.common.dialogs.BaseDialog;

/**
 * author: admin
 * date: 2017/05/15
 * version: 0
 * mail: secret
 * desc: AskDialog
 */

public class AskDialog extends BaseDialog {

    public AskDialog(AskDialogBuilder builder) {
        super(builder);
    }

    @Override
    protected void onRootViewInitialized(LinearLayout mRootView) {
        addTitle(mRootView);
        addAsk(mRootView);
        addConCan(mRootView);
    }

    void addTitle(LinearLayout mRootView) {
        AskDialogBuilder builder = getBuilder();

        TextView tv_title = new TextView(getContext());

        float df = getContext().getResources().getDisplayMetrics().scaledDensity;
        int pb = (int) (0.5f * df * builder.getTitleTextSize() + 0.5f);
        tv_title.setPadding(builder.getRadius()[0] + pb, builder.getRadius()[0] + pb, 0, pb);

        tv_title.setText(builder.getTitleStr());
        tv_title.setTextSize(builder.getTitleTextSize());
        tv_title.setTextColor(builder.getTitleTextColor());

        mRootView.addView(tv_title);
    }

    void addAsk(LinearLayout mRootView) {
        AskDialogBuilder builder = getBuilder();

        TextView tv_ask = new TextView(getContext());

        float df = getContext().getResources().getDisplayMetrics().scaledDensity;
        int p = (int) (0.618f * df * builder.getAskTextSize() + 0.5f);
        int pb = (int) (0.5f * df * builder.getTitleTextSize() + 0.5f);
        tv_ask.setPadding(builder.getRadius()[0] + pb, 0 , 0, p);

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int titleStrLength = builder.getTitleStr().length();
        titleStrLength = titleStrLength < 6 ? titleStrLength + 1 : 7;
        for (int i = 0; i < titleStrLength; i++) {
            ssb.append("正");
        }
        ssb.append(builder.getAskStr());
        ssb.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, titleStrLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_ask.setText(ssb);

        tv_ask.setTextSize(builder.getTitleTextSize());
        tv_ask.setTextColor(builder.getTitleTextColor());

        mRootView.addView(tv_ask);
    }

    void addConCan(LinearLayout mRootView) {
        final AskDialogBuilder builder = getBuilder();

        LinearLayout ll_concan = new LinearLayout(getContext());
        ll_concan.setGravity(Gravity.RIGHT);

        float sd = getContext().getResources().getDisplayMetrics().scaledDensity;
        float df = getContext().getResources().getDisplayMetrics().density;
        int p = (int) (0.5f * sd * builder.getTitleTextSize() + 0.5f);
        ll_concan.setPadding(p, 0, p, (int)(p * 1.6f));

        TextView tv_confirm = new TextView(getContext());
        tv_confirm.setText(builder.getConfirmStr());
        tv_confirm.setTextSize(builder.getConfirmTextSize());
        tv_confirm.setTextColor(builder.getConfirmTextColor());
        tv_confirm.setPadding((int)(df * 10), 0, (int)(df * 5), 0);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(builder.getConfirmClickListener() != null){
                    builder.getConfirmClickListener().onClick(v);
                }
            }
        });

        TextView tv_cancel = new TextView(getContext());
        tv_cancel.setText(builder.getCancelStr());
        tv_cancel.setTextSize(builder.getCancelTextSize());
        tv_cancel.setTextColor(builder.getCancelTextColor());
        tv_cancel.setPadding((int)(df * 10), 0, (int)(df * 5), 0);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(builder.getCancelClickListener() != null){
                    builder.getCancelClickListener().onClick(v);
                }
            }
        });

        ll_concan.addView(tv_cancel);
        ll_concan.addView(tv_confirm);

        mRootView.addView(ll_concan);
    }
}
