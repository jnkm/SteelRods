package com.example.jordan.steelrods;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
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
        byte[] byteArray = getIntent().getByteArrayExtra("image");

        mImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        displayImage();

    }

    private void setUpButtons() {
        mCountButton = (Button) findViewById(R.id.count_button);

        mCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runBlobDetection();
                displayImage();
            }
        });
    }

    /**
     * Calls the native method for blob detection. Will override the currently selected bitmap with
     * the blob-detected image.
     */
    private void runBlobDetection() {
        Mat m = new Mat();
        Utils.bitmapToMat(mImage, m);
        getBlobKeypoints(m.getNativeObjAddr());
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bmp);
        }
        catch (CvException e){}
        mImage = bmp;
    }

    private void displayImage() {
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setImageBitmap(mImage);
    }

    public native void getBlobKeypoints(long image);

}
