package com.example.bandzoneplayerunofficial.songsActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import com.example.bandzoneplayerunofficial.helpers.PlayerHelper;
import com.example.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.example.bandzoneplayerunofficial.objects.Band;
import com.example.bandzoneplayerunofficial.objects.Track;

import java.util.List;

public class TracksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<BandProfileItem> listRecyclerItem;

    public TracksAdapter(Context context, List<BandProfileItem> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem; // null here on init
        PlayerStatic.init(this.context, this);
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView album;
        private LinearLayout pauseHolder;
        private SeekBar progressHolder;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.trackName);
            album = (TextView) itemView.findViewById(R.id.albumName);
            pauseHolder = (LinearLayout) itemView.findViewById(R.id.pauseButtonHolder);
            progressHolder = (SeekBar) itemView.findViewById(R.id.seekBar);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position) {
            // behavior of PLAYER lib and bindView():

            // STATIC lib
            // plays when opened profile of another band
            // run on click
            // run when app opened and next track played
            // does not run when app on background and track changed
            // when of profile of other band, plays track with next index of new band
            // ^^^ app crash when on profile of another band trying to play next track [RESOLVED]

            // INSTANCE
            // stops when opened profile of another band
            // run on click
            // run in next track when opened
            // does not run when on backgound
            // --

            Track track = (Track) listRecyclerItem.get(position);
            title.setText(track.getTitle());
            title.setTag(track);
            album.setText(track.getAlbum());
            PlayerHelper.updateUIanimation(context, pauseHolder, progressHolder, track);
        }

        @Override
        public void onClick(View v) {
            Track track = (Track) v.findViewById(R.id.trackName).getTag();
            PlayerStatic.setTracklist(listRecyclerItem);
            PlayerStatic.play(listRecyclerItem.indexOf(track));
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
                            PlayerStatic.showPlayerIfPlaying(listRecyclerItem);
                            return false;
                        }
                    })
                    .into(image);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i > 0) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.band_tracks_list, viewGroup, false);
            return new TrackViewHolder(itemView);
        } else if (i < 0) {
            View loadingView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.band_info, viewGroup, false);
            return new BandInfoViewHolder(loadingView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //viewHolder.setIsRecyclable(false); // removes artifacts but NAKOKOT shitty performance
        int viewType = getItemViewType(i);
        if (viewType < 0) {
            ((BandInfoViewHolder) viewHolder).bindView(i);
        } else if (viewType > 0) {
            ((TrackViewHolder) viewHolder).bindView(i);
        } else {
            throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String className = listRecyclerItem.get(position).getClass().getSimpleName();
        switch (className) {
            case "Track":
                return BandProfileItem.TYPE_TRACK * (position+1);
            case "Band":
                return BandProfileItem.TYPE_BAND * (position+1);
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