package uk.ac.lincoln.students.a13488071.whatsinmyfridge;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
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

//adapted code from Android Studio website
//adapted code from workshop tasks in week 2

//creates variables
public class PostPictures extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView ImView;
    String mCurrentPhotoPath;
    ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_pictures);
        ImView = (ImageView)findViewById(R.id.imageView); //finds image view
    }

    //takes the picture
    public void takePicture(View view) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePicture.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = getFile();
            } catch (IOException ex) { //error exception
                Toast.makeText(getApplicationContext(), "Error while saving picture.", Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) { //save image
                Uri photoURI = FileProvider.getUriForFile(this, "uk.ac.lincoln.students.a13488071.whatsinmyfridge.fileprovider", photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            try {
                Bitmap ImageBit = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(mCurrentPhotoPath));
                ImView.setImageBitmap(ImageBit);
                Toast.makeText(this, "Picture was saved!", Toast.LENGTH_SHORT).show();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } else if (requestCode == RESULT_CANCELED) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
        }
    }

    //this method will create and return the path to the image file
    //saving the picture
    private File getFile() throws IOException {
        //Create an Image filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}