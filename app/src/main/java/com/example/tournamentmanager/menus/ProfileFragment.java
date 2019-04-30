package com.example.tournamentmanager.menus;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tournamentmanager.ImageDownloader;
import com.example.tournamentmanager.ImageUploader;
import com.example.tournamentmanager.R;
import com.example.tournamentmanager.ServerDB;
import com.example.tournamentmanager.tournaments.MyParticipationsActivity;
import com.example.tournamentmanager.tournaments.MyTournamentsActivity;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements ImageDownloader.ImageDownloadedListener {

    private String user;
    private ImageView imageView;
    private int CAMERA_CODE = 0;
    private int GALERY_CODE = 1;

    Uri imageUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences session = getContext().getSharedPreferences("session", 0);
        user = session.getString("session", null);

        imageView = view.findViewById(R.id.imageView_profile_picture);
        TextView textViewUsername = view.findViewById(R.id.textView_username);
        Button buttonMyTournaments = view.findViewById(R.id.button_my_tournaments);
        Button buttonMyParticipations = view.findViewById(R.id.button_my_participations);

        textViewUsername.setText(user);

        ServerDB serverDB = new ServerDB(getContext());
        serverDB.getUserLocation(user);

        String currentRegion = null;
        try {
            currentRegion = serverDB.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (currentRegion == null){
            currentRegion = "";
        }

        Spinner spinnerRegions = view.findViewById(R.id.spinner_regions);
        for (int i=0;i<spinnerRegions.getCount();i++){
            if (spinnerRegions.getItemAtPosition(i).toString().equals(currentRegion)){
                spinnerRegions.setSelection(i);
                break;
            }
        }

        serverDB = new ServerDB(getContext());
        serverDB.getUserImage(user, this);


        spinnerRegions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (selected.equals("")){
                    selected = "NULL";
                }

                ServerDB serverDB = new ServerDB(getContext());
                serverDB.updateUserLocation(user, selected);

                String result = null;
                try {
                    result = serverDB.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (result == null) {
                    serverDB.notifyError(getActivity().getWindow().getDecorView().getRootView());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonMyTournaments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyTournamentsActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        buttonMyParticipations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyParticipationsActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.choose_image_input));
                CharSequence[] options = {getString(R.string.option_camera), getString(R.string.option_galery)};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override public
                    void onClick(DialogInterface dialog, int which) {

                        Intent intent;

                        switch (which){
                            case 0:
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                                String filename = "IMG_" + timeStamp + "_";
                                File dir = getActivity().getFilesDir();
                                try {
                                    File imgFile = File.createTempFile(filename, ".jpg", dir);
                                    imageUri = FileProvider.getUriForFile(getContext(), "com.example.tournamentmanager.provider", imgFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, CAMERA_CODE);
                                break;
                            case 1:
                                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, GALERY_CODE);
                                break;
                        }
                    }});
                builder.create();
                builder.show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_CODE && resultCode == RESULT_OK){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ImageUploader imageLoader = new ImageUploader();
                imageLoader.uploadCompressed(getContext(), image, user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            imageView.setImageURI(imageUri);
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ImageUploader imageLoader = new ImageUploader();
                imageLoader.uploadCompressed(getContext(), image, user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onImageDownloaded(Bitmap image) {
        imageView.setImageBitmap(image);
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        ImageView imageViewEdit = getView().findViewById(R.id.imageView_edit);
        imageViewEdit.setVisibility(View.VISIBLE);
    }
}
