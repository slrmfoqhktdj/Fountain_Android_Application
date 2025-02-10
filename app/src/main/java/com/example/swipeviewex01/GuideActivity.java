package com.example.swipeviewex01;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;


public class GuideActivity extends AppCompatActivity {


    private ViewPager2 vpPager;
    private LinearLayout indicator;
    protected int[] images = new int[]{
          R.drawable.whitetheme,R.drawable.whitetheme,R.drawable.whitetheme,R.drawable.whitetheme,R.drawable.whitetheme
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        vpPager = findViewById(R.id.guide_vpPager);
        indicator = findViewById(R.id.layoutIndicators);
        TextView skip = findViewById(R.id.guide_skip_bt);
        TextView next = findViewById(R.id.guide_next_bt);

        vpPager.setOffscreenPageLimit(2);
        vpPager.setAdapter(new GuideAdapter(this,images));

        vpPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    vpPager.setCurrentItem(position);
                }
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        setupIndicators(images.length);



        skip.setOnClickListener(v -> finish());

        next.setOnClickListener(v -> {
            int current = getItem()+1;
            if(current<images.length){
                vpPager.setCurrentItem(current);
            }else{
                finish();
            }
        });


    }

    private int getItem(){
        return vpPager.getCurrentItem();
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            indicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = indicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) indicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }
}
