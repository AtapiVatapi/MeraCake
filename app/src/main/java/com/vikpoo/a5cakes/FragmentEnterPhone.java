package com.vikpoo.a5cakes;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FragmentEnterPhone extends Fragment {

    interface OnPhoneEnteredListener{
        public void onConfirmClicked(String phone);
    }

    private OnPhoneEnteredListener listener;

    private Button mSendOtp;
    private EditText mPhone;
    private RelativeLayout mProgressLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnPhoneEnteredListener) context;
        if(listener == null)
        {
            Log.d("FRAG_ENTER_PHONE","OnPhoneEnteredListener methods not implemented in parent activity");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enter_phone_no,container,false);

        mPhone = view.findViewById(R.id.phone_no);
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String phone = charSequence.toString().trim();
                    if(phone.length()>10 && phone.charAt(0) != '+')
                    {
                        Toast.makeText(getContext(),"Plz enter valid 10 digit no.", Toast.LENGTH_SHORT).show();
                    }
                    if(phone.length()==13)
                    {
                        if(phone.charAt(0) == '+')
                        {
                            //Toast.makeText(getContext(), phone.substring(3), Toast.LENGTH_SHORT).show();
                            mPhone.setText(phone.substring(3));
                        }
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSendOtp = view.findViewById(R.id.send_otp_button);
        mSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp();
            }
        });
        mProgressLayout = view.findViewById(R.id.rel);
        return view;
    }

    private void sendOtp()
    {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(mPhone.getWindowToken(),0);
        String phone = mPhone.getText().toString().trim();
        if(phone.length() == 10)
        {
            mProgressLayout.setVisibility(View.VISIBLE);
            phone = "+91"+phone;
            listener.onConfirmClicked(phone);

        }
        else
        {
            Toast.makeText(getContext(), "Please enter valid mobile no. to proceed", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopProgress()
    {
        mProgressLayout.setVisibility(View.GONE);
    }

}
