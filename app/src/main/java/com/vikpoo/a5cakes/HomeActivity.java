package com.vikpoo.a5cakes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RelativeLayout mOrdersRel, mOffersRel,mHelpSupportRel , mTCRel, mAboutUsRel, mLogoutRel;

    private DashboardFragment fragment;

    private FragmentManager manager;
    private DrawerLayout mDrawer;
    private String userID;

    private boolean isDrawerOpen  = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }
        userID = getIntent().getStringExtra("USER_ID");
        manager = getSupportFragmentManager();
        fragment = (DashboardFragment) manager.findFragmentById(R.id.dashboard);
        if(fragment == null)
        {
            fragment = new DashboardFragment();
            manager.beginTransaction()
                    .add(R.id.dashboard,fragment,"DASH_FRAG")
                    .commit();
        }
        initUI();
        fetchDetails();
    }

    private void initUI()
    {
        mDrawer = findViewById(R.id.drawer_layout);
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if(isDrawerOpen)
                {
                    mDrawer.closeDrawer(GravityCompat.START,true);
                    isDrawerOpen = false;
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                isDrawerOpen = true;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                isDrawerOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.home_icon);

        mOrdersRel = findViewById(R.id.my_orders_rel);
        mOrdersRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open MyOrders activity
                Intent intent = new Intent(HomeActivity.this, MyOrdersActivity.class);
                startActivity(intent);
            }
        });

        mOffersRel = findViewById(R.id.offers_rel);
        mOffersRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open offers activity
                Intent intent = new Intent(HomeActivity.this, OffersActivity.class);
                startActivity(intent);
            }
        });

        mHelpSupportRel = findViewById(R.id.help_support_rel);
        mHelpSupportRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open help support activity
                Intent intent = new Intent(HomeActivity.this, HelpAndSupportActivity.class);
                startActivity(intent);

            }
        });

        mTCRel = findViewById(R.id.termsAndCond_rel);
        mTCRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open tc activity
                Intent intent = new Intent(HomeActivity.this, TAndCActivity.class);
                startActivity(intent);
            }
        });

        mAboutUsRel = findViewById(R.id.about_us_rel);
        mAboutUsRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open about us page
                Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });

        mLogoutRel = findViewById(R.id.logout_rel);
        mLogoutRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout from app
                LogoutPermissionDialogFragment dialog = new LogoutPermissionDialogFragment();
                dialog.setListener(new LogoutPermissionDialogFragment.OnDialogActionListener() {
                    @Override
                    public void onYesClicked() {

                    }
                });

                dialog.show(getSupportFragmentManager(),null);
            }
        });

    }

    private void fetchDetails()
    {
        FirebaseFirestore.getInstance().collection("users")
                .document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    User user= task.getResult().toObject(User.class);
                    Log.d("USER_DETAILS",user.toString());

                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Some problem occured. Plz try again", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("USER_ERROR",e.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!isDrawerOpen)
        {
            mDrawer.openDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(isDrawerOpen)
        {
            mDrawer.closeDrawer(GravityCompat.START,true);
            return;
        }
        super.onBackPressed();
    }
}
