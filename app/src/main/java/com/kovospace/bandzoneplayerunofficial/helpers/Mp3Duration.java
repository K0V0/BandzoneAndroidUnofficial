package com.kovospace.bandzoneplayerunofficial.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * True playing time of a local mp3, read from the file itself.
 *
 * MediaPlayer cannot be trusted here: an mp3 that is variable bitrate *and* has no Xing/Info/VBRI
 * header gives it nothing to work from, so it extrapolates from the first frame's bitrate. On a
 * track opening with a quiet 32 kbps lead-in that turns 3:23 into 22:03, and drags the seekbar
 * scale along with it.
 *
 * Cheapest strategy first: the VBR header, when there is one, holds the exact frame count in the
 * first frame. Only headerless files get walked frame by frame.
 */
public final class Mp3Duration {

    private static final int HEADER_PROBE_BYTES = 64 * 1024;

    private static final int[] BITRATES_MPEG1 =
            {0, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 0};
    private static final int[] BITRATES_MPEG2 =
            {0, 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160, 0};
    private static final int[] RATES_MPEG1 = {44100, 48000, 32000};
    private static final int[] RATES_MPEG2 = {22050, 24000, 16000};
    private static final int[] RATES_MPEG25 = {11025, 12000, 8000};

    private Mp3Duration() {}

    /**
     * @return duration in milliseconds, or null when the file is missing or cannot be parsed -
     *         callers fall back to whatever MediaPlayer reports.
     */
    public static Long read(String path) {
        if (path == null) {
            return null;
        }
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        try {
            byte[] probe = read(file, HEADER_PROBE_BYTES);
            Long fromHeader = fromVbrHeader(probe);
            if (fromHeader != null) {
                return fromHeader;
            }
            return byCountingFrames(read(file, (int) Math.min(file.length(), Integer.MAX_VALUE)));
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] read(File file, int maxBytes) throws IOException {
        byte[] buffer = new byte[maxBytes];
        InputStream in = new FileInputStream(file);
        try {
            int total = 0;
            int n;
            while (total < maxBytes && (n = in.read(buffer, total, maxBytes - total)) != -1) {
                total += n;
            }
            if (total == maxBytes) {
                return buffer;
            }
            byte[] exact = new byte[total];
            System.arraycopy(buffer, 0, exact, 0, total);
            return exact;
        } finally {
            in.close();
        }
    }

    private static int audioStart(byte[] d) {
        if (d.length > 10 && d[0] == 'I' && d[1] == 'D' && d[2] == '3') {
            return 10 + (((d[6] & 0x7F) << 21) | ((d[7] & 0x7F) << 14)
                    | ((d[8] & 0x7F) << 7) | (d[9] & 0x7F));
        }
        return 0;
    }

    private static boolean isFrameHeader(byte[] d, int i) {
        if (i + 4 > d.length) {
            return false;
        }
        if ((d[i] & 0xFF) != 0xFF || (d[i + 1] & 0xE0) != 0xE0) {
            return false;
        }
        int version = (d[i + 1] >> 3) & 3;   // 0 = MPEG2.5, 1 = reserved, 2 = MPEG2, 3 = MPEG1
        int layer = (d[i + 1] >> 1) & 3;     // 1 = Layer III
        int bitrateIdx = (d[i + 2] >> 4) & 0xF;
        int rateIdx = (d[i + 2] >> 2) & 3;
        return version != 1 && layer == 1 && bitrateIdx != 0 && bitrateIdx != 0xF && rateIdx != 3;
    }

    private static int sampleRate(byte[] d, int i) {
        int version = (d[i + 1] >> 3) & 3;
        int rateIdx = (d[i + 2] >> 2) & 3;
        if (version == 3) {
            return RATES_MPEG1[rateIdx];
        }
        return version == 2 ? RATES_MPEG2[rateIdx] : RATES_MPEG25[rateIdx];
    }

    private static int samplesPerFrame(byte[] d, int i) {
        return ((d[i + 1] >> 3) & 3) == 3 ? 1152 : 576;
    }

    private static int frameLength(byte[] d, int i) {
        int version = (d[i + 1] >> 3) & 3;
        int bitrateIdx = (d[i + 2] >> 4) & 0xF;
        int padding = (d[i + 2] >> 1) & 1;
        int bitrate = (version == 3 ? BITRATES_MPEG1[bitrateIdx] : BITRATES_MPEG2[bitrateIdx]) * 1000;
        return ((version == 3 ? 144 : 72) * bitrate / sampleRate(d, i)) + padding;
    }

    private static int findFirstFrame(byte[] d) {
        for (int i = audioStart(d); i < d.length - 4; i++) {
            if (isFrameHeader(d, i)) {
                return i;
            }
        }
        return -1;
    }

    private static Long fromVbrHeader(byte[] d) {
        int f = findFirstFrame(d);
        if (f < 0) {
            return null;
        }
        int version = (d[f + 1] >> 3) & 3;
        boolean mono = ((d[f + 3] >> 6) & 3) == 3;
        int sideInfo = version == 3 ? (mono ? 17 : 32) : (mono ? 9 : 17);

        int xing = f + 4 + sideInfo;
        if (matches(d, xing, "Xing") || matches(d, xing, "Info")) {
            int flags = readInt(d, xing + 4);
            if (flags == Integer.MIN_VALUE || (flags & 1) == 0) {
                return null;
            }
            int frames = readInt(d, xing + 8);
            return frames <= 0 ? null : toMillis(frames, samplesPerFrame(d, f), sampleRate(d, f));
        }

        int vbri = f + 4 + 32;
        if (matches(d, vbri, "VBRI")) {
            int frames = readInt(d, vbri + 14);
            return frames <= 0 ? null : toMillis(frames, samplesPerFrame(d, f), sampleRate(d, f));
        }
        return null;
    }

    private static Long byCountingFrames(byte[] d) {
        int i = findFirstFrame(d);
        if (i < 0) {
            return null;
        }
        int samplesPerFrame = samplesPerFrame(d, i);
        int sampleRate = sampleRate(d, i);
        long frames = 0;
        while (i < d.length - 4) {
            if (isFrameHeader(d, i)) {
                frames++;
                int len = frameLength(d, i);
                if (len <= 4) {
                    break;
                }
                i += len;
            } else {
                i++; // resync past padding or a damaged frame
            }
        }
        return frames == 0 ? null : toMillis(frames, samplesPerFrame, sampleRate);
    }

    private static Long toMillis(long frames, int samplesPerFrame, int sampleRate) {
        return sampleRate <= 0 ? null : frames * samplesPerFrame * 1000L / sampleRate;
    }

    private static boolean matches(byte[] d, int off, String tag) {
        if (off < 0 || off + tag.length() > d.length) {
            return false;
        }
        for (int i = 0; i < tag.length(); i++) {
            if (d[off + i] != tag.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private static int readInt(byte[] d, int off) {
        if (off < 0 || off + 4 > d.length) {
            return Integer.MIN_VALUE;
        }
        return ((d[off] & 0xFF) << 24) | ((d[off + 1] & 0xFF) << 16)
                | ((d[off + 2] & 0xFF) << 8) | (d[off + 3] & 0xFF);
    }
}
