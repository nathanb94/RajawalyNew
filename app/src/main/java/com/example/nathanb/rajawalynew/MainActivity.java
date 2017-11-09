package com.example.nathanb.rajawalynew;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Renderer renderer;
    RajawaliSurfaceView surface = null;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // final RajawaliSurfaceView surface = new RajawaliSurfaceView(this);
        surface = (RajawaliSurfaceView) findViewById(R.id.surface);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);

        // Add mSurface to your root view
//        addContentView(surface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));

        renderer = new Renderer(this);

        surface.setSurfaceRenderer(renderer);

        surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                renderer.onTouch(motionEvent);

                return true;
            }
        });

    }

    public void plus(View view) {

        renderer.setPlus(10);

      //  Log.d(TAG, "plus: "+ ++counter +"  "+renderer.unProject(0,0,0));
    }

    public void minus(View view) {
        renderer.setPlus(-10);
    }
}
