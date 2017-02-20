package cn.sczhckj.order.overwrite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.sczhckj.order.until.show.L;

/**
 * @describe: RecyclerView自定义虚线分割线
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class DashlineItemDivider extends RecyclerView.ItemDecoration {

    private int color = Color.GRAY;

    private int line = 5;

    private float width = 1;

    private int showNum = 0;

    public DashlineItemDivider(int color, int line, float width) {
        this.color = color;
        this.line = line;
        this.width = width;
    }

    public DashlineItemDivider(int color, int line, float width, int showNum) {
        this.color = color;
        this.line = line;
        this.width = width;
        this.showNum = showNum;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent) {
        try {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            if (showNum != 0) {
                childCount = showNum;
            }
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                // TODO: 2016/12/20 这里报空指针
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                //以下计算主要用来确定绘制的位置
                final int top = child.getBottom() + params.bottomMargin;

                //绘制虚线
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(color);
                Path path = new Path();
                path.moveTo(left, top);
                path.lineTo(right, top);
                PathEffect effects = new DashPathEffect(new float[]{line, line, line, line}, 5);//此处单位是像素不是dp  注意 请自行转化为dp
                paint.setStrokeWidth(width);
                paint.setPathEffect(effects);
                c.drawPath(path, paint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
