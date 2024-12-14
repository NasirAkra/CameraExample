package com.example.cameraexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final int camera_ReQ_Code=100;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.camera);
        Button button=findViewById(R.id.buttoncamera);
        button.setOnClickListener(v -> {
            Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera,camera_ReQ_Code);

        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==camera_ReQ_Code)
            {
                assert data != null;
                Bitmap image=(Bitmap) (Objects.requireNonNull(data.getExtras()).get("data"));
                imageView.setImageBitmap(image);

            }
        }
    }
}