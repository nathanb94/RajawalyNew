package com.example.nathanb.rajawalynew;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.rajawali3d.view.TextureView;

public class Object3DActivity extends AppCompatActivity implements View.OnTouchListener, Renderer.onRenderListener {

    private Renderer renderer;

    TextureView surface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surface = findViewById(R.id.surface);

        surface.setFrameRate(60.0);

        surface.setRenderMode(TextureView.RENDERMODE_WHEN_DIRTY);

        renderer = new Renderer(this, this);

        surface.setSurfaceRenderer(renderer);

        surface.setOnTouchListener(this);

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            // this needs to be defined on the renderer:
            renderer.getObjectAt(motionEvent.getX(), motionEvent.getY());

        }

        return super.onTouchEvent(motionEvent);

    }

    @Override
    public void onClick(final String s) {

        new Thread()
        {
            public void run()
            {
                Object3DActivity.this.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(Object3DActivity.this, s ,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();

    }
}
