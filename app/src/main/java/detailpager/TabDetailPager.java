package detailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.beijingnews_library.utils.CacheUtils;
import com.example.beijingnews_library.utils.ConstantUtils;
import com.example.maxin.beijingnews.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import activity.NewsDetailActivity;
import base.MenuDetailBasePager;
import domain.NewsCenterBean;
import domain.TabDetailPagerBean;
import okhttp3.Call;
import view.HorizontalScrollViewPager;

import static com.example.beijingnews_library.utils.ConstantUtils.BASE_URL;

/**
 * Created by shkstart on 2017/6/5.
 */

public class TabDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    private HorizontalScrollViewPager  viewpager;
    private TextView tvTitle;
    private LinearLayout llPointGroup;
    private ListView lv;
    private String url;
    private MyAdapter adapter;
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    private int prePosition;
    private List<TabDetailPagerBean.DataBean.NewsBean> newsBean;
    private PullToRefreshListView pull_refresh_list;
    private boolean isLoadMore= false;
    private String moreUrl;

      /**
        * 新闻ID数组key
        */
    private String READ_NEWS_ID_ARRAY_KEY ="read_news_id_array_key";


    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean=childrenBean;
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view =View.inflate(context,R.layout.pager_tab_detail,null);

        pull_refresh_list= (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);

        lv=pull_refresh_list.getRefreshableView();

        //添加刷新音效

        /*SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(context);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        pull_refresh_list.setOnPullEventListener(soundListener);*/


        View viewTopNews=View.inflate(context,R.layout.tab_detail_topnews,null);

        viewpager = (HorizontalScrollViewPager )viewTopNews.findViewById( R.id.viewpager );
        tvTitle = (TextView)viewTopNews.findViewById( R.id.tv_title );
        llPointGroup = (LinearLayout)viewTopNews.findViewById( R.id.ll_point_group );

        //lv = (ListView)view.findViewById( R.id.lv);
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
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore=false;
                getDataFromNet(url);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore=true;
                getDataFromNet(moreUrl);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int realPositon=position-2;

                TabDetailPagerBean.DataBean.NewsBean newsItem = newsBean.get(realPositon);

                String readNewsIdArray= CacheUtils.getString(context,READ_NEWS_ID_ARRAY_KEY);

                if(!readNewsIdArray.contains(newsItem.getId()+"")) {

                    readNewsIdArray=readNewsIdArray+newsItem.getId()+",";

                    CacheUtils.putString(context,READ_NEWS_ID_ARRAY_KEY,readNewsIdArray);

                    adapter.notifyDataSetChanged();
                }
                Intent intent=new Intent(context, NewsDetailActivity.class);
                String url=ConstantUtils.BASE_URL+newsItem.getUrl();
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url= BASE_URL + childrenBean.getUrl();
        getDataFromNet(url);
    }

    private void getDataFromNet(String response) {
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
                        pull_refresh_list.onRefreshComplete();//结束下拉刷新
                    }


                });
    }

    private void processData(String response) {

        TabDetailPagerBean bean = new Gson().fromJson(response, TabDetailPagerBean.class);

        String more = bean.getData().getMore(); //更多数据

        if(!TextUtils.isEmpty(more)) {
            moreUrl= BASE_URL+more;
        }
        if(!isLoadMore) {
            //顶部 topnews
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
            //listview 数据
            newsBean = bean.getData().getNews();
            adapter=new MyAdapter();
            lv.setAdapter(adapter);
        }else {
            newsBean.addAll(bean.getData().getNews());
            adapter.notifyDataSetChanged();
            isLoadMore=false;
        }


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

            viewHolder.tvTime.setText(bean.getPubdate());

            String imageUrl = BASE_URL+bean.getListimage();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.pic_item_list_default)
                    .error(R.drawable.pic_item_list_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivIcon);

            String s=CacheUtils.getString(context,READ_NEWS_ID_ARRAY_KEY);
            if(s.contains(bean.getId()+"")){
                viewHolder.tvDesc.setTextColor(Color.GRAY);
            }else {
                viewHolder.tvDesc.setTextColor(Color.BLACK);
            }
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

            String url= BASE_URL+topnews.get(position).getTopimage();
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
