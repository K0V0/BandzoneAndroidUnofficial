package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastMessage {
    private Context context;
    private int messageId;

    public ToastMessage(Context context) {
        this(context, 0);
    }

    public ToastMessage(Context context, int messageId) {
        this.context = context;
        this.messageId = messageId;
    }

    public void send() {
        toastMessage();
    }

    public void send(int messageId) {
        this.messageId = messageId;
        toastMessage();
    }

    private void toastMessage() {
        Toast toast = Toast.makeText(this.context, this.messageId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
