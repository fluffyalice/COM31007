package uk.ac.shef.oak.com4510.ui.map;

import static android.location.LocationManager.GPS_PROVIDER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.databinding.MapActivityBinding;
import uk.ac.shef.oak.com4510.mydatabase.CacheService;
import uk.ac.shef.oak.com4510.ui.home.MapViewModel;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapViewModel historyViewModel;
    private MapActivityBinding binding;
    private final int Time = 3000;
    private int N = 0;

    double latitude = -34.92873;
    double longitude = 138.59995;

    ArrayList<com.google.android.libraries.maps.model.LatLng> alists = new ArrayList<com.google.android.libraries.maps.model.LatLng>();

    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, Time);


            double dom = new Random().nextInt(100) - 30;
            double dom1 = new Random().nextInt(100) - 40;
            latitude = latitude + dom / 100.0;
            longitude = longitude + dom1 / 100.0;

            addMyPolyline(latitude, longitude);


            N = N + 1;
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,20));
        handler.postDelayed(runnable, Time);
    }

    //    double latitude=-34.92873;
    //    double  longitude=138.59995;

    private LatLng endPerth = new LatLng(latitude, longitude);

    public void addMyPolyline(double latitude, double longitude) {

        LatLng PERTH = new LatLng(latitude, longitude);
        mMap.addPolyline(new PolylineOptions()
                .color(Color.RED)
                .width(5)
                .clickable(true)
                .add(endPerth, PERTH));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 7));

        com.google.android.libraries.maps.model.LatLng latLng = new com.google.android.libraries.maps.model.LatLng(latitude, longitude);
        alists.add(latLng);
        endPerth = PERTH;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MapActivityBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        historyViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MapViewModel.class);


        String strTitle = getIntent().getStringExtra("title");
        binding.tvTitle.setText(strTitle);


        initUI();
    }

    private void initUI() {

        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fmap));
        mapFragment.getMapAsync(this);


        binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);

                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickPhotoLauncher.launch(intent);
            }

            ActivityResultLauncher<Intent> pickPhotoLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();

                            }
                        }
                    });
        });

        binding.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera_1();
            }
        });

        binding.ivPosition.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.requestLocationUpdates(GPS_PROVIDER, 2000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {

                    }
                });
            }
        });


        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toStop();
            }
        });

        com.google.android.libraries.maps.model.LatLng latLng = new com.google.android.libraries.maps.model.LatLng(endPerth.latitude, endPerth.longitude);
        alists.add(latLng);
    }

    //title: String?, startTime: Double?=null,stopTime:Double?,temperature:String?, pressure:String?
    private void toStop() {

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        long startTime = intent.getLongExtra("startTime", 0);

        long endTime = new Date().getTime();
        String strTemperature = "0.9℃";
        String strhPa = "1013.25";


//        CacheService.set();
        Toast.makeText(this, "save Data Success", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onDestroy() {

        handler.removeCallbacks(runnable);

        super.onDestroy();
    }


    private void openCamera_1() {
        checkPermissionAndCamera();
    }

    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;

    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(this.getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            photoLauncher.launch(intent);//(intent, 1);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    ActivityResultLauncher<Intent> photoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                    }
                }
            });
}