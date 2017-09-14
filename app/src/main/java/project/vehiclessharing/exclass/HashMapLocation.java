package project.vehiclessharing.exclass;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tuan on 05/09/2017.
 */

public class HashMapLocation {
    private HashMap<String,LatLng> latLngHashMap;

    public HashMapLocation(){
        latLngHashMap = new HashMap<>();//Hashmap store all the location inside map
    }

    public HashMap<String,LatLng> getLatLngHashMap(){
        return latLngHashMap;
    }

    public ArrayList<LatLng> getListLatlng(){
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        for(Map.Entry<String,LatLng> map : latLngHashMap.entrySet()){
            list.add(map.getValue());
        }
        return list;
    }

    public LatLng getLatlng(String uniqueKey){
        return this.latLngHashMap.get(uniqueKey);
    }

    public void addLatLngIntoHashMap(String uniqueKey, LatLng latLng){
        this.latLngHashMap.put(uniqueKey,latLng);
    }

    public void removeLatLngInHashMap(String uniqueKey){
        this.latLngHashMap.remove(uniqueKey);
    }
}
