package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;
import androidx.core.content.ContextCompat;

import java.io.File;

public abstract class FileStorage {
    protected Context context;
    protected File appDataStorage;
    protected String appDataPath;
    protected File sdcardStorage;
    protected File emulatedStorage;
    protected String storageSdcardDataPath;
    protected String storageEmulatedDataPath;
    protected File workingDirectory;
    protected String workingDirectoryPath;

    public FileStorage(Context context) {
        this.context = context;
        checkStorage();
    }

    private void checkStorage() {
        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        if (storages.length > 1 && storages[0] != null && storages[1] != null) {
            // ma sd kartu
            emulatedStorage = storages[0];
            storageEmulatedDataPath = storages[0].toString();
            sdcardStorage = storages[1];
            storageSdcardDataPath = storages[1].toString();
            appDataStorage = sdcardStorage;
            appDataPath = storageSdcardDataPath;
        } else if (storages.length == 1) {
            // je emulovana
            emulatedStorage= storages[0];
            storageEmulatedDataPath = storages[0].toString();
            appDataStorage = emulatedStorage;
            appDataPath = storageEmulatedDataPath;
        } else {
            // ani jedno
        }
        workingDirectory = appDataStorage;
        workingDirectoryPath = appDataPath;
    }

    protected void createDirIfNotExist(String path) {
        if (!isDirectory(path)) {
            File f = new File(workingDirectoryPath + "/" + path);
            f.mkdir();
        }
    }

    protected void setWorkingDirectory(String path) {
        workingDirectoryPath = appDataPath + "/" + path;
        workingDirectory = new File(workingDirectoryPath);
    }

    public String getWorkingDirectoryPath() {
        return workingDirectoryPath;
    }

    public boolean fileExists(String path) {
        File f = new File(path);
        return fileExists(f);
    }

    public boolean fileExists(File file) {
        return file.exists() && !file.isDirectory();
    }

    public void removeFile(String path) {
        File f = new File(path);
        if (fileExists(f)) {
            f.delete();
        }
    }

    public boolean isDirectory(String path) {
        File f = new File(workingDirectoryPath + "/" + path);
        return isDirectory(f);
    }

    public boolean isDirectory(File file) {
        return file.isDirectory();
    }

}
