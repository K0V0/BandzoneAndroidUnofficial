package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;
import java.util.List;

public class PlayerHelper {
    private Context context;

    public PlayerHelper() {}

    public PlayerHelper(Context context) {
        this.context = context;
    }

    public static void updatePlayState(List<BandProfileItem> itemsList, Track currentTrack) {
        for (int i = 0; i < itemsList.size(); i++) {
            BandProfileItem item = itemsList.get(i);
            if (item.getClass() == Track.class) {
                if (((Track) item).getOrder() == currentTrack.getOrder()) {
                    ((Track) item).setPlaying(true);
                } else {
                    ((Track) item).setPlaying(false);
                }
            }
        }
    }

    public static Band getBandFromList(List<BandProfileItem> list) {
        for (BandProfileItem item : list) {
            if (item.getClass() == Band.class) {
                return (Band) item;
            }
        }
        return null;
    }

    public static boolean isBandInList(List<BandProfileItem> list, Band wantedBand) {
        Band listBand = getBandFromList(list);
        return listBand.getSlug().equals(wantedBand.getSlug());
    }

    public static int posOfTrackInList(List<BandProfileItem> list, Track wantedTrack) {
        int index = -1;
        for (BandProfileItem item : list) {
            if (item.getClass() == Track.class) {
                if (item.contains(wantedTrack)) {
                    return list.indexOf(item);
                }
            }
        }
        return index;
    }

}
