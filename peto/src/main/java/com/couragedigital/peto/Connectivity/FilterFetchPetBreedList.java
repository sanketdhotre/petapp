package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.Adapter.FilterBreedAdapter;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.FilterBreedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class FilterFetchPetBreedList {
    private static final String TAG = PetBreedsSpinnerList.class.getSimpleName();
    private static List<String> filterSelectedCategoryLists;
    private static String method;
    private static String format;
    private static Context context;

    public FilterFetchPetBreedList(View v) {
        this.context = v.getContext();
    }


    //http://storage.couragedigital.com/dev/api/petappapi.php
    public static void fetchPetBreeds(List<String> filterCategoryLists, final List<FilterBreedList> filterBreedLists, final FilterBreedAdapter filterBreedAdapter) {
        String url = URLInstance.getUrl();
        filterSelectedCategoryLists = filterCategoryLists;
        method = "filterCategoryWiseBreed";
        format = "json";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", method);
        params.put("format", format);
        JSONArray arrayOfSelectedFilterCategories = new JSONArray(filterSelectedCategoryLists);
        params.put("filterSelectedCategories", arrayOfSelectedFilterCategories.toString());

        JsonObjectRequest filterFetchBreedRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("filterBreedsCategoryWise");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        FilterBreedList filterBreedList = new FilterBreedList();
                                        filterBreedList.setBreedText(obj.getString("pet_breed"));
                                        // adding pet to pets array
                                        filterBreedLists.add(filterBreedList);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                filterBreedAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        filterFetchBreedRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(filterFetchBreedRequest);
    }
}
