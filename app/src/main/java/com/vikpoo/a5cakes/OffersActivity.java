package com.vikpoo.a5cakes;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OffersActivity extends AppCompatActivity {

    private RecyclerView mOffersList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        initToolbar();
        initUI();
    }

    private void initUI()
    {
        getWindow().setStatusBarColor(Color.BLACK);
        mOffersList = findViewById(R.id.offers_list_view);
        mOffersList.setLayoutManager(new LinearLayoutManager(this));
        mOffersList.setAdapter(new OffersListAdapter(this));
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

}

class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.OfferViewHolder> {

    private Context context;

    public OffersListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.offer_view,parent,false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 7;
    }


    class OfferViewHolder extends RecyclerView.ViewHolder
    {

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
