package project.vehiclessharing.callback;

import com.directions.route.Route;
import com.directions.route.RouteException;

import java.util.ArrayList;

/**
 * Created by Tuan on 04/08/2017.
 */

public interface MainResult {
    public void signOutSuccess();

    public void routingSuccess(ArrayList<Route> arrayList);

    public void routingFailure(RouteException e);
}
