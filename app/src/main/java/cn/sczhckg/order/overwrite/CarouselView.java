package cn.sczhckg.order.overwrite;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.sczhckg.order.R;
import cn.sczhckg.order.until.ConvertUtils;

public class CarouselView extends FrameLayout implements
		OnPageChangeListener {

	private Context context;
	private int totalCount = 100;
	private int showCount;
	private int currentPosition = 0;
	private InsideViewPager viewPager;// 解决Scrollview、ViewPager冲突
	private LinearLayout carouselLayout;
	private Adapter adapter;
	private int pageItemWidth;
	private final int START_FLIPPING = 0;
	private final int STOP_FLIPPING = 1;
	private boolean showPoint = true;
	private boolean autoRoll = true;
	private int rollTime = 3000;
	private OnPageChangeListener onPageChangeListener;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			currentPosition = (currentPosition + 1) % totalCount;
			switch (msg.what) {
			case START_FLIPPING:
				if (currentPosition == totalCount - 1) {
					viewPager.setCurrentItem(showCount - 1, false);
				} else {
					viewPager.setCurrentItem(currentPosition);
				}

				handler.sendEmptyMessageDelayed(START_FLIPPING, rollTime); // 延时滚动

				break;
			case STOP_FLIPPING:
				handler.removeMessages(START_FLIPPING);

				break;
			}
		}
	};

	public CarouselView(Context context) {
		super(context);
		this.context = context;
	}

	public CarouselView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public CarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	private void init(int currentItem) {
		currentPosition = 0;
		viewPager.setAdapter(null);
		carouselLayout.removeAllViews();
		if (adapter.isEmpty()) {
			return;
		}
		int count = adapter.getCount();
		showCount = adapter.getCount();
		for (int i = 0; i < count; i++) {
			View view = new View(context);
			// if (currentPosition==i){
			// view.setPressed(true);
			// LinearLayout.LayoutParams params = new
			// LinearLayout.LayoutParams(pageItemWidth +
			// ConvertUtils.dip2px(context,3),pageItemWidth +
			// ConvertUtils.dip2px(context,3));
			// params.setMargins(pageItemWidth, 0, 0, 0);
			// view.setLayoutParams(params);
			// }else {
			// LinearLayout.LayoutParams params = new
			// LinearLayout.LayoutParams(pageItemWidth,pageItemWidth);
			// params.setMargins(pageItemWidth,0,0,0);
			// view.setLayoutParams(params);
			// }

			if (currentPosition == i) {
				view.setPressed(true);
			} else {
				view.setPressed(false);
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					pageItemWidth, pageItemWidth);
			params.setMargins(pageItemWidth, 0, 0, 0);
			view.setLayoutParams(params);
			if (showPoint && count > 1) {
				view.setBackgroundResource(R.drawable.carousel_layout_page);
			}

			carouselLayout.addView(view);
		}

		viewPager.setAdapter(new ViewPagerAdapter());
		viewPager.setCurrentItem(currentItem);

		handler.removeMessages(START_FLIPPING);
		if (count > 1 && autoRoll) {
			//轮播图数量大于1 才发送轮播消息
			handler.sendEmptyMessageDelayed(START_FLIPPING, rollTime); // 延时滚动
			viewPager.setCanScroll(true);
			
		}else{
			//轮播图数量等于1
			viewPager.setCanScroll(false);
		}

	}
	
	
	

	public LinearLayout getCarouselLayout() {
		return carouselLayout;
	}

	public void setCarouselLayout(LinearLayout carouselLayout) {
		this.carouselLayout = carouselLayout;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public int getShowCount() {
		return showCount;
	}
	
	
	
	
	public int getPageItemWidth() {
		return pageItemWidth;
	}

	public void setPageItemWidth(int pageItemWidth) {
		this.pageItemWidth = pageItemWidth;
	}

	public int getRollTime() {
		return rollTime;
	}

	public void setRollTime(int rollTime) {
		this.rollTime = rollTime;
	}

	public boolean isAutoRoll() {
		return autoRoll;
	}

	public void setAutoRoll(boolean autoRoll) {
		this.autoRoll = autoRoll;
	}

	public boolean isShowPoint() {
		return showPoint;
	}

	public void setShowPoint(boolean showPoint) {
		this.showPoint = showPoint;
	}

	public InsideViewPager getViewPager() {
		return viewPager;
	}

	public void setAdapter(Adapter adapter,int currentItem) {
		this.adapter = adapter;
		if (adapter != null) {
			if (onPageChangeListener != null) {
				this.viewPager.setOnPageChangeListener(onPageChangeListener);
			}else{
				this.viewPager.setOnPageChangeListener(this);
			}
			init(currentItem);
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		View view = LayoutInflater.from(context).inflate(
				R.layout.carousel_layout, null);
		this.viewPager = (InsideViewPager) view.findViewById(R.id.gallery);
		this.carouselLayout = (LinearLayout) view
				.findViewById(R.id.CarouselLayoutPage);
		pageItemWidth = ConvertUtils.dip2px(context, 5);
		
//		this.viewPager.setOnPageChangeListener(this);

		
		addView(view);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		currentPosition = position;
		int count = carouselLayout.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = carouselLayout.getChildAt(i);
//			if (position % showCount == i) {
//				view.setSelected(true);
//				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//						pageItemWidth + ConvertUtils.dip2px(context, 3),
//						pageItemWidth + ConvertUtils.dip2px(context, 3));
//				params.setMargins(pageItemWidth, 0, 0, 0);
//				view.setLayoutParams(params);
//			} else {
//				view.setSelected(false);
//				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//						pageItemWidth, pageItemWidth);
//				params.setMargins(pageItemWidth, 0, 0, 0);
//				view.setLayoutParams(params);
//			}
			
			if (position % showCount == i) {
				view.setSelected(true);

			} else {
				view.setSelected(false);

			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					pageItemWidth, pageItemWidth);
			params.setMargins(pageItemWidth, 0, 0, 0);
			view.setLayoutParams(params);

		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		switch (state) {
		case ViewPager.SCROLL_STATE_IDLE: // 闲置

			if (!handler.hasMessages(START_FLIPPING))
				handler.sendEmptyMessageDelayed(START_FLIPPING, rollTime); // 延时滚动

			break;
		case ViewPager.SCROLL_STATE_DRAGGING: // 拖动中

			handler.sendEmptyMessage(STOP_FLIPPING); // 取消滚动

			break;
		case ViewPager.SCROLL_STATE_SETTLING: // 拖动结束
			break;
		}
	}

	class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return totalCount;
		}

		@Override	
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			position %= showCount;
			View view = adapter.getView(position);
			container.addView(view);
			return view;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			super.finishUpdate(container);
			int position = viewPager.getCurrentItem();
			if (position == 0) {
				position = showCount;
				viewPager.setCurrentItem(position, false);
			} else if (position == totalCount - 1) {
				position = showCount - 1;
				viewPager.setCurrentItem(position, false);
			}
		}
	}

	public interface Adapter {
		boolean isEmpty();

		View getView(int position);

		int getCount();
	}

	public OnPageChangeListener getOnPageChangeListener() {
		return onPageChangeListener;
	}

	public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
		this.onPageChangeListener = onPageChangeListener;
	}
	
//	private OnPageChangeListener setCustomOnPageChangeListener(OnPageChangeListener mOnPageChangeListener){
//		onPageChangeListener = mOnPageChangeListener;
//		return onPageChangeListener;
//	}
	
}
