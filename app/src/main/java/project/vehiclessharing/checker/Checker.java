package project.vehiclessharing.checker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import project.vehiclessharing.util.Utils;

/**
 * Created by Tuan on 09/08/2017.
 */

public class Checker {
    private Activity activity;

    public Checker(){

    }
    public Checker(Activity activity){
        this.activity = activity;
    }

    /**
     * request location permission
     * @return true - can
     */
    public void requestLocationPermission() {

        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {


                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        Utils.REQ_PERMISSION_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        Utils.REQ_PERMISSION_LOCATION);
            }
        }
    }

    /**
     * Check permission to using location for setMyLocationEnable (Point blue in google map)
     * @return true - can
     */
    public boolean checkAccessLocationPermission() {
        if (ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

}
