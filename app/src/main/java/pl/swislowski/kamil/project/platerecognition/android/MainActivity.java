package pl.swislowski.kamil.project.platerecognition.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import pl.swislowski.kamil.project.platerecognition.android.constants.Constants;
import pl.swislowski.kamil.project.platerecognition.android.fragment.MapFragment;
import pl.swislowski.kamil.project.platerecognition.android.service.rrpa.RRPAsyncTask;
import pl.swislowski.kamil.project.platerecognition.spring.web.model.RegistrationPlateModel;

import static pl.swislowski.kamil.project.platerecognition.android.constants.Constants.REQUEST_TAKE_PHOTO;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private String currentPhotoPath;
    private TextView descriptionTextView;
    private TextView mapTextView;
    private GoogleMap mMap;

    private ActivityActionPerformerListener listener;

    public interface ActivityActionPerformerListener{
        void actionPerform(String string);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        descriptionTextView = findViewById(R.id.descriptionTextView);
        mapTextView = findViewById(R.id.mapTextView);

        getSupportFragmentManager().beginTransaction()
//                    .add(R.id.descriptionFragment, DescriptionFragment.newInstance())
                .add(R.id.mapFragment, MapFragment.newInstance())
                .commitNow();

        MapFragment mapFragment =(MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        Log.i(TAG, "########### #### mapFragment : " + mapFragment);
        listener = mapFragment;



        FloatingActionButton fab = findViewById(R.id.fab);
//        Intent mapIntent = new Intent(this, MapsActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Log.i(TAG, "MainActivity on click.");
//                startActivity(mapIntent);

                boolean hasCamera = checkCameraHardware(getApplicationContext());

                if (hasCamera) {
                    dispatchTakePictureIntent();
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapFragment);


//        mapFragment.getMapAsync(this);

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

        if (requestCode == Constants.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            try {
                RRPAsyncTask asyncTask = new RRPAsyncTask();
                AsyncTask<InputStream, Void, RegistrationPlateModel> execute =
                        asyncTask.execute(new FileInputStream(currentPhotoPath));
                RegistrationPlateModel registrationPlateModel = execute.get();
                Log.i(TAG, "##### RegistrationPlateModel : " + registrationPlateModel);

                if (registrationPlateModel != null) {
                    String registrationNumber = registrationPlateModel.getRegistrationNumber();
                    mapTextView.setText(registrationNumber);

                    String locationLabel = registrationPlateModel.getLocationLabel();
                    listener.actionPerform(locationLabel);

//                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                            .findFragmentById(R.id.mapFragment);
//                    Log.i(TAG, "####### mapFragment : " + mapFragment);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i(TAG, "####### onMapReady ");
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(52.4293273, 19.4619176);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
//        mMap.setLatLngBoundsForCameraTarget(new LatLngBounds());
        Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geo.getFromLocationName("Województwo mazowieckie\n" +
                    "powiat gostyniński (Gostynin)", 1);
            Log.i(TAG, "##### Addresses : " + addresses);
        } catch (IOException e) {
            e.printStackTrace();
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
