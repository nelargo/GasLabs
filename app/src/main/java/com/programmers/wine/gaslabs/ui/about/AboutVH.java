package com.programmers.wine.gaslabs.ui.about;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.programmers.wine.gaslabs.R;

public class AboutVH extends RecyclerView.ViewHolder {
    protected TextView title;
    protected TextView subtitle;
    protected View root;

    public AboutVH(View root) {
        super(root);
        this.root = root;
        title = (TextView) root.findViewById(R.id.card_item_about_title);
        subtitle = (TextView) root.findViewById(R.id.card_item_about_subtitle);
    }

    public void hideSubtitle() {
        if (subtitle != null) {
            subtitle.setVisibility(View.GONE);
        }
    }

    public void populateCard(AboutItem item) {
        if (item == null) {
            return;
        }

        if (title != null) {
            title.setText(item.getTitle());
        }

        if (subtitle != null) {
            subtitle.setText(item.getSubtitle());
        }

        switch (item.getType()) {
            case POLICIES:
                hideSubtitle();
                break;
            case TERMS:
                hideSubtitle();
                break;
        }

    }

}
