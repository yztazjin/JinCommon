package ttyy.com.jincommon.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ttyy.com.common.future.PyInfoGetter;
import ttyy.com.common.log.log;

public class MainActivity extends AppCompatActivity {

    TextView tv_ip;

    TextView tv_port;

    TextView tv_channel;

    View tv_wm_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_ip = (TextView) findViewById(R.id.tv_ip);
        tv_port = (TextView) findViewById(R.id.tv_port);
        tv_channel = (TextView) findViewById(R.id.tv_channel);
        tv_wm_test = findViewById(R.id.tv_wm_test);

        tv_ip.setText("IP: "+ PyInfoGetter.getPyValue(this, "ip"));
        tv_port.setText("PORT: "+PyInfoGetter.getPyValue(this, "port"));
        tv_channel.setText("Channel: "+PyInfoGetter.getPyValue(this, "channel"));

        log.d("Test", "TestLog");
        tv_wm_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WMTestActivity.class));
            }
        });
    }
}
