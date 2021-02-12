package com.kovospace.bandzoneplayerunofficial.songsActivityClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.kovospace.bandzoneplayerunofficial.helpers.FileStorage;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageFile extends FileStorage {

    public ImageFile(Context context) {
        super(context);
        createDirIfNotExist("covers");
        setWorkingDirectory("covers");
    }

    public void saveDrawableIfNotExist(Band band, Drawable drawable) {
        if (!band.isImageAvailableOffline()) {
            createDirIfNotExist(band.getSlug());
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            File imageFile = new File(band.getImageFullLocalPath());
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
