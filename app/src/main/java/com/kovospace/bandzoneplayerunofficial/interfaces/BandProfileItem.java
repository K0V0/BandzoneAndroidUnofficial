package com.kovospace.bandzoneplayerunofficial.interfaces;

public interface BandProfileItem {
    int TYPE_TRACK = 1;
    int TYPE_BAND = -1;
    boolean contains(BandProfileItem o);
    String getLocalOrHref();
    boolean hasOfflineCopy();
    boolean isAvailableOffline();
}
