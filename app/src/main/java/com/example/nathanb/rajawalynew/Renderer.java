package com.example.nathanb.rajawalynew; /**
 * Created by nathanb on 11/9/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.ALoader;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.async.IAsyncLoaderCallback;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.util.ObjectColorPicker;
import org.rajawali3d.util.OnObjectPickedListener;
import org.rajawali3d.util.RajLog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by nathanb on 11/2/2017.
 */

public class Renderer extends RajawaliRenderer implements IAsyncLoaderCallback, OnObjectPickedListener {


    private final Context context;

    private final onRenderListener mListener;

    private PointLight mLight;

    private RotateOnAxisAnimation mCameraAnim;

    private Object3D parsedObject;

    private ObjectColorPicker mPicker;

    private Object3D parsedObject2;

    private Object3D parsedObject3;

    private Object3D parsedObject4;

    private Object3D parsedObject5;

    private Object3D parsedObject6;

    private Object3D parsedObject7;

    private Object3D parsedObject8;

    private Object3D parsedObject9;

    private Object3D parsedObject10;

    private Object3D parsedObject11;

    private Object3D parsedObject12;

    private Material material;

    private Material material1;

    private ArrayList<Object3D> object3DArrayList;

    public Renderer(Context context, onRenderListener listener) {

        super(context);

        this.context = context;

        setFrameRate(60);

        mListener = listener;
    }

    @Override
    protected void initScene() {

        initLight();

        initSceneParams();

        initMaterials();

            final LoaderOBJ loaderOBJ = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.car_obj);
            loadModel(loaderOBJ, this, R.raw.car_obj);

//        }else {
//
//            showObjects();
//
//        }
    }

    private void initMaterials() {

        Texture earthTexture = new Texture("Earth", R.drawable.body);

        material = new Material();
//        material.enableLighting(true);
//        material.setDiffuseMethod(new DiffuseMethod.Lambert());

        try {

            material.addTexture(mTextureManager.addTexture(earthTexture));

        } catch (ATexture.TextureException error) {
            Log.d("DEBUG", "TEXTURE ERROR");

        }

        material1 = new Material();

        material1.setColor(new float[]{0,0,0,0});

        material.setColor(new float[]{0,0,0,0});

    }

    private void initSceneParams() {

        getCurrentScene().addLight(mLight);

        getCurrentCamera().setZ(16);

        getCurrentScene().setBackgroundColor(0.7f, 0.7f, 0.7f, 1.0f);

    }

    private void initLight() {

        mLight = new PointLight();

        mLight.setPosition(0, 0, 4);

        mLight.setPower(3);

    }



    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {

        super.onRender(elapsedTime, deltaTime);

    }




    @Override
    public void onModelLoadComplete(ALoader loader) {

        object3DArrayList = new ArrayList<Object3D>();

        RajLog.d("Model load complete: " + loader);
        final LoaderOBJ obj = (LoaderOBJ) loader;

        parsedObject = obj.getParsedObject().getChildAt(2);
        object3DArrayList.add(parsedObject);

        parsedObject2 = obj.getParsedObject().getChildAt(0);
        object3DArrayList.add(parsedObject2);

        parsedObject3 = obj.getParsedObject().getChildAt(11);
        object3DArrayList.add(parsedObject3);

        parsedObject4 = obj.getParsedObject().getChildAt(1);
        object3DArrayList.add(parsedObject4);

        parsedObject5 = obj.getParsedObject().getChildAt(3);
        object3DArrayList.add(parsedObject5);

        parsedObject6 = obj.getParsedObject().getChildAt(4);
        object3DArrayList.add(parsedObject6);

        parsedObject7 = obj.getParsedObject().getChildAt(5);
        object3DArrayList.add(parsedObject7);

        parsedObject8 = obj.getParsedObject().getChildAt(6);
        object3DArrayList.add(parsedObject8);

        parsedObject9 = obj.getParsedObject().getChildAt(7);
        object3DArrayList.add(parsedObject9);

        parsedObject10 = obj.getParsedObject().getChildAt(8);
        object3DArrayList.add(parsedObject10);

        parsedObject11 = obj.getParsedObject().getChildAt(9);
        object3DArrayList.add(parsedObject11);

        parsedObject12 = obj.getParsedObject().getChildAt(10);
        object3DArrayList.add(parsedObject12);

        showObjects();

    }

    private void showObjects() {

        mPicker = new ObjectColorPicker(this);

        mPicker.setOnObjectPickedListener(this);

        for (Object3D object3D : object3DArrayList){

            object3D.setPosition(Vector3.ZERO);

            object3D.setDoubleSided(true);

            object3D.setBackSided(true);

            object3D.setMaterial(material);

            object3D.setScale(6);

            object3D.setPosition(0,0,0);

            getCurrentScene().addChild(object3D);

            mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.Y, 360);
            mCameraAnim.setDurationMilliseconds(8000);
            mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
            mCameraAnim.setTransformable3D(object3D);

            getCurrentScene().registerAnimation(mCameraAnim);

            mCameraAnim.play();

            if (object3D.getName().equals("cube") || object3D.getName().equals("cubesecond")){

                object3D.setMaterial(material1);

                object3D.setAlpha(0);

                mPicker.registerObject(object3D);

            }

        }


