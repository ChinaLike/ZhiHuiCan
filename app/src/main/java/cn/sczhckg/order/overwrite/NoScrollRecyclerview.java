package cn.sczhckg.order.overwrite;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @describe: 自定义不能滚动的Recyclerview
 * @author: Like on 2016/11/14.
 * @Email: 572919350@qq.com
 */

public class NoScrollRecyclerview extends RecyclerView {

    public NoScrollRecyclerview(Context context) {
        super(context);
    }

    public NoScrollRecyclerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollRecyclerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
