package com.vikpoo.a5cakes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class CakeDetailsActivity extends AppCompatActivity {

    private ViewPager mCakeSlider;
    private TabLayout mCakeSliderTabs;
    private RelativeLayout mPlaceOrderRel;
    private ImageView mQuantityRemove, mQuantityAdd;
    private TextView mCakeName, mQuantity, mCakeFlavour, mCakeType, mCakeShape, mCakePrice;
    private RadioGroup mWeightChoice, mEggChoice;
    private EditText mCakeMessage;
    private Cake mCake;

    String RUPEE = "\u20B9";

    static String WEIGHT[] = {"0.5kg" ,"1kg","2kg"};


    private int mCakeQuantity, mCakeWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_details);
        getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));

        Intent intent = getIntent();
        mCake = intent.getParcelableExtra("CAKE");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(mCake.getName());
        initToolbar();

        mCakeQuantity = 1;
        mCakeWeight = 0;

        initUI();

    }

    private void initUI() {

        mCakeSlider = findViewById(R.id.cake_image_slider);
        mCakeSliderTabs = findViewById(R.id.tabLayout);
        mCakeSliderTabs.setVisibility(View.GONE);

        mCakeSlider.setAdapter(new ImageSliderAdapter(this, new String[]{mCake.image}));
        mCakeSliderTabs.setupWithViewPager(mCakeSlider);

        mPlaceOrderRel = findViewById(R.id.place_order_rel);
        mPlaceOrderRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CakeDetailsActivity.this, OrderDetailsActivity.class);
                intent.putExtra("WEIGHT",WEIGHT[mCakeWeight]);
                intent.putExtra("PRICE",mCake.getPrice().get(mCakeWeight));
                intent.putExtra("QUANTITY",mCakeQuantity);
                intent.putExtra("NAME",mCake.getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mCakeName = findViewById(R.id.cake_name);
        mCakeName.setText(mCake.getName());

        mQuantity = findViewById(R.id.quantity);
        mQuantity.setText(mCakeQuantity + "");

        //Fetch these details of cake from db . Make these details available in the cake's db....
        mCakeFlavour = findViewById(R.id.cake_flavour);
        mCakeType = findViewById(R.id.cake_type);
        mCakeShape = findViewById(R.id.cake_shape);

        mCakePrice = findViewById(R.id.cake_price);
        mCakePrice.setText(RUPEE+" "+Integer.parseInt(mCake.getPrice().get(mCakeWeight)) * mCakeQuantity + "");

        mQuantityRemove = findViewById(R.id.quantity_minus);
        mQuantityAdd = findViewById(R.id.quantity_plus);

        mQuantityRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCakeQuantity > 1) {
                    mCakeQuantity -= 1;
                    mQuantity.setText(mCakeQuantity + "");
                    mCakePrice.setText(RUPEE+" "+Integer.parseInt(mCake.getPrice().get(mCakeWeight)) * mCakeQuantity + "");
                }
                else
                    Toast.makeText(CakeDetailsActivity.this, "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
            }
        });

        mQuantityAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCakeQuantity < 4) {
                    mCakeQuantity += 1;
                    mQuantity.setText(mCakeQuantity + "");
                    mCakePrice.setText(RUPEE+" "+Integer.parseInt(mCake.getPrice().get(mCakeWeight)) * mCakeQuantity + "");
                }
                else
                    Toast.makeText(CakeDetailsActivity.this, "Maximum quantity is 4", Toast.LENGTH_SHORT).show();
            }
        });

        mWeightChoice = findViewById(R.id.weight_choice);
        mWeightChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Toast.makeText(CakeDetailsActivity.this, i + "", Toast.LENGTH_SHORT).show();
                switch (i)
                {
                    case R.id.w1:
                        mCakeWeight = 0;
                        break;
                    case R.id.w2:
                        mCakeWeight = 1;
                        break;
                    case R.id.w3:
                        mCakeWeight = 2;
                }
                mCakePrice.setText(RUPEE+" "+Integer.parseInt(mCake.getPrice().get(mCakeWeight)) * mCakeQuantity + "");
            }
        });
        mEggChoice = findViewById(R.id.egg_choice);
        mEggChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Toast.makeText(CakeDetailsActivity.this, i + "", Toast.LENGTH_SHORT).show();
            }
        });

        mCakeMessage = findViewById(R.id.cake_message);
    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.home_back_icon);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onBackPressed();
        return true;
    }

}
