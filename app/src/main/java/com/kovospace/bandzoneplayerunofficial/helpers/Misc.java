package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;

public class Misc {

    public static int getPixels(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int calculatePagesCount(int count, int perPage) {
        int result = count / perPage;
        if (count % perPage > 0) {
            return result + 1;
        }
        return result;
    }

    // Tracks are stored on the removable card whenever there is one
    // (FileStorage.checkStorage), and that card is usually FAT32/exFAT, which
    // rejects these characters outright - a title like "DE JE ZAS TEN KOKOT?"
    // makes createNewFile() fail and the download dies with no file written.
    private static final String ILLEGAL_FILENAME_CHARS = "[\\\\/:*?\"<>|\\x00-\\x1F]";
    private static final String TRAILING_FILENAME_CHARS = "[. _]+$";
    private static final String FALLBACK_FILENAME = "track";

    public static String sanitizeFileName(String name) {
        if (name == null) {
            return FALLBACK_FILENAME;
        }
        // trailing dots and spaces are illegal on FAT too, so strip whatever
        // is left dangling at the end after the replacement
        String sanitized = name
                .trim()
                .replaceAll(ILLEGAL_FILENAME_CHARS, "_")
                .replaceAll(TRAILING_FILENAME_CHARS, "");
        return sanitized.isEmpty() ? FALLBACK_FILENAME : sanitized;
    }
}
