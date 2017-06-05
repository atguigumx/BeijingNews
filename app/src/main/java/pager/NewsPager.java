package pager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import activity.MainActivity;
import base.BasePager;
import base.MenuDetailBasePager;
import detailpager.InteractMenuDetailPager;
import detailpager.NewsMenuDetailPager;
import detailpager.PhotosMenuDetailPager;
import detailpager.TopicMenuDetailPager;
import detailpager.VoteMenuDetailPager;
import domain.NewsCenterBean;
import fragment.LeftMenuFragment;
import okhttp3.Call;
import utils.CacheUtils;
import utils.ConstantUtils;

/**
 * Created by shkstart on 2017/6/2.
 */

public class NewsPager extends BasePager {
    private List<NewsCenterBean.DataBean> datas;
    private ArrayList<MenuDetailBasePager> pagers;
    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        System.out.println("新闻加载了...");
        //设置标题
        tv_title.setText("新闻");

        //创建子类的视图
        /*TextView textView = new TextView(context);
        textView.setText("新闻页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);*/

        ib_menu.setVisibility(View.VISIBLE);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity= (MainActivity) context;
                SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
                slidingMenu.toggle();
            }
        });

        //添加到布局上
        //fl_content.addView(textView);
        String json=CacheUtils.getString(context,ConstantUtils.NEWSCENTER_PAGER_URL);
        if(!TextUtils.isEmpty(json)) {
                processData(json);
        }
        getDateFromNet();
    }

    private void getDateFromNet() {
        //新闻中心的网络路径
        String url = ConstantUtils.NEWSCENTER_PAGER_URL;
        OkHttpUtils
                .get()
                .url(url)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG","请求失败=="+e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG","请求成功=="+response);
                        CacheUtils.putString(context,ConstantUtils.NEWSCENTER_PAGER_URL,response);
                        processData(response);
                    }


                });
    }

    private void processData(String response) {

        NewsCenterBean newsCenterBean =new Gson().fromJson(response,NewsCenterBean.class);

        Log.e("TAG",newsCenterBean.getData().get(0).getChildren().get(0).getTitle());
        datas=newsCenterBean.getData();

        MainActivity mainActivity= (MainActivity) context;

        pagers=new ArrayList<>();
        pagers.add(new NewsMenuDetailPager(context,datas.get(0).getChildren()));
        pagers.add(new TopicMenuDetailPager(context));
        pagers.add(new PhotosMenuDetailPager(context));
        pagers.add(new InteractMenuDetailPager(context));
        pagers.add(new VoteMenuDetailPager(context));

        LeftMenuFragment leftMenuFragment = mainActivity.getleftMenuFragment();
        leftMenuFragment.setData(datas);

        swichPager(0);
    }

   /* private NewsCenterBean parseJson(String response) {
        NewsCenterBean newsCenterBean = new NewsCenterBean();
        try {

            JSONObject jsonObject = new JSONObject(response);
            //解析retcode
            int retcode = jsonObject.optInt("retcode");
            //设置数据
            newsCenterBean.setRetcode(retcode);

            JSONArray data = jsonObject.optJSONArray("data");
            if(data!=null&data.length()>0) {
                List<NewsCenterBean.DataBean>dataBean=new ArrayList();
                newsCenterBean.setData(dataBean);
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonObjct1= (JSONObject) data.get(i);
                    if(jsonObjct1!=null) {
                        NewsCenterBean.DataBean dataBean1 = new NewsCenterBean.DataBean();
                        int id=jsonObjct1.optInt("id");
                        dataBean1.setId(id);
                        String title = jsonObjct1.optString("title");
                        dataBean1.setTitle(title);
                        int type = jsonObjct1.optInt("type");
                        dataBean1.setType(type);
                        String url = jsonObjct1.optString("url");
                        dataBean1.setUrl(url);

                        JSONArray childrenData = jsonObjct1.optJSONArray("children");
                        if(childrenData!=null&&childrenData.length()>0) {
                            List<NewsCenterBean.DataBean.ChildrenBean> children = new ArrayList<>();
                            dataBean1.setChildren(children);
                            for(int i1 = 0; i1 < childrenData.length(); i1++) {
                                JSONObject childrenJson = (JSONObject) childrenData.get(i1);
                                if(childrenJson!=null) {
                                    NewsCenterBean.DataBean.ChildrenBean childrenBean = new NewsCenterBean.DataBean.ChildrenBean();
                                    childrenBean.setId(childrenJson.optInt("id"));
                                    childrenBean.setTitle(childrenJson.optString("title"));
                                    childrenBean.setType(childrenJson.optInt("type"));
                                    childrenBean.setUrl(childrenJson.optString("url"));
                                    children.add(childrenBean);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsCenterBean;
    }*/

    public void swichPager(int position) {
        MenuDetailBasePager basePager = pagers.get(position);
        View rootView=basePager.rootView;
        fl_content.removeAllViews();
        fl_content.addView(rootView);
        basePager.initData();
    }
}
