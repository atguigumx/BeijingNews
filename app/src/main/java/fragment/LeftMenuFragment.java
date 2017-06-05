package fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maxin.beijingnews.R;

import java.util.List;

import activity.MainActivity;
import base.BaseFragment;
import domain.NewsCenterBean;
import pager.NewsPager;

/**
 * Created by shkstart on 2017/6/2.
 */

public class LeftMenuFragment extends BaseFragment {
    private List<NewsCenterBean.DataBean> datas;
    private ListView listView;
    private MyAdapter adapter;
    private int prePosition;
    @Override
    public View initView() {
        listView=new ListView(context);
        listView.setPadding(0,70,0,0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                prePosition=  position;
                adapter.notifyDataSetChanged();

                MainActivity main= (MainActivity) context;
                main.getSlidingMenu().toggle(); //点击后自动滑回去

                //根据位置切换到对应的详情页面
                switchPager(prePosition);
            }
        });
        return listView;
    }

    public void setData(List<NewsCenterBean.DataBean> datas) {
        this.datas=datas;
        adapter=new MyAdapter();
        listView.setAdapter(adapter);
        switchPager(prePosition);
    }

    private void switchPager(int postion) {
        MainActivity mainActivity = (MainActivity) context;
        //2.得到ContentFragment
        ContentFragment contentFragment = mainActivity.getContentFragment();
        //3.得到NewsPager
        NewsPager newsPager = contentFragment.getNewsPager();
        //4.调用切换方法
        newsPager.swichPager(postion);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas==null?0:datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView text = (TextView) View.inflate(context, R.layout.item_leftmenu, null);

            if(prePosition==i) {
                text.setEnabled(true); //红色
            }else {
                text.setEnabled(false);//白色
            }

            //根据位置得到数据
            NewsCenterBean.DataBean dataBean = datas.get(i);
            //Log.e("TAG",dataBean.getTitle());
            text.setText(dataBean.getTitle());
            return text;
        }
    }
}
