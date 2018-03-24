package edu.nus.trailblazelearn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.PlaceAutoCompleteAdapter;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.utility.ApplicationConstants;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    private final static String TAG = ApplicationConstants.mapsActivity;
    // private final static String FINE_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    //private final static String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final static float DEFAULT_ZOOM = 15f;
    private LatLngBounds LAT_LNG_BOUNDS=new LatLngBounds(new LatLng(47.64299816, -122.14351988), new LatLng(47.64299816, -122.14351988));
    private GoogleMap mMap;
    private Integer placePickerRequest=1;
    private FloatingActionButton mPlacePicker;
    private ImageButton searchedLocation;
    private AutoCompleteTextView searchInput;
    PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleApiClient mGoogleApiClient;
    Context context;
    Address address;
    String mapAddress;
    private TextView getPlace;
    private Button btn_selection;
    int PLACE_PICKER_REQUEST,result = 1;
    Place mPlace;
    TrailStation trailStationPlace;
    private LatLngBounds locationBound, defaultLocationBound;
    private LatLng location,editLocation;
    private double editlatitude,editlongitude;
    String trailCode,editAddress;
    private boolean editMode;
    Integer stationSize, stationId;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        trailCode=(String)getIntent().getSerializableExtra(ApplicationConstants.trailCodeMap);
        stationSize=(Integer)getIntent().getSerializableExtra(ApplicationConstants.stationSize);
        editMode=(Boolean) getIntent().getSerializableExtra("editMode");
        stationId = (Integer) getIntent().getSerializableExtra("stationId");

        searchInput = (AutoCompleteTextView) findViewById(R.id.search_input);
        getPlace = (TextView) findViewById(R.id.getPlace);
        mPlacePicker =(FloatingActionButton)findViewById(R.id.ic_place_picker);
        btn_selection=(Button)findViewById(R.id.confirm_selection);
        searchedLocation = findViewById(R.id.ic_magnify);
        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder= new PlacePicker.IntentBuilder();
                if(locationBound == null) {
                    builder.setLatLngBounds(defaultLocationBound);
                }
                else {
                    builder.setLatLngBounds(locationBound);
                }
                Intent intent;
                try {
                    startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        initial();
       // public void onAttach(Activity activity){
           // this.activity = activity;
      //  }

        searchedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    geolocate();

            }
        });

        btn_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address == null) {
                   Toast.makeText(MapsActivity.this, "Please set a Location", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!editMode) {
                        Intent intent = new Intent(getApplicationContext(), CreateTrailStationActivity.class);
                        intent.putExtra(ApplicationConstants.stationLocation, location);
                        intent.putExtra(ApplicationConstants.address, address.getAddressLine(0));

                        intent.putExtra(ApplicationConstants.trailCodeMap, trailCode);
                        intent.putExtra(ApplicationConstants.stationSize, stationSize);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), UpdateTrailStationActivity.class);
                        intent.putExtra(ApplicationConstants.stationLocation, location);
                        intent.putExtra(ApplicationConstants.address, address.getAddressLine(0));
                        intent.putExtra("stationId",stationId);
                        intent.putExtra(ApplicationConstants.trailCodeMap, trailCode);
                        intent.putExtra(ApplicationConstants.stationSize, stationSize);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                mPlace = PlacePicker.getPlace(this,data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, mPlace.getId());
                placeResult.setResultCallback(updatePlaceDetaisCallBack);
                mapAddress = String.format("Place: %s" , mPlace.getAddress());

                if(!editMode) {
                    Intent intent = new Intent(getApplicationContext(), CreateTrailStationActivity.class);
                    intent.putExtra(ApplicationConstants.stationLocation, location);
                    intent.putExtra(ApplicationConstants.address, mapAddress);

                    intent.putExtra(ApplicationConstants.trailCodeMap, trailCode);
                    intent.putExtra(ApplicationConstants.stationSize, stationSize);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), UpdateTrailStationActivity.class);
                    intent.putExtra(ApplicationConstants.stationLocation, location);
                    intent.putExtra(ApplicationConstants.address, mapAddress);
                    intent.putExtra("stationId",stationId);
                    intent.putExtra(ApplicationConstants.trailCodeMap, trailCode);
                    intent.putExtra(ApplicationConstants.stationSize, stationSize);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    private void initial() {

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
        searchInput.setOnItemClickListener(adapterAutoClickListener);

        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);
        searchInput.setAdapter(placeAutoCompleteAdapter);

    }

    public void geolocate() {
            String searchlocation = searchInput.getText().toString();
            Geocoder geocoder = new Geocoder(MapsActivity.this);
            List<Address> list = new ArrayList();
            try {
                list = geocoder.getFromLocationName(searchlocation, 1);
            } catch (IOException e) {
                Log.e(TAG, "geolocate Exception" + e.getMessage());
            }
                if (list.size() > 0) {
                    address = list.set(0, list.get(0));
                    Log.d(TAG, "geolocation found" + address.toString());
                    locationBound = new LatLngBounds(new LatLng(address.getLatitude(), address.getLongitude()), new LatLng(address.getLatitude(), address.getLongitude()));
                } else {
                    locationBound = defaultLocationBound;
                }
                if(address != null) {

                    location = new LatLng(address.getLatitude(), address.getLongitude());

                    moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
                }
        }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * Added markers or lines, listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        if(editlatitude==0 && editlongitude==0) {
            defaultLocationBound = new LatLngBounds(new LatLng(1.3480323, 103.7725324), new LatLng(1.3480323, 103.7725324));
        }
        else {
            defaultLocationBound = new LatLngBounds(new LatLng(editlatitude, editlongitude), new LatLng(editlatitude, editlongitude));
            moveCamera(new LatLng(editlatitude,editlongitude),DEFAULT_ZOOM, editAddress);
        }
        initial();
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moving the camera to lat:" + latLng.latitude + ", lng" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(options);
    }


    private AdapterView.OnItemClickListener adapterAutoClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = placeAutoCompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(updatePlaceDetaisCallBack);
        }
    };

    private ResultCallback<PlaceBuffer> updatePlaceDetaisCallBack = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "Place query not succeded");
                places.release();
                return;
            }
            final Place place = places.get(0);
          //  trailStationPlace = new TrailStation();
           // trailStationPlace.setStationAddress(place.getAddress().toString());
            //trailStationPlace.setLatLng(place.getLatLng());
            //trailStationPlace.setLocationName(place.getName().toString());
            //Log.d(TAG, "place detail" + trailStationPlace);

            moveCamera(place.getLatLng(), DEFAULT_ZOOM, place.getName().toString());
            places.release();
        }
    };
}


