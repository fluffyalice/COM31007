package uk.ac.shef.oak.com4510.ui.map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.databinding.MapActivityBinding;
import uk.ac.shef.oak.com4510.mydatabase.CacheEntity;
import uk.ac.shef.oak.com4510.mydatabase.CacheService;
import uk.ac.shef.oak.com4510.mydatabase.MyImage;
import uk.ac.shef.oak.com4510.mydatabase.MyLatLng;
import uk.ac.shef.oak.com4510.ui.history.MyImageDialog;
import uk.ac.shef.oak.com4510.ui.home.MapViewModel;
import uk.ac.shef.oak.com4510.util.PermissionHelper;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapViewModel historyViewModel;
    private MapActivityBinding binding;
    private final int Time = 20000;    // Time interval, in ms

    double initLatitude = 0;
    double initLongitude = 0;

    double latitude = initLatitude;
    double longitude = initLongitude;
    double strTemperature = 0d;
    double strHPa = 0d;

    int REQUEST_CODE = 483;
    long lTime = 0;
    String timeElapsed = "";

    ArrayList<MyLatLng> alists = new ArrayList<MyLatLng>();
    String key;

    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, Time);
            // Code to be repeated at regular intervals

//            double dom = new Random().nextInt(100) - 50;
//            double dom1 = new Random().nextInt(100) - 50;
//            latitude = latitude + dom / 800.0;
//            longitude = longitude + dom1 / 800.0;

            addMyPolyline(latitude, longitude);

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (alists != null && alists.size() > 0) {//恢复横竖屏数据状态
            endPerth = new LatLng(alists.get(0).latitude, alists.get(0).longitude);
            for (int n = 1; n < alists.size(); n++) {
                LatLng PERTH = new LatLng(alists.get(n).latitude, alists.get(n).longitude);
                mMap.addPolyline(new PolylineOptions()
                        .color(Color.RED)
                        .width(5)
                        .clickable(true)
                        .add(endPerth, PERTH));
                endPerth = PERTH;
            }

            latitude = endPerth.latitude;
            longitude = endPerth.longitude;

            CacheEntity centity = CacheService.get(key);
            List<MyImage> lMyImages = centity.getImagebean();

            for (int n = 0; n < lMyImages.size(); n++) {
                LatLng sydney = new LatLng(lMyImages.get(n).getDoulbeLatitude(), lMyImages.get(n).getDoulbeLongitude());
                String strImagePath = lMyImages.get(n).imageUrl;
                Bitmap bitmap2 = null;
                try {
                    bitmap2 = BitmapFactory.decodeFile(strImagePath);

                } catch (Exception e) {
                    try {
                        options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        bitmap2 = BitmapFactory.decodeFile(strImagePath);
                    } catch (Exception excepetion) {
                    }
                }

                try {
                    Bitmap bitmap1 = Bitmap.createBitmap(bitmap2, 100, 100, 100, 100);
                    mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromBitmap(bitmap1))).setTag(strImagePath);
                } catch (Exception e) {
                    mMap.addMarker(new MarkerOptions().position(sydney)).setTag(strImagePath);
                }

            }

        } else {
            endPerth = new LatLng(initLatitude, initLongitude);
            MyLatLng latLng = new MyLatLng(endPerth.latitude, endPerth.longitude);
            alists.add(latLng);
        }


        LatLng PERTH = new LatLng(initLatitude, initLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 15));
        handler.postDelayed(runnable, Time);    // Start the timer

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                showMarkImage(marker.getTag());
                return false;
            }
        });

    }

    private void showMarkImage(Object tag) {
        Bitmap bitmap = BitmapFactory.decodeFile(tag + "");
        MyImageDialog myImageDialog = new MyImageDialog(this, R.style.dialogWindowAnim, 0, -300, bitmap);
        myImageDialog.show();
    }


    public LatLng endPerth = null;

    public void addMyPolyline(double latitude, double longitude) {

        LatLng PERTH = new LatLng(latitude, longitude);
        mMap.addPolyline(new PolylineOptions()
                .color(Color.RED)
                .width(5)
                .clickable(true)
                .add(endPerth, PERTH));

        MyLatLng latLng = new MyLatLng(latitude, longitude);
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

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "Failed to obtain location permission.", Toast.LENGTH_SHORT).show();
        }

        initUI();
        initPermission();
        hasCamera();
        PermissionHelper permissionHelper = new PermissionHelper();
        permissionHelper.requestBackgroundLocationPermission(this);

    }

    static final String initLatitude_VALUE = "initLatitude";
    static final String initLongitude_VALUE = "initLongitude";
    static final String alists_VALUE = "alists";
    static final String key_VALUE = "key";


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Save custom values into the bundle

        savedInstanceState.putDouble(initLatitude_VALUE, initLatitude);
        savedInstanceState.putDouble(initLongitude_VALUE, initLongitude);
        savedInstanceState.putString(key_VALUE, key);


        Gson gson = new Gson();
        String str = gson.toJson(alists);
        savedInstanceState.putString(alists_VALUE, str);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        initLatitude = savedInstanceState.getDouble(initLatitude_VALUE);
        initLongitude = savedInstanceState.getDouble(initLongitude_VALUE);
        key = savedInstanceState.getString(key_VALUE);


        String talists = savedInstanceState.getString(alists_VALUE);

        Gson g = new Gson();
        alists = g.fromJson(talists, new TypeToken<List<MyLatLng>>() {
        }.getType());

    }

    private void hasCamera() {
        Context context = this;
        PackageManager packageManager = context.getPackageManager();
        // if device support camera?
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            //yes
            binding.ivPhoto.setVisibility(View.VISIBLE);
        } else {
            //no
            binding.ivPhoto.setVisibility(View.GONE);
        }
    }

    /**
     * Get location permission
     */
    private void initPermission() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,

                }, 11);

            }
        }, 1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                // Check permission status
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    /*
                      If the user completely refuses to grant permission, the user is usually prompted to go to the Set Permissions
                      screen After the first failed grant, when exiting the app and re-entering, the Allow Permissions
                      prompt will be brought up again here
                     */
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,}, 102);
                    Log.d("info:", "-----get--Permissions--success--1-");
                } else {
                    /*
                      The user does not completely refuse to grant permissions the first time he installs it,
                      the permission prompt box is called up, and then it never prompts again.
                     */
                    Log.d("info:", "-----get--Permissions--success--2-");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,}, 102);
                }
            } else {
                getMyLocation();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getMyLocation() {
        // Get user location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, new Listener());
        // Have another for GPS provider just in case.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new Listener());
        // Try to request the location immediately
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null) {
            initLatitude = location.getLatitude();
            initLongitude = location.getLongitude();
            latitude = initLatitude;
            longitude = initLongitude;
            getWeather();

            SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fmap));
            mapFragment.getMapAsync(this);

