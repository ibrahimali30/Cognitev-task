package com.ibrahim.cognitev_task;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

 class ExpandableSection extends StatelessSection {

    final String title;
    final List<Campaign> list;
    boolean expanded = true;

    ExpandableSection(String title, List<Campaign> list) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.section_ex4_item)
                .headerResourceId(R.layout.section_ex4_header)
                .build());

        this.title = title;
        this.list = list;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? list.size() : 0;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        Campaign campaign = list.get(position);

        itemHolder.tvItem.setText(campaign.getName());

//            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(this,
//                            String.format("Clicked on position #%s of Section %s",
//                                    sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()), title),
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);

        headerHolder.imgArrow.setImageResource(
                expanded ? R.drawable.ic_keyboard_arrow_up_black_18dp : R.drawable.ic_keyboard_arrow_down_black_18dp
        );

        headerHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                MainActivity.sectionAdapter.notifyDataSetChanged();
            }
        });
    }

     private class HeaderViewHolder extends RecyclerView.ViewHolder {

         private final View rootView;
         private final TextView tvTitle;
         private final ImageView imgArrow;

         HeaderViewHolder(View view) {
             super(view);

             rootView = view;
             tvTitle = view.findViewById(R.id.tvTitle);
             imgArrow = view.findViewById(R.id.imgArrow);
         }
     }

     private class ItemViewHolder extends RecyclerView.ViewHolder {

         private final View rootView;
         private final TextView tvItem;

         ItemViewHolder(View view) {
             super(view);

             rootView = view;
             tvItem = view.findViewById(R.id.tvItem);
         }
     }
}
