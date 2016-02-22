package com.couragedigital.petapp.Connectivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FilterPetListDeleteMemCacheObject {

    public static String url = URLInstance.getUrl();

    public static void deletePetListFilterObject(String email) {

        url = url + "?method=deleteFilterPetListObject&format=json&email="+email;
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("deleteFilterPetListObjectResponse")== true) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(petListReq);
    }
}
