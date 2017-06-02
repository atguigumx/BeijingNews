package base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.maxin.beijingnews.R;

/**
 * Created by shkstart on 2017/6/2.
 */

public class BasePager {
    public Context context;
    public View rootView;
    public TextView tv_title;
    public ImageButton ib_menu;
    //子类的视图添加到该布局上
    public FrameLayout fl_content;
    public BasePager(Context context){
        this.context=context;
        rootView=View.inflate(context, R.layout.base_pager,null);
        tv_title = (TextView) rootView.findViewById(R.id.tv_titile);
        fl_content = (FrameLayout) rootView.findViewById(R.id.fl_content);
        ib_menu = (ImageButton) rootView.findViewById(R.id.ib_menu);
    }

    //子类要绑定数据的时候重写该方法

    public void initData(){

    }
}
