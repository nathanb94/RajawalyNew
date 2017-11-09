package com.example.nathanb.rajawalynew; /**
 * Created by nathanb on 11/9/2017.
 */

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.ALoader;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.async.IAsyncLoaderCallback;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.NormalMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.util.ArrayUtils;
import org.rajawali3d.util.RajLog;

/**
 * Created by nathanb on 11/2/2017.
 */

public class Renderer extends RajawaliRenderer implements IAsyncLoaderCallback {


    private static final float RAY_ITERATIONS = 1000;
    private final Context context;

    private DirectionalLight directionalLight;

    private Sphere earthSphere;
    private PointLight mLight;
    private EllipticalOrbitAnimation3D mLightAnim;
    private RotateOnAxisAnimation mCameraAnim;
    private Object3D parsedObject;
    private double mRotate;
    private int plus;

    public Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);
    }

    @Override
    protected void initScene() {

        mLight = new PointLight();
        mLight.setPosition(0, 0, 4);
        mLight.setPower(3);

        getCurrentScene().addLight(mLight);
        getCurrentCamera().setZ(16);

        getCurrentScene().setBackgroundColor(0.7f, 0.7f, 0.7f, 1.0f);

//        Material material = new Material();
//        material.enableLighting(true);
//        material.setDiffuseMethod(new DiffuseMethod.Lambert());
//        material.setColor(0);
//
//        Texture earthTexture = new Texture("Earth", R.drawable.earthtruecolor_nasa_big);
//
//        try{
//            material.addTexture(earthTexture);
//
//        } catch (ATexture.TextureException error){
//            Log.d("DEBUG", "TEXTURE ERROR");
//        }
//
//        earthSphere = new Sphere(1, 24, 24);
//        earthSphere.setMaterial(material);
//        getCurrentScene().addChild(earthSphere);
//
//        earthSphere.setPosition(2,-3,5);

        final LoaderOBJ loaderOBJ = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.e100mod_obj);
        loadModel(loaderOBJ, this, R.raw.e100mod_obj);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {


        if (parsedObject != null) {
            checkCollision(event.getX(), event.getY(), parsedObject);
        }
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {

        super.onRender(elapsedTime, deltaTime);

        if (parsedObject != null) {

            parsedObject.rotate(Vector3.Axis.Y, plus);
        }
        plus = 0;

        //Log.d(TAG, "onRender: "+mRotate);


    }

    /**
     * Convert the 4D input into 3D space (or something like that, otherwise the gluUnproject values are incorrect)
     *
     * @param v 4D input
     * @return 3D output
     * @author http://stackoverflow.com/users/1029225/mh
     */
    public static float[] fixW(float[] v) {
        float w = v[3];
        for (int i = 0; i < 4; i++)
            v[i] = v[i] / w;
        return v;
    }


    private boolean checkCollision(float x, float y, Object3D object) {


        float[] nearPoint = {0f, 0f, 0f, 0f};
        float[] farPoint = {0f, 0f, 0f, 0f};
        float[] rayVector = {0f, 0f, 0f};

        float[] mmatrix = ArrayUtils.convertDoublesToFloats(getCurrentCamera().getViewMatrix().getDoubleValues());
        float[] pmatrix = ArrayUtils.convertDoublesToFloats(getCurrentCamera().getProjectionMatrix().getDoubleValues());

        int[] mViewport = new int[]{0, 0, mDefaultViewportWidth, mDefaultViewportHeight};

        y = mViewport[3] - y;

        //Retreiving position projected on near plane
        android.opengl.GLU.gluUnProject(x, y, -1f, mmatrix, 0, pmatrix, 0, mViewport, 0, nearPoint, 0);

        //Retreiving position projected on far plane
        android.opengl.GLU.gluUnProject(x, y, 1f, mmatrix, 0, pmatrix, 0, mViewport, 0, farPoint, 0);

        // extract 3d Coordinates put of 4d Coordinates
        nearPoint = fixW(nearPoint);
        farPoint = fixW(farPoint);

        //Processing ray vector
        rayVector[0] = farPoint[0] - nearPoint[0];
        rayVector[1] = farPoint[1] - nearPoint[1];
        rayVector[2] = farPoint[2] - nearPoint[2];

        // calculate ray vector length
        float rayLength = (float) Math.sqrt((rayVector[0] * rayVector[0]) + (rayVector[1] * rayVector[1]) + (rayVector[2] * rayVector[2]));

        //normalizing ray vector
        rayVector[0] /= rayLength;
        rayVector[1] /= rayLength;
        rayVector[2] /= rayLength;

        float[] collisionPoint = {0f, 0f, 0f};
        float[] objectCenter = {
                (float) object.getWorldPosition().x,
                (float) object.getWorldPosition().y,
                (float) object.getWorldPosition().z,
        };

        //Iterating over ray vector to check for collisions
        for (int i = 0; i < 1000; i++) {
            collisionPoint[0] = rayVector[0] * rayLength / 1000 * i;
            collisionPoint[1] = rayVector[1] * rayLength / 1000 * i;
            collisionPoint[2] = -rayVector[2] * rayLength / 1000 * i;

//            Log.d("nathan", "checkCollision vector : x "+collisionPoint[0]+" y "+collisionPoint[1]+" z "+collisionPoint[2]);
//
//            Log.d("nathan", "checkCollision: objects : x "+object.getX()+" y "+object.getY()+" z "+object.getZ()+"/n");

            if (poinSphereCollision(collisionPoint, objectCenter, 2)) {

                Log.d("nathan", "checkCollision vector : x " + collisionPoint[0] + " y " + collisionPoint[1] + " z " + collisionPoint[2]);

                Log.d("nathan", "checkCollision: objects : x " + objectCenter[0] + " y " + objectCenter[1] + " z " + objectCenter[2] + "/n");

                return true;
            }
        }


        return false;

    }


    public static Boolean poinSphereCollision(float[] point, float[] center, float radius) {

//        return ((point[0] - center[0]) * (point[0] - center[0]) +
//                (point[1] - center[1]) * (point[1] - center[1]) +
//                (point[2] - center[2]) * (point[2] - center[2]) < (radius * radius));

        return ((point[0] - center[0]) * (point[0] - center[0]) +
                (point[1] - center[1]) * (point[1] - center[1])  < (radius * radius));
    }


    @Override
    public Vector3 unProject(double x, double y, double z) {

        return super.unProject(x, y, z);

    }

    @Override
    public void onModelLoadComplete(ALoader loader) {

        RajLog.d("Model load complete: " + loader);
        final LoaderOBJ obj = (LoaderOBJ) loader;
        parsedObject = obj.getParsedObject();
        parsedObject.setPosition(Vector3.ZERO);
        parsedObject.setDoubleSided(true);
        parsedObject.setBackSided(true);



        Texture earthTexture = new Texture("Earth", R.drawable.body);
        //NormalMapTexture normalMap = new NormalMapTexture("normalMap", R.drawable.farmhouse_map);

        Material material = new Material();
//        material.enableLighting(true);
//        material.setDiffuseMethod(new DiffuseMethod.Lambert());

        try {
            material.addTexture(mTextureManager.addTexture(earthTexture));
           // material.addTexture(mTextureManager.addTexture(normalMap));

        } catch (ATexture.TextureException error) {
            Log.d("DEBUG", "TEXTURE ERROR");
        }

        parsedObject.setMaterial(material);

        parsedObject.setScale(5);

        parsedObject.setPosition(10,0,0);

        getCurrentScene().addChild(parsedObject);

        mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.Y, 360);
        mCameraAnim.setDurationMilliseconds(8000);
        mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
        mCameraAnim.setTransformable3D(parsedObject);

        getCurrentScene().registerAnimation(mCameraAnim);

        mCameraAnim.play();

        getCurrentCamera().setZ(100);

    }

    @Override
    public void onModelLoadFailed(ALoader aLoader) {
        RajLog.e(this, "Model load failed: " + aLoader);
    }

    public void setPlus(int plus) {
        this.plus = plus;

        mRotate += plus;

        if (mRotate > 360) {

            mRotate -= 360;

        } else if (mRotate < 0) {

            mRotate += 360;
        }

    }

    public void onTouch(MotionEvent motionEvent) {

        if (parsedObject != null) {
            checkCollision(motionEvent.getX(), motionEvent.getY(), parsedObject);

          //  Vector3 vec3 = unProject(motionEvent.getX(), motionEvent.getY(), 1);

           // Log.d(TAG, "checkCollision vec3 : x " + vec3.x + " y " + vec3.y + " z " + vec3.z);
        }
    }
}
