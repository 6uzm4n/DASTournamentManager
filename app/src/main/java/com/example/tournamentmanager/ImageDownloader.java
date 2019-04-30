package com.example.tournamentmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {


    private ImageDownloadedListener listener;

    @Override
    protected Bitmap doInBackground(String... strings) {
        String image64 = strings[0];
        byte[] imageArray = Base64.decode(image64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
    }

    public void downloadCompressed(ImageDownloadedListener pListener, String pImage){
        listener = pListener;
        this.execute(pImage);
    }

    @Override
    protected void onPostExecute(Bitmap b) {
        listener.onImageDownloaded(b);
        listener = null;
    }

    public interface ImageDownloadedListener {
        void onImageDownloaded(Bitmap image);
    }
}
