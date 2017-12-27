package com.example.jordan.steelrods;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import android.widget.Toast;
import android.provider.MediaStore;
import android.database.Cursor;
import android.net.Uri;


public class MainActivity extends AppCompatActivity {

    private Button mCameraButton;
    private Button mGalleryButton;
    private static int RESULT_LOAD_IMG = 1;
    private static int REQUEST_IMAGE_CAPTURE = 2;
    String imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpButtons();


    }

    private void setUpButtons() {
        mCameraButton = (Button) findViewById(R.id.home_button_1);
        mGalleryButton = (Button) findViewById(R.id.home_button_2);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG) {
            try {
                // When an Image is picked
                if (resultCode == RESULT_OK && null != data) {
                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    Intent start = new Intent(MainActivity.this, ImagePreview.class);
                    start.putExtra("image_path", imgDecodableString);
                    startActivity(start);
                } else {
                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE) {
        }
    }
}
