package com.vikpoo.a5cakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.applandeo.materialcalendarview.CalendarView;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatePickerDialog extends DialogFragment {

    private CalendarView mCalendar;
    private OnDayClickListener mListener;
    private String mCurrentDate;

    public void setmCurrentDate(String mCurrentDate) {
        this.mCurrentDate = mCurrentDate;
    }

    interface OnDayClickListener
    {
        void onDayClicked(String date);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.date_picker_dialog,null);
        mCalendar = view.findViewById(R.id.calendar);

        mCalendar.setMinimumDate(Calendar.getInstance());

        mListener = (OnDayClickListener) getActivity();

        SimpleDateFormat parse = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = parse.parse(mCurrentDate);
            Log.d("DATE_SELEC",date.toString());
            mCalendar.setDate(date);
        } catch (Exception e) {
            Log.d("DATE_SEL",e.getMessage());
        }

        mCalendar.setOnDayClickListener(eventDay -> {
            if(eventDay.getCalendar().before(Calendar.getInstance()))
            {
                Toast.makeText(getActivity(), "Please select valid date", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date(eventDay.getCalendar().getTimeInMillis());
            mListener.onDayClicked(format.format(date));
            getDialog().dismiss();
        });
        builder.setView(view);
        return builder.create();
    }

}
