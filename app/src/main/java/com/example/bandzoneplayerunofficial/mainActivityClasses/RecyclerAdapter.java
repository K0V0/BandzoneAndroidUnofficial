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

    private static final int TYPE = 1;
    private final Context context;
    private final List<Band> listRecyclerItem;

    public RecyclerAdapter(Context context, List<Band> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView coverArt;
        private TextView styl;
        //private TextView date;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nazovKapely);
            coverArt = (ImageView) itemView.findViewById(R.id.coverArt);
            styl = (TextView) itemView.findViewById(R.id.styl);
            //date = (TextView) itemView.findViewById(R.id.date);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case TYPE:

            default:

                View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.bands_list, viewGroup, false);

                return new ItemViewHolder((layoutView));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int viewType = getItemViewType(i);

        switch (viewType) {
            case TYPE:
            default:

                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                Band bands = (Band) listRecyclerItem.get(i);

                itemViewHolder.name.setText(bands.getTitle());
                Glide.with(this.context).load(bands.getImage_url()).into(itemViewHolder.coverArt);
                //itemViewHolder.coverArt.setImageURI(bands.getImage_url());
                itemViewHolder.styl.setText(bands.getGenre() + " - " + bands.getCity());
        }

    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}