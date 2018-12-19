package uk.ac.lincoln.students.a13488071.whatsinmyfridge;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadIngredients(View view) {

        Intent intent = new Intent(this, FridgeIngredients.class);
        startActivity(intent);
    }
    public void loadRecipe(View view) {

        Intent intent = new Intent(this, SavedRecipes.class);
        startActivity(intent);
    }

    public void loadPictures(View view) {

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{permissions[0], permissions[1], permissions[2]}, 1);

    }

    // handles the users permission options
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the work!
                    // display short notification stating permission granted
                    Toast.makeText(MainActivity.this, "Permission granted for camera!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PostPictures.class);
                    startActivity(intent);

                } else {

                    AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                    a.setMessage("Camera Permission Denied! To Proceed Please Grant Permission For The Camera...").setCancelable(true);
                    AlertDialog ab = a.create();
                    ab.show();
                }
                return;
            }
        }
    }
}
