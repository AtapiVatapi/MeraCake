package com.vikpoo.a5cakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerDialog extends DialogFragment {

    interface OnTimeSelectListener{
        void onTimeSelected(String time);
    }

    private OnTimeSelectListener mListener;
    private Button mDone;
    private TimePicker mTimePicker;
    private int hour,minutes;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.time_picker_dialog,null);
        mListener = (OnTimeSelectListener) getActivity();
        mDone = view.findViewById(R.id.done);
        mTimePicker = view.findViewById(R.id.time_picker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = mTimePicker.getHour();
            minutes = mTimePicker.getMinute();
        }
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int h, int m) {
               hour = h; minutes = m;
            }
        });
        mDone.setOnClickListener(view1 -> {
            String d = "am";
            if(hour>12)
            {
                hour %= 12; d = "pm";
            }
            mListener.onTimeSelected(hour+" : "+minutes+" "+d);
            getDialog().dismiss();
        });
        builder.setView(view);
        return builder.create();
    }
}
