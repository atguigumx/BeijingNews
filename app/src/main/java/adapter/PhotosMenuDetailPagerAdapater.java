package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.beijingnews_library.utils.BitmapCacheUtils;
import com.example.beijingnews_library.utils.ConstantUtils;
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


    private final RecyclerView recyclerView;
    private BitmapCacheUtils bitmapCacheUtils;
    private final Context context;
    private final List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1 :
                    Bitmap bitmap = (Bitmap) msg.obj;
                    int position = msg.arg1;
                    Log.e("TAG","请求图片成功=="+position);
                    ImageView imageview = (ImageView) recyclerView.findViewWithTag(position);
                    if(imageview != null && bitmap != null){
                        imageview.setImageBitmap(bitmap);
                    }
                    break;
                case 2 :
                    position = msg.arg1;
                    Log.e("TAG","请求图片失败=="+position);
                    break;
            }
        }
    };
    public PhotosMenuDetailPagerAdapater(Context context, List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news,RecyclerView recyclerView) {
        this.context=context;
        this.datas=news;
        //把Hanlder传入构造方法
        bitmapCacheUtils=new BitmapCacheUtils(handler);
        this.recyclerView=recyclerView;
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
        String imageUrl = BASE_URL+newsBean.getListimage();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.pic_item_list_default)
                .error(R.drawable.pic_item_list_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_icon);
       /*Bitmap bitmap=bitmapCacheUtils.getBitmap(imageUrl,position);
        holder.iv_icon.setTag(position);
        if(bitmap!=null) {
            holder.iv_icon.setImageBitmap(bitmap);
        }*/
    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_icon;
        TextView tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String imageUrl= ConstantUtils.BASE_URL+datas.get(getLayoutPosition()).getListimage();
                    Intent intent = new Intent(context, PicassoSampleActivity.class);
                    intent.setData(Uri.parse(imageUrl));
                    context.startActivity(intent);
                }
            });
        }

    }
}
