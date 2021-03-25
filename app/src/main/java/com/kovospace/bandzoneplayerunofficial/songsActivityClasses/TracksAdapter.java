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

package com.kovospace.bandzoneplayerunofficial.songsActivityClasses;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.kovospace.bandzoneplayerunofficial.R;
import com.kovospace.bandzoneplayerunofficial.databases.DbHelper;
import com.kovospace.bandzoneplayerunofficial.helpers.Connection;
import com.kovospace.bandzoneplayerunofficial.helpers.Settings;
import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;

import java.util.List;

public class TracksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<BandProfileItem> listRecyclerItem;
    private Mp3File mp3File;
    private ImageFile imageFile;
    private Connection connectionTester;
    private boolean isOnline;

    public TracksAdapter(Context context, List<BandProfileItem> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem; // null here on init or zero size - check
        this.mp3File = new Mp3File(this.context);
        this.imageFile = new ImageFile(this.context);
        this.connectionTester = new Connection(this.context);
        connectionTester.getConnectionMethod();
        isOnline = connectionTester.isConnectionAvailable();
        Player.init(this.context, this);
    }

    private void runPlayer(View v) {
        Track track = (Track) v.findViewById(R.id.trackName).getTag();
        Player.uiInit(
                v.findViewById(R.id.trackLoading),
                v.findViewById(R.id.pauseButton),
                v.findViewById(R.id.seekbarHolder),
                v.findViewById(R.id.seekBar),
                v.findViewById(R.id.trackTimeCurrent),
                v.findViewById(R.id.trackTimeTotal)
        );
        PlayerAnimations.showLoading(true, v.findViewById(R.id.trackLoading));
        Player.setTracklist(listRecyclerItem);
        Player.play(listRecyclerItem.indexOf(track));
    }

    private void togglePlay(View v) {
        if (Player.isPlaying()) {
            Player.pause();
            ((ImageButton) v).setImageResource(R.mipmap.play);
        } else if (Player.pauseState() == 1) {
            Player.play();
            ((ImageButton) v).setImageResource(R.mipmap.pause);
        }
    }

    private void downloadMP3(View view, Track track) {
        ProgressBar downloadLoading = view.findViewById(R.id.downloadLoading);
        ImageButton downloadButton = view.findViewById(R.id.downloadButton);
        if (!mp3File.fileExists(track.getTrackFullLocalPath())) {
            Settings.triggerBandTrackDowloaded((Band) listRecyclerItem.get(0));
            // ^ pozor do buducnosti, non-track contentu moze pribudnut, info o kapele nemusi byt na poz 0
            PRDownloader.download(
                    track.getHref(),
                    mp3File.getWorkingDirectoryPath() + "/" + track.getBandSlug(),
                    track.getTitle() + ".mp3"
            )
                    .build()
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            PlayerAnimations.downloadComplete(downloadButton, downloadLoading);
                            DbHelper.rememberBandAndTracksForOffline(listRecyclerItem);
                        }
                        @Override
                        public void onError(Error error) {
                        }
                    });
            downloadLoading.animate().alpha(1.0f).setDuration(200);
        } else {
            // remove download
            mp3File.removeFile(track.getTrackFullLocalPath());
            if (!isOnline) {
                // image to cannot download
                downloadButton.setImageResource(R.mipmap.no_download_foreground);
                view.setOnClickListener(null);
            } else {
                downloadButton.setImageResource(R.mipmap.download_foreground);
            }
            DbHelper.rememberBandAndTracksForOffline(listRecyclerItem);
        }
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        private LinearLayout baseHolder;
        private TextView title;
        private TextView album;
        private SeekBar progressBar;
        private ImageButton pauseButton;
        private LinearLayout progressbarHolder;
        private ProgressBar trackLoading;
        private TextView currentTime;
        private TextView totalTime;
        private ImageButton downloadButton;
        private ProgressBar downloadLoading;
        private ImageView trackSaved;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            baseHolder = itemView.findViewById(R.id.baseHolder);
                title = baseHolder.findViewById(R.id.trackName);
                album = baseHolder.findViewById(R.id.albumName);
                pauseButton = baseHolder.findViewById(R.id.pauseButton);
                trackLoading = baseHolder.findViewById(R.id.trackLoading);
                trackSaved = baseHolder.findViewById(R.id.trackSaved);
            progressbarHolder = itemView.findViewById(R.id.seekbarHolder);
                progressBar = progressbarHolder.findViewById(R.id.seekBar);
                currentTime = progressbarHolder.findViewById(R.id.trackTimeCurrent);
                totalTime = progressbarHolder.findViewById(R.id.trackTimeTotal);
                downloadButton = progressbarHolder.findViewById(R.id.downloadButton);
                downloadLoading = progressbarHolder.findViewById(R.id.downloadLoading);
            PlayerAnimations.init(context);
            //itemView.setOnClickListener(this);
        }

        public void bindView(int position) {
            Track track = (Track) listRecyclerItem.get(position);
            title.setText(track.getTitle());
            title.setTag(track);
            album.setText(track.getAlbum());
            if (isOnline || (!isOnline && track.isAvailableOffline())) {
                baseHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { runPlayer(view); }
                });
                pauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { togglePlay(v); }
                });
                downloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { downloadMP3(view, track); }
                });
                if (track.isAvailableOffline()) {
                    trackSaved.setVisibility(View.VISIBLE);
                }
            } else {
                view.setAlpha(0.5f);
            }
            if (mp3File.fileExists(track.getTrackFullLocalPath())) {
                downloadButton.setImageResource(R.mipmap.remove_foreground);
            }
            PlayerAnimations.animate(pauseButton, progressbarHolder, trackLoading, track);
            if (track.isPlaying()) { // it is not actually playing, it is set to be played
                Player.uiInit(trackLoading, pauseButton, progressbarHolder, progressBar, currentTime, totalTime);
            }
        }

        @Override
        public void onClick(View v) {}
    }

    public class BandInfoViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView genre;
        private ImageView image;

        public BandInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bandName);
            genre = itemView.findViewById(R.id.bandGenreAndCity);
            image = itemView.findViewById(R.id.bandImage);
        }

        public void bindView(int position) {
            Band band = (Band) listRecyclerItem.get(position);
            title.setText(band.getTitle());
            genre.setText(band.getGenre() + " - " + band.getCity());
            Glide
                    .with(context)
                    .load(band.getLocalOrHref())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            changeLayout();
                            imageFile.saveDrawableIfNotExist(band, resource);
                            Player.showPlayerIfPlaying(listRecyclerItem);
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
        } else if (viewType == 0) {
            // ziadny track
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
        LinearLayout bandRetarder = ((Activity) context).findViewById(R.id.bandWaiter);
        bandRetarder.animate().alpha(0.0F).setDuration(250).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                bandRetarder.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }
}