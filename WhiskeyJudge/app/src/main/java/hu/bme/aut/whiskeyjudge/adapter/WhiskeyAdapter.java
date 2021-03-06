package hu.bme.aut.whiskeyjudge.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import hu.bme.aut.whiskeyjudge.R;
import hu.bme.aut.whiskeyjudge.data.WhiskeyItem;


public class WhiskeyAdapter
        extends RecyclerView.Adapter<WhiskeyAdapter.WhiskeyViewHolder> {

    private final List<WhiskeyItem> items;

    private WhiskeyItemClickListener listener;

    public WhiskeyAdapter(WhiskeyItemClickListener listener) {
        this.listener = listener;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public WhiskeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_whiskey_list, parent, false);
        return new WhiskeyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WhiskeyViewHolder holder, int position) {
        WhiskeyItem item = items.get(position);
        holder.nameTextView.setText(item.name);
        holder.categoryTextView.setText(item.category.name());
        holder.priceTextView.setText(item.estimatedPrice + " Ft");
        holder.iconImageView.setImageResource(getImageResource(item.category));

        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface WhiskeyItemClickListener {
        //void onItemChanged(WhiskeyItem item);
        void onItemDeleted(WhiskeyItem item);

        void requestItemChanging(WhiskeyItem item);

        void onWhiskeyItemRate(WhiskeyItem ratedItem);
    }

    private @DrawableRes
    int getImageResource(WhiskeyItem.Category category) {
        @DrawableRes int ret;
        switch (category) {
            case SCOTTISH:
                ret = R.drawable.whiskey;
                break;
            case IRISH:
                ret = R.drawable.irish;
                break;
            case BOURBON:
                ret = R.drawable.bourbon;
                break;
            case CANADIAN:
                ret = R.drawable.canada;
                break;
            default:
                ret = 0;
        }
        return ret;
    }

    public void addItem(WhiskeyItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }


    public void deleteItem(WhiskeyItem item) {
        int temp = items.indexOf(item);
        items.remove(item);
        notifyItemRemoved(temp);
    }

    public void update(List<WhiskeyItem> whiskeyItems) {
        items.clear();
        items.addAll(whiskeyItems);
        notifyDataSetChanged();
    }


    class WhiskeyViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImageView;
        TextView nameTextView;
        TextView categoryTextView;
        TextView priceTextView;
        ImageButton removeButton;
        ImageButton editButton;

        WhiskeyItem item;

        WhiskeyViewHolder(View itemView) {
            super(itemView);

            iconImageView = itemView.findViewById(R.id.WhiskeyItemIconImageView);
            nameTextView = itemView.findViewById(R.id.WhiskeyItemNameTextView);
            categoryTextView = itemView.findViewById(R.id.WhiskeyItemCategoryTextView);
            priceTextView = itemView.findViewById(R.id.WhiskeyItemPriceTextView);
            removeButton = itemView.findViewById(R.id.WhiskeyItemRemoveButton);
            editButton = itemView.findViewById(R.id.WhiskeyItemModifyButton);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemDeleted(item);

                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.requestItemChanging(item);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onWhiskeyItemRate(item);
                }
            });

        }
    }
}