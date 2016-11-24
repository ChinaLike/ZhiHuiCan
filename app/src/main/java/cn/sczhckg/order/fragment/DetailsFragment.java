package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.Config;
import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.DetailsAdapter;
import cn.sczhckg.order.data.bean.Constant;
import cn.sczhckg.order.data.bean.DetailsBean;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.bean.PriceTypeBean;
import cn.sczhckg.order.data.event.BottomChooseEvent;
import cn.sczhckg.order.data.event.CartNumberEvent;
import cn.sczhckg.order.data.event.RefreshCartEvent;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.image.GlideLoading;
import cn.sczhckg.order.overwrite.CarouselView;
import cn.sczhckg.order.until.AppSystemUntil;
import cn.sczhckg.order.until.ConvertUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 详情界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class DetailsFragment extends BaseFragment implements Callback<DetailsBean> {

    @Bind(R.id.details_add)
    Button detailsAdd;
    @Bind(R.id.dishes_name)
    TextView dishesName;
    @Bind(R.id.details_price)
    TextView detailsPrice;
    @Bind(R.id.details_favorable_recycler)
    RecyclerView detailsFavorableRecycler;
    @Bind(R.id.dishes_sales)
    TextView dishesSales;
    @Bind(R.id.dishes_like)
    TextView dishesLike;
    @Bind(R.id.details_dishes_minus)
    ImageView detailsDishesMinus;
    @Bind(R.id.details_dishes_number)
    TextView detailsDishesNumber;
    @Bind(R.id.details_dishes_add)
    ImageView detailsDishesAdd;
    @Bind(R.id.details_precent)
    TextView detailsPrecent;
    @Bind(R.id.details_progress)
    ProgressBar detailsProgress;
    @Bind(R.id.like_number)
    TextView likeNumber;
    @Bind(R.id.details_back)
    ImageView detailsBack;
    @Bind(R.id.details_banner)
    CarouselView detailsBanner;

    private DetailsAdapter adapter;
    /**
     * 菜品详情
     */
    private DishesBean dishesBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        DishesBean bean = (DishesBean) object;
        dishesBean = bean;
        dishesName.setText(bean.getName());
        detailsPrice.setText("¥  " + bean.getPrice());
        dishesSales.setText("月销量  " + bean.getSales());
        dishesLike.setText("  " + bean.getCollect());
        detailsDishesNumber.setText(bean.getNumber() + "");
        /**判断权限是否可以点餐*/
        if (bean.getPermiss() == Constant.PREMISS_AGREE) {
            detailsAdd.setClickable(true);
            detailsDishesMinus.setClickable(true);
            detailsDishesAdd.setClickable(true);
        } else {
            detailsAdd.setClickable(false);
            detailsDishesMinus.setClickable(false);
            detailsDishesAdd.setClickable(false);
        }
        Call<DetailsBean> detailsBeanCall = RetrofitRequest.service().dishesDeatails(bean.getId(), bean.getName());
        detailsBeanCall.enqueue(this);
    }

    @Override
    public void init() {
        detailsBanner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AppSystemUntil.height(getContext())*2/3));
    }

    @Override
    public void onResponse(Call<DetailsBean> call, Response<DetailsBean> response) {
        DetailsBean bean = response.body();
        if (bean != null) {
            bannerAdapter(bean.getUrls());
            favorableAdapter(bean.getPriceType());
            initProgress(bean.getGoodEvaluate(), bean.getTotalEvaluate());
        }
    }

    @Override
    public void onFailure(Call<DetailsBean> call, Throwable t) {

    }

    private void initProgress(int curr, int total) {
        likeNumber.setText(total + "");
        float progress = (float) (curr * 1.0 / total);
        detailsPrecent.setText((int) (progress * 100) + "%");
        detailsProgress.setProgress((int) (progress * 100));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.details_add, R.id.details_dishes_minus, R.id.details_dishes_add, R.id.details_back})
    public void onClick(View view) {
        int number = dishesBean.getNumber();
        switch (view.getId()) {
            case R.id.details_add:
                /**判断数量是否为0且本桌可以点菜*/
                if (!detailsDishesNumber.getText().toString().trim().equals("0") && dishesBean.getPermiss() == Constant.PREMISS_AGREE) {
                    EventBus.getDefault().post(new RefreshCartEvent(dishesBean));
                }
                break;
            case R.id.details_dishes_minus:
                if (number > 0) {
                    number--;
                    detailsDishesNumber.setText(number + "");
                    dishesBean.setNumber(number);
                }
                break;
            case R.id.details_dishes_add:
                number++;
                detailsDishesNumber.setText(number + "");
                dishesBean.setNumber(number);
                break;
            case R.id.details_back:
                EventBus.getDefault().post(new BottomChooseEvent(Constant.DISHES_DETAILS_OUT));
                break;
        }
    }

    /**
     * 菜品图片轮播图
     *
     * @param urlList
     */
    private void bannerAdapter(final List<String> urlList) {
        detailsBanner.setAdapter(new CarouselView.Adapter() {
            @Override
            public boolean isEmpty() {
                return urlList.size() == 0;
            }

            @Override
            public View getView(int position) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_image, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image);
                GlideLoading.loadingDishes(getContext(), urlList.get(position), imageView);
                return view;
            }

            @Override
            public int getCount() {
                return urlList.size();
            }
        }, 0);
    }

    /**
     * 优惠价适配
     *
     * @param mList
     */
    private void favorableAdapter(List<PriceTypeBean> mList) {
        adapter = new DetailsAdapter(getContext(), mList);
        detailsFavorableRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        detailsFavorableRecycler.setAdapter(adapter);
    }

    /**
     * 购物车添加数据变动
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cartEventBus(CartNumberEvent event) {
        if (event.getBean().getId().equals(dishesBean.getId())) {
            detailsDishesNumber.setText(event.getBean().getNumber() + "");
        }
    }

}
