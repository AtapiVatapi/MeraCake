package com.vikpoo.a5cakes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListSelectorDialog extends DialogFragment {

    private RecyclerView mListView;
    private Context context;

    private ListAdapter.OnListItemClickListener listener;

    public void setListener(ListAdapter.OnListItemClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.dialog_list_selector,container,false);
        mListView = view.findViewById(R.id.list_view);

        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(new ListAdapter(getActivity(),listener, getDialog()));
         return view;
    }
}

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListView>{

    interface OnListItemClickListener{
        void onListItemClickListener(int pos);
    }

    private Context context;

    private OnListItemClickListener listener;
    private Dialog dialog;

    public ListAdapter(Context context,OnListItemClickListener listItemClickListener, Dialog dialog) {
        this.context = context;
        listener = listItemClickListener;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ListView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_view,parent,false);
        return new ListView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListView holder, int position) {
        holder.title.setText("Delhi");
        holder.mRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onListItemClickListener(holder.getAdapterPosition());
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class ListView extends RecyclerView.ViewHolder{

        TextView title;
        RelativeLayout mRel;
        public ListView(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name);
            mRel = itemView.findViewById(R.id.view_rel);
        }
    }
}
