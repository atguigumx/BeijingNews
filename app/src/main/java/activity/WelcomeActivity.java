package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

import com.example.maxin.beijingnews.R;

public class WelcomeActivity extends AppCompatActivity {
    /*private RelativeLayout rl_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        rl_welcome = (RelativeLayout)findViewById(R.id.rl_welcome);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(1500);
        aa.setFillAfter(true);

        AnimationSet as = new AnimationSet(false);
        as.addAnimation(aa);

        rl_welcome.startAnimation(aa);

        as.setAnimationListener(new MyAnimationListener());
    }
    class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            //动画开始时的回调
            Log.e("TAG","Start");
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.e("TAG","onAnimationEnd");
            //动画结束时的回调
            startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }*/
    private RelativeLayout rl_welcome;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        rl_welcome = (RelativeLayout) findViewById(R.id.rl_welcome);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(1500);
        aa.setFillAfter(true);//渐变

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(aa);

        rl_welcome.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e("TAG","onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("TAG","onAnimationEnd");
                startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
