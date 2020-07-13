package pl.swislowski.kamil.project.platerecognition.android.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pl.swislowski.kamil.project.platerecognition.android.MainActivity;
import pl.swislowski.kamil.project.platerecognition.android.R;

public class MapFragment extends Fragment implements MainActivity.ActivityActionPerformerListener {

    private static final String TAG = "MapFragment";

    private MapView mMapView;
    private GoogleMap googleMap;

    private String currentLocation = "Gostynin";

    private ActionPerformerListener actionPerformerListener;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void actionPerform(String string) {
        Log.i(TAG, "Set location for : " + string);
        currentLocation = string;

        Geocoder geo = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geo.getFromLocationName(currentLocation, 1);
            Log.i(TAG, "##### Addresses : " + addresses);

            Address address = addresses.get(0);
            if (address != null) {
                LatLng currentLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        googleMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {

            }

            @Override
            public void deactivate() {

            }
        });
    }

    public interface ActionPerformerListener {
        public void action(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "###### Context : " + context);
        if (context instanceof ActionPerformerListener) {
            actionPerformerListener = (ActionPerformerListener) context;
        }
    }

    public void fragmentAction() {
        actionPerformerListener.action("######## Action Performed Text");
    }

    @Override
    public void onDetach() {
        actionPerformerListener = null;
        super.onDetach();
    }

    public void onClick() {
        Log.i(TAG, "############# onClick method");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Button viewByIdButton = (Button) rootView.findViewById(R.id.button);
//        viewByIdButton.setOnClickListener((event) -> {
//            Log.i(TAG, "Button clicked " + event);
//            actionPerformerListener.action("#### TEST TEXT ####");
//        });

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                // For showing a move to my location button
//                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
//                LatLng sydney = new LatLng(-34, 151);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                LatLng sydney = new LatLng(52.4293273, 19.4619176);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));

                Geocoder geo = new Geocoder(requireContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geo.getFromLocationName(currentLocation, 1);
                    Log.i(TAG, "##### Addresses : " + addresses);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
