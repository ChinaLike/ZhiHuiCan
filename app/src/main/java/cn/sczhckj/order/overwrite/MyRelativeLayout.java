package cn.sczhckj.order.overwrite;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @ describe: 记住悬浮按钮的位置，所有控件中需要添加可拖动悬浮按钮的都需要实现onLayout方法
 * @ author: Like on 2017-02-27.
 * @ email: 572919350@qq.com
 */

public class MyRelativeLayout extends RelativeLayout {

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof DraggableFloatingButton) {
                // 为了防止浮动按钮恢复原位，布局子控件位置时使用上次记录的位置
                DraggableFloatingButton child = (DraggableFloatingButton) getChildAt(i);
                if (child.getLastLeft() != -1) {
                    child.layout(child.getLastLeft(), child.getLastTop(), child.getLastRight(), child.getLastBottom());
                }
                break;
            }
        }
    }
}
