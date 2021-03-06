package project.vehiclessharing.view;

import android.widget.TextView;

import com.directions.route.Route;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by Tuan on 04/08/2017.
 */

public interface MainView {

    void signOutSuccess();

    void setUIHeader(FirebaseUser user);

    void requestLocationPermission();//Request location permission when unavailable

    void updateUI();//Update UI when locaiton permission is available

    void setCurrentLocation(boolean value);

    void moveCameraToLocation(ArrayList<LatLng> arrayList);

    void findPlaceAutocomplete(int valueRequest);//show find PlaceAutoComplete

    void updateUIAfterfindPlace(TextView textView, Place place);//Update UI after user choices place success.

    void addMarker(LatLng latLng,int idTextView);//add Marker source or destination reply on id TextView

    Marker addMarkerIntoMap(LatLng latLng, String color, int drawer);//Add marker into map

    void updateUIRoutingSuccess(ArrayList<Route> arrayList);//Update UI when having list location for direction.


}
