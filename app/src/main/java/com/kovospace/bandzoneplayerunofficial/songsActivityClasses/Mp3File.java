package com.kovospace.bandzoneplayerunofficial.songsActivityClasses;

import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.helpers.FileStorage;

public class Mp3File extends FileStorage {

    public Mp3File(Context context) {
        super(context);
        createDirIfNotExist("music");
        setWorkingDirectory("music");
    }

}
