package base;

import android.content.Context;
import android.view.View;

/**
 * Created by shkstart on 2017/6/3.
 */

public abstract class MenuDetailBasePager {
    public final Context context;
    public View rootView;
    public MenuDetailBasePager(Context context){
        this.context=context;
        rootView=initView();
    }
    //该方法抽象,由子类实现
    public abstract View initView();
    public void initData(){

    }
}
