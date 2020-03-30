package pl.swislowski.kamil.project.platerecognition.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import pl.swislowski.kamil.project.platerecognition.android.constants.Constants;
import pl.swislowski.kamil.project.platerecognition.android.service.RecognitionRegistrationPlateResultService;
import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView recognizedNumberTextview;
    private TextView voivodeshipTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recognizedNumberTextview = findViewById(R.id.recognized_numbers_textview);
        voivodeshipTextView = findViewById(R.id.voivodeshipTextView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Log.i(TAG, "MainActivity on click.");
                boolean hasCamera = checkCameraHardware(getApplicationContext());

                if (hasCamera) {
                    dispatchTakePictureIntent();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "On activity result");

        RecognitionRegistrationPlateResultService recognitionRegistrationPlateResultService =
                new RecognitionRegistrationPlateResultService();

        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            RegistrationPlateModel registrationPlateModel =
                    recognitionRegistrationPlateResultService.cameraResult(imageBitmap);
            Log.i(TAG, "#####RegistrationPlateModel : " + registrationPlateModel);

            String registrationNumber = registrationPlateModel.getRegistrationNumber();

            recognizedNumberTextview.setText(registrationNumber);
        }
    }
}
