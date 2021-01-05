package com.example.bandzoneplayerunofficial.mainActivityClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bandzoneplayerunofficial.R;
import com.example.bandzoneplayerunofficial.objects.Band;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final Context context;
    private List<Band> listRecyclerItem;

    public RecyclerAdapter(Context context, List<Band> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView coverArt;
        private TextView styl;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nazovKapely);
            coverArt = (ImageView) itemView.findViewById(R.id.coverArt);
            styl = (TextView) itemView.findViewById(R.id.styl);
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
                return new ItemViewHolder((itemView));
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
                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                Band bands = (Band) listRecyclerItem.get(i);
                itemViewHolder.name.setText(bands.getTitle());
                Glide.with(this.context).load(bands.getImage_url()).into(itemViewHolder.coverArt);
                itemViewHolder.styl.setText(bands.getGenre() + " - " + bands.getCity());
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