package com.example.nathanb.rajawalynew;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.rajawali3d.view.TextureView;

public class Object3DActivity extends AppCompatActivity implements Renderer.onRenderListener {

    TextureView surface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surface = findViewById(R.id.surface);

        surface.setFrameRate(60.0);

        surface.setRenderMode(TextureView.RENDERMODE_WHEN_DIRTY);

        Renderer mRenderer = new Renderer(this, this, surface);

        surface.setSurfaceRenderer(mRenderer);

    }

    @Override
    public void onClick(final String s) {

        new Thread() {
            public void run() {
                Object3DActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Object3DActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();

    }


}
