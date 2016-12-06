package cn.sczhckj.order.overwrite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.sczhckj.order.R;

/**
 * @describe: 带斜线的TextView,
 * FontMetrics介绍具体参考：http://mikewang.blog.51cto.com/3826268/871765/
 * @author: Like on 2016/11/7.
 * @Email: 572919350@qq.com
 */

public class SlantTextView extends TextView {

    private Paint paint;

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
        int width = getMeasuredWidth();

        int height = getMeasuredHeight();
        paint = new Paint();
        paint.setTextSize(getTextSize());
        paint.setColor(getResources().getColor(R.color.text_color_cart_price_s));
        paint.setStrokeWidth(2);
        float textWidth = getCharacterWidth(getText().toString()) * getScaleX();
        float textHeight = getCharacterHeight(getText().toString()) * getScaleY();
        canvas.drawLine((width - textWidth) / 2, (height + textHeight) / 2, (width + textWidth) / 2, (height - textHeight) / 2, paint);
        canvas.save();


    }

    /**
     * 获取每个字符的宽度，此处要总体宽度
     *
     * @param text
     * @return
     */
    private float getCharacterWidth(String text) {
        if (null == text || "".equals(text))
            return 0;
        float width = 0F;

        float text_width = paint.measureText(text);//得到总体长度
        width = text_width / text.length();//每一个字符的长度
        return text_width;
    }

    /**
     * 获取字体高度
     *
     * @param text
     * @return
     */
    private float getCharacterHeight(String text) {
        if (null == text || "".equals(text))
            return 0;
        // FontMetrics对象
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();


        return fontMetrics.leading+fontMetrics.ascent+fontMetrics.descent;
    }

}
