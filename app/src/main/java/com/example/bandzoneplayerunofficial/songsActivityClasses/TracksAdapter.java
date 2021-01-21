package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.bandzoneplayerunofficial.R;
import com.example.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.example.bandzoneplayerunofficial.objects.Band;
import com.example.bandzoneplayerunofficial.objects.Track;

import java.util.List;

public class TracksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<BandProfileItem> listRecyclerItem;

    public TracksAdapter(Context context, List<BandProfileItem> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView album;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.trackName);
            album = (TextView) itemView.findViewById(R.id.albumName);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position) {
            Track track = (Track) listRecyclerItem.get(position);
            title.setText(track.getTitle());
            title.setTag(new TrackTag( track.getHref() ));
            album.setText(track.getAlbum());
        }

        @Override
        public void onClick(View v) {
            TrackTag tag = (TrackTag) v.findViewById(R.id.trackName).getTag();
            Player.play(context, tag.getHref());
        }
    }

    public class BandInfoViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView genre;
        private ImageView image;

        public BandInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.bandName);
            genre = (TextView) itemView.findViewById(R.id.bandGenreAndCity);
            image = (ImageView) itemView.findViewById(R.id.bandImage);
        }

        public void bindView(int position) {
            Band band = (Band) listRecyclerItem.get(position);
            title.setText(band.getTitle());
            genre.setText(band.getGenre());
            Glide
                    .with(context)
                    .load(band.getImage_url())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            changeLayout();
                            return false;
                        }
                    })
                    .into(image);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case BandProfileItem.TYPE_TRACK:
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.band_tracks_list, viewGroup, false);
                return new TrackViewHolder(itemView);
            case BandProfileItem.TYPE_BAND:
                View loadingView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.band_info, viewGroup, false);
                return new BandInfoViewHolder(loadingView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int viewType = getItemViewType(i);
        switch (viewType) {
            case BandProfileItem.TYPE_BAND:
                ((BandInfoViewHolder) viewHolder).bindView(i);
                break;
            case BandProfileItem.TYPE_TRACK:
                ((TrackViewHolder) viewHolder).bindView(i);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String className = listRecyclerItem.get(position).getClass().getSimpleName();
        switch (className) {
            case "Track":
                return BandProfileItem.TYPE_TRACK;
            case "Band":
                return BandProfileItem.TYPE_BAND;
            default:
                return 0;
        }
    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }

    private void changeLayout() {
        LinearLayout bandView = ((Activity) context).findViewById(R.id.bandView);
        LinearLayout bandRetarder = ((Activity) context).findViewById(R.id.bandWaiter);
        bandView.removeView(bandRetarder);
        LinearLayout bandHolder = ((Activity) context).findViewById(R.id.bandHolder);
        bandHolder.animate().alpha(1.0F).setDuration(250);
    }
}