//        parsedObject2.setMaterial(material1);
//
//        parsedObject2.setAlpha(0);
//
//        // parsedObject2.setTransparent(true);
//
//        parsedObject3.setMaterial(material1);
//
//        parsedObject3.setAlpha(0);
//
//        //parsedObject3.setTransparent(true);

        getCurrentCamera().setZ(100);


//        mPicker.registerObject(parsedObject2);
//        mPicker.registerObject(parsedObject3);
        mPicker.registerObject(object3DArrayList.get(5));

    }

    @Override
    public void onModelLoadFailed(ALoader aLoader) {
        RajLog.e(this, "Model load failed: " + aLoader);
    }


    public void getObjectAt(float x, float y) {

        if(mPicker != null){

            mPicker.getObjectAt(x, y);

        }

    }

    @Override
    public void onObjectPicked(Object3D object) {

        Log.d(TAG, "onObjectPicked: Touchhhhh "+object.getName());

        if (object.getName().equals("cube")){

            mListener.onClick("this is the engine ventilation");

        }else if (object.getName().equals("cubesecond")){

            mListener.onClick("this is the left door");
        }
    }


    public interface onRenderListener{

        void onClick(String s);
    }
}

//    /**
//     * Convert the 4D input into 3D space (or something like that, otherwise the gluUnproject values are incorrect)
//     *
//     * @param v 4D input
//     * @return 3D output
//     * @author http://stackoverflow.com/users/1029225/mh
//     */
//    public static float[] fixW(float[] v) {
//        float w = v[3];
//        for (int i = 0; i < 4; i++)
//            v[i] = v[i] / w;
//        return v;
//    }
//
//
//    private boolean checkCollision(float x, float y, Object3D object) {
//
//
//        float[] nearPoint = {0f, 0f, 0f, 0f};
//        float[] farPoint = {0f, 0f, 0f, 0f};
//        float[] rayVector = {0f, 0f, 0f};
//
//        float[] mmatrix = ArrayUtils.convertDoublesToFloats(getCurrentCamera().getViewMatrix().getDoubleValues());
//        float[] pmatrix = ArrayUtils.convertDoublesToFloats(getCurrentCamera().getProjectionMatrix().getDoubleValues());
//
//        int[] mViewport = new int[]{0, 0, mDefaultViewportWidth, mDefaultViewportHeight};
//
//        y = mViewport[3] - y;
//
//        //Retreiving position projected on near plane
//        android.opengl.GLU.gluUnProject(x, y, -1f, mmatrix, 0, pmatrix, 0, mViewport, 0, nearPoint, 0);
//
//        //Retreiving position projected on far plane
//        android.opengl.GLU.gluUnProject(x, y, 1f, mmatrix, 0, pmatrix, 0, mViewport, 0, farPoint, 0);
//
//        // extract 3d Coordinates put of 4d Coordinates
//        nearPoint = fixW(nearPoint);
//        farPoint = fixW(farPoint);
//
//        //Processing ray vector
//        rayVector[0] = farPoint[0] - nearPoint[0];
//        rayVector[1] = farPoint[1] - nearPoint[1];
//        rayVector[2] = farPoint[2] - nearPoint[2];
//
//        // calculate ray vector length
//        float rayLength = (float) Math.sqrt((rayVector[0] * rayVector[0]) + (rayVector[1] * rayVector[1]) + (rayVector[2] * rayVector[2]));
//
//        //normalizing ray vector
//        rayVector[0] /= rayLength;
//        rayVector[1] /= rayLength;
//        rayVector[2] /= rayLength;
//
//        float[] collisionPoint = {0f, 0f, 0f};
//        float[] objectCenter = {
//                (float) object.getWorldPosition().x,
//                (float) object.getWorldPosition().y,
//                (float) object.getWorldPosition().z,
//        };
//
//        //Iterating over ray vector to check for collisions
//        for (int i = 0; i < 1000; i++) {
//            collisionPoint[0] = rayVector[0] * rayLength / 1000 * i;
//            collisionPoint[1] = rayVector[1] * rayLength / 1000 * i;
//            collisionPoint[2] = -rayVector[2] * rayLength / 1000 * i;
//
////            Log.d("nathan", "checkCollision vector : x "+collisionPoint[0]+" y "+collisionPoint[1]+" z "+collisionPoint[2]);
////
////            Log.d("nathan", "checkCollision: objects : x "+object.getX()+" y "+object.getY()+" z "+object.getZ()+"/n");
//
//            if (poinSphereCollision(collisionPoint, objectCenter, 2)) {
//
//                Log.d("nathan", "checkCollision vector : x " + collisionPoint[0] + " y " + collisionPoint[1] + " z " + collisionPoint[2]);
//
//                Log.d("nathan", "checkCollision: objects : x " + objectCenter[0] + " y " + objectCenter[1] + " z " + objectCenter[2] + "/n");
//
//                return true;
//            }
//        }
//
//
//        return false;
//
//    }
//
//
//    public static Boolean poinSphereCollision(float[] point, float[] center, float radius) {
//
////        return ((point[0] - center[0]) * (point[0] - center[0]) +
////                (point[1] - center[1]) * (point[1] - center[1]) +
////                (point[2] - center[2]) * (point[2] - center[2]) < (radius * radius));
//
//        return ((point[0] - center[0]) * (point[0] - center[0]) +
//                (point[1] - center[1]) * (point[1] - center[1])  < (radius * radius));
//    }
