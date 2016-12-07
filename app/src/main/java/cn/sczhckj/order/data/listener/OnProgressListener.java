package cn.sczhckj.order.data.listener;

/**
 * @describe: 进度下载监听
 * @author: Like on 2016/12/7.
 * @Email: 572919350@qq.com
 */

public interface OnProgressListener {
    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
