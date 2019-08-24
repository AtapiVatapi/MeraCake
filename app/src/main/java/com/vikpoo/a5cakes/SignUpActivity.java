package com.vikpoo.a5cakes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements PermissionDialogFragment.OnPermissionListener{

    private String id, phone;

    private Button mConfirm;
    private EditText mName, mEmail;

    private RelativeLayout mProgressView;

    public boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);

        id = getIntent().getStringExtra("ID");
        phone = getIntent().getStringExtra("PHONE");

        mConfirm = findViewById(R.id.confirm_signup);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        mName = findViewById(R.id.user_name);
        mEmail = findViewById(R.id.user_email);

        mProgressView = findViewById(R.id.progress);
    }

    private void signup()
    {
        View view = SignUpActivity.this.getCurrentFocus();
        if(view!= null)
        {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
        String name = mName.getText().toString();
        if(name.length() <2)
        {
            Toast.makeText(SignUpActivity.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            mName.setText("");
            mName.setHintTextColor(Color.RED);
            mName.setHint("Please enter valid name");
            return;
        }

        String email = mEmail.getText().toString();
        if(email.length()>0)
        {
            if(!isValid(email))
            {
                Toast.makeText(SignUpActivity.this, "Please enter valid email.", Toast.LENGTH_SHORT).show();
                mEmail.setText("");
                return;
            }
        }

        mProgressView.setVisibility(View.VISIBLE);

        HashMap<String, Object> user = new HashMap<String, Object>();
        user.put("name", name);
        user.put("phone",phone);
        user.put("email", email);

        FirebaseFirestore.getInstance().collection("users").document(id).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this, "user created", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                            return;
                        }
                        Toast.makeText(SignUpActivity.this, "Some error occured.Plz try again", Toast.LENGTH_SHORT).show();
                        Log.d("SIGNUP_FAIL","ERROR");
                        mProgressView.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error occured : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("SIGNUP_USER",e.getMessage());
                        mProgressView.setVisibility(View.GONE);
                    }
                });


    }

    @Override
    public void onBackPressed() {
        PermissionDialogFragment dialogFragment = new PermissionDialogFragment();
        dialogFragment.setMessage("If you quit now, then you will have to re verify your mobile no. Are you sure you want to quit?");
        dialogFragment.setTitle("Quit");
        dialogFragment.show(getSupportFragmentManager(),"ONBACK_SIGNUP");
    }

    @Override
    public void onYesClicked() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SignUpActivity.this, VerifyUserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
