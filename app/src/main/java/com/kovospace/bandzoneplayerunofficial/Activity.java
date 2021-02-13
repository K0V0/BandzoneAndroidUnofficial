package com.kovospace.bandzoneplayerunofficial;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.Connection;

public abstract class Activity extends AppCompatActivity {
    protected Connection connectionTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionTest = new Connection(this);
        connectionTest.getConnectionMethod();
    }
}
