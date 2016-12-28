package cn.sczhckj.order.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.sczhckj.order.R;

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
                .placeholder(R.drawable.order_status_loading)
                .error(R.drawable.order_status_loadingfaild)
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
                .placeholder(R.drawable.both_btn_login_nor)
                .error(R.drawable.both_btn_login_faild)
                .into(imageView);
    }

    /**
     * 加载服务列表
     *
     * @param mContext  上下文
     * @param url       图片地址
     * @param imageView 控件
     */
    public static void loadingService(Context mContext, String url, ImageView imageView) {
        Glide
                .with(mContext.getApplicationContext())
                .load(url)
                .placeholder(R.drawable.service_status_loading)
                .error(R.drawable.service_status_loadingfaild)
                .into(imageView);
    }


}
