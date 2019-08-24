package com.vikpoo.a5cakes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class VerifyUserActivity extends AppCompatActivity implements FragmentEnterPhone.OnPhoneEnteredListener, FragmentVerifyOtp.OnOtpConfirmedListener {

    private FragmentManager manager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private PhoneAuthProvider mPhoneAuth;
    private FragmentEnterPhone fragmentPhone;
    private FragmentVerifyOtp fragmentVerifyOtp;

    private String mVerifcationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String token, phone, id;
    private RelativeLayout mWelcomeView;

    private TextView mHeyName, mWelcomeMessage;

    private boolean isOTPSent = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        manager = getSupportFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        mPhoneAuth = PhoneAuthProvider.getInstance();
        db = FirebaseFirestore.getInstance();

        Fragment fragment = manager.findFragmentById(R.id.view_holder);
        if (fragment == null) {

            fragmentPhone = new FragmentEnterPhone();

            manager.beginTransaction()
                    .add(R.id.view_holder, fragmentPhone)
                    .commit();
        }

        mWelcomeView = findViewById(R.id.welcome_view);
        mHeyName = findViewById(R.id.name);
        mWelcomeMessage = findViewById(R.id.welcome_msg);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onConfirmClicked(final String phone) {

        this.phone = phone;
        mPhoneAuth.verifyPhoneNumber(
                phone,
                30L,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        fragmentVerifyOtp.setOtpText(phoneAuthCredential.getSmsCode());
                        fragmentVerifyOtp.confirmOtp();
                        //signIn(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            fragmentVerifyOtp.setWrongOtpEnable();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(VerifyUserActivity.this, "Login Server is down. Please try after some time.", Toast.LENGTH_SHORT).show();
                            fragmentPhone.stopProgress();
                        }

                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        isOTPSent = true;
                        mVerifcationId = s;
                        mResendToken = forceResendingToken;
                        fragmentVerifyOtp = new FragmentVerifyOtp();
                        fragmentVerifyOtp.setMobile(phone);
                        manager.beginTransaction()
                                .replace(R.id.view_holder, fragmentVerifyOtp)
                                .addToBackStack("VERIFY")
                                .commit();

                    }
                }
        );

    }

    private void signIn(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //signin is successful i.e. otp confirmed let's check whether user exists or not
                    final FirebaseUser user = mAuth.getCurrentUser();
                    id = user.getUid();
                    db.collection("users")
                            .document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().exists()) {// user found i.e, user exists -> fetch details -> start homeActivity
                                    Toast.makeText(VerifyUserActivity.this, "Signed in as " + task.getResult().getString("name"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(VerifyUserActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                                Toast.makeText(VerifyUserActivity.this, "User not found: Let's create user", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VerifyUserActivity.this, SignUpActivity.class);
                                intent.putExtra("ID", id);
                                intent.putExtra("PHONE", phone);
                                startActivityForResult(intent, 200);
                                return;
                            }
                            Toast.makeText(VerifyUserActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            fragmentVerifyOtp.stopProgress();
                            Log.d("VERIFY_USER","ERROR");

                        }//end of onComplete
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            fragmentVerifyOtp.stopProgress();
                        }
                    });//end of onFailure
                }// end of if task is successful block
                else {
                    Toast.makeText(VerifyUserActivity.this, "Otp entered is wrong.", Toast.LENGTH_SHORT).show();
                    //wrong code entered
                    fragmentVerifyOtp.setWrongOtpEnable();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Read what kind of error is going to come.
                // Toast.makeText(VerifyUserActivity.this, "Poor or no internet connection. Please try again", Toast.LENGTH_SHORT).show();
                fragmentVerifyOtp.stopProgress();
            }
        });
    }



    @Override
    public void onOtpConfirmed(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerifcationId, code);
        signIn(credential);
    }

    @Override
    public void resendOtp() {
        //resend otp
        Toast.makeText(VerifyUserActivity.this, "Resending Otp...", Toast.LENGTH_SHORT).show();
        mPhoneAuth.verifyPhoneNumber(
                phone,
                30L,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        fragmentVerifyOtp.setOtpText(phoneAuthCredential.getSmsCode());
                        fragmentVerifyOtp.confirmOtp();
                        //signIn(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            //wrong code entered
                            fragmentVerifyOtp.setWrongOtpEnable();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(VerifyUserActivity.this, "Login Server is down. Please try after some time.", Toast.LENGTH_SHORT).show();
                            fragmentPhone.stopProgress();
                        }

                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        mVerifcationId = s;
                        mResendToken = forceResendingToken;
                        isOTPSent = true;
                        fragmentVerifyOtp = new FragmentVerifyOtp();
                        fragmentVerifyOtp.setMobile(phone);
                        manager.beginTransaction()
                                .replace(R.id.view_holder, fragmentVerifyOtp)
                                // .addToBackStack("VERIFY")
                                .commit();

                    }
                },
                mResendToken
        );

    }//resend otp end

    @Override
    public void changeMobile() {
        Intent intent = new Intent(VerifyUserActivity.this, VerifyUserActivity.class);
        startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else
            finish();
    }//change mobile

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                //signin user
                db.collection("users").document(id).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult() != null && task.getResult().exists()) {
                                        //new user is here -> Goto AboutUs Activity with TrialPack Status
                                        // if user exists i.e., signup was successful then go to aboutusActivity.
                                        final DocumentSnapshot user = task.getResult();
                                        mWelcomeView.setVisibility(View.VISIBLE);
                                        mHeyName.setText("Hey! " + user.get("name") + "");
                                        mWelcomeMessage.setText("Welcome to ");

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(VerifyUserActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        },3000);


                                    } else {
                                        //Log.d("VERIFY_ONRESULT", "task couldn't be done");
                                        Toast.makeText(VerifyUserActivity.this, "Signup unsuccessful. Please try again after sometime.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(VerifyUserActivity.this, "Something went wrong. Please try again...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("VerifyUSer_ONRESULT", e.getMessage());
                        Toast.makeText(VerifyUserActivity.this, "Check your internet connection...Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // some error screen to be here
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isOTPSent) {
            if (fragmentVerifyOtp.onBackPressed())
                return;
        }
        super.onBackPressed();
    }
}
