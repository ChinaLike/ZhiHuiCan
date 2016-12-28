package cn.sczhckj.order.until;

/**
 * @ describe: 文本工具类
 * @ author: Like on 2016/12/28.
 * @ email: 572919350@qq.com
 */

public class TextUntils {

    /**
     * 判断整形对象是否为空
     *
     * @param obj 整形对象
     * @return
     */
    public static Integer empty(Integer obj) {
        if (obj == null) {
            return 0;
        } else {
            return obj;
        }
    }

    /**
     * 判断整形对象是否为空
     *
     * @param obj         整形对象
     * @param defaultCode 默认值
     * @return
     */
    public static Integer empty(Integer obj, int defaultCode) {
        if (obj == null) {
            return defaultCode;
        } else {
            return obj;
        }
    }

    /**
     * 判断长整形对象是否为空
     *
     * @param obj 长整形对象
     * @return
     */
    public static Long empty(Long obj) {
        if (obj == null) {
            return 0L;
        } else {
            return obj;
        }
    }

    /**
     * 判断长整形对象是否为空
     *
     * @param obj         长整形对象
     * @param defaultCode 默认
     * @return
     */
    public static Long empty(Long obj, Long defaultCode) {
        if (obj == null) {
            return defaultCode;
        } else {
            return obj;
        }
    }

    /**
     * 判断双精度对象是否为空
     *
     * @param obj 双精度对象
     * @return
     */
    public static Double empty(Double obj) {
        if (obj == null) {
            return 0D;
        } else {
            return obj;
        }
    }

    /**
     * 判断双精度对象是否为空
     *
     * @param obj         双精度对象
     * @param defaultCode 默认
     * @return
     */
    public static Double empty(Double obj, Double defaultCode) {
        if (obj == null) {
            return defaultCode;
        } else {
            return obj;
        }
    }


}
