package com.example.jordan.steelrods;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;

public class ImagePreview extends AppCompatActivity {

    private Button mCountButton;
    private Bitmap mImage;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3" );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        setUpButtons();

        Intent intentExtras = getIntent();
        String image_path = intentExtras.getStringExtra("image_path");

        showImageWrapper(image_path);

    }

    private void setUpButtons() {
        mCountButton = (Button) findViewById(R.id.home_button_2);

        mCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countImage();
                displayImage();
            }
        });
    }

    private void countImage() {
        Mat m = new Mat();
        Utils.bitmapToMat(mImage, m);
        KeyPoint kp = getBlobKeypoints(m);
        m = addKeypointsToImage(m, kp);

        Bitmap bmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bmp);

        mImage = bmp;
    }

    private void displayImage() {
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setImageBitmap(mImage);
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private void showImage(String image_path) {
        ImageView imgView = (ImageView) findViewById(R.id.imageView2);
        imgView.setImageBitmap(BitmapFactory.decodeFile(image_path));
    }

    private void showImageWrapper(String image_path) {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showMessageOKCancel("You need to allow access to Photos",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        showImage(image_path);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ImagePreview.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native KeyPoint getBlobKeypoints(Mat image);
    public native Mat addKeypointsToImage(Mat image, KeyPoint keypoints);


}
