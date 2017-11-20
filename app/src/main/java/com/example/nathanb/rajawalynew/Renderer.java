package com.example.nathanb.rajawalynew;

import android.app.Activity;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.ALoader;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.async.IAsyncLoaderCallback;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;

import org.rajawali3d.util.GLU;
import org.rajawali3d.util.ObjectColorPicker;
import org.rajawali3d.util.OnObjectPickedListener;
import org.rajawali3d.util.RajLog;
import org.rajawali3d.view.TextureView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by nathanb on 11/2/2017.
 */

public class Renderer extends org.rajawali3d.renderer.Renderer implements IAsyncLoaderCallback, OnObjectPickedListener, MyArcballCamera.MyArcballCameraListener {


    private final onRenderListener mListener;
    private final Activity context;
    private final TextureView surface;

    private PointLight mLight;

    private ObjectColorPicker mPicker;

    private Material material;

    private Material material1;

    private ArrayList<Object3D> object3DArrayList;

    private Object3D mParsedObject;


    public Renderer(Activity context, onRenderListener listener, TextureView surface) {

        super(context);

        setFrameRate(60);

        mListener = listener;

        this.context = context;

        this.surface = surface;

    }

    @Override
    protected void initScene() {

        initLight();

        initSceneParams();

        initMaterials();

        final LoaderOBJ loaderOBJ = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.car_obj);
        loadModel(loaderOBJ, this, R.raw.car_obj);

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

        material1.setColor(new float[]{0, 0, 0, 0});

        material.setColor(new float[]{0, 0, 0, 0});

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
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                                 float yOffsetStep, int xPixelOffset, int yPixelOffset) {

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

        object3DArrayList = new ArrayList<>();

        RajLog.d("Model load complete: " + loader);

        final LoaderOBJ obj = (LoaderOBJ) loader;

        mParsedObject = obj.getParsedObject();

        for (int i = 0; i < mParsedObject.getNumChildren(); i++) {

            object3DArrayList.add(mParsedObject.getChildAt(i));

        }

        showObjects();

    }

    private void showObjects() {

        mPicker = new ObjectColorPicker(this);

        mPicker.setOnObjectPickedListener(this);

        MyArcballCamera arcball = new MyArcballCamera(context, this, surface, mParsedObject);

        arcball.setPosition(0,0,100); //optional

        getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);

        for (Object3D object3D : object3DArrayList) {

            object3D.setPosition(Vector3.ZERO);

            object3D.setDoubleSided(true);

            object3D.setBackSided(true);

            object3D.setMaterial(material);

            object3D.setScale(6);

            object3D.setPosition(0, 0, 0);

            getCurrentScene().addChild(object3D);


            //setAnimation(object3D);


            if (object3D.getName().equals("cube") || object3D.getName().equals("cubesecond")) {

                object3D.setMaterial(material1);

                object3D.setAlpha(0);

                mPicker.registerObject(object3D);

            }

        }

        getCurrentCamera().setLookAt(mParsedObject.getPosition());

        mPicker.registerObject(object3DArrayList.get(5));

    }

    private void setAnimation(Object3D object3D) {

        RotateOnAxisAnimation mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.Y, 360);

        mCameraAnim.setDurationMilliseconds(8000);

        mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);

        mCameraAnim.setTransformable3D(object3D);

        getCurrentScene().registerAnimation(mCameraAnim);

        mCameraAnim.play();

    }


    @Override
    public void onModelLoadFailed(ALoader aLoader) {
        RajLog.e("Model load failed: " + aLoader);
    }


    public void getObjectAt(float x, float y) {

        if (mPicker != null) {

            mPicker.getObjectAt(x, y);

        }

    }

    @Override
    public void onObjectPicked(@NonNull Object3D object) {

        Log.d(TAG, "onObjectPicked: Touchhhhh " + object.getName());

        if (object.getName().equals("cube")) {

            mListener.onClick("this is the engine ventilation");

        } else if (object.getName().equals("cubesecond")) {

            mListener.onClick("this is the left door");
        }
    }

    @Override
    public void onNoObjectPicked() {

    }

    //Personal listener from MyCArcballCamera
    @Override
    public void onObjectClicked(float x, float y) {

        getObjectAt(x, y);

    }


    public interface onRenderListener {

        void onClick(String s);
    }
}
//
//    private double[] mNearPos4;
//    private double[] mFarPos4;
//    private Vector3 mNearPos;
//    private Vector3 mFarPos;
//    private Vector3 mNewObjPos;
//
//    public void move(float x, float y) {
//        if (object3DArrayList != null) {
//
//            for (int i = 0; i < object3DArrayList.size(); i++) {
//
//                int[] viewport = new int[]{0, 0, getViewportWidth(), getViewportHeight()};
//                double[] nearPos = new double[4];
//                double[] farPos = new double[4];
//
//                GLU.gluUnProject(x, getViewportHeight() - y, 0,
//                        getCurrentCamera().getViewMatrix().getDoubleValues(), 0,
//                        getCurrentCamera().getProjectionMatrix().getDoubleValues(), 0,
//                        viewport, 0, nearPos, 0);
//                GLU.gluUnProject(x, getViewportHeight() - y, 1.f,
//                        getCurrentCamera().getViewMatrix().getDoubleValues(), 0,
//                        getCurrentCamera().getProjectionMatrix().getDoubleValues(), 0,
//                        viewport, 0, farPos, 0);
//
//                Vector3 nearVec = new Vector3(nearPos[0] / nearPos[3], nearPos[1] / nearPos[3], nearPos[2] / nearPos[3]);
//                Vector3 farVec = new Vector3(farPos[0] / farPos[3], farPos[1] / farPos[3], farPos[2] / farPos[3]);
//
//                float factor = (float) ((Math.abs(object3DArrayList.get(i).getZ()) + nearVec.z) / (getCurrentCamera().getFarPlane() - getCurrentCamera().getNearPlane()));
//
//                Vector3 diff = farVec.subtract(nearVec);
//                diff.multiply(factor);
//                nearVec.add(diff);
//
//                object3DArrayList.get(i).setX(nearVec.x);
//                object3DArrayList.get(i).setY(nearVec.y - 4);
//            }
//        }
//    }


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
