package com.vikpoo.a5cakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class PermissionDialogFragment extends DialogFragment {

    public interface OnPermissionListener{
        void onYesClicked();
    }

    private OnPermissionListener listener;

    private String title, message;

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_permission,null);
        TextView mTitle = view.findViewById(R.id.title);
        TextView mMsg = view.findViewById(R.id.message);
        Button mYes = view.findViewById(R.id.yes);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onYesClicked();
                getDialog().cancel();
            }
        });
        Button mNo = view.findViewById(R.id.no);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        mTitle.setText(title);
        mMsg.setText(message);
        dialog.setView(view);
        return dialog.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnPermissionListener) context;
    }
}