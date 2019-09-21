package com.vikpoo.a5cakes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;


public class OrderDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDayClickListener, TimePickerDialog.OnTimeSelectListener {

    private Button mDatePicker, mTimePicker;
    private CardView mDeliveryCard;
    private ImageView mQuantityRemove, mQuantityAdd;
    private TextView mQuantity, mCakeName, mCakeDetails, mCakeQuantity, mCakeAmount, mDeliveryCharge, mTotalAmount, mDelAddress, mDelToName, mDelToPhone;

    private int quantity, price, cake_amount, total_amount, delivery_charge;
    private String weight, name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getWindow().setStatusBarColor(getResources().getColor(R.color.appThemeColor));
        initToolbar();

        Intent intent = getIntent();
        quantity = intent.getIntExtra("QUANTITY", 1);
        price = Integer.parseInt(intent.getStringExtra("PRICE"));
        weight = intent.getStringExtra("WEIGHT");
        name = intent.getStringExtra("NAME");

        cake_amount = price * quantity;
        delivery_charge = 0;
        total_amount = cake_amount + delivery_charge;

        initUI();
    }

    private void initUI() {
        mDatePicker = findViewById(R.id.date_picker);
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog();
                dialog.setmCurrentDate(mDatePicker.getText().toString());
                dialog.show(getSupportFragmentManager(), null);
            }
        });

        mTimePicker = findViewById(R.id.time_picker);
        mTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog();
                dialog.show(getSupportFragmentManager(), null);
            }
        });

        mDeliveryCard = findViewById(R.id.delivery_card);
        mDeliveryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailsActivity.this, DeliveryDetailsActivity.class);
                startActivityForResult(intent, 301);
            }
        });


        mQuantityRemove = findViewById(R.id.quantity_minus);
        mQuantity = findViewById(R.id.quantity);
        mQuantityAdd = findViewById(R.id.quantity_add);

        mCakeQuantity = findViewById(R.id.cake_quantity);
        mCakeQuantity.setText("x" + quantity);

        mCakeName = findViewById(R.id.cake_name);
        mCakeName.setText(name);

        mCakeAmount = findViewById(R.id.cake_amount);
        mCakeAmount.setText(cake_amount + "");

        mCakeDetails = findViewById(R.id.cake_details);
        mCakeDetails.setText(weight);

        mTotalAmount = findViewById(R.id.total_amount);
        mTotalAmount.setText(total_amount + "");

        mDeliveryCharge = findViewById(R.id.delivery_charge);

        mQuantity.setText(quantity + "");
        mQuantityRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 1) {
                    quantity--;
                    cake_amount = price * quantity;
                    total_amount = cake_amount + delivery_charge;
                    mQuantity.setText(quantity + "");
                    mCakeQuantity.setText("x" + quantity);
                    mCakeAmount.setText(cake_amount + "");
                    mTotalAmount.setText(total_amount + "");
                    //price set

                } else {
                    Toast.makeText(OrderDetailsActivity.this, "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mQuantityAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quantity < 4) {
                    quantity++;
                    cake_amount = price * quantity;
                    total_amount = cake_amount + delivery_charge;
                    mQuantity.setText(quantity + "");
                    mCakeQuantity.setText("x" + quantity);
                    mCakeAmount.setText(cake_amount + "");
                    mTotalAmount.setText(total_amount + "");
                    //price set

                } else {
                    Toast.makeText(OrderDetailsActivity.this, "Maximum quantity is 4", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDelAddress = findViewById(R.id.delivery_address);
        mDelToName = findViewById(R.id.deliver_to_name);
        mDelToPhone = findViewById(R.id.deliver_to_phone);
    }

    @Override
    public void onDayClicked(String date) {
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        mDatePicker.setText(date);
        //get date variable set here
    }

    @Override
    public void onTimeSelected(String time) {
        mTimePicker.setText(time);
        //get time variable set here
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 301 && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra("DEL_DATA");
            if (bundle != null) {
//                Toast.makeText(this, ""+bundle.getString("REC_NAME"), Toast.LENGTH_SHORT).show();
                boolean sendToElse = bundle.getBoolean("SEND_TO_ELSE");
                String senderName, senderPhone;
                if (sendToElse) {
                    senderName = bundle.getString("SENDER_NAAME");
                    senderPhone = bundle.getString("SENDER_PHONE");
                }

                String recName, recPhone, address, landmark, city, area;
                recName = bundle.getString("REC_NAME");
                mDelToName.setText(recName);
                recPhone = bundle.getString("REC_PHONE");
                mDelToPhone.setText(recPhone);
                address = bundle.getString("ADDRESS");
                mDelAddress.setText(address);
                landmark = bundle.getString("LANDMARK");
                city = bundle.getString("CITY");
                area = bundle.getString("AREA");
                //send this data to orders api
            }
        }
    }
}
