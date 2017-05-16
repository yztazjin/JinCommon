package ttyy.com.common.dialogs.cstm;

import android.app.Dialog;
import android.view.View;

/**
 * author: admin
 * date: 2017/05/16
 * version: 0
 * mail: secret
 * desc: CstmOnClickListener
 */

public abstract class CstmOnClickListener implements View.OnClickListener{

    protected Dialog instance;

    protected void dismiss(){
        if(instance != null){
            instance.dismiss();
        }
    }
}
