package ttyy.com.jincommon.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import ttyy.com.common.log.log;

/**
 * Author: hjq
 * Date  : 2017/03/31 19:40
 * Name  : WMTestActivity
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class WMTestActivity extends Activity implements View.OnClickListener {

    Button tv_wm_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wm_test);

        tv_wm_test = (Button) findViewById(R.id.tv_wm_test);
        tv_wm_test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        JinWindow.getInstance(this).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        log.d("WMTestActivity", "WMTestActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        log.d("WMTestActivity", "WMTestActivity onStop");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        log.d("WMTestActivity", "WMTestActivity onBackPressed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log.d("WMTestActivity", "WMTestActivity onDestroy");
    }
}
