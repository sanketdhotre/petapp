package com.couragedigital.peto;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class TabFragmentGroomerDetails extends AppCompatActivity implements View.OnClickListener {
    String image_path = "";
    String groomeraddress = "";


    ImageView groomerImage;

    TextView address;
    String groomername;
    Button callbutton;
    Button emailbutton;
    String email;
    String phoneno;

    Bitmap groomerDetailsbitmap;
    Toolbar groomerDetailsToolbar;
    CollapsingToolbarLayout groomerDetailsCollapsingToolbar;
    CoordinatorLayout groomerDetailsCoordinatorLayout;
    AppBarLayout groomerDetailsAppBaLayoutr;
    NestedScrollView groomerDetailsNestedScrollView;

    LinearLayout petGroomerDetailsLinearLayout;
    RelativeLayout petGroomerDetailsFirstRelativeLayout;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfragment_groomer_details);

        Intent intent = getIntent();
        if (null != intent) {
            image_path = intent.getStringExtra("GROOMER_IMAGE");
            groomername = intent.getStringExtra("GROOMER_NAME");
            groomeraddress = intent.getStringExtra("GROOMER_ADDRESS");
            email = intent.getStringExtra("GROOMER_EMAIL");
            phoneno = intent.getStringExtra("GROOMER_CONTACT");
        }

        groomerDetailsToolbar = (Toolbar) findViewById(R.id.petGroomerDetailsToolbar);
        setSupportActionBar(groomerDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        groomerDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        groomerDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.groomerDetailsCollapsingToolbar);
        groomerDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.groomerDetailsCoordinatorLayout);
        groomerDetailsAppBaLayoutr = (AppBarLayout) findViewById(R.id.petGroomerDetailsAppBar);
        groomerDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petGroomerDetailsNestedScrollView);
        petGroomerDetailsLinearLayout = (LinearLayout) findViewById(R.id.petGroomerDetailsLinearLayout);
        petGroomerDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.petGroomerDetailsFirstRelativeLayout);

        groomerImage = (ImageView) findViewById(R.id.petGroomerHeaderImage);
        address = (TextView) findViewById(R.id.petGroomerAddress);
        callbutton = (Button) findViewById(R.id.groomerDetailCallButton);
        emailbutton = (Button) findViewById(R.id.groomerDetailsEmailButton);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, image_path);
        address.setText(groomeraddress);
        groomerDetailsCollapsingToolbar.setTitle(groomername);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) groomerDetailsAppBaLayoutr.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        groomerDetailsAppBaLayoutr.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        petGroomerDetailsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Integer heightOfFirstRelativeLayout = petGroomerDetailsFirstRelativeLayout.getHeight();

                petGroomerDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + 200);
            }
        });

        callbutton.setOnClickListener(this);
        emailbutton.setOnClickListener(this);
    }

    public class FetchImageFromServer extends AsyncTask<String, String, String> {
        String urlForFetch;
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return urlForFetch;
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            Glide.with(groomerImage.getContext()).load(url).centerCrop().crossFade().into(groomerImage);
        }
    }

    private void copy(InputStream in, BufferedOutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) groomerDetailsAppBaLayoutr.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(groomerDetailsCoordinatorLayout, groomerDetailsAppBaLayoutr, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onBackPressed() {
        TabFragmentGroomerDetails.this.finish();
    }


    public void onClick(View view) {

        if (view.getId() == R.id.groomerDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phoneno));
            startActivity(callIntent);
        }else if(view.getId() == R.id.groomerDetailsEmailButton){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email, null));
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = TabFragmentGroomerDetails.this.getPackageManager();
        ComponentName component = new ComponentName(TabFragmentGroomerDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = TabFragmentGroomerDetails.this.getPackageManager();
        ComponentName component = new ComponentName(TabFragmentGroomerDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}