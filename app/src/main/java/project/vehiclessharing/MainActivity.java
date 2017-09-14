package project.vehiclessharing;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.Route;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.vehiclessharing.broadcast.NetworkOrLocationReciver;
import project.vehiclessharing.exclass.HashMapLocation;
import project.vehiclessharing.exclass.HashMapMarker;
import project.vehiclessharing.exclass.ImageClass;
import project.vehiclessharing.model.MainHelper;
import project.vehiclessharing.presenter.MainPresenter;
import project.vehiclessharing.util.Utils;
import project.vehiclessharing.view.MainView;

import static project.vehiclessharing.R.id.map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainView,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        LocationListener, View.OnClickListener {

    public static boolean firstTime = true;//Move and add market the first time
    private int signinBy;//value determine account is Google/Facebook
    private FirebaseUser currentUser;//Current user

    //MVP
    private MainPresenter mainPresenter;
    private MainHelper mainHelper;

    private GoogleApiClient singinGoogleApiClient;//sign in google API client
    private GoogleApiClient locationGoogleApiClient;//location google API client
    private Location currentLocation;
    private LatLng sourceLatlng;
    private LatLng destinationLatlng;

    //[START] View header
    private View viewHeader = null; // View header
    private TextView tvFullName;
    private TextView tvEmail;
    private ImageView imgUser;
    //[END] View header

    FloatingActionMenu fabMenu;

    //[START]Information request
    com.github.clans.fab.FloatingActionButton fabShowLayoutCreateRequest;
    private RelativeLayout inforLayoutCreateRequest;
    private TextView tvDismissCreateRequest;
    private TextView tvSourceLocation;
    private TextView tvDestinationLocation;
    //[END]Information request

    private NavigationView navigationView = null;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private NetworkOrLocationReciver networkOrLocationReciver;//Listener state network or location service

    private HashMapMarker hashMapMarker;//Hashmap store all markers inside map
    private HashMapLocation hashMapLocation;//Hashmap store all loations inside map
    private ArrayList<Route> arrCurrentUserRoute;//array list points of routing
    private Polyline polyline;

    //temp
    RelativeLayout relativeLayouttemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//DO NOT ROTATE the screen even if the user is shaking his phone like mad
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
        }
        if (mainPresenter == null) {
            if (mainHelper == null) {
                mainHelper = new MainHelper(this);
            }
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            mainPresenter = new MainPresenter(this, mainHelper);
            signinBy = mainPresenter.signInBy(currentUser);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        configGoogleApiClient();
        addControls();
        addEvents();
        setContentUIHeader();

//        inforLayout = (RelativeLayout) findViewById(R.id.layout_inforequest);
//        inforLayout.bringToFront();


        //temp

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        relativeLayouttemp = (RelativeLayout) findViewById(R.id.layout_request);
//        final TextView imgbtn = (TextView) findViewById(R.id.btn_back_relayout);
//        relativeLayouttemp.bringToFront();
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                relativeLayouttemp.setVisibility(View.VISIBLE);
//            }
//        });
//
//        imgbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                relativeLayouttemp.setVisibility(View.INVISIBLE);
//            }
//        });

        getPhoneInfo();
//        HashMap<String,String> params = new HashMap<>();
//        Log.d("Token_signin1",String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)));
//        Log.d("Token_signin2",String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getIdToken(false)));
////        params.put("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());
//        params.put("text","demo");
//        MakeRequest.makingRequest(Utils.STRING_URL_API + "addMessage?text={text}", Request.Method.GET, params, new RequestCallback() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                Toast.makeText(getApplicationContext(), "addUser success", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "addUser error", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void getPhoneInfo() {
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
            Log.e("request_phonenumber", "PHONE NUMBER = " + tMgr.getLine1Number());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}, Utils.REQ_PERMISSION_READ_SMS);
        }
    }

    private void addEvents() {
        navigationView.setNavigationItemSelectedListener(this);
        mapFragment.getMapAsync(this);

        //[START] Broadcast
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.location.PROVIDERS_CHANGED");
        registerReceiver(networkOrLocationReciver, filter);
        //[END] Broadcast

        //[START]Information request
        fabShowLayoutCreateRequest.setOnClickListener(this);
        tvDismissCreateRequest.setOnClickListener(this);
        tvSourceLocation.setOnClickListener(this);
        tvDestinationLocation.setOnClickListener(this);
        //[END]Information request
    }

    private void addControls() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);

        //[START] View header
        viewHeader = navigationView.getHeaderView(0);
        tvFullName = (TextView) viewHeader.findViewById(R.id.tv_fullname);
        tvEmail = (TextView) viewHeader.findViewById(R.id.tv_email);
        imgUser = (ImageView) viewHeader.findViewById(R.id.img_user);
        //[END] View header

        //[START]Information request
        fabShowLayoutCreateRequest = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_item_1);
        inforLayoutCreateRequest = (RelativeLayout) findViewById(R.id.layout_create_request);
        inforLayoutCreateRequest.bringToFront();
        tvDismissCreateRequest = (TextView) findViewById(R.id.tv_dismiss_create_request);
        tvSourceLocation = (TextView) findViewById(R.id.tv_source);
        tvDestinationLocation = (TextView) findViewById(R.id.tv_destination);
        //[END]Information request

        //Map
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

        //Broadcast
        networkOrLocationReciver = new NetworkOrLocationReciver(mainPresenter,this);

        //Hashmap store all markers and locations
        hashMapMarker = new HashMapMarker();
        hashMapLocation = new HashMapLocation();
    }

    /**
     * Configure google API client
     */
    private void configGoogleApiClient(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        singinGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    /**
     * Build googleAPI Client Location
     */
    public synchronized void buildLocationGoogleApiClient() {
        try {
            if(locationGoogleApiClient == null) {
                locationGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .build();
            }
            if(!locationGoogleApiClient.isConnected()) {
                locationGoogleApiClient.connect();
            }
        } catch (Exception e){
            Log.e("build_eroor",String.valueOf(e.getMessage()));
        }
    }

    private void setContentUIHeader(){
        mainPresenter.setContentUIHeader(currentUser);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_signout) {
            switch (signinBy){
                case 1: {
                    mainPresenter.signOutGoogle(singinGoogleApiClient);
                    break;
                }
                case 2: {
                    mainPresenter.signOutFacebook();
                    break;
                }
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void signOutSuccess() {
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
        finish();
    }

    @Override
    public void setUIHeader(FirebaseUser user) {
        tvFullName.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());
        Picasso.with(this)
                .load(user.getPhotoUrl())
                .into(imgUser);
    }

    @Override
    public void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {


                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        Utils.REQ_PERMISSION_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        Utils.REQ_PERMISSION_LOCATION);
            }
        }
    }

    @Override
    public void updateUI() {
        buildLocationGoogleApiClient();
        Toast.makeText(this, "Location permission available", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCurrentLocation(boolean value) {
        googleMap.setMyLocationEnabled(value);
    }

    @Override
    public void moveCameraToLocation(ArrayList<LatLng> arrayList) {
        if(arrayList.size() <= 0) return;
        else {
            if(arrayList.size() == 1)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(arrayList.get(0).latitude,arrayList.get(0).longitude),15));
            else {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                //the include method will calculate the min and max bound.
                for (LatLng latLng : arrayList)
                    builder.include(latLng);
                LatLngBounds bounds = builder.build();

                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                googleMap.animateCamera(cu);

                //Temp Routing
                if(arrayList.size() >= 3){
                    mainPresenter.routeAmongLocations(hashMapLocation.getLatlng(Utils.STRING_KEY_START_CURRENT_USER),
                        hashMapLocation.getLatlng(Utils.STRING_KEY_DESTINATION_CURRENT_USER), null);
                }

            }
        }
    }

    @Override
    public void findPlaceAutocomplete(int valueRequest) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, valueRequest);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void updateUIAfterfindPlace(TextView textView, Place place) {
        textView.setText(place.getAddress());
        mainPresenter.addMarker(place.getLatLng(),textView.getId());
        Log.d("LOLLLL",String.valueOf(place.getName() + " - " + place.getLatLng()) );
    }

    @Override
    public void addMarker(LatLng latLng,int idTextView) {
        switch (idTextView){
            case R.id.tv_source: {
                hashMapLocation.addLatLngIntoHashMap(Utils.STRING_KEY_START_CURRENT_USER,latLng);
                if(hashMapMarker.getMarker(Utils.STRING_KEY_START_CURRENT_USER) == null)
                    hashMapMarker.addMarkerIntoHashMap(Utils.STRING_KEY_START_CURRENT_USER,
                            mainPresenter.addMarkerIntoMap(latLng,"#056087",R.drawable.ic_marker_current));
                else
                    hashMapMarker.getMarker(Utils.STRING_KEY_START_CURRENT_USER).setPosition(latLng);
                break;
            }
            case R.id.tv_destination: {
                hashMapLocation.addLatLngIntoHashMap(Utils.STRING_KEY_DESTINATION_CURRENT_USER,latLng);
                if(hashMapMarker.getMarker(Utils.STRING_KEY_DESTINATION_CURRENT_USER) == null)
                    hashMapMarker.addMarkerIntoHashMap(Utils.STRING_KEY_DESTINATION_CURRENT_USER,
                            mainPresenter.addMarkerIntoMap(latLng,"#FE2E2E",R.drawable.ic_marker_current));
                else
                    hashMapMarker.getMarker(Utils.STRING_KEY_DESTINATION_CURRENT_USER).setPosition(latLng);
                break;
            }
        }
        mainPresenter.moveCameraToLocation(hashMapLocation.getListLatlng());
    }

    @Override
    public Marker addMarkerIntoMap(LatLng latLng, String color, int drawer) {
        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .icon(ImageClass.vectorToBitmap(drawer, Color.parseColor(color),getResources()))
        );
    }

    @Override
    public void updateUIRoutingSuccess(ArrayList<Route> arrayList) {
        try {
            arrCurrentUserRoute = arrayList;
            if (polyline != null) polyline.remove();
            PolylineOptions polyoptions = new PolylineOptions();
            polyoptions.color(Color.BLUE);
            polyoptions.width(10);
            for (int i = 0; i < arrayList.size(); i++) {
                polyoptions.addAll(arrayList.get(i).getPoints());
                polyline = googleMap.addPolyline(polyoptions);
            }
        } catch (Exception e){
            Log.d("routing_error",String.valueOf(e.getMessage()));
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("error_connect_google",String.valueOf(connectionResult.toString()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Utils.REQ_PERMISSION_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    buildLocationGoogleApiClient();
                    Toast.makeText(this, "permission was granted", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case Utils.REQ_PERMISSION_READ_SMS: {
                getPhoneInfo();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkOrLocationReciver);
        locationGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(5000); //5 seconds
            locationRequest.setFastestInterval(3000); //3 seconds
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    locationGoogleApiClient, locationRequest, this);
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(locationGoogleApiClient);
        } catch (Exception e){
            Log.d("location_connected", e.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("service_suspended", String.valueOf(i));
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        if(firstTime){
            firstTime = false;
                fabMenu.setVisibility(View.VISIBLE);
            mainPresenter.setMyLocationEnable(true);
            hashMapLocation.addLatLngIntoHashMap(Utils.STRING_KEY_CURRENTLOCATION_CURRNET_USER,
                    new LatLng(location.getLatitude(), location.getLongitude()));
            mainPresenter.moveCameraToLocation(hashMapLocation.getListLatlng());
        }
        Log.d("location_changed",String.valueOf(location));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Utils.REQ_RESULT_SOURCE_PLACE: {
                if (resultCode == RESULT_OK) {
                    // retrive the data by using getPlace() method.
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());

                    mainPresenter.updateAfterChoicePlace(tvSourceLocation,place);

                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Log.e("Tag", status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
            }
            case Utils.REQ_RESULT_DESTINATION_PLACE:{
                if (resultCode == RESULT_OK) {
                    // retrive the data by using getPlace() method.
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());

                    mainPresenter.updateAfterChoicePlace(tvDestinationLocation,place);

                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Log.e("Tag", status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_source: {
                mainPresenter.findPlace(Utils.REQ_RESULT_SOURCE_PLACE);
                break;
            }
            case R.id.tv_destination: {
                mainPresenter.findPlace(Utils.REQ_RESULT_DESTINATION_PLACE);
                break;
            }
            case R.id.tv_dismiss_create_request:{
                inforLayoutCreateRequest.setVisibility(View.INVISIBLE);
                break;
            }
            case R.id.fab_menu_item_1: {
                if(inforLayoutCreateRequest.getVisibility() == View.VISIBLE)
                    inforLayoutCreateRequest.setVisibility(View.INVISIBLE);
                else
                    inforLayoutCreateRequest.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

}
