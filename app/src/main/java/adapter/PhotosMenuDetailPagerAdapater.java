package adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.maxin.beijingnews.R;

import java.util.List;

import activity.PicassoSampleActivity;
import domain.PhotosMenuDetailPagerBean;

import static com.example.beijingnews_library.utils.ConstantUtils.BASE_URL;
import static com.example.maxin.beijingnews.R.id.iv_icon;

/**
 * Created by shkstart on 2017/6/6.
 */

public class PhotosMenuDetailPagerAdapater extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapater.MyViewHolder> {


    private final Context context;
    private final List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas;
    private String imageUrl;


    public PhotosMenuDetailPagerAdapater(Context context, List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news) {
        this.context=context;
        this.datas=news;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.item_photos, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.iv_icon= (ImageView) view.findViewById(iv_icon);
        myViewHolder.tv_title= (TextView) view.findViewById(R.id.tv_title);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean = datas.get(position);
        holder.tv_title.setText(newsBean.getTitle());
        imageUrl = BASE_URL+newsBean.getListimage();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.pic_item_list_default)
                .error(R.drawable.pic_item_list_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_icon);
    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
        //String imageUrl = ConstantUtils.BASE_URL + datas.get(getLayoutPosition()).getListimage();
        //String imageUrl=ConstantUtils.BASE_URL+datas.get(getLayoutPosition()).getListimage();

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PicassoSampleActivity.class);
                    intent.setData(Uri.parse(imageUrl));
                    context.startActivity(intent);
                }
            });
        }

    }
}
