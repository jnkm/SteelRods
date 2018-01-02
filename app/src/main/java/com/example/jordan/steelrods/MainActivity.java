package com.example.jordan.steelrods;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private Button mCameraButton;
    private Button mGalleryButton;
    private static final int REQUEST_LOAD_IMG = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    String pictureImagePath = "";

    private Bitmap selectedBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpButtons();
    }

    private void setUpButtons() {
        mCameraButton = (Button) findViewById(R.id.home_button_1);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = timeStamp + ".jpg";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                File file = new File(pictureImagePath);
                Uri outputFileUri = Uri.fromFile(file);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        mGalleryButton = (Button) findViewById(R.id.home_button_2);
        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_LOAD_IMG);
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
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                selectedBitmap = BitmapFactory.decodeStream(inputStream);
            }
            catch (Exception e)
            {

            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                System.out.println("FILE EXISTS");
                selectedBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
        }

        startPreviewActivity();
    }

    /**
     * This method will start the preview activity if an image has been selected.
     */
    void startPreviewActivity()
    {
        if (selectedBitmap == null) {
            return;
        }

        ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
        selectedBitmap.compress(Bitmap.CompressFormat.PNG,100, bitmapStream);
        byte[] byteArray = bitmapStream.toByteArray();

        Intent loadPreviewClass = new Intent(this, ImagePreview.class);
        loadPreviewClass.putExtra("image", byteArray);
        startActivity(loadPreviewClass);
        finish();
    }
}
