package com.couragedigital.peto.Connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.NullRespone_DialogeBox;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.GroomerListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetFetchGroomerList {
    private static final String TAG = PetFetchGroomerList.class.getSimpleName();
    private static Context context;

    public PetFetchGroomerList(View v) {
        context = v.getContext();
    }

    public static List petFetchGroomerList(final List<GroomerListItem> groomerList, final RecyclerView.Adapter adapter, String url, final ProgressDialog progressDialog) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetGroomerResponse");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        GroomerListItem groomerListItem = new GroomerListItem();
                                        groomerListItem.setGroomerName(obj.getString("name"));
                                        groomerListItem.setGroomerAdress(obj.getString("address"));
                                        groomerListItem.setContact(obj.getString("contact"));
                                        groomerListItem.setEmail(obj.getString("email"));
                                        groomerListItem.setGroomerImage_path(obj.getString("image"));
                                        groomerListItem.setCity(obj.getString("city"));
                                        groomerListItem.setArea(obj.getString("area"));
                                        // adding pet to pets array
                                        groomerList.add(groomerListItem);
                                        adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            Intent gotoNullError = new Intent(context, NullRespone_DialogeBox.class);
                            context.startActivity(gotoNullError);
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();
                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
                progressDialog.hide();
            }
        });
        /*petListReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        AppController.getInstance().addToRequestQueue(petListReq);
        return groomerList;
    }

}
