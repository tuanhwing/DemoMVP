package project.vehiclessharing.presenter;

import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import project.vehiclessharing.callback.MainResult;
import project.vehiclessharing.model.MainHelper;
import project.vehiclessharing.view.MainView;

/**
 * Created by Tuan on 04/08/2017.
 */

public class MainPresenter implements MainResult {
    private MainView view;
    private MainHelper model;

    public MainPresenter(MainView view, MainHelper model){
        this.view = view;
        this.model = model;
        this.model.setOnMainResult(this);
    }

    /**
     * User signed by account Facebook/Google
     * @param user
     * @return
     */
    public int signInBy(FirebaseUser user){
        return model.signInBy(user);
    }

    /**
     * Update UI header navigation drawer
     * @param user
     */
    public void setContentUIHeader(FirebaseUser user){
        view.setUIHeader(user);
    }

    /**
     * Sign out with account Google
     * @param googleApiClient
     */
    public void signOutGoogle(GoogleApiClient googleApiClient){
        model.signOutGoogle(googleApiClient);
    }

    public void checkPermission(){
        if(!model.checkLocationPermission())
            view.requestLocationPermission();
        else {
            view.updateUI();
        }
    }

    public void setMyLocationEnable(boolean value){
        view.setCurrentLocation(value);
    }

    public void moveCameraToLocation(ArrayList<LatLng> arrayList){
        view.moveCameraToLocation(arrayList);
    }

    /**
     * Called when user click textview place
     * @param value
     */
    public void findPlace(int value){
        view.findPlaceAutocomplete(value);
    }

    /**
     * Called when user pick place success
     * @param textView
     * @param place
     */
    public void updateAfterChoicePlace(TextView textView, Place place){
        view.updateUIAfterfindPlace(textView,place);
    }

    /**
     * Add source or destination location
     * @param latLng
     * @param idTextView
     */
    public void addMarker(LatLng latLng,int idTextView){
        view.addMarker(latLng, idTextView);
    }

    /**
     * Sign out with account Facebook
     */
    public void signOutFacebook(){
        model.signOutFacebook();
    }

    @Override
    public void signOutSuccess() {
        view.signOutSuccess();
    }

}
