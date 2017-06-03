package activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.maxin.beijingnews.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import fragment.ContentFragment;
import fragment.LeftMenuFragment;
import utils.DensityUtil;

public class MainActivity extends SlidingFragmentActivity {
    public static final String LEFT_TAG = "left_tag";
    public static final String MAIN_TAG = "main_tag";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.设置主页面
        setContentView(R.layout.activity_main);
        //2.设置左侧菜单
        setBehindContentView(R.layout.leftmenu);
        //3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        //4.设置支持滑动的模式：全屏滑动，边缘滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //5.设置页面模式：左侧菜单+主页面；左侧菜单+主页面+右侧菜单； 主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);
        //6.设置主页面占的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));

        initFragment();
    }

    private void initFragment() {
        //1.得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //3.替换两个Fragment
        ft.replace(R.id.fl_leftmenu,new LeftMenuFragment(), LEFT_TAG);
        ft.replace(R.id.fl_main_content,new ContentFragment(), MAIN_TAG);
        //4.提交事务
        ft.commit();
    }

    public LeftMenuFragment getleftMenuFragment() {
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFT_TAG);
    }
}
