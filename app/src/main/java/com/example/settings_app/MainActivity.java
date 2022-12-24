package com.example.settings_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ApplicationErrorReport;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.BatteryState;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
public class MainActivity extends AppCompatActivity {
    static int torch_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCenter.start(getApplication(), "{65929dcd-b88e-420c-8ce2-0df6818a1e5b}",Analytics.class, Crashes.class);
        //TORCH
        ToggleButton torch = findViewById(R.id.toggleButton);

        torch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraManager c2 = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                if (torch_flag == 0) {

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            c2.setTorchMode(c2.getCameraIdList()[0], true);
                        }
                        torch_flag = 1;
                    } catch (CameraAccessException e) {
                        Toast.makeText(MainActivity.this, "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            c2.setTorchMode(c2.getCameraIdList()[0], false);
                            torch_flag = 0;
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });


        //AUDIO

        SeekBar seek = findViewById(R.id.seekBar);
        SeekBar media = findViewById(R.id.seekBar2);

        SeekBar system = findViewById(R.id.seekBar3);
        SeekBar notification = findViewById(R.id.seekBar4);
        AudioManager a1 = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seek.setMax(a1.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        // set seekbars max level to media and system sounds
        media.setMax(a1.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        notification.setMax(a1.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        system.setMax(a1.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        seek.setMax(a1.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        //this is to set initial progress
        media.setProgress(a1.getStreamVolume(AudioManager.STREAM_MUSIC));// up to here to set initial progress
        system.setProgress(a1.getStreamVolume(AudioManager.STREAM_SYSTEM));// up to here to set initial progress
        seek.setProgress(a1.getStreamVolume(AudioManager.STREAM_ALARM));// up to here to set initial progress
        notification.setProgress(a1.getStreamVolume(AudioManager.STREAM_NOTIFICATION));// up to here to set initial progress
        system.setMax(a1.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        //Implement on prograsschange
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                a1.setStreamVolume(AudioManager.STREAM_ALARM, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        media.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                AudioManager media_vol=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
                a1.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        system.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                AudioManager system_vol=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
                a1.setStreamVolume(AudioManager.STREAM_SYSTEM, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        notification.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                a1.setStreamVolume(AudioManager.STREAM_NOTIFICATION, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        BatteryManager battery = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
        TextView batter = findViewById(R.id.batterystatus);
//        ApplicationErrorReport.BatteryInfo batteryInfo=new ApplicationErrorReport.BatteryInfo();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            BatteryManager batteryManager2 = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
            batter.append("\nCharging :" + batteryManager2 + "   \nCurrent Level: " + battery.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                TextView t1=findViewById(R.id.textView6);
                t1.append(String.valueOf(location.getLatitude())+" long:"+String.valueOf(location.getLongitude()));
            }
        });


    }
}