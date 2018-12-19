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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostPictures extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    ImageView ImView;
    String mCurrentPhotoPath;
    ContentValues values;
    private Uri file;
    Bitmap help1;
    ThumbnailUtils thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_pictures);
    }

    public void takePicture(View view) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getFile());
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT,file);

        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
        }
    }

    //this method will create and return the path to the image file
    private File getFile() {
        File folder = Environment.getExternalStoragePublicDirectory("/From_camera/imagens");// the file path

        //if it doesn't exist the folder will be created
        if(!folder.exists())
        {folder.mkdir();}

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+ timeStamp + "_";
        File image_file = null;

        try {
            image_file = File.createTempFile(imageFileName,".jpg",folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentPhotoPath = image_file.getAbsolutePath();
        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE) {
            if (requestCode == Activity.RESULT_OK) {
                    try{
                        help1 = MediaStore.Images.Media.getBitmap(getContentResolver(),file);
                        ImView.setImageBitmap( thumbnail.extractThumbnail(help1,help1.getWidth(),help1.getHeight()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

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
