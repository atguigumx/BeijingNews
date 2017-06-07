package detailpager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.maxin.beijingnews.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import activity.MainActivity;
import base.MenuDetailBasePager;
import domain.NewsCenterBean;

/**
 * Created by shkstart on 2017/6/3.
 */

public class TopicMenuDetailPager extends MenuDetailBasePager {
    private final List<NewsCenterBean.DataBean.ChildrenBean> childrenBean;
    private ViewPager viewPager;
    private ImageButton ib_next;
    private TabLayout indicator;
    private List<TabDetailPager> tabDetailPagers;
    public TopicMenuDetailPager(Context context, List<NewsCenterBean.DataBean.ChildrenBean> children) {
        super(context);
        this.childrenBean=children;
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.pager_topic_menu_detail,null);
        viewPager= (ViewPager) view.findViewById(R.id.viewpager);
        indicator= (TabLayout) view.findViewById(R.id.indicator);
        ib_next= (ImageButton) view.findViewById(R.id.ib_next);
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    //SlidingMenu可以滑动
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else{
                    //不可以滑动
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
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
        tabDetailPagers=new ArrayList<>();
        for(int i = 0; i < childrenBean.size(); i++) {
            tabDetailPagers.add(new TabDetailPager(context,childrenBean.get(i)));
        }

        viewPager.setAdapter(new MyAdapter());
        indicator.setupWithViewPager(viewPager);
        indicator.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return childrenBean==null?0:childrenBean.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            tabDetailPager.initData();
            return rootView;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return childrenBean.get(position).getTitle();
        }
    }
}
