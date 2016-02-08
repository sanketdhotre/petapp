package com.couragedigital.petapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.couragedigital.petapp.model.MyListingPetMateListItem;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class MyListingPetMateListDetails extends AppCompatActivity implements View.OnClickListener {

    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String breed = "";
    String age = "";
    String gender = "";
    String description = "";

    TextView mlPetMateDetailsImageText;
    View mlPetMateDetailsImagesDividerLine;
    ImageView mlPetMateDetailsFirstImageThumbnail;
    ImageView mlPetMateDetailsSecondImageThumbnail;
    ImageView mlPetMateDetailsThirdImageThumbnail;
    ImageView mlPetMateImage;
    TextView mlPetMateBreed;
    TextView mlPetMateAge;
    TextView mlPetMateGender;
    TextView mlPetMateDescription;
    View mlPetMateDetailsDividerLine;

    Bitmap mlPetMateDetailsbitmap;
    Toolbar mlPetMateDetailstoolbar;
    CollapsingToolbarLayout mlPetMateDetailsCollapsingToolbar;
    CoordinatorLayout mlPetMateDetailsCoordinatorLayout;
    AppBarLayout mlPetMateDetailsAppBarLayout;
    NestedScrollView mlPetMateDetailsNestedScrollView;

    MyListingPetMateListItem petMateListItems = new MyListingPetMateListItem();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylistingpetmatelistdetail);

        Intent intent = getIntent();
        if (null != intent) {
            firstImagePath = intent.getStringExtra("PET_FIRST_IMAGE");
            secondImagePath = intent.getStringExtra("PET_SECOND_IMAGE");
            thirdImagePath = intent.getStringExtra("PET_THIRD_IMAGE");
            breed = intent.getStringExtra("PET_MATE_BREED");
            age = intent.getStringExtra("PET_MATE_AGE");
            gender = intent.getStringExtra("PET_MATE_GENDER");
            description = intent.getStringExtra("PET_MATE_DESCRIPTION");
        }

        mlPetMateDetailstoolbar = (Toolbar) findViewById(R.id.myListingPetMateDetailsToolbar);
        setSupportActionBar(mlPetMateDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mlPetMateDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mlPetMateDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.myListingPetMateDetailsCollapsingToolbar);

        mlPetMateDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.myListingPetMateDetailsCoordinatorLayout);

        mlPetMateDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.myListingPetMateDetailsAppBar);

        mlPetMateDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.myListingPetMateDetailsNestedScrollView);

        mlPetMateImage = (ImageView) findViewById(R.id.myListingPetMateDetailHeaderImage);
        mlPetMateDetailsImageText = (TextView) findViewById(R.id.myListingPetMateDetailsImageText);
        mlPetMateDetailsImagesDividerLine = findViewById(R.id.myListingPetMateDetailsImagesDividerLine);
        mlPetMateDetailsFirstImageThumbnail = (ImageView) findViewById(R.id.myListingPetMateDetailsFirstImageThumbnail);
        mlPetMateDetailsSecondImageThumbnail = (ImageView) findViewById(R.id.myListingPetMateDetailsSecondImageThumbnail);
        mlPetMateDetailsThirdImageThumbnail = (ImageView) findViewById(R.id.myListingPetMateDetailsThirdImageThumbnail);

        mlPetMateBreed = (TextView) findViewById(R.id.myListingPetMateBreedInPetDetails);
        //petListingType = (TextView) findViewById(R.id.petListingTypeInPetDetails);
        mlPetMateAge = (TextView) findViewById(R.id.myListingPetMateAgeInPetDetails);
        mlPetMateGender = (TextView) findViewById(R.id.myListingPetMateGenderInPetDetails);
        mlPetMateDescription = (TextView) findViewById(R.id.myListingPetMateDescriptionInPetDetails);
        mlPetMateDetailsDividerLine = findViewById(R.id.myListingPetMateDetailsDividerLine);

        mlPetMateDetailsFirstImageThumbnail.setOnClickListener(this);
        mlPetMateDetailsSecondImageThumbnail.setOnClickListener(this);
        mlPetMateDetailsThirdImageThumbnail.setOnClickListener(this);

        mlPetMateDetailsbitmap = getBitmapImageFromURL(firstImagePath);
        mlPetMateImage.setImageBitmap(mlPetMateDetailsbitmap);
        mlPetMateDetailsCollapsingToolbar.setTitle("For Mating");

        mlPetMateDetailsImageText.setText("Images of " + breed);
        mlPetMateDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        mlPetMateDetailsFirstImageThumbnail.setImageBitmap(mlPetMateDetailsbitmap);
        if (secondImagePath != null) {
            mlPetMateDetailsbitmap = getBitmapImageFromURL(secondImagePath);
            mlPetMateDetailsSecondImageThumbnail.setImageBitmap(mlPetMateDetailsbitmap);
        }
        if (thirdImagePath != null) {
            mlPetMateDetailsbitmap = getBitmapImageFromURL(thirdImagePath);
            mlPetMateDetailsThirdImageThumbnail.setImageBitmap(mlPetMateDetailsbitmap);
        }
        String breedOfPet = "<b>Breed: </b>" + breed;
        mlPetMateBreed.setText(Html.fromHtml(breedOfPet));
        String ageOfPet = "<b>Age: </b>" + age;
        mlPetMateAge.setText(Html.fromHtml(ageOfPet));
        String genderOfPet = "<b>Gender: </b>" + gender;
        mlPetMateGender.setText(Html.fromHtml(genderOfPet));
        String descriptionOfPet = "<b>Description: </b>" + description;
        mlPetMateDescription.setText(Html.fromHtml(descriptionOfPet));
        mlPetMateDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mlPetMateDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;


        mlPetMateDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });
    }

    private Bitmap getBitmapImageFromURL(String imagePath) {
        InputStream in = null;
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();

                StrictMode.setThreadPolicy(policy);
            }
            in = new BufferedInputStream(new URL(imagePath).openStream(), 4 * 1024);
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        BufferedOutputStream out = new BufferedOutputStream(dataStream, 4 * 1024);
        try {
            copy(in, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] data = dataStream.toByteArray();
        mlPetMateDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return mlPetMateDetailsbitmap;
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mlPetMateDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(mlPetMateDetailsCoordinatorLayout, mlPetMateDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    @Override
    public void onBackPressed() {
        MyListingPetMateListDetails.this.finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.myListingPetMateDetailsFirstImageThumbnail) {
            mlPetMateDetailsbitmap = getBitmapImageFromURL(firstImagePath);
            mlPetMateImage.setImageBitmap(mlPetMateDetailsbitmap);
        } else if (v.getId() == R.id.myListingPetMateDetailsSecondImageThumbnail) {
            mlPetMateDetailsbitmap = getBitmapImageFromURL(secondImagePath);
            mlPetMateImage.setImageBitmap(mlPetMateDetailsbitmap);
        } else if (v.getId() == R.id.myListingPetMateDetailsThirdImageThumbnail) {
            mlPetMateDetailsbitmap = getBitmapImageFromURL(thirdImagePath);
            mlPetMateImage.setImageBitmap(mlPetMateDetailsbitmap);
        }
    }
}