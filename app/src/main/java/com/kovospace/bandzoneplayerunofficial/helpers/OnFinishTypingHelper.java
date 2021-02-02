package com.kovospace.bandzoneplayerunofficial.helpers;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

public abstract class OnFinishTypingHelper {
    private Handler handler = new Handler(Looper.getMainLooper());
    private long delay = 1000;
    private long last_text_edit = 0;
    private String text = "";

    public abstract void doStuffNotOften();
    public abstract void doStuffOnZero();
    //public abstract void doStuffBefore();
    //public abstract void doStuffOn();
    //public abstract void doStuffAfter();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                doStuffNotOften();
            }
        }
    };

    public TextWatcher watchText() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                //doStuffBefore();
            }
            @Override
            public void onTextChanged (final CharSequence s, int start, int before, int count) {
                //You need to remove this to run only once
                handler.removeCallbacks(input_finish_checker);
                //doStuffOn();
            }
            @Override
            public void afterTextChanged (final Editable s) {
                //doStuffAfter();
                if (s.length() <= 0) {
                    setText("");
                    doStuffOnZero();
                }
                last_text_edit = System.currentTimeMillis();
                handler.postDelayed(input_finish_checker, delay);
                setText(s.toString());
                // E/SpannableStringBuilder: SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length
                // ^ bug in swiftkey and few others keyboard
            }
        };
    }

}
