package com.vikpoo.a5cakes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class CakeDetailsActivity extends AppCompatActivity {

    ViewPager mCakeSlider;
    TabLayout mCakeSliderTabs;
    RelativeLayout mPlaceOrderRel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));

        mCakeSlider = findViewById(R.id.cake_image_slider);
        mCakeSliderTabs = findViewById(R.id.tabLayout);

        mCakeSlider.setAdapter(new ImageSliderAdapter(this));
        mCakeSliderTabs.setupWithViewPager(mCakeSlider);

        mPlaceOrderRel = findViewById(R.id.place_order_rel);
        mPlaceOrderRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CakeDetailsActivity.this, DeliveryAddressActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }
}
