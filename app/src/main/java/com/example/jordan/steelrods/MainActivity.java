package com.example.jordan.steelrods;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class MainActivity extends AppCompatActivity {

    private Button mCameraButton;
    private Button mGalleryButton;
    private static final int REQUEST_LOAD_IMG = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Context mContext;
    Uri photoURI;
    File photoFile;

    private Bitmap selectedBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null)
        {
            photoURI= savedInstanceState.getParcelable("outputFileUri");
        }

        setUpButtons();
    }

    private void setUpButtons() {
        mCameraButton = (Button) findViewById(R.id.home_button_1);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                Date d = new Date();
                CharSequence s  = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
                values.put(MediaStore.Images.Media.TITLE, s.toString() + ".jpg");
                photoURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intentPicture, REQUEST_IMAGE_CAPTURE);
            }
        });

        mGalleryButton = (Button) findViewById(R.id.home_button_2);
        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_LOAD_IMG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOAD_IMG && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            photoURI = data.getData();
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            if (data != null && data.getData() != null)
            {
                photoURI = data.getData();
            }
        }
        String picturePath = getRealPathFromURI(photoURI,
                this);
        startPreviewActivity(picturePath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelable("outputFileUri", photoURI);
        super.onSaveInstanceState(outState);
    }


    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }
    /**
     * This method will start the preview activity if an image has been selected.
     */
    void startPreviewActivity(String path)
    {
        System.out.println("Path is:" + path);
        Intent loadPreviewClass = new Intent(this, ImagePreview.class);
        loadPreviewClass.putExtra("img_path", path);
        startActivity(loadPreviewClass);
        finish();
    }

}
