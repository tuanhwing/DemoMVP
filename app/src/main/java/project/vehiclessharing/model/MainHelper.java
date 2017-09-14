package project.vehiclessharing.model;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import project.vehiclessharing.callback.MainResult;
import project.vehiclessharing.checker.Checker;
import project.vehiclessharing.exclass.MRouting;
import project.vehiclessharing.util.Utils;

/**
 * Created by Tuan on 04/08/2017.
 */

public class MainHelper {

    private MainResult mainResult;
    private Activity activity;
    private FirebaseAuth firebaseAuth;

    public MainHelper(){

    }

    public MainHelper(Activity activity){
        this.activity = activity;
    }

    /**
     * User signed by account Facebook/Google
     * @param user Firebase user
     * @return
     */
    public int signInBy(FirebaseUser user){
        int result = 0;
        for (UserInfo temp : user.getProviderData()) {
            if(temp.getProviderId().equals(Utils.StringGoogleSignIn))//Google
                result = 1;
            else if(temp.getProviderId().equals(Utils.StringFacebookSignIn))//Facebook
                result = 2;
        }
        return result;
    }

    public void setOnMainResult(MainResult mainResult) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.mainResult = mainResult;
    }

    /**
     * Sign out Google+
     * @param googleApiClient
     */
    public void signOutGoogle(GoogleApiClient googleApiClient){
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        mainResult.signOutSuccess();
                    }
                });
    }

    /**
     * Sign out Facebook
     */
    public void signOutFacebook(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        mainResult.signOutSuccess();
    }

    public boolean checkLocationPermission(){
        Checker checker = new Checker(activity);
        return checker.checkAccessLocationPermission();
    }

    /**
     * Get address from location
     * @param latitude
     * @param longtitude
     * @return
     */
    public List<Address> getAddressFromLocation(double latitude,double longtitude){
        List<Address> addresses  = null;
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude,longtitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    /**
     * Routing two location
     * @param latLng1
     * @param latLng2
     * @param waypoints
     */
    public void routeAmongLocations(LatLng latLng1, LatLng latLng2, ArrayList<LatLng> waypoints){
        MRouting mRouting = new MRouting(mainResult);
        mRouting.routingAmongLocations(latLng1,latLng2,waypoints);
    }

}
