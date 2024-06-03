package com.example.cameraapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btnPicture;
    private static final int REQUEST_CODE = 22;
    ImageView imageView;

    private ImageButton toggleButton;
    boolean hasCameraFlash = false;
    boolean flashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        btnPicture = findViewById(R.id.btnCamera);
        imageView = findViewById(R.id.imageView1);
        toggleButton = findViewById(R.id.toggleButton);

        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);



        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (hasCameraFlash) {
                        if (flashOn) {
                            flashOn = false;
                            toggleButton.setImageResource(R.drawable.power_off);
                            try {
                                flashLightoff();
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }

                        } else {
                            flashOn = true;
                            toggleButton.setImageResource(R.drawable.power_on);
                            try {
                                flashLightOn();
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "No Flash Available!", Toast.LENGTH_SHORT).show();
                    }
                    }



        });



        btnPicture.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {


                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,REQUEST_CODE);
            }
        });



    }



    private void flashLightOn() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId,true);
        Toast.makeText(this, "Flash Light is On!", Toast.LENGTH_SHORT).show();
    }

    private void flashLightoff() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId,false);
        Toast.makeText(this, "Flash Light is Off!", Toast.LENGTH_SHORT).show();
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

        }

        else{
            Toast.makeText(this, "Cancelled!!!", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }


    }




}