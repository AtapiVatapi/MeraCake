package com.vikpoo.a5cakes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

public class DashboardFragment extends Fragment implements CakeListAdapter.OnCakeClicked {
    private RecyclerView mCakesList;
    FirebaseFirestore db;
    ListenerRegistration registration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_dashboard,container,false);
        mCakesList = view.findViewById(R.id.cakes_list);
        mCakesList.setLayoutManager(new GridLayoutManager(getContext(),2));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCakesList.setAdapter(new CakeListAdapter(getContext(), DashboardFragment.this));
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
        registration.remove();
    }

    @Override
    public void onResume() {
        super.onResume();

        registration = db.collection("available_cakes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if(e != null)
                        {
                            Toast.makeText(getContext(), "Some error occured while fetching data.", Toast.LENGTH_SHORT).show();
                            Log.d("CAKES_FETCH",e.getMessage());
                            return;
                        }

                        for(DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges())
                        {
                            switch (documentChange.getType())
                            {
                                case ADDED:
                                    Log.d("CAKE_FETCH_ADD",documentChange.getDocument().getData().toString());
                                    break;
                                case MODIFIED:
                                    Log.d("CAKE_FETCH_MOD",documentChange.getDocument().getData().toString());
                                    break;
                                case REMOVED:
                                    Log.d("CAKE_FETCH_DEL",documentChange.getDocument().getData().toString());
                                    break;
                            }
                        }
                    }
                });


    }

    @Override
    public void onCakeClicked(int pos) {
        Toast.makeText(getContext(), "Cake no."+pos, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), CakeDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}

class CakeListAdapter extends RecyclerView.Adapter<CakeListAdapter.CakeViewHolder>{

    public interface OnCakeClicked{
        void onCakeClicked(int pos);
    }

    public String[] CAKE_NAMES= {
      "Black Forest",
            "Red velvet",
            "Fruit cake",
            "Pineapple cake",
            "Oreo cake",
            "Truffle cake"
    };
    public String[] CAKES = {
            "http://www.petraveikkola.com/wp-content/uploads/2016/04/Petra-Veikkola-Photography-food-photography-and-styling-7-of-47.jpg",
            "https://twolovesstudio.com/wp-content/uploads/2018/12/How-I-Improved-My-Cake-Photography-In-One-Day-8.jpg",
            "https://cdn.sallysbakingaddiction.com/wp-content/uploads/2019/01/vanilla-cake-2.jpg",
            "https://i.pinimg.com/236x/ea/f8/37/eaf837e368c4d6e8260041391f542a22.jpg",
            "https://images.pexels.com/photos/1070850/pexels-photo-1070850.jpeg?cs=srgb&dl=blackberry-blur-cake-1070850.jpg&fm=jpg",
            "https://petapixel.com/assets/uploads/2017/11/leicalenscake-800x760.jpg"
    };

    public String[] COST = {
      "\u20B9 240",
            "\u20B9 300",
            "\u20B9 260",
            "\u20B9 280",
            "\u20B9 360",
            "\u20B9 240"
    };

    private Context context;
    private OnCakeClicked listener;

    public CakeListAdapter(Context context , OnCakeClicked listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CakeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cake_list_view,viewGroup,false);
        return new CakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CakeViewHolder cakeViewHolder, int i) {
        Glide.with(context)
                .load(CAKES[i])
                .into(cakeViewHolder.mCakeImg);

        cakeViewHolder.mCakeName.setText(CAKE_NAMES[i]);
        cakeViewHolder.mCakeCost.setText(COST[i]);

        cakeViewHolder.mCakeRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCakeClicked(cakeViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class CakeViewHolder extends RecyclerView.ViewHolder{
        ImageView mCakeImg;
        RelativeLayout mCakeRel;
        TextView mCakeName, mCakeCost;

        public CakeViewHolder(@NonNull View itemView) {
            super(itemView);
            mCakeImg = itemView.findViewById(R.id.cake_img);
            mCakeRel = itemView.findViewById(R.id.cake_rel);
            mCakeName = itemView.findViewById(R.id.cake_name);
            mCakeCost = itemView.findViewById(R.id.cake_cost);
        }
    }
}
