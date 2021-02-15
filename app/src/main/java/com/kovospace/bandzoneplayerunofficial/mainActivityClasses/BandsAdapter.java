package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kovospace.bandzoneplayerunofficial.R;
import com.kovospace.bandzoneplayerunofficial.SongsActivity;
import com.kovospace.bandzoneplayerunofficial.objects.Band;

import java.util.List;

public class BandsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final Context context;
    private List<Band> listRecyclerItem;

    public BandsAdapter(Context context, List<Band> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class BandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private ImageView coverArt;
        private TextView styl;
        private String slug;
        private ImageButton isOnSD;

        public BandViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nazovKapely);
            coverArt = (ImageView) itemView.findViewById(R.id.coverArt);
            styl = (TextView) itemView.findViewById(R.id.styl);
            isOnSD = itemView.findViewById(R.id.savedOnDisk);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            slug = findBand(name.getText().toString()).getSlug();
            Intent myIntent = new Intent(context, SongsActivity.class);
            myIntent.putExtra("slug", slug);
            context.startActivity(myIntent);
        }

        private Band findBand(String bandName) {
            for (Band band : listRecyclerItem) {
                if (bandName == band.getTitle()) {
                    return band;
                }
            }
            return null;
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case VIEW_TYPE_ITEM:
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.bands_list, viewGroup, false);
                return new BandViewHolder((itemView));
            case VIEW_TYPE_LOADING:
                View loadingView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.bands_list_loading, viewGroup, false);
                return new LoadingViewHolder(loadingView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int viewType = getItemViewType(i);
        switch (viewType) {
            case VIEW_TYPE_LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
                break;
            case VIEW_TYPE_ITEM:
                BandViewHolder bandViewHolder = (BandViewHolder) viewHolder;
                Band band = (Band) listRecyclerItem.get(i);
                bandViewHolder.name.setText(band.getTitle());
                Glide.with(this.context).load(band.getLocalOrHref()).into(bandViewHolder.coverArt);
                bandViewHolder.styl.setText(band.getGenre() + " - " + band.getCity());
                if (band.isFromDb()) { bandViewHolder.isOnSD.setVisibility(View.VISIBLE); }
                else { bandViewHolder.isOnSD.setVisibility(View.INVISIBLE); }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listRecyclerItem.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }

}