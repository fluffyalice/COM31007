package uk.ac.shef.oak.com4510.ui.mapdetail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.databinding.MapActivityBinding;
import uk.ac.shef.oak.com4510.databinding.MapDetailActivityBinding;
import uk.ac.shef.oak.com4510.mydatabase.CacheEntity;
import uk.ac.shef.oak.com4510.mydatabase.CacheService;
import uk.ac.shef.oak.com4510.mydatabase.MyImage;
import uk.ac.shef.oak.com4510.mydatabase.MyLatLng;

public class MapDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapDetailActivityBinding binding;


    double initLatitude = -34.92873;
    double initLongitude = 138.59995;

    double latitude = initLatitude;
    double longitude = initLongitude;


    CacheEntity centity = null;
    String key = "";


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng PERTH = new LatLng(initLatitude, initLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 7));


        if (centity == null)
            return;

        ArrayList<MyLatLng> aMLngs = centity.getAlists();
        for (int n = 0; n < aMLngs.size(); n++) {
            addMyPolyline(aMLngs.get(n).latitude, aMLngs.get(n).longitude);
        }

        List<MyImage> lMyImages = centity.getImagebean();

        for (int n = 0; n < lMyImages.size(); n++) {
            LatLng sydney = new LatLng(lMyImages.get(n).getDoulbeLatitude(), lMyImages.get(n).getDoulbeLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Image" + (n + 1)));
        }

    }

    private LatLng endPerth = new LatLng(latitude, longitude);

    public void addMyPolyline(double latitude, double longitude) {

        LatLng PERTH = new LatLng(latitude, longitude);
        mMap.addPolyline(new PolylineOptions()
                .color(Color.RED)
                .width(5)
                .clickable(true)
                .add(endPerth, PERTH));

        endPerth = PERTH;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = MapDetailActivityBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setActionBar(binding.toolbar);

        initUI();
    }


    private void initUI() {

        key = getIntent().getStringExtra("key");

        centity = CacheService.get(key);

        List<MyImage> lMyImages = centity.getImagebean();
        if (lMyImages != null && lMyImages.size() > 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(lMyImages.get(0).imageUrl);
            binding.ivTitleImage.setImageBitmap(bitmap);
        }

        setTitle(centity.title);
        binding.tvTitle.setText(centity.title);

        long l = centity.stopTime - centity.startTime;

        //Calculate the difference in days, hours, minutes and seconds respectively
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        String timeElapsed = "";

        if (day > 0)
            timeElapsed = day + "day " + hour + "hour " + min + "min " + s + "seconds ";
        else if (  hour > 0 )
            timeElapsed = hour + "hour " + min + "min " + s + "seconds ";
        else if (  min > 0)
            timeElapsed = min + "min " + s + "seconds ";
        else if (s > 0)
            timeElapsed =  s + "seconds ";
        else
            timeElapsed = "0seconds";

        binding.tvTemperature.setText(timeElapsed);

        SimpleDateFormat dateFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("EEE, d MMM yyyy h:m", Locale.ENGLISH);

            String date = dateFormat.format(new Date(centity.startTime));
            binding.tvDate.setText(date);
        }

        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fmap));
        mapFragment.getMapAsync(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete) {

            CacheService.delete(key);
            Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}