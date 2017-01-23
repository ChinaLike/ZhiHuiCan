package cn.sczhckj.order.until;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * @ describe:  文件操作
 * @ author: Like on 2017-01-21.
 * @ email: 572919350@qq.com
 */

public class FileUntils {

    /**
     * 判断SD卡是否存在
     *
     * @return
     */
    public static boolean existSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断SD卡剩余空间
     *
     * @return
     */
    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    /**
     * SD卡总容量
     *
     * @return
     */
    public static long getSDAllSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    /**
     * 获取sd卡的文件路径
     *
     * @return
     */
    public static String getSdPath() {

        return Environment.getExternalStorageDirectory() + "/";

    }

    /**
     * 在SD卡创建文件夹
     *
     * @param fileDir
     */
    public static void createFileDir(String fileDir) {
        String path = getSdPath() + fileDir;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
