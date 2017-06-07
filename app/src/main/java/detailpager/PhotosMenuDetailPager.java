package detailpager;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.beijingnews_library.utils.ConstantUtils;
import com.example.maxin.beijingnews.R;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import adapter.PhotosMenuDetailPagerAdapater;
import base.MenuDetailBasePager;
import domain.NewsCenterBean;
import domain.PhotosMenuDetailPagerBean;
import okhttp3.Call;

/**
 * Created by shkstart on 2017/6/3.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean dataBean;

    private RecyclerView recyclerview;
    private ProgressBar progressbar;
    private String url;
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news;
    private PhotosMenuDetailPagerAdapater adapter;
    private SwipeRefreshLayout swiperefresh;

    public PhotosMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);  //使用父类的context,不要再自己定义一个Context context,报错一下午
        this.dataBean=dataBean;
    }

    @Override
    public View initView() {

        View view=View.inflate(context, R.layout.pager_photos_menu_detail,null);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swiperefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(url);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url= ConstantUtils.BASE_URL+dataBean.getUrl();
        getDataFromNet(url);
    }

    private void getDataFromNet(String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "图组请求失败==" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "图组请求成功==");
                        processData(response);

                    }


                });
    }

    private void processData(String response) {
        PhotosMenuDetailPagerBean bean = new Gson().fromJson(response, PhotosMenuDetailPagerBean.class);
        news = bean.getData().getNews();
        if(news!=null&&news.size()>0) {
            progressbar.setVisibility(View.GONE);
            adapter=new PhotosMenuDetailPagerAdapater(context,news,recyclerview);
            recyclerview.setAdapter(adapter);
            recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL,false));
        }
        //刷新完成
        swiperefresh.setRefreshing(false);
    }
    private boolean isList=true;
    public void changeListAndGride(ImageButton ib_switch_list_glide) {
        if(isList){
            recyclerview.setLayoutManager(new GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false));
            //显示GrideView
            ib_switch_list_glide.setBackgroundResource(R.drawable.icon_pic_list_type);
            isList=false;
        }else {
            //显示ListView
            recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
            ib_switch_list_glide.setBackgroundResource(R.drawable.icon_pic_grid_type);
            isList=true;
        }
    }
}
