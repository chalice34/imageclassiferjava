package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView imagev;
    private MaterialButton labelimagebutton;
    private TextView result;

    private ProgressDialog progressDialog;

    private ImageLabeler imageLabeler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagev=findViewById(R.id.imagev);
        labelimagebutton=findViewById(R.id.labelimagebutton);
        result=findViewById(R.id.result);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        imageLabeler= ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        /*ImageLabelerOptions imageLabelerOptions=new ImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.95f)
                .build();
        imageLabeler = ImageLabeling.getClient(imageLabelerOptions);*/

        //Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.bottle);

        /*Uri imageuri=null;
        try {
            Bitmap bitmap2= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        BitmapDrawable bitmapDrawable=(BitmapDrawable)imagev.getDrawable();
        Bitmap bitmap3=bitmapDrawable.getBitmap();

        labelimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                labelimage(bitmap3);
            }
        });

    }

    private void labelimage(Bitmap bitmap3) {
        progressDialog.setMessage("preparing image");
        progressDialog.show();

        InputImage inputImage= InputImage.fromBitmap(bitmap3,0);
        progressDialog.setMessage("Labeling image");


        imageLabeler.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> imageLabels) {

                        for(ImageLabel imageLabel:imageLabels){
                            String text=imageLabel.getText();

                            float confidence=imageLabel.getConfidence();
                            int index=imageLabel.getIndex();

                            result.append("text:"+text+"\nconfidence:"+confidence+"\nindex"+index+"\n\n");
                        }

                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Failed to label due to "+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });


    }

}