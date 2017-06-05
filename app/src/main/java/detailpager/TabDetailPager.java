package detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.maxin.beijingnews.R;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import base.MenuDetailBasePager;
import domain.NewsCenterBean;
import domain.TabDetailPagerBean;
import okhttp3.Call;
import utils.ConstantUtils;

/**
 * Created by shkstart on 2017/6/5.
 */

public class TabDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    private ViewPager viewpager;
    private TextView tvTitle;
    private LinearLayout llPointGroup;
    private ListView lv;
    private String url;
    private MyAdapter adapter;
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    private int prePosition;
    private List<TabDetailPagerBean.DataBean.NewsBean> newsBean;


    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean=childrenBean;
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view =View.inflate(context,R.layout.pager_tab_detail,null);
        View viewTopNews=View.inflate(context,R.layout.tab_detail_topnews,null);

        viewpager = (ViewPager)viewTopNews.findViewById( R.id.viewpager );
        tvTitle = (TextView)viewTopNews.findViewById( R.id.tv_title );
        llPointGroup = (LinearLayout)viewTopNews.findViewById( R.id.ll_point_group );

        lv = (ListView)view.findViewById( R.id.lv );
        lv.addHeaderView(viewTopNews);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                llPointGroup.getChildAt(prePosition).setEnabled(false);
                llPointGroup.getChildAt(position).setEnabled(true);
                prePosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url= ConstantUtils.BASE_URL + childrenBean.getUrl();
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils
                .get()
                .url(url)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "请求失败==" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                       // Log.e("TAG", "请求成功==" + response);
                        //缓存数据
                        processData(response);
                    }


                });
    }

    private void processData(String response) {
        //顶部
        TabDetailPagerBean bean = new Gson().fromJson(response, TabDetailPagerBean.class);
        topnews = bean.getData().getTopnews();
        viewpager.setAdapter(new MyPagerAdapter());

        tvTitle.setText(topnews.get(prePosition).getTitle());

        llPointGroup.removeAllViews(); //把之前的点移除

        for(int i = 0; i < topnews.size(); i++) {
            ImageView point = new ImageView(context);
            point.setBackgroundResource(R.drawable.point_selector);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(8,8);
            point.setLayoutParams(params);

            if(i==0) {
                point.setEnabled(true);
            }else {
                point.setEnabled(false);
                params.leftMargin=8;
            }
            llPointGroup.addView(point);
        }
        //listview
        newsBean = bean.getData().getNews();
        adapter=new MyAdapter();
        lv.setAdapter(adapter);

    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return newsBean.size();
        }

        @Override
        public Object getItem(int i) {
            return newsBean.get(i);
        }

        @Override
        public long getItemId(int i) {
            return newsBean.get(i).getId();
        }

        @Override
        public View getView(int positon, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(convertView==null) {
                convertView=View.inflate(context,R.layout.item_tab_detail,null);
                viewHolder=new ViewHolder();
                viewHolder.tvDesc= (TextView) convertView.findViewById(R.id.tv_desc);
                viewHolder.tvTime= (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.ivIcon= (ImageView) convertView.findViewById(R.id.iv_icon);

                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            //根据位置得到对应的数据
            TabDetailPagerBean.DataBean.NewsBean bean =newsBean.get(positon);

            viewHolder.tvDesc.setText(bean.getTitle());
         //   Log.e("TAG",bean.getTitle()+"hahaha");
            viewHolder.tvTime.setText(bean.getPubdate());

            String imageUrl = ConstantUtils.BASE_URL+bean.getListimage();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.pic_item_list_default)
                    .error(R.drawable.pic_item_list_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivIcon);
            return convertView;
        }
    }
     static class ViewHolder{
           ImageView ivIcon;
           TextView tvDesc;
           TextView tvTime;

    }

    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.pic_item_list_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            String url=ConstantUtils.BASE_URL+topnews.get(position).getTopimage();
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.pic_item_list_default)
                    .error(R.drawable.pic_item_list_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
