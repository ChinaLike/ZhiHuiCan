package cn.sczhckg.order.overwrite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.sczhckg.order.R;

/**
 * @describe: 带斜线的TextView
 * @author: Like on 2016/11/7.
 * @Email: 572919350@qq.com
 */

public class SlantTextView extends TextView{

    public SlantTextView(Context context) {
        super(context);
    }

    public SlantTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlantTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width=getMeasuredWidth();
        int height=getMeasuredHeight();
        Canvas slantCanvas=new Canvas();
        Paint paint=new Paint();
        paint.setColor(getResources().getColor(R.color.text_color_cart_price_s));
        paint.setStrokeWidth(1);
        slantCanvas.drawLine(0,0,width,-height,paint);

    }
}
