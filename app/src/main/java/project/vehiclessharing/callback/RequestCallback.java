package project.vehiclessharing.callback;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Tuan on 07/09/2017.
 */

public interface RequestCallback {
    void onSuccess(JSONObject result);
    void onError(VolleyError error);
}
