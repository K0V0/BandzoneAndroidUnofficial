package com.kovospace.bandzoneplayerunofficial.helpers;

import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;
import java.util.List;

public class PlayerHelper {

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

    public static String milisecondsToHuman(long milliseconds) {
        String result = "";
        final long MILI_TO_HOUR = 3600000;
        final long MILI_TO_MINS = 60000;
        int hrs = 0;
        int mins = 0;
        int secs = 0;
        hrs = (int) (milliseconds / MILI_TO_HOUR);
        mins = (int) ((milliseconds % MILI_TO_HOUR) / MILI_TO_MINS);
        secs = (int) (((milliseconds % MILI_TO_HOUR) % MILI_TO_MINS) / 1000);
        if (hrs > 0) {
            result = hrs + ":" + timeNum(mins) + ":" + timeNum(secs);
        } else if (mins > 0) {
            result = mins + ":" + timeNum(secs);
        } else {
            result = "0:" + timeNum(secs);
        }
        return result;
    }

    private static String timeNum(int num) {
        if (num < 10) {
            return "0" + num;
        } else {
            return "" + num;
        }
    }

}
