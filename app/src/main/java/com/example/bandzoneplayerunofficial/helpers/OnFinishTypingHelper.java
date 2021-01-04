package com.example.bandzoneplayerunofficial.helpers;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

public class OnFinishTypingHelper {
    private Handler handler = new Handler();
    private long delay = 1000;
    private long last_text_edit = 0;
    private String text = "";

    public void doStuff() {
        // to override
    }

    public String getText() {
        return text;
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                doStuff();
            }
        }
    };

    public TextWatcher watchText() {
        TextWatcher textWatch = new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count,
                                           int after){
            }
            @Override
            public void onTextChanged (final CharSequence s, int start, int before,
                                        int count){
                //You need to remove this to run only once
                handler.removeCallbacks(input_finish_checker);
            }
            @Override
            public void afterTextChanged (final Editable s){
                //avoid triggering event when text is empty
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                    text = s.toString();
                } else {

                }
            }
        };
        return textWatch;
    }

}
