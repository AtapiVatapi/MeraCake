package com.vikpoo.a5cakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutPermissionDialogFragment extends DialogFragment {

    private Button mYes, mNo;
    private TextView mTitle, mMessage;
    private String title, message;

    public interface OnDialogActionListener
    {
        void onYesClicked();
    }

    private OnDialogActionListener listener;

    public void setListener(OnDialogActionListener listener)
    {
        this.listener = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_logout_permisiion,null);
        mYes = view.findViewById(R.id.yes);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //Log me out
                FirebaseAuth.getInstance().signOut();
                listener.onYesClicked();
            }
        });
        mNo = view.findViewById(R.id.no);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });
        mTitle = view.findViewById(R.id.title);
        mMessage = view.findViewById(R.id.message);
        builder.setView(view);
        return builder.create();
    }

}
