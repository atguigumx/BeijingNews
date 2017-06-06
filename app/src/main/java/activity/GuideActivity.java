package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.beijingnews_library.utils.CacheUtils;
import com.example.beijingnews_library.utils.DensityUtil;
import com.example.maxin.beijingnews.R;

import java.util.ArrayList;

import static com.example.maxin.beijingnews.R.id.iv_red_point;
import static com.example.maxin.beijingnews.R.id.ll_point_group;

public class GuideActivity extends AppCompatActivity {
    private Button button;
    private LinearLayout llPointGroup;
    private ImageView ivRedPoint;
    private ViewPager viewpager;
    public static String STARTMAIN="1993";
    private ArrayList<ImageView> list;
    private int leftMarg;
    private int widthDpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findViews();
        list = new ArrayList();
        widthDpi = DensityUtil.dip2px(this, 10);
        int[] ids = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        for(int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            list.add(imageView);

            ImageView grayPoint = new ImageView(this);
            grayPoint.setBackgroundResource(R.drawable.point_gray);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthDpi, widthDpi);
            if(i!=0) {
                layoutParams.leftMargin=widthDpi;
            }
            grayPoint.setLayoutParams(layoutParams);
            llPointGroup.addView(grayPoint);

        }
        //将图片加入ViewPager
        viewpager.setAdapter(new MyAdapter());
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        button.setOnClickListener(new MyOnClickListener());
    }

    private void findViews() {
        button = (Button) findViewById(R.id.button);
        llPointGroup = (LinearLayout) findViewById(ll_point_group);
        ivRedPoint = (ImageView) findViewById(iv_red_point);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }



    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = list.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }



    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //红点移动的距离 = ViewPager页面的百分比* 间距
            float left=leftMarg*(position+positionOffset);

            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();

            params.leftMargin= (int) left;
            ivRedPoint.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {
            if(position==list.size()-1) {
                button.setVisibility(View.VISIBLE);
            }else {
                button.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //记录进入过引导页面
            CacheUtils.putBoolean(GuideActivity.this,STARTMAIN,true);

            startActivity(new Intent(GuideActivity.this,MainActivity.class));

            finish();
        }
    }

    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            //removeGlobalOnLayoutListener
            ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            //间距 = 第1个点距离左边距离 - 第0个点距离左边距离
            leftMarg=llPointGroup.getChildAt(1).getLeft()-llPointGroup.getChildAt(0).getLeft();
        }
    }
}
