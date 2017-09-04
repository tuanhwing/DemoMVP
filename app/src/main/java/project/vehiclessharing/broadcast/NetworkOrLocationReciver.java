package project.vehiclessharing.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import project.vehiclessharing.presenter.MainPresenter;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Tuan on 09/08/2017.
 */

public class NetworkOrLocationReciver extends BroadcastReceiver{

    private MainPresenter mainPresenter;
    private Activity activity;

    public NetworkOrLocationReciver(){

    }

    public NetworkOrLocationReciver(MainPresenter mainPresenter, Activity activity){
        this.mainPresenter = mainPresenter;
        this.activity = activity;
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(!(lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) && !isAvailableInternet()) {
            mainPresenter.setMyLocationEnable(false);
            Toast.makeText(getApplicationContext(), "Both Internet and Location unavailale!", Toast.LENGTH_SHORT).show();
        } else if(!isAvailableInternet() && (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            Toast.makeText(getApplicationContext(), "Internet unavailale!", Toast.LENGTH_SHORT).show();
        } else if(!(lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) && isAvailableInternet()) {
            mainPresenter.setMyLocationEnable(false);
            Toast.makeText(getApplicationContext(), "Location unavailale!", Toast.LENGTH_SHORT).show();
        } else if ((lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) && isAvailableInternet()){
            mainPresenter.setMyLocationEnable(true);
            mainPresenter.checkPermission();
            Log.d("LOLLLLL","broadcasr");
        }

    }

    private boolean isAvailableInternet(){
        ConnectivityManager cm =
                (ConnectivityManager) this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
