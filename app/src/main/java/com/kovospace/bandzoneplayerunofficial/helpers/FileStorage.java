package com.kovospace.bandzoneplayerunofficial.helpers;

import android.content.Context;
import androidx.core.content.ContextCompat;

import java.io.File;

public abstract class FileStorage {
    //protected Storage storage;
    protected Context context;
    //protected String rootPath;
    protected File appDataStorage;
    protected String appDataPath;

    protected File sdcardStorage;
    protected File emulatedStorage;
    protected String storageSdcardDataPath;
    protected String storageEmulatedDataPath;

    protected File workingDirectory;
    //protected String storageInternalDataPath;

    public FileStorage(Context context) {
        this.context = context;
        //this.storage = new Storage(this.context);
        checkStorage();
    }

    private void checkStorage() {
        //System.out.println(Environment.getExternalStorageState());
        //System.out.println(context.getExternalFilesDir(null));
        //context.getExternalFilesDir(null);
        System.out.println("----------------");

        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        if (storages.length > 1 && storages[0] != null && storages[1] != null) {
            emulatedStorage = storages[0];
            storageEmulatedDataPath = storages[0].toString();
            sdcardStorage = storages[1];
            storageSdcardDataPath = storages[1].toString();
            appDataStorage = sdcardStorage;
            appDataPath = storageSdcardDataPath;
            /*for (int i = 0; i < 2; i++) {
                System.out.println(storages[i].toString());

            }*/
            // ma sd kartu
        } else if (storages.length == 1) {
            emulatedStorage= storages[0];
            storageEmulatedDataPath = storages[0].toString();
            appDataStorage = emulatedStorage;
            appDataPath = storageEmulatedDataPath;
            // je emulovana
        } else {
            // ani jedno
        }
        workingDirectory = appDataStorage;


        /*System.out.println(Environment.isExternalStorageEmulated());
        System.out.println(ContextCompat.getExternalFilesDirs(context, null).length);

        if (storage.isExternalWritable()) {
            // zobrazuje "akoze" sd kartu - particia internej pamate
            System.out.println("external storage ok");
            rootPath = storage.getExternalStorageDirectory();
            appDataPath = context.getExternalFilesDir(null).toString();
        } else {

        }*/

        //System.out.println(rootPath);
        System.out.println(appDataPath);
    }

    protected void createDirIfNotExist(String path) {
        if (!isDirectory(path)) {
            File f = new File(appDataPath + "/" + path);
            f.mkdir();
        }
    }

    protected void setWorkingDirectory(String path) {
        workingDirectory = new File(appDataPath + "/" + path);
    }

    protected boolean isDirectory(String path) {
        File f = new File(appDataPath + "/" + path);
        if(f.isDirectory()) {
            return true;
        }
        return false;
    }


}
