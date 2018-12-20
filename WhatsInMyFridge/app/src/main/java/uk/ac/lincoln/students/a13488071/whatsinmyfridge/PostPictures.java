package uk.ac.lincoln.students.a13488071.whatsinmyfridge;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostPictures extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    ImageView ImView;
    String mCurrentPhotoPath;
    ContentValues values;
    Bitmap help1;
    ThumbnailUtils thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_pictures);
    }

    public void takePicture(View view) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePicture.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = getFile();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "Error while saving picture.", Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "uk.ac.lincoln.students.a13488071.whatsinmyfridge.fileprovider", photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    //this method will create and return the path to the image file

    private File getFile() throws IOException {
        //Create an Image filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && requestCode == Activity.RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImView.setImageBitmap(imageBitmap);
            } else if (requestCode == RESULT_CANCELED) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);

//                super.onActivityResult(requestCode, resultCode, data);
//                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                    ImView.setImageBitmap(bitmap);
//                } else if (requestCode == RESULT_CANCELED) {
//                    Intent intent = new Intent(this, MainActivity.class);
//                    startActivity(intent);
//                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
//                }
            }
        }
    }
}
