package pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import base.BasePager;

/**
 * Created by shkstart on 2017/6/2.
 */

public class NewsPager extends BasePager {
    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //设置标题
        tv_title.setText("新闻");

        //创建子类的视图
        TextView textView = new TextView(context);
        textView.setText("新闻页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        //添加到布局上
        fl_content.addView(textView);
    }
}
