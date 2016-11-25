package cn.sczhckg.order.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.sczhckg.order.R;

/**
 * @describe: Glide加载图片
 * @author: Like on 2016/11/17.
 * @Email: 572919350@qq.com
 */

public class GlideLoading {

    /**
     * 加载菜品图片
     *
     * @param mContext  上下文
     * @param url       图片地址
     * @param imageView 控件
     */
    public static void loadingDishes(Context mContext, String url, ImageView imageView) {
        Glide
                .with(mContext.getApplicationContext())
                .load(url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading_faild)
                .into(imageView);
    }

    /**
     * 加载头像
     *
     * @param mContext  上下文
     * @param url       地址
     * @param imageView 控件
     */
    public static void loadingHeader(Context mContext, String url, ImageView imageView) {
        Glide
                .with(mContext.getApplicationContext())
                .load(url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading_faild)
                .into(imageView);
    }


}
