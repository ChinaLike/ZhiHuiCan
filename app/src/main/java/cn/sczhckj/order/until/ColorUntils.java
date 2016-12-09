package cn.sczhckj.order.until;

import android.graphics.Color;

/**
 * @describe: 颜色转换器
 * @author: Like on 2016/12/9.
 * @Email: 572919350@qq.com
 */

public class ColorUntils {


    /**
     * #FFFFFF转化成long型
     * @param color
     * @return
     */
    public static int stringToHex(String color) throws NumberFormatException{
        if (color!=null&&!"".equals(color)){
            return Color.parseColor(color);
        }
        return 0;
    }

}
