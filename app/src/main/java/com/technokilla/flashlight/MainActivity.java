package com.technokilla.flashlight;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    Button OnOffBtn, OpenAppInfoBtn;
    TextView batteryPercentView;
    private static final int CAMERA_REQUEST_CODE = 555;
    boolean hasCameraFlash = false;

    private final BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int battery_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            batteryPercentView.setText(battery_level + "% Battery Level");
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryPercentView = findViewById(R.id.battery_percent);
        this.registerReceiver(this.batteryLevelReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);

        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        this.OnOffBtn =  findViewById(R.id.button);
        OpenAppInfoBtn = (Button)findViewById(R.id.open_appinfo_btn);

        // turning on flashlight when opening app
        if (hasCameraFlash){
            flashLightOn();
            OnOffBtn.setText("TURN OFF");
        }

        OnOffBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (hasCameraFlash){

                    if (OnOffBtn.getText().toString().contains("ON")){
                        flashLightOn();
                        OnOffBtn.setText("TURN OFF");
                    }
                    else {
                        flashLightOff();
                        OnOffBtn.setText("TURN ON");
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "No flash available on your device", Toast.LENGTH_SHORT).show();
                }

            }
        });


        OpenAppInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppInfoPage();
            }
        });

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            Toast.makeText(MainActivity.this, "Turned On", Toast.LENGTH_SHORT).show();
        }
        catch (CameraAccessException e) {
            Toast.makeText(MainActivity.this, "Failed to turn on flash light.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            Toast.makeText(MainActivity.this, "Turned Off", Toast.LENGTH_SHORT).show();
        }
        catch (CameraAccessException e) {
            Toast.makeText(MainActivity.this, "Failed to turn off flash light.", Toast.LENGTH_SHORT).show();
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasCameraFlash = getPackageManager().
                            hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                } else {
                    OnOffBtn.setEnabled(false);
                    TextView textView = (TextView)findViewById(R.id.textView);
                    textView.setText("Please grant all permission before use this application");
                    Toast.makeText(MainActivity.this, "Permission denied for the camera", Toast.LENGTH_SHORT).show();
                    OpenAppInfoBtn.setVisibility(View.VISIBLE);
                }
                break;
        }
    }



    private void openAppInfoPage() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" +getApplicationContext().getPackageName()));
            startActivity(intent);

            Toast.makeText(getApplicationContext(),"Please allow all permissions",Toast.LENGTH_LONG).show();
        }
        catch (ActivityNotFoundException e){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.about_btn){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://techno-killa.blogspot.com"));
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.website_btn){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://techno-killa.blogspot.com"));
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.feedback_btn){
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.rating_btn){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID));
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.share_btn){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,"Hi! Check out this this awesome torch light app. https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            startActivity(Intent.createChooser(intent, "Share with"));
        }
        else if(item.getItemId() == R.id.more_app_btn){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=5002650060821952731"));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}