//            handleLatLng(location.getLatitude(), location.getLongitude());
        }
    }

    /**
     * Handle lat lng.
     */
    private void handleLatLng(double latitudevalue, double longitudevalue) {
        Log.e("TAG---", "-----------(" + latitudevalue + "," + longitudevalue + ")");

        latitude = latitudevalue;
        longitude = longitudevalue;
    }

    /**
     * Listener for changing gps coords.
     */
    private class Listener implements LocationListener {
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            handleLatLng(latitude, longitude);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private void getWeather() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getData(initLatitude, initLongitude, "1fcc755daefae8665f1c5813da81333c");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String getData(double lat, double lon, String key) throws Exception {

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + key);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                InputStream ips = conn.getInputStream();
                byte[] data = read(ips);
                String str = new String(data);
                //System.out.println(str);
                setTempAndPressure(str);
                return str;
            } else {
                return "Network error :conn.getResponseCode() =" + conn.getResponseCode();
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            return "HttpService.getGamesData()An exception occurs！ " + e.getMessage();
        }

    }


    private void setTempAndPressure(String str) {
        Gson gson = new Gson();
        OpenWeatherMap opwMap = gson.fromJson(str, OpenWeatherMap.class);

        strTemperature = opwMap.getMain().getTemp();
        strHPa = opwMap.getMain().getPressure();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                binding.tvTemperature.setText(opwMap.getMain().getTemp() + "℃");
                binding.tvHPa.setText(opwMap.getMain().getPressure() + "hPa");
            }
        });
    }

    /*
     * Read the data in the stream
     * */
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Failed to obtain storage permission.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 102) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getMyLocation();
            }
        }

    }


    private void initUI() {
        key = UUID.randomUUID().toString();


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

                                if (data != null) {
                                    String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(MapActivity.this, data.getData());
                                    saveImage(realPathFromUri, 1);
                                } else {
                                    Toast.makeText(MapActivity.this, "The picture is damaged, please select it again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
        });

        binding.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionAndCamera();
            }
        });

        binding.ivPosition.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                LatLng PERTH = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 15));
            }
        });

        binding.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toStop();
            }
        });


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                lTime++;
                long day = lTime / (24 * 60 * 60);
                long hour = (lTime / (60 * 60) - day * 24);
                long min = ((lTime / (60)) - day * 24 * 60 - hour * 60);
                long s = (lTime - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

                if (hour > 0)
                    timeElapsed = hour + ": " + min + ": " + s + " ";
                else if (min > 0)
                    timeElapsed = "00:" + min + ": " + s;
                else if (s > 0)
                    timeElapsed = "00:00:" + s;
                else
                    timeElapsed = "00:";

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.tvTime.setText(timeElapsed);
                    }
                });

            }
        };
        Timer timer = new Timer();

        timer.schedule(task, 0, 1000);

    }

    int addImageIndex = 1;
    BitmapFactory.Options options;

    /**
     * 0--taking photo 1--gallery
     *
     * @param strImagePath
     * @param index
     */
    private void saveImage(String strImagePath, int index) {

        CacheService.addImage(key, strImagePath, latitude + "", longitude + "");
        LatLng sydney = new LatLng(latitude, longitude);
//        Bitmap bitmap = BitmapFactory.decodeFile(strImagePath);
//         Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 100, 100, 100, 100);

        Bitmap bitmap2 = null;
        try {
            bitmap2 = BitmapFactory.decodeFile(strImagePath);

        } catch (Error e) {
            try {
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap2 = BitmapFactory.decodeFile(strImagePath, options);

            } catch (Exception excepetion) {
            }
        }


        if (index == 1) {
            try{
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap2, 100, 100, 100, 100);
                mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromBitmap(bitmap1))).setTag(strImagePath);

            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        else
            mMap.addMarker(new MarkerOptions().position(sydney)).setTag(strImagePath);


        addImageIndex++;
    }


    /***
     * Gets the path after the photo is taken
     * @param data
     * @return
     */
    public String getTakePhotoPath(Intent data) {
        Bitmap photo = null;
        Uri uri = data.getData();
        if (uri != null) {
            photo = BitmapFactory.decodeFile(uri.getPath());
        }
        if (photo == null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                photo = (Bitmap) bundle.get("data");
            } else {

                return "";
            }
        }

        FileOutputStream fileOutputStream = null;
        try {
            // Get the SD card root directory
            String saveDir = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                saveDir = this.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString();
            } else {
                saveDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
            // Create a directory
            File dir = new File(saveDir);
            if (!dir.exists()) dir.mkdir();
            // Generate the file name
//            SimpleDateFormat t = new SimpleDateFormat("yyyyMMdd");


            String filename = getRandomFileName() + ".jpg";
            /** Create a file */
            File file = new File(saveDir, filename);
            /*** open file output stream */
            fileOutputStream = new FileOutputStream(file);
            // Generate the image file
            /**
             * The first parameter in the compress(bitmap.pressformat format, int Quality, OutputStream stream) method corresponding to Bitmap.
             * The CompressFormat class is an enumeration with three values: JPEG, PNG, and WEBP. Among them,
             * PNG is a lossless format (ignoring quality Settings), which invalidates the compression quality as the second parameter in the method,
             * The WEBP format is new to Google, and according to the official document, "WEBP images are 40% smaller than JPEG images at the same quality.
             */
            photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            /*** the full path of the photo */
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    public String getRandomFileName() {

        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        Date date = new Date();

        String str = simpleDateFormat.format(date);

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// Get 5 random digits

        return rannum + str;// The current time
    }


    //title: String?, startTime: Double?=null,stopTime:Double?,temperature:String?, pressure:String?
    private void toStop() {

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        long startTime = intent.getLongExtra("startTime", 0);

        long endTime = new Date().getTime();
//        String strTemperature = "0.9℃";
//        String strhPa = "1013.25";
        CacheService.setLatLng(key, title, startTime, endTime, strTemperature + "", strHPa + "", alists);

        Toast.makeText(this, "Information saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    // requestCode for camera permissions
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;

    /**
     * Check permissions and take photos.
     * Check permissions before invoking the camera.
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(this.getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent();
            // Specify the Action to start the system camera
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            photoLauncher.launch(intent);//(intent, 1);
        } else {
            // No permission, apply for permission.
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
                        String strImagePath = getTakePhotoPath(data);
                        saveImage(strImagePath, 0);

                    }
                }
            });
}