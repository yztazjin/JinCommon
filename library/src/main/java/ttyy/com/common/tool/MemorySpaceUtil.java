package ttyy.com.common.tool;

import android.os.Environment;
import android.os.StatFs;

/**
 * Author: hjq
 * Date  : 2017/04/27 19:45
 * Name  : MemorySpaceUtil
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class MemorySpaceUtil {

    protected MemorySpaceUtil(){

    }

    public int getTotalSpaceBytes(String path){

        StatFs stat = new StatFs(path);
        int blockSize = stat.getBlockSize();
        int blockCount = stat.getBlockCount();

        return blockSize * blockCount;
    }

    public int getAvailableSpaceBytes(String path){

        StatFs stat = new StatFs(path);
        int blockSize = stat.getBlockSize();
        int availableCount = stat.getAvailableBlocks();

        return blockSize * availableCount;
    }

    public int getSDCardTotalBytes(){

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && Environment.getExternalStorageDirectory() != null){

            return getTotalSpaceBytes(Environment.getExternalStorageDirectory().getAbsolutePath());
        }

        return -1;
    }

    public int getSDCardAvailableBytes(){

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && Environment.getExternalStorageDirectory() != null){

            return getAvailableSpaceBytes(Environment.getExternalStorageDirectory().getAbsolutePath());
        }

        return -1;
    }

    public int getRomTotalBytes(){

        if(Environment.getDataDirectory() != null){
            return getTotalSpaceBytes(Environment.getDataDirectory().getAbsolutePath());
        }

        return -1;
    }

    public int getRomAvailableBytes(){
        if(Environment.getDataDirectory() != null){
            return getAvailableSpaceBytes(Environment.getDataDirectory().getAbsolutePath());
        }

        return -1;
    }
}
