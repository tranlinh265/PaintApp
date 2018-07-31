package com.example.linhtm.demopaintapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private CustomView customView;
    private ImageView ivBackground;
    private ImageView ivResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customView = findViewById(R.id.custom_view);
        ivBackground = findViewById(R.id.iv_background);
        ivResult = findViewById(R.id.iv_result);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_clear:
                customView.resetLayout();
                break;
            case R.id.btn_image:
                pickImage();
                break;
            case R.id.btn_show:
                showImage();
                break;
        }
    }

    public static final int PICK_IMAGE = 1;

    private void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void showImage() {
        try {
            Bitmap bmp1 = loadBitmapFromView(customView);
            Bitmap bmp2 = loadBitmapFromView(ivBackground);

            Bitmap result = overlay(bmp2, bmp1);
            ivResult.setImageBitmap(result);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE  && resultCode == Activity.RESULT_OK){
            Uri result;
            String filePathStr = null;

            if (data != null && data.getData() != null ){
                try{
                    result = data.getData();
                    ivBackground.setImageURI(result);
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//                        filePathStr = FileUtils.getPathFromImageUri(this, result);
//                    } else {
//                        filePathStr = FileUtils.getRealPathFromDocumentUri(this, result);
//                    }
                }catch (Exception e) {
                    //do not catch
                }
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    public Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() <= 0) {
            v.measure(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        Bitmap b = Bitmap.createBitmap( v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}
