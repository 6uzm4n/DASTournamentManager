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

    /**
     * Dado un listener y una imagen codificada, se decodifica esta en segundo plano.
     * @param pListener Listener que recibirá la información de la imagen decodificada
     * @param pImage    Imagen a decodificar.
     */
    public void downloadCompressed(ImageDownloadedListener pListener, String pImage){
        listener = pListener;
        this.execute(pImage);
    }

    @Override
    protected void onPostExecute(Bitmap b) {
        // Cuando la imagen haya sido decodificada se comunica esto al listener
        listener.onImageDownloaded(b);
        listener = null;
    }

    /**
     * Interfaz que implementará el listener.
     */
    public interface ImageDownloadedListener {
        void onImageDownloaded(Bitmap image);
    }
}
