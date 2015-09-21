package com.programmers.wine.gaslabs.ui.about;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.programmers.wine.gaslabs.R;

import java.util.List;

public class AboutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<AboutItem> itemList;
    // private Context context;

    public AboutAdapter(List<AboutItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_about, parent, false);
        // context = parent.getContext();
        return new AboutVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AboutItem item = itemList.get(position);
        if (holder instanceof AboutVH) {
            ((AboutVH) holder).populateCard(item);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
