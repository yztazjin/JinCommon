package ttyy.com.common.dialogs;

import android.app.Activity;

import ttyy.com.common.dialogs.ask.AskDialogBuilder;
import ttyy.com.common.dialogs.chose.ChoseDialogBuilder;
import ttyy.com.common.dialogs.msg.MsgDialogBuilder;

/**
 * author: admin
 * date: 2017/05/15
 * version: 0
 * mail: secret
 * desc: DialogHelper
 */

public class DialogHelper {

    private DialogHelper(){}

    public static AskDialogBuilder getAskDialog(Activity context){
        return new AskDialogBuilder(context);
    }

    public static MsgDialogBuilder getMsgDialog(Activity context){
        return new MsgDialogBuilder(context);
    }

    public static ChoseDialogBuilder getChooseDialog(Activity context){
        return new ChoseDialogBuilder(context);
    }

}
