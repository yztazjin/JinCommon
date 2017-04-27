package ttyy.com.common;

/**
 * Author: hjq
 * Date  : 2017/04/27 20:04
 * Name  : UnitTransfer
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class UnitTransfer {

    static final int KB = 1024;
    static final int MB = 1024 * 1024;
    static final int GB = 1024 * 1024 * 1024;

    public static float ByteToKB(float value){
        float rst = value / KB;
        return rst;
    }

    public static float ByteToMB(float value){
        float rst = value / MB;
        return rst;
    }

    public static float ByteToGB(float value){
        float rst = value / GB;
        return rst;
    }

    public static float KBToByte(float value){
        return value * KB;
    }

    public static float MBToByte(float value){
        return value * MB;
    }

    public static float GBToByte(float value){
        return value * GB;
    }

}
