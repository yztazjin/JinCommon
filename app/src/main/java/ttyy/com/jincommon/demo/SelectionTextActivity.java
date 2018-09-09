package ttyy.com.jincommon.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import ttyy.com.common.text.selection.UiSelectorTextView;

/**
 * Author: hjq
 * Date  : 2018/09/08 14:05
 * Name  : SelectionTextActivity
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SelectionTextActivity extends Activity{

    TextView tv_text = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_text);
        tv_text = (TextView) findViewById(R.id.tv_text);

        UiSelectorTextView.wrap(tv_text);
    }
}
