package ttyy.com.common.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * author: admin
 * date: 2017/05/15
 * version: 0
 * mail: secret
 * desc: BaseDialog
 */

public abstract class BaseDialog extends Dialog{

    protected LinearLayout mRootView;

    public BaseDialog(BaseDialogBuilder builder) {
        super(builder.getContext());

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mRootView = new LinearLayout(builder.getContext());
        mRootView.setOrientation(LinearLayout.VERTICAL);
        mRootView.setBackground(builder.getWindowBackgroundColorDrawable());
        setContentView(mRootView);
    }

    protected abstract void onRootViewInitialized(LinearLayout mRootView);

    public LinearLayout getRootView(){
        return mRootView;
    }

}
