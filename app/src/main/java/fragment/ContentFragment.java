package fragment;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.maxin.beijingnews.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import activity.MainActivity;
import base.BaseFragment;
import base.BasePager;
import pager.HomePager;
import pager.NewsPager;
import pager.SettingPager;
import view.NoScrollViewPager;

/**
 * Created by shkstart on 2017/6/2.
 */

public class ContentFragment extends BaseFragment {
    private NoScrollViewPager vp;
    private RadioGroup rgMain;
    private RadioButton rbHome;
    private RadioButton rbNews;
    private RadioButton rbSetting;

    private ArrayList<BasePager> pagers;


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_content, null);
        vp = (NoScrollViewPager) view.findViewById(R.id.vp);
        rgMain = (RadioGroup) view.findViewById(R.id.rg_main);
        rbHome = (RadioButton) view.findViewById(R.id.rb_home);
        rbNews = (RadioButton) view.findViewById(R.id.rb_news);
        rbSetting = (RadioButton) view.findViewById(R.id.rb_setting);

        rgMain.check(R.id.rb_home);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        pagers = new ArrayList<>();
        pagers.add(new HomePager(context));
        pagers.add(new NewsPager(context));
        pagers.add(new SettingPager(context));

        setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//设置默认不能侧滑

        vp.setAdapter(new MyPagerAdapter());
        pagers.get(0).initData();
        //  setListener();
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_home:
                        vp.setCurrentItem(0);  //第二个参数加false取消页面变换动画
                        setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                        pagers.get(0).initData();
                        break;
                }
                switch (i) {
                    case R.id.rb_news:
                        vp.setCurrentItem(1);
                        setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        pagers.get(1).initData();
                        break;
                }
                switch (i) {
                    case R.id.rb_setting:
                        vp.setCurrentItem(2);
                        pagers.get(2).initData();
                        setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                        break;
                }

            }
        });
    }

    public NewsPager getNewsPager() {
        return (NewsPager) pagers.get(1);
    }

    /*private void setListener() {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagers.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }*/

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagers.get(position);
            View rootView = basePager.rootView;
            //调用initData方法,不调用没有子类数据
            //basePager.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /*private void isEnableSlidingMenu(boolean f) {
        MainActivity mainActivity = (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if(f) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }*/
    private void setTouchModeAbove(int touchmodeNone) {
        MainActivity main= (MainActivity) context;
        SlidingMenu slidingMenu = main.getSlidingMenu();
        slidingMenu.setTouchModeAbove(touchmodeNone);
    }

}
