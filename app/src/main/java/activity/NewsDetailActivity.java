package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.maxin.beijingnews.R;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Uri url;

    private ImageButton ibContentBack;
    private ImageButton ibTitleBarTextSize;
    private ImageButton ibTitleBarShare;
    private ProgressBar progress;
    private WebView wv_news;

    int timpSize=2;
    int realSize=timpSize;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        url=getIntent().getData();
       // Log.e("OMG",url);
        initView();

    }

    private void initView() {


        ibContentBack = (ImageButton)findViewById( R.id.ib_content_back );
        ibTitleBarTextSize = (ImageButton)findViewById( R.id.ib_title_bar_text_size );
        ibTitleBarShare = (ImageButton)findViewById( R.id.ib_title_bar_share );
        progress = (ProgressBar)findViewById(R.id.progress);
        wv_news = (WebView)findViewById(R.id.wv_news);

        ibContentBack.setVisibility(View.VISIBLE);
        ibTitleBarTextSize.setVisibility(View.VISIBLE);
        ibTitleBarShare.setVisibility(View.VISIBLE);

        ibContentBack.setOnClickListener( this );
        ibTitleBarTextSize.setOnClickListener( this );
        ibTitleBarShare.setOnClickListener( this );

        wv_news.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progress.setVisibility(View.GONE);
            }
        });

        settings = wv_news.getSettings();
        //支持JavaScrip,必须加这个方法
        settings.setJavaScriptEnabled(true);
        //启用页面上放大缩小按钮
        settings.setBuiltInZoomControls(true);
         //启用页面双击缩放功能
        settings.setUseWideViewPort(true);

        wv_news.loadUrl(url.toString());
    }

    public void onClick(View v) {
         if ( v == ibContentBack ) {
            //返回
            finish();;
        } else if ( v == ibTitleBarTextSize ) {
            // 改变字体大小
            showChangeSizeDialog();
        } else if ( v == ibTitleBarShare ) {
            // 分享
        }
    }

    private void showChangeSizeDialog() {
        String[] items={"超大字体","大字体","正常字体","小字体","超小字体"};
        new AlertDialog.Builder(this)
                    .setTitle("设置字体")
                    .setSingleChoiceItems(items,realSize, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            timpSize=i;
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                realSize=timpSize;
                                changeSize(realSize);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
    }

    private void changeSize(int realSize) {
        switch (realSize) {
            case 0 :
                settings.setTextZoom(200);
                break;
            case 1 :
                settings.setTextZoom(150);
                break;
            case 2 :
                settings.setTextZoom(100);
                break;
            case 3 :
                settings.setTextZoom(75);
                break;
            case 4 :
                settings.setTextZoom(50);
                break;
        }
    }

}
