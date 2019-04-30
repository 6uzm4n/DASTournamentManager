package com.example.tournamentmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUploader extends AsyncTask<Bitmap, Void, String> {

    private String user;
    private Context context;

    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        Bitmap image = bitmaps[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        System.out.println("Bitmap size:" + stream.size());
        byte[] byteArray = stream.toByteArray();
        String image64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        System.out.println("String size:" + image64.length());
        return image64;
    }

    public void uploadCompressed(Context pContext, Bitmap pImage, String pUser){
        user = pUser;
        context = pContext;
        this.execute(pImage);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ServerDB serverDB = new ServerDB(context);
        serverDB.updateUserImage(user, s);
    }
}
