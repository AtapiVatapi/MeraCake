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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements CakeListAdapter.OnCakeClicked {
    private RecyclerView mCakesList;
    FirebaseFirestore db;
    ListenerRegistration registration;
    private CakeListAdapter mCakesAdapter;
    private List<Cake> mCakesDataList;

    private TextView mHeyMsg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_dashboard,container,false);
        mCakesList = view.findViewById(R.id.cakes_list);
        mHeyMsg = view.findViewById(R.id.hey_msg);
        mHeyMsg.setText("Welcome to 5'cakes.");
        mCakesList.setLayoutManager(new GridLayoutManager(getContext(),2));
        mCakesDataList = new ArrayList<>();
        return view;
    }

    public void setHeyMsg(String name)
    {
        if(mHeyMsg!=null)
        mHeyMsg.setText("Hey! "+name+", Let's get you delicious cakes.");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCakesAdapter = new CakeListAdapter(getContext(),DashboardFragment.this, mCakesDataList);
        mCakesList.setAdapter(mCakesAdapter);
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
        //if internet is there then clear persistence
        //db.clearPersistence();
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

                        mCakesDataList.clear();
                        for(DocumentSnapshot doc: queryDocumentSnapshots)
                        {
                            mCakesDataList.add(doc.toObject(Cake.class));
//                            Log.d("CAKE_DETAILS",doc.toObject(Cake.class).getPrice().get(0)+"");
                            mCakesAdapter.notifyDataSetChanged();
                        }

                    }
                });


    }

    @Override
    public void onCakeClicked(int pos) {
       // Toast.makeText(getContext(), "Cake no."+pos, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), CakeDetailsActivity.class);
        intent.putExtra("CAKE",mCakesDataList.get(pos));
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}

class CakeListAdapter extends RecyclerView.Adapter<CakeListAdapter.CakeViewHolder>{

    String RUPEE = "\u20B9";

    private List<Cake> CakesList;
    public interface OnCakeClicked{
        void onCakeClicked(int pos);
    }

    private Context context;
    private OnCakeClicked listener;

    public CakeListAdapter(Context context , OnCakeClicked listener, List<Cake> CakesList) {
        this.context = context;
        this.listener = listener;
        this.CakesList = CakesList;
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
                .load(CakesList.get(i).getImage())
                .into(cakeViewHolder.mCakeImg);

        cakeViewHolder.mCakeName.setText(CakesList.get(i).getName());
        cakeViewHolder.mCakeCost.setText(RUPEE+" "+CakesList.get(i).getPrice().get(0));

        cakeViewHolder.mCakeRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCakeClicked(cakeViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return CakesList.size();
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
