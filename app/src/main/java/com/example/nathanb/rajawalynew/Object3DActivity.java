package com.example.nathanb.rajawalynew;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.rajawali3d.view.TextureView;

public class Object3DActivity extends AppCompatActivity implements View.OnTouchListener, Renderer.onRenderListener {

    private Renderer mRenderer;

    TextureView surface = null;

    private float xpos;

    private float ypos;

    private float xd;

    private float yd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surface = findViewById(R.id.surface);

        surface.setFrameRate(60.0);

        surface.setRenderMode(TextureView.RENDERMODE_WHEN_DIRTY);

        mRenderer = new Renderer(this, this);

        surface.setSurfaceRenderer(mRenderer);

        surface.setOnTouchListener(this);

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            mRenderer.getObjectAt(motionEvent.getX(), motionEvent.getY());

            xpos = motionEvent.getX();
            ypos = motionEvent.getY();

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            xpos = -1;
            ypos = -1;

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            xd = motionEvent.getX() - xpos;
            yd = motionEvent.getY() - ypos;

            xpos = motionEvent.getX();
            ypos = motionEvent.getY();

            if (xd < 0) {
                mRenderer.up = true;
            } else {
                mRenderer.down = true;
            }
            if (yd < 0) {
                mRenderer.left = true;
            } else {
                mRenderer.right = true;
            }

        }

        return true;

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
