package com.hangzhou.uama;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import uama.hangzhou.image.photochoose.FourPicturesChoose;
import uama.hangzhou.image.photochoose.PhotoChoose;
import uama.hangzhou.image.widget.MyGridView;


public class MainActivity extends AppCompatActivity {
    private SimpleDraweeView zoomDrawView;
    private MyGridView myGridView;
    private FourPicturesChoose fourPicturesChoose;
    ImageView image1, image2, image3, image4;
    //    private ShareView shareView;
    private PhotoChoose photoChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(MainActivity.this);
        setContentView(R.layout.activity_main);
        init();
        photoChoose = new PhotoChoose(this, myGridView, 9);
    }

    private void init() {
        image1 = (ImageView) findViewById(R.id.photo_choose_img1);
        image2 = (ImageView) findViewById(R.id.photo_choose_img2);
        image3 = (ImageView) findViewById(R.id.photo_choose_img3);
        image4 = (ImageView) findViewById(R.id.photo_choose_img4);
        fourPicturesChoose = new FourPicturesChoose(this, image1, image2, image3, image4, ContextCompat.getColor(this, R.color.green_light),
                100);
        myGridView = (MyGridView) findViewById(R.id.grid_view_publish_photo);
        photoChoose = new PhotoChoose(this, myGridView, 6);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fourPicturesChoose.setImageList(requestCode, resultCode, data);
    }
}
