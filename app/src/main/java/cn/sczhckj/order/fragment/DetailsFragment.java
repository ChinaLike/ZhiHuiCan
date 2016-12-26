package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.food.ImageBean;
import cn.sczhckj.order.data.event.SwitchViewEvent;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.image.GlideLoading;
import cn.sczhckj.order.mode.FoodMode;
import cn.sczhckj.order.mode.impl.FavorImpl;
import cn.sczhckj.order.mode.impl.FoodControlImpl;
import cn.sczhckj.order.mode.impl.TagCloudImpl;
import cn.sczhckj.order.overwrite.CarouselView;
import cn.sczhckj.order.overwrite.TagFlowLayout;
import cn.sczhckj.order.until.AppSystemUntil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 菜品详情界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class DetailsFragment extends BaseFragment implements Callback<Bean<List<ImageBean>>> {


    @Bind(R.id.details_banner)
    CarouselView detailsBanner;
    @Bind(R.id.dishes_name)
    TextView dishesName;
    @Bind(R.id.details_price)
    TextView detailsPrice;
    @Bind(R.id.details_favorable_recycler)
    TagFlowLayout detailsFavorableRecycler;
    @Bind(R.id.dishes_sales)
    TextView dishesSales;
    @Bind(R.id.details_like)
    ImageView detailsLike;
    @Bind(R.id.details_dishes_minus)
    ImageView dishesMinus;
    @Bind(R.id.details_dishes_number)
    TextView detailsDishesNumber;
    @Bind(R.id.details_dishes_add)
    ImageView dishesAdd;
    @Bind(R.id.details_back)
    ImageView detailsBack;
    @Bind(R.id.dishes_like)
    TextView dishesLike;
    /**
     * 请求数据
     */
    private FoodMode mFoodMode;
    /**
     * 保存临时数据
     */
    private FoodBean mFoodBean = new FoodBean();
    /**
     * 是否加载成功
     */
    private boolean isSuccess = false;
    /**
     * 标签云控件实现
     */
    private TagCloudImpl mTagCloud;

    /**
     * 点赞控件实现
     */
    private FavorImpl mFavorImpl;
    /**
     * 整个分类数据
     */
    private List<FoodBean> beanList = new ArrayList<>();
    /**
     * 菜品控制实现
     */
    private FoodControlImpl mFoodControl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_details, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void setData(Object object) {
        mFoodBean = (FoodBean) object;
        initBanner(mFoodBean.getId(), mFoodBean.getCateId());
        initBase();
    }

    /**
     * 初始化基本数据
     */
    private void initBase() {
        dishesName.setText(mFoodBean.getName());
        detailsPrice.setText(mFoodBean.getOriginPrice() + "");
        dishesSales.setText("月销量 " + mFoodBean.getSales());
        dishesLike.setText(mFoodBean.getFavors() + "");
        if (mFoodBean.isFavor()) {
            detailsLike.setSelected(true);
            dishesLike.setTextColor(ContextCompat.getColor(getContext(), R.color.favor_sel));
        } else {
            detailsLike.setSelected(false);
            dishesLike.setTextColor(ContextCompat.getColor(getContext(), R.color.favor_nor));
        }
        detailsDishesNumber.setText(mFoodBean.getCount() + "");
        initPrices();
    }

    /**
     * 初始化Banner轮播数据
     *
     * @param foodId
     * @param cateId
     */
    private void initBanner(int foodId, int cateId) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setFoodId(foodId);
        bean.setCateId(cateId);
        mFoodMode.images(bean, this);
    }

    /**
     * 初始化价格表
     */
    private void initPrices() {
        mTagCloud = new TagCloudImpl(getContext());
        mTagCloud.setPrice(detailsFavorableRecycler, mFoodBean.getPrices());

    }

    @Override
    public void init() {
        detailsBanner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AppSystemUntil.height(getContext()) * 2 / 3));
        mFoodMode = new FoodMode();
        mFavorImpl = new FavorImpl(getContext());
        mFoodControl = new FoodControlImpl(getContext());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    /**
     * 菜品图片轮播图
     *
     * @param urlList
     */
    private void bannerAdapter(final List<ImageBean> urlList) {
        detailsBanner.setAdapter(new CarouselView.Adapter() {
            @Override
            public boolean isEmpty() {
                return urlList.size() == 0;
            }

            @Override
            public View getView(int position) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_image, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image);
                GlideLoading.loadingDishes(getContext(), urlList.get(position).getImageUrl(), imageView);
                TextView context = (TextView) view.findViewById(R.id.context);
                context.setText(urlList.get(position).getRemark());
                return view;
            }

            @Override
            public int getCount() {
                return urlList.size();
            }
        }, 0);
    }

    @OnClick({R.id.favor_parent, R.id.details_dishes_minus, R.id.details_dishes_add, R.id.details_back, R.id.details_banner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.favor_parent:
                /**点赞*/
                mFavorImpl.favor(detailsLike, dishesLike, mFoodBean);
                break;
            case R.id.details_dishes_minus:
                /**减菜*/
                mFoodControl.minusFood(dishesMinus, detailsDishesNumber, mFoodBean);
                break;
            case R.id.details_dishes_add:
                /**加菜*/
                mFoodControl.addFood(dishesAdd, detailsDishesNumber, mFoodBean, beanList);
                break;
            case R.id.details_back:
                /**返回*/
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.DISHES_DETAILS_OUT));
                break;
            case R.id.details_banner:
                /**请求失败，重新请求*/
                if (!isSuccess) {
                    initBanner(mFoodBean.getId(), mFoodBean.getCateId());
                }
                break;
        }
    }

    @Override
    public void onResponse(Call<Bean<List<ImageBean>>> call, Response<Bean<List<ImageBean>>> response) {
        Bean<List<ImageBean>> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            isSuccess = true;
            bannerAdapter(bean.getResult());
        }
    }

    @Override
    public void onFailure(Call<Bean<List<ImageBean>>> call, Throwable t) {

    }

    /**
     * 设置整个分类的数据
     *
     * @param beanList
     */
    public void setBeanList(List<FoodBean> beanList) {
        this.beanList = beanList;
    }
}
