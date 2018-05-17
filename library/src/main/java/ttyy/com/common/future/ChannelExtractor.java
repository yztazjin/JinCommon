package com.common.future;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Channel For Signature V2 And V1
 */
public class ChannelExtractor {
    static final String TAG = "ChannelExtractor";

    public static String extractChannel(Context context, String defaultValue){
        String zipPath = context.getApplicationContext().getPackageCodePath();
        try {
            RandomAccessFile apk = new RandomAccessFile(zipPath, "r");
            long len = apk.length();

            // find the eocd start
            byte[] buffer4 = new byte[4];
            long eocdStart = -1;
            for(int i=0; i<len-22; i++){
                apk.seek(len-22-i);
                apk.read(buffer4, 0, buffer4.length);
                if(buffer4[0] == 0x50
                        && buffer4[1] == 0x4b
                        && buffer4[2] == 0x05
                        && buffer4[3] == 0x06){
                    eocdStart = len - 22 - i;
                    break;
                }
            }

            if(eocdStart < 0){
                throw new Exception("Didn't find the EoCD Start Offset");
            }

            // get centrol directionary start
            apk.seek(eocdStart+16);
            apk.read(buffer4, 0, buffer4.length);
            long cdStart = convertBytesToInt(buffer4);

            // get centrol directionary size
            apk.seek(eocdStart+12);
            apk.read(buffer4, 0, buffer4.length);
            long cdSize = convertBytesToInt(buffer4);

            if( (cdStart + cdSize) == eocdStart ){
                Log.i(TAG, "Correct Apk Zip File Struct");
            }else {
                Log.w(TAG, "Error Apk Zip File Struct");
                return null;
            }

            byte[] buffer8 = new byte[8];
            apk.seek(cdStart - 24);
            apk.read(buffer8, 0, buffer8.length);
            long sigBlockSize = convertBytesToInt(buffer8);
            long sigBlockStart = cdStart - sigBlockSize - 8;

            long start = sigBlockStart + 8;
            String channelString = defaultValue;
            while(true){
                apk.seek(start);
                apk.read(buffer8, 0, buffer8.length);
                long idValuePairLength = convertBytesToInt(buffer8);

                apk.seek(start + 8);
                apk.read(buffer4, 0, buffer4.length);
                long id = convertBytesToInt(buffer4);
                if(id == 0x7109871a){
                    Log.i(TAG, "find the magic id");
                }else if(id == 0x77777777){
                    // our custom channel id
                    byte[] buffer = new byte[(int) (idValuePairLength - 4)];
                    apk.seek(start + 12);
                    apk.read(buffer, 0, buffer.length);
                    channelString = new String(buffer, "utf-8");
                    break;
                }

                start += idValuePairLength + 8;
                if(start < cdStart - 24){

                }else {
                    break;
                }
            }

            return channelString;
        } catch (FileNotFoundException e) {
            Log.w(TAG, e.getMessage());
        } catch (IOException e) {
            Log.w(TAG, e.getMessage());
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
        return defaultValue;
    }

    static long convertBytesToInt(byte[] buffer){
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        try{
            bb.order(ByteOrder.LITTLE_ENDIAN);
            return bb.getInt() & 0xffffffffL;
        }finally {
            bb.clear();
        }
    }

}
