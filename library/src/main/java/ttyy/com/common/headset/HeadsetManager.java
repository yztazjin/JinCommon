package ttyy.com.common.headset;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author: admin
 * date: 2017/07/06
 * version: 0
 * mail: secret
 * desc: HeadsetManager
 * permission:
 *   <uses-permission android:name="android.permission.BLUETOOTH"/>  蓝牙权限
 *   <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/> 蓝牙权限 蓝牙设备具体信息获取权限 蓝牙配对权限
 */

public class HeadsetManager {

    Context mContext;

    HeadSetReceiver mHeadSetReceiver;
    IntentFilter mHeadSetFilter;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothA2dp mBluetoothA2dp;

    AudioManager mAudioManager;

    private HeadsetManager(Context context){
        mContext = context;
        mHeadSetReceiver = new HeadSetReceiver();
        mHeadSetFilter = new IntentFilter();

        // 有线耳机
        mHeadSetFilter.addAction(Intent.ACTION_HEADSET_PLUG);

        // HeadSet协议 蓝牙耳机 连接状态
        mHeadSetFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        // 耳机音频信道连接状态
        mHeadSetFilter.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);

        // 蓝牙耳机 查询 适配状态接收过滤
        mHeadSetFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mHeadSetFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mHeadSetFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        mHeadSetFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // MODE_IN_COMMUNICATION 适配Android5.0及以上
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    }

    public static HeadsetManager create(Context context){
        return new HeadsetManager(context);
    }

    /**
     * 注册侦听
     */
    public void register(){
        mContext.registerReceiver(mHeadSetReceiver, mHeadSetFilter);
    }

    /**
     * 取消侦听
     */
    public void unRegister(){
        if(mHeadSetFilter != null){
            mContext.unregisterReceiver(mHeadSetReceiver);
            mHeadSetFilter = null;
        }
    }

    /**
     * 手机是否支持蓝牙
     * @return
     */
    public boolean isDeviceSupportBlueTooth(){

        return mBluetoothAdapter != null;
    }

    /**
     * 手机蓝牙是否打开
     * @return
     */
    public boolean isBlueToothOpend(){
        if(mBluetoothAdapter == null){
            return false;
        }
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * 打开手机蓝牙
     * @return
     */
    public boolean openBlueTooth(){
        return mBluetoothAdapter.enable();
    }

    /**
     * 获取当前耳机的连接设备类型 有线耳机 蓝牙耳机 未知
     * @return
     */
    public DeviceType getConnectedDeviceType(){

        if(mAudioManager.isWiredHeadsetOn()){
            return DeviceType.WIRED_HEADSET;
        }

        if(mBluetoothAdapter == null){
            return DeviceType.UNKNOWN;
        }

        int a2dp = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        int headset = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        int health = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);

        if(a2dp == BluetoothProfile.STATE_CONNECTED
                || headset == BluetoothProfile.STATE_CONNECTED
                || health == BluetoothProfile.STATE_CONNECTED){
            return DeviceType.BLUETOOTH;
        }

        return DeviceType.UNKNOWN;
    }

    public boolean startBluetoothDiscovery(){
        if(mBluetoothAdapter == null){
            return false;
        }

        if(!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        }

        mBluetoothAdapter.startDiscovery();

        mBluetoothAdapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                if(profile == BluetoothProfile.A2DP){
                    //Service连接成功，获得BluetoothA2DP
                    mBluetoothA2dp = (BluetoothA2dp) proxy;
                }
            }

            @Override
            public void onServiceDisconnected(int profile) {

            }
        }, BluetoothProfile.A2DP);

        return true;
    }

    /**
     * 音频播放，音频采集 常用于电话场景
     */
    public void startSCO(){
        mAudioManager.startBluetoothSco();
        mAudioManager.setBluetoothScoOn(true);
    }

    /**
     * 音频播放，音频采集 常用于电话场景
     */
    public void stopSCO(){
        mAudioManager.stopBluetoothSco();
        mAudioManager.setBluetoothScoOn(false);
    }

    /**
     * 停止蓝牙扫描
     * @return
     */
    public boolean stopBluetoothDiscovery(){
        if(mBluetoothAdapter == null){
            return false;
        }

        mBluetoothAdapter.cancelDiscovery();

        return true;
    }

    /**
     * 连接设备
     * @param device
     */
    private void connect(BluetoothDevice device){
        if(mBluetoothA2dp == null){
            return;
        }
        if(mBluetoothA2dp == null){
            return;
        }

        try {
            Method connect = mBluetoothA2dp.getClass().getDeclaredMethod("connect", BluetoothDevice.class);
            connect.setAccessible(true);
            connect.invoke(mBluetoothA2dp,device);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 蓝牙广播
     */
    class HeadSetReceiver extends BroadcastReceiver{
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(Intent.ACTION_HEADSET_PLUG)){
                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {
                        // 拔出有线耳机
                        if(callback != null){
                            callback.onWiredHeadsetDisconnected();
                        }
                    } else if (intent.getIntExtra("state", 0) == 1) {
                        // 插入无线耳机
                        if(callback != null){
                            callback.onWiredHeadsetConnected();
                        }
                    }
                }

                return;
            }

            int state = 0;
            switch (action){
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch(blueState){
                        case BluetoothAdapter.STATE_TURNING_ON:
                            // 蓝牙正在打开
                            break;
                        case BluetoothAdapter.STATE_ON:
                            // 蓝牙已经打开
                            if(callback != null){
                                callback.onBluetoothOpened();
                            }
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            // 蓝牙正在关闭
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            // 蓝牙已经关闭
                            if(callback != null){
                                callback.onBluetoothClosed();
                            }
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    // 蓝牙设备查找广播
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    int deviceClassType = device.getBluetoothClass().getDeviceClass();
                    //找到指定的蓝牙设备
                    if ((deviceClassType == BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET
                            || deviceClassType == BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES)) {
                        if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                            // 已经配对成功了 那么直接连接蓝牙设备
                            stopBluetoothDiscovery();
                            // 连接蓝牙设备
                            connect(device);
                        }else {
                            // start bond，开始配对
                            device.createBond();
                        }
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    // 蓝牙设备配对广播
                    int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,BluetoothDevice.BOND_NONE);
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (bondState){
                        case BluetoothDevice.BOND_BONDED:
                            // 配对成功
                            // 取消搜索
                            stopBluetoothDiscovery();
                            // 连接蓝牙设备
                            connect(device);
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            // 正在配对
                            break;
                        case BluetoothDevice.BOND_NONE:
                            // 不知道是蓝牙耳机的关系还是什么原因，经常配对不成功
                            // 配对不成功的话，重新尝试配对
                            // 继续配对
                            device.createBond();
                            break;
                        default:
                            break;

                    }
                    break;
                case BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED:
                    // 蓝牙设备与手机连接状态
                    state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, 0);

                    switch (state){
                        case 1:
                            // 蓝牙耳机正在连接
                            break;
                        case 2:
                            // 蓝牙耳机已经连接上
                            if(callback != null){
                                callback.onBluetoothHeadsetConnected();
                            }
                            break;
                        case 3:
                            // 蓝牙耳机正在断连
                            break;
                        case 0:
                            // 蓝牙耳机已经断连
                            if(callback != null){
                                callback.onBluetoothHeadsetDisconnected();
                            }
                            break;
                    }

                    break;
                case BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED:
                    // AudioManager与蓝牙设备连接状态
                    state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, 10);
                    switch (state){
                        case BluetoothHeadset.STATE_AUDIO_DISCONNECTED:
                            // SCO 音频信道断连
                            break;
                        case BluetoothHeadset.STATE_AUDIO_CONNECTING:
                            // SCO 音频信道正在连接
                            break;
                        case BluetoothHeadset.STATE_AUDIO_CONNECTED:
                            // SCO 音频信道已经连接上
                            break;
                    }

                    break;
            }

        }
    }

    public enum DeviceType{

        WIRED_HEADSET,
        BLUETOOTH,
        UNKNOWN;

    }

    StateCallback callback;

    /**
     * 状态回调
     * @param callback
     */
    public void setCallback(StateCallback callback){
        this.callback = callback;
    }

    public interface StateCallback{

        void onWiredHeadsetConnected();

        void onWiredHeadsetDisconnected();

        void onBluetoothOpened();

        void onBluetoothClosed();

        void onBluetoothHeadsetConnected();

        void onBluetoothHeadsetDisconnected();

    }

}
