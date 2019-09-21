package com.vikpoo.a5cakes;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.applandeo.materialcalendarview.CalendarView;

public class DeliveryDetailsActivity extends AppCompatActivity  {

    private CheckBox mSendToSomeone;
    private  RelativeLayout mSenderInfoRel;

    private EditText mSenderName, mSenderMobile, mRecName, mRecMobile, mAddress, mLandmark;
    private Button mSave;
    private TextView mCity, mArea;

    private int city,area;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        initToolbar();
        city = area = -1;
        getWindow().setStatusBarColor(Color.BLACK);
        initUI();
    }

    private void initUI()
    {
        mSenderInfoRel = findViewById(R.id.sender_info_rel);

        mSendToSomeone = findViewById(R.id.send_to_else);
        mSendToSomeone.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked)
            {
                mSenderInfoRel.setVisibility(View.VISIBLE);
                mSenderInfoRel.setAlpha(0f);
                mSenderInfoRel.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .setListener(null);
            }
            else
            {
                mSenderInfoRel.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                mSenderInfoRel.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
            }

        });
        mSenderName = findViewById(R.id.sender_name);
        mSenderMobile = findViewById(R.id.sender_mobile);
        mRecName = findViewById(R.id.reciever_name);
        mRecMobile = findViewById(R.id.reciever_mobile);
        mAddress = findViewById(R.id.address);
        mLandmark = findViewById(R.id.landmark);

        mCity = findViewById(R.id.city);
        mCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListSelectorDialog selector = new ListSelectorDialog();
                selector.setListener(new ListAdapter.OnListItemClickListener() {
                    @Override
                    public void onListItemClickListener(int pos) {
                        city = pos;
//                        Toast.makeText(DeliveryDetailsActivity.this, "city"+pos, Toast.LENGTH_SHORT).show();
                    }
                });
                selector.show(getSupportFragmentManager(),null);
            }
        });

        mArea = findViewById(R.id.area);
        mArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListSelectorDialog selector = new ListSelectorDialog();
                selector.setListener(new ListAdapter.OnListItemClickListener() {
                    @Override
                    public void onListItemClickListener(int pos) {
                        area =  pos;
//                        Toast.makeText(DeliveryDetailsActivity.this, "area"+pos, Toast.LENGTH_SHORT).show();
                    }
                });
                selector.show(getSupportFragmentManager(),null);
            }
        });

        mSave = findViewById(R.id.save_details);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
            }
        });
    }

    private void saveDetails()
    {
        Bundle bundle = new Bundle();

        String senderName, senderPhone;

        if(mSendToSomeone.isChecked())
        {
            //Check sender's name & mobile info
            senderName  = mSenderName.getText().toString();
            if(senderName.length()==0)
            {
                mSenderName.setHint("Please enter valid name");
                mSenderName.setHintTextColor(Color.RED);
                return;
            }
            senderPhone = mSenderMobile.getText().toString().trim();
            if(senderPhone.length() != 10)
            {
                mSenderMobile.setText("");
                mSenderMobile.setHintTextColor(Color.RED);
                mSenderMobile.setHint("Please enter valid mobile");
                return;
            }
            bundle.putBoolean("SEND_TO_ELSE",true);
            bundle.putString("SENDER_NAME",senderName);
            bundle.putString("SENDER_PHONE",senderPhone);
        }



        String recName = mRecName.getText().toString();
        if(recName.length() == 0)
        {
            mRecName.setHint("Please enter valid name");
            mRecName.setHintTextColor(Color.RED);
            return;
        }
        bundle.putString("REC_NAME",recName);

        String recMobile = mRecMobile.getText().toString().trim();
        if(recMobile.length() !=10)
        {
            mRecMobile.setText("");
            mRecMobile.setHintTextColor(Color.RED);
            mRecMobile.setHint("Please enter valid mobile");
            return;
        }
        bundle.putString("REC_PHONE",recMobile);

        String address = mAddress.getText().toString();
        if(address.length() == 0)
        {
            mAddress.setHintTextColor(Color.RED);
            mAddress.setHint("Please enter valid address");
            return;
        }
        bundle.putString("ADDRESS",address);

        String landmark = mLandmark.getText().toString();
        if(landmark.length() == 0)
        {
            mLandmark.setHintTextColor(Color.RED);
            mLandmark.setHint("Please enter valid landmark");
            return;
        }
        bundle.putString("LANDMARK",landmark);

        if(city == -1)
        {
            Toast.makeText(this, "Please select your city", Toast.LENGTH_SHORT).show();
            return;
        }
        bundle.putString("CITY",""+city);

        if(area == -1)
        {
            Toast.makeText(this, "Please select your area", Toast.LENGTH_SHORT).show();
            return;
        }
        bundle.putString("AREA",""+area);

        Toast.makeText(this, "finishing", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, new Intent().putExtra("DEL_DATA",bundle));
        finish();
        //everything is validated so let's save the result => set Result to intent
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
