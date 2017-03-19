package ttyy.com.jincommon.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ttyy.com.common.future.PyInfoGetter;

public class MainActivity extends AppCompatActivity {

    TextView tv_ip;

    TextView tv_port;

    TextView tv_channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_ip = (TextView) findViewById(R.id.tv_ip);
        tv_port = (TextView) findViewById(R.id.tv_port);
        tv_channel = (TextView) findViewById(R.id.tv_channel);

        tv_ip.setText("IP: "+ PyInfoGetter.getPyValue(this, "ip"));
        tv_port.setText("PORT: "+PyInfoGetter.getPyValue(this, "port"));
        tv_channel.setText("Channel: "+PyInfoGetter.getPyValue(this, "channel"));
    }
}