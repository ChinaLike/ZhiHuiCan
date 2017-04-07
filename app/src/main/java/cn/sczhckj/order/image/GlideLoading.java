package cn.sczhckj.order.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.OutputStream;

import cn.sczhckj.order.R;
import cn.sczhckj.order.until.show.L;

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
    public static void loadingDishes(Context mContext, String url, final ImageView imageView) {
        if (url == null || "".equals(url)) {
            Glide
                    .with(mContext.getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.order_status_picloading)
                    .error(R.drawable.order_status_picloadfaild)
                    .into(imageView);
        } else {
            Glide
                    .with(mContext.getApplicationContext())
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.order_status_picloading)
                    .error(R.drawable.order_status_picloadfaild)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
        }
    }

    /**
     * 加载菜品详情图片
     *
     * @param mContext  上下文
     * @param url       图片地址
     * @param imageView 控件
     */
    public static void loadingDetails(Context mContext, String url, final ImageView imageView) {
        if (url == null || "".equals(url)) {
            Glide
                    .with(mContext.getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.details_status_picloading)
                    .error(R.drawable.details_status_picloadfaild)
                    .into(imageView);
        } else {
            Glide
                    .with(mContext.getApplicationContext())
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.details_status_picloading)
                    .error(R.drawable.details_status_picloadfaild)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
        }
    }



    /**
     * 加载头像
     *
     * @param mContext  上下文
     * @param url       地址
     * @param imageView 控件
     */
    public static void loadingHeader(Context mContext, String url, final ImageView imageView) {
        if (url == null || "".equals(url)) {
            Glide
                    .with(mContext.getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.both_btn_login_nor)
                    .error(R.drawable.both_btn_login_faild)
                    .into(imageView);
        } else {
            Glide
                    .with(mContext.getApplicationContext())
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.both_btn_login_nor)
                    .error(R.drawable.both_btn_login_faild)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
        }
    }

    /**
     * 加载服务列表
     *
     * @param mContext  上下文
     * @param url       图片地址
     * @param imageView 控件
     */
    public static void loadingService(Context mContext, String url, final ImageView imageView) {
        if (url == null || "".equals(url)) {
            Glide
                    .with(mContext.getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.service_status_loading)
                    .error(R.drawable.service_status_loadingfaild)
                    .into(imageView);
        } else {
            Glide
                    .with(mContext.getApplicationContext())
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.service_status_loading)
                    .error(R.drawable.service_status_loadingfaild)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
        }
    }


}
