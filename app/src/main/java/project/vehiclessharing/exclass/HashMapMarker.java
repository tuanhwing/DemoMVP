package project.vehiclessharing.exclass;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Hashmap store all the marker inside map
 */

public class HashMapMarker {
    private HashMap<String,Marker> markerHashMap;

    public HashMapMarker(){
        markerHashMap = new HashMap<>();//Hashmap store all the marker inside map
    }

    public HashMap<String,Marker> getMarkerHashMap(){
        return markerHashMap;
    }

    public Marker getMarker(String uniqueKey){
        return this.markerHashMap.get(uniqueKey);
    }

    public void addMarkerIntoHashMap(String uniqueKey, Marker marker){
        this.markerHashMap.put(uniqueKey,marker);
    }

    public void removeMarkerInHashMap(String uniqueKey){
        this.markerHashMap.remove(uniqueKey);
    }

}
