package com.couragedigital.petapp.Connectivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.Adapter.FilterCategoryPetMateAdapter;
import com.couragedigital.petapp.Adapter.SpinnerItemsAdapter;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.FilterCategoryList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FilterFetchPetMateCategoryList {

    private static final String url = URLInstance.getUrl();

    public static List fetchPetMateCategory(List petCategoryList, FilterCategoryPetMateAdapter adapter) {
        String urlToFetch = url + "?method=showPetCategories&format=json";
        JsonObjectRequest petCategoryReq = new JsonObjectRequest(Request.Method.GET, urlToFetch, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetCategoriesResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    FilterCategoryList filterCategoryList = new FilterCategoryList();
                                    filterCategoryList.setCategoryText(obj.optString("pet_category"));

                                    petCategoryList.add(filterCategoryList);

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
                VolleyLog.d("Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(petCategoryReq);
        return petCategoryList;
    }
}
