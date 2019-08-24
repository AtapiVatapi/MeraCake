package com.vikpoo.a5cakes;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FragmentVerifyOtp extends Fragment {

    interface OnOtpConfirmedListener {
        public void onOtpConfirmed(String code);

        public void resendOtp();

        public void changeMobile();
    }

    private OnOtpConfirmedListener listener;

    private TextView mIncorrectOtp, mCounter, mPhoneText;
    private Button mConfirmOtp, mResendOtp, mChangeMobile;
    private EditText mOtpInput;
    private LinearLayout mTimerView;
    private CountDownTimer mTimer;
    private int time;
    private RelativeLayout mProgressView;
    private String phone;

    private boolean isProgressRunning = false;
    private boolean isSixDigitOtp = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp_verification_activity, container, false);
        mConfirmOtp = view.findViewById(R.id.confirm_otp_button);
        mConfirmOtp.setClickable(false);
        mConfirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSixDigitOtp)
                    confirmOtp();
            }
        });
        time = 30;
        mOtpInput = view.findViewById(R.id.otp_input);
        mOtpInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String otp = charSequence.toString().trim();
                if (otp.length() < 6) {
                    isSixDigitOtp = false;
                    mConfirmOtp.setBackgroundColor(getResources().getColor(R.color.appThemeColor));
                } else {
                    isSixDigitOtp = true;
                    mConfirmOtp.setBackgroundColor(getResources().getColor(R.color.DE_ORANGE));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mIncorrectOtp = view.findViewById(R.id.incorrect_otp);
        mResendOtp = view.findViewById(R.id.resend_otp);
        mResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOtp();
            }
        });

        mPhoneText = view.findViewById(R.id.phone_no);
        mPhoneText.setText("Enter thr 6-digit code recieved on " + phone);

        mCounter = view.findViewById(R.id.counter);
        mTimerView = view.findViewById(R.id.timer_view);
        mTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                time--;
                mCounter.setText("00:" + time);
            }

            @Override
            public void onFinish() {
                mResendOtp.setVisibility(View.VISIBLE);
                mTimerView.setVisibility(View.INVISIBLE);
            }
        };
        mTimer.start();

        mProgressView = view.findViewById(R.id.rel);
        mChangeMobile = view.findViewById(R.id.chnage_mobile_no);
        mChangeMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMobile();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnOtpConfirmedListener) context;
        if (listener == null) {
            Log.d("VERIFY_USER_OTP", "OnOtpConfirmedListener methods not implemented in parent ativity");
        }
    }

    void confirmOtp() {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(mOtpInput.getWindowToken(), 0);
        String code = mOtpInput.getText().toString();
        if (code.length() != 6) {
            Toast.makeText(getContext(), "Please enter a valid 6-digit Otp.", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressView.setVisibility(View.VISIBLE);
        isProgressRunning = true;
        listener.onOtpConfirmed(code);
    }

    public void setOtpText(String code) {
        mOtpInput.setText(code);
    }

    public void setWrongOtpEnable() {
        mTimerView.setVisibility(View.INVISIBLE);
        mProgressView.setVisibility(View.INVISIBLE);
        mIncorrectOtp.setVisibility(View.VISIBLE);
        mResendOtp.setVisibility(View.VISIBLE);
    }

    private void resendOtp() {
        listener.resendOtp();
        mProgressView.setVisibility(View.VISIBLE);
    }

    public void setMobile(String phone) {
        this.phone = phone;
    }

    private void changeMobile() {
        listener.changeMobile();
    }

    public void stopProgress() {
        mProgressView.setVisibility(View.GONE);
        isProgressRunning = false;
    }

    public boolean onBackPressed() {
        if (isProgressRunning) {
            Toast.makeText(getContext(), "Please wait for otp verification.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
