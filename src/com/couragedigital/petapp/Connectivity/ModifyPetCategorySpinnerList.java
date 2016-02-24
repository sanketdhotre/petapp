package com.couragedigital.petapp.Connectivity;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.Adapter.ModifySpinnerItemsAdapter;
import com.couragedigital.petapp.Adapter.SpinnerItemsAdapter;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ModifyPetCategorySpinnerList {
    private static final String TAG = ModifyPetCategorySpinnerList.class.getSimpleName();
    private static final String url = URLInstance.getUrl()+"?method=showPetCategories&format=json";

    public static List fetchPetCategory(List petCategoryList, ModifySpinnerItemsAdapter adapter) {
        JsonObjectRequest petCategoryReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetCategoriesResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    petCategoryList.add(obj.optString("pet_category"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(petCategoryReq);
        return petCategoryList;
    }
}