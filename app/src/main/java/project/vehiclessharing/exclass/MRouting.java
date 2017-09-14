package project.vehiclessharing.exclass;

import android.util.Log;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import project.vehiclessharing.callback.MainResult;

/**
 * Created by Tuan on 05/09/2017.
 */

public class MRouting implements RoutingListener {

    private MainResult mainResult;
    private String TAG_ROUTING = "tag_routing";

    public MRouting(){

    }

    public MRouting(MainResult mainResult){
        this.mainResult = mainResult;
    }

    public ArrayList<LatLng> convertListWayPoints(LatLng start, LatLng destination, ArrayList<LatLng> waypoints){
        ArrayList<LatLng> result = new ArrayList<LatLng>();
        result.add(start);
        if(waypoints != null )
            for (LatLng latLng : waypoints)
                result.add(latLng);
        result.add(destination);
        return result;
    }

    public void routingAmongLocations(LatLng start, LatLng destination, ArrayList<LatLng> waypoints){
        ArrayList<LatLng> paramaters = convertListWayPoints(start, destination, waypoints);
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(paramaters)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        mainResult.routingFailure(e);
    }

    @Override
    public void onRoutingStart() {
        Log.d(TAG_ROUTING,"routing start");
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        mainResult.routingSuccess(arrayList);
    }

    @Override
    public void onRoutingCancelled() {
        Log.e(TAG_ROUTING,"routing cancelled");
    }
}
