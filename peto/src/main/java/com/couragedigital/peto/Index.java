package com.couragedigital.peto;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.couragedigital.peto.Connectivity.UserAllDetails;
import com.couragedigital.peto.DialogBox.NotifyNetworkConnectionMessage;
import com.couragedigital.peto.Adapter.HomeListAdapter;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.Services.MyFirebaseInstanceIDService;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.model.DialogListInformaion;
import com.couragedigital.peto.model.IndexListInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Index extends BaseActivity {

    public ArrayList<IndexListInfo> indexListInfosArray;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HomeListAdapter mAdapter;

    private static Index instance = new Index();
    static Context context;
    String email;

    public List<DialogListInformaion> dialogListForViewPets = new ArrayList<DialogListInformaion>();
    public List<DialogListInformaion> dialogListForViewPetMets = new ArrayList<DialogListInformaion>();
    public List<DialogListInformaion> dialogListForpetClinic = new ArrayList<DialogListInformaion>();
    public CoordinatorLayout homeListCoordinatorLayout;

    public static Index getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        SessionManager sessionManager = new SessionManager(Index.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        try {
            UserAllDetails.fetchContactNo(email);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ConnectivityManager cm =
                (ConnectivityManager)Index.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected && email == null) {
            isCheckLogin();
        }
        else if(isConnected && email != null) {
            homeListMenu();
            isCheckLogin();
        }
        else {
            Intent networkReciever = new Intent(Index.this, NotifyNetworkConnectionMessage.class);
            networkReciever.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            networkReciever.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Index.this.startActivity(networkReciever);
        }
    }

    public void isCheckLogin() {
        boolean checkLogin = sessionManager.isLoggedIn();
        if (!checkLogin) {
            // start your home screen
            Intent intent = new Intent(Index.this, SignIn.class);
            this.startActivity(intent);
            this.finish();
        }
    }
    
    private void homeListMenu() {

        homeListCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.indexListCoordinatorLayout);

        //Home menus Tittle Names
        final String[] titlename = new String[]{"Pets","Shop","Help a pet","Services", "Clinics","Pet Mate"};
        final String[] description = new String[]{"Buy, Adopt or Sell", "Shop products for your pet","Donate to NGO", "All kinds of services for your pet", "Nearby Pet Clinics","Find your pet the perfect partner"};
        final int[] background = {R.drawable.pet_view_list, R.drawable.pet_shop, R.drawable.pet_ngo, R.drawable.pet_services, R.drawable.pet_doctors,R.drawable.pet_mate};
        indexListInfosArray = new ArrayList<IndexListInfo>();
        for (int i = 0; i < titlename.length; i++) {
            IndexListInfo lf = new IndexListInfo();
            lf.setTittle(titlename[i]);
            lf.setDescription(description[i]);
            lf.setThumbnail(background[i]);
            indexListInfosArray.add(lf);
        }

        //View/List of pets Dailogbox Menu tittles Names
        final String[] title = new String[]{"Upload Details for adoption or selling", "List of Pets for sale & adoption"};
        final int[] icons = {R.drawable.addpet, R.drawable.view};
        for (int i = 0; i < title.length; i++) {
            DialogListInformaion dialogListInformaion1 = new DialogListInformaion();
            dialogListInformaion1.setTittle(title[i]);
            dialogListInformaion1.setIcons(icons[i]);
            dialogListForViewPets.add(dialogListInformaion1);
        }

        //Mating Dailogbox Menu tittles Names
        final String[] title2 = new String[]{"Register your pet", "Browse partner for your pet"};
        final int[] icons2 = {R.drawable.addpet, R.drawable.view};
        for (int i = 0; i < title2.length; i++) {
            DialogListInformaion dialogListInformaion2 = new DialogListInformaion();
            dialogListInformaion2.setTittle(title2[i]);
            dialogListInformaion2.setIcons(icons2[i]);
            dialogListForViewPetMets.add(dialogListInformaion2);
        }

        final String[] petclinictitle = new String[]{"View Home Location", "View Current Location"};
        final int[] petclinicicon = {R.drawable.home_location, R.drawable.current_location};
        for (int i = 0; i < petclinictitle.length; i++) {
            DialogListInformaion dialogListpetclinic = new DialogListInformaion();
            dialogListpetclinic.setTittle(petclinictitle[i]);
            dialogListpetclinic.setIcons(petclinicicon[i]);
            dialogListForpetClinic.add(dialogListpetclinic);
        }

        recyclerView = (RecyclerView) findViewById(R.id.indexList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new HomeListAdapter(indexListInfosArray, dialogListForViewPets, dialogListForViewPetMets, dialogListForpetClinic, homeListCoordinatorLayout);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = Index.this.getPackageManager();
        ComponentName component = new ComponentName(Index.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = Index.this.getPackageManager();
        ComponentName component = new ComponentName(Index.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}