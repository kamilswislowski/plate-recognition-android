package pl.swislowski.kamil.project.platerecognition.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import pl.swislowski.kamil.project.platerecognition.android.constants.Constants;
import pl.swislowski.kamil.project.platerecognition.android.service.rrpa.RRPAsyncTask;
import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;

import static pl.swislowski.kamil.project.platerecognition.android.constants.Constants.REQUEST_TAKE_PHOTO;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String currentPhotoPath;
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

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
//        }
//    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "pl.swislowski.kamil.project.platerecognition.android",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "On activity result");

//        RecognitionRegistrationPlateResultService recognitionRegistrationPlateResultService =
//                new RecognitionRegistrationPlateResultService();

        if (requestCode == Constants.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            RegistrationPlateModel registrationPlateModel =
//                    recognitionRegistrationPlateResultService.cameraResult(imageBitmap);

            try {
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get(MediaStore.EXTRA_OUTPUT);

//                ByteArrayOutputStream bos = null;
//                ByteArrayInputStream bs = null;
//
//                bos = new ByteArrayOutputStream();
//                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 98, bos);
//                byte[] bitmapdata = bos.toByteArray();
//                bs = new ByteArrayInputStream(bitmapdata);

//                ByteArrayOutputStream baos = new ByteArrayOutputStream();

//                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                Log.i(TAG, "#### imageBitmap height: " + imageBitmap.getHeight());
//                Log.i(TAG, "#### imageBitmap width: " + imageBitmap.getWidth());
//                byte[] buf = baos.toByteArray();
//                InputStream is = new ByteArrayInputStream(buf);

//                File sdCard = Environment.getExternalStorageDirectory();
//                Log.i(TAG, "#### sdCard: " + sdCard);
//                File dir = new File (sdCard.getAbsolutePath() + "/Download");
//                File dir = new File(sdCard.getAbsolutePath() + "/Android/data");
//                Log.i(TAG, "#### dir: " + dir);
//                boolean mkdirs = dir.mkdirs();
//                File file = new File(dir, "/filename.jpg");

//                FileOutputStream f = new FileOutputStream(file);
//                f.write(buf);
//                Files.write(Paths.get("image.jpg"), buf);

//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                String imageFileName = "_ANDROID_" + timeStamp;

//                Resources resources = getResources();
//                InputStream inputStream = resources.openRawResource(R.raw.fiat_tablice);

//                InputStream is = new FileInputStream(currentPhotoPath);

                RRPAsyncTask asyncTask = new RRPAsyncTask();
                AsyncTask<InputStream, Void, RegistrationPlateModel> execute =
                        asyncTask.execute(new FileInputStream(currentPhotoPath));
                RegistrationPlateModel registrationPlateModel = execute.get();
                Log.i(TAG, "##### RegistrationPlateModel : " + registrationPlateModel);

                if (registrationPlateModel != null) {
                    String registrationNumber = registrationPlateModel.getRegistrationNumber();
                    recognizedNumberTextview.setText(registrationNumber);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "##### AFTER execute AsyncTask");


        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
