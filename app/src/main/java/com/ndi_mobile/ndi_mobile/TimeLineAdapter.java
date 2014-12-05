package com.ndi_mobile.ndi_mobile;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Antoine on 05/12/2014.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder>{

        private List<String[]> contacts;
        private int rowLayout;
        private Context mContext;

        public TimeLineAdapter(List<String[]> contacts, int rowLayout, Context context) {
            this.contacts = contacts;
            this.rowLayout = rowLayout;
            this.mContext = context;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
            return new ViewHolder(v);
        }
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.contactName.setText(contacts.get(i)[0]);
            viewHolder.contactDate.setText(contacts.get(i)[1]);

            if(contacts.get(i)[2] == "OK") {
                viewHolder.cardLayout.setCardBackgroundColor(Color.parseColor("#1abc9c"));
                viewHolder.contactImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.thumbs_up));
            }
            else if (contacts.get(i)[2] == "KO") {
                viewHolder.cardLayout.setCardBackgroundColor(Color.parseColor("#e74c3c"));
                viewHolder.contactImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.thumbs_down));
            }
            else if (contacts.get(i)[2] == "WARNING") {
                viewHolder.cardLayout.setCardBackgroundColor(Color.parseColor("#f39c12"));
                viewHolder.contactImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.warning_icon));
            }
        }

        @Override
        public int getItemCount() {
            return contacts == null ? 0 : contacts.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView contactName;
            public TextView contactDate;
            public CardView cardLayout;
            public ImageView contactImage;

            public ViewHolder(View itemView) {
                super(itemView);
                contactName = (TextView) itemView.findViewById(R.id.contactName);
                contactDate = (TextView) itemView.findViewById(R.id.contactDate);
                cardLayout =  (CardView) itemView.findViewById(R.id.card_view);
                contactImage = (ImageView) itemView.findViewById(R.id.contactImage);
            }
        }
}
