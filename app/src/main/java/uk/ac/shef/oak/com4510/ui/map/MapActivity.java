package uk.ac.shef.oak.com4510.ui.map;

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
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.databinding.MapActivityBinding;
import uk.ac.shef.oak.com4510.mydatabase.CacheService;
import uk.ac.shef.oak.com4510.mydatabase.MyLatLng;
import uk.ac.shef.oak.com4510.ui.home.MapViewModel;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapViewModel historyViewModel;
    private MapActivityBinding binding;
    private final int Time = 3000;    //time duration, unit ms
    private int N = 0;


    double initLatitude = -34.92873;
    double initLongitude = 138.59995;

    double latitude = initLatitude;
    double longitude = initLongitude;

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
            //Code to be executed repeatedly at regular intervals

            double dom = new Random().nextInt(100) - 30;
            double dom1 = new Random().nextInt(100) - 40;
            latitude = latitude + dom / 100.0;
            longitude = longitude + dom1 / 100.0;

            addMyPolyline(latitude, longitude);


            N = N + 1;
            System.out.println( N + "times of  " + dom);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

//        mMap.setMyLocationEnabled(true);
//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//
//                initLatitude = location.getLatitude();
//                initLongitude = location.getLongitude();
//
//                LatLng PERTH = new LatLng(initLatitude, initLongitude);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 7));
//                handler.postDelayed(runnable, Time);
//            }
//        });

        LatLng PERTH = new LatLng(initLatitude, initLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 7));
        handler.postDelayed(runnable, Time);    //Start timer
    }

    private LatLng endPerth = new LatLng(latitude, longitude);

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

        requestPermission();
        initUI();
        getWeather();
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
//                System.out.println(str);
                setTempAndPressure(str);
                return str;
            } else {
                return "Network error ：conn.getResponseCode() =" + conn.getResponseCode();
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            return "HttpService.getGamesData()Exceptions occur！ " + e.getMessage();
        }

    }

    private void setTempAndPressure(String str) {
        Gson gson = new Gson();
        OpenWeatherMap opwMap = gson.fromJson(str, OpenWeatherMap.class);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                binding.tvTemperature.setText(opwMap.getMain().getTemp() + "°C");
                binding.tvHPa.setText(opwMap.getMain().getPressure() + "hPa");
            }
        });
    }

    /**
     * Reading data from a stream
     */
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

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Determine permission first
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                writeFile();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }

            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                getMyLocation();
            }
        } else {
            writeFile();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                writeFile();
            } else {
                Toast.makeText(this, "Failed to obtain storage access", Toast.LENGTH_SHORT).show();
            }

        }

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMyLocation();
                }
                break;
        }

    }

    private void getMyLocation() {


    }

    /**
     * Simulating file writing
     */
    private void writeFile() {

    }

    private void initUI() {
        key = UUID.randomUUID().toString();
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

                                if (data != null) {
                                    String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(MapActivity.this, data.getData());
                                    saveImage(realPathFromUri);
                                } else {
                                    Toast.makeText(MapActivity.this, "Image damaged, please reselect", Toast.LENGTH_SHORT).show();
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
                LatLng PERTH = new LatLng(initLatitude, initLongitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 7));
            }
        });

        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toStop();
            }
        });

        MyLatLng latLng = new MyLatLng(endPerth.latitude, endPerth.longitude);
        alists.add(latLng);


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Rewrite the run() method to return the system time

                lTime++;

                //Calculate the difference in days, hours, minutes and seconds respectively
                long day = lTime / (24 * 60 * 60);
                long hour = (lTime / (60 * 60) - day * 24);
                long min = ((lTime / (60)) - day * 24 * 60 - hour * 60);
                long s = (lTime - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);


//                if (day > 0)
//                    timeElapsed = day + "day " + hour + "hour " + min + "min " + s + "seconds ";
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

        // Scheduled execution of a task at 1000 ms intervals from now on (regular repetitive scheduling of TimerTask)
        timer.schedule(task, 0, 1000);

    }

    int addImageIndex = 1;

    private void saveImage(String strImagePath) {

        CacheService.addImage(key, strImagePath, latitude + "", longitude + "");
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Image" + addImageIndex));
        addImageIndex++;
    }

    /***
     * Get the path after taking a photo
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
            String saveDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            // Create a new catalogue
            File dir = new File(saveDir);
            if (!dir.exists()) dir.mkdir();
            // Generate file name
//            SimpleDateFormat t = new SimpleDateFormat("yyyyMMdd");


            String filename = getRandomFileName() + ".jpg";
            // Generate new file
            File file = new File(saveDir, filename);
            fileOutputStream = new FileOutputStream(file);
            /**
             * Compress(CompressFormat format, int quality, OutputStream stream) method of the corresponding Bitmap is the first parameter.
             * The CompressFormat class is an enumeration with three values: JPEG, PNG and WEBP. where
             * PNG is a lossless format (ignoring the quality setting), which causes the second parameter in the method, CompressFormat quality, to fail.
             * JPEG is not explained.
             * The WEBP format is new to Google and according to official information "WEBP images are 40% smaller than JPEG images with the same quality.
             */
            photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
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

        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// Get a 5-digit random number

        return rannum + str;// Current time
    }


    //title: String?, startTime: Double?=null,stopTime:Double?,temperature:String?, pressure:String?
    private void toStop() {

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        long startTime = intent.getLongExtra("startTime", 0);

        long endTime = new Date().getTime();
        String strTemperature = "0.9℃";
        String strhPa = "1013.25";
        CacheService.setLatLng(key, title, startTime, endTime, strTemperature, strhPa, alists);

        Toast.makeText(this, "Save message successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    // The requestCode for camera permission
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;

    /**
     * Check permissions and take a picture.
     * Check permissions before calling up the camera
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(this.getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent();
            // Specify the Action to turn on the system camera
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            photoLauncher.launch(intent);//(intent, 1);
        } else {
            //No permission, request permission。
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
                        //这里写把图片保存到哪！！！！！！！！
                        String strImagePath = getTakePhotoPath(data);
                        saveImage(strImagePath);

                    }
                }
            });
}