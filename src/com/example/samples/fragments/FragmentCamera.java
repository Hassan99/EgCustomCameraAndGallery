package com.example.samples.fragments;

/**
 * Created by hassan on 14-09-15.
 */

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.samples.R;
import com.example.samples.activity.MainActivity;
import com.example.samples.additionalclasses.TempModels;
import com.example.samples.db.DbUris;
import com.example.samples.dialogfrgment._CameraDialogFragment;



public class FragmentCamera extends _CameraDialogFragment implements SurfaceHolder.Callback, OnClickListener {

    public Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Parameters par;
    public boolean previewing = false;


    Button btnTakePicture, btnFlashOn, btnFlashOff;
    public Button btnImageOk, btnImageCancel;
    ImageView imgPreviewView;
    boolean flashStatus;
    EditText edtLabel;
    View view;


    public static final int MEDIA_TYPE_IMAGE = 1;


    RelativeLayout rltImagePreview, rltControls;
    public Uri uri;
    public boolean captureState = true;
    TempModels tempModels= TempModels.getInstance();


    @SuppressLint("NewApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (getDialog() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                getDialog().getWindow().setBackgroundDrawableResource(
                        android.R.drawable.screen_background_light_transparent);
            }
        }

        view = inflater.inflate(R.layout.fragment_camera, null);


        surfaceView = (SurfaceView) view.findViewById(R.id.camerapreview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btnImageOk = (Button) view.findViewById(R.id.btnImageOk);
        btnImageCancel = (Button) view.findViewById(R.id.btnImageCancel);

        imgPreviewView = (ImageView) view.findViewById(R.id.imgPreviewView);
        rltImagePreview = (RelativeLayout) view.findViewById(R.id.rltImagePreview);
        rltControls = (RelativeLayout) view.findViewById(R.id.rltControls);

        btnTakePicture = (Button) view.findViewById(R.id.btnTakepicture);//Button capture initialized.
        btnFlashOn = (Button) view.findViewById(R.id.btnFlashOn);
        btnFlashOff = (Button) view.findViewById(R.id.btnFlashOff);
        edtLabel=(EditText)view.findViewById(R.id.edtLabel);
        btnTakePicture.setOnClickListener(this);
        btnFlashOn.setOnClickListener(this);
        btnFlashOff.setOnClickListener(this);
        btnImageOk.setOnClickListener(this);
        btnImageCancel.setOnClickListener(this);

        visibleSurfaceView();
        return view;

    }

    boolean checkFlashStatus() {
        boolean hasFlash = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        return hasFlash;
    }

    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {// TODO Auto-generated method stub
            btnTakePicture.setEnabled(true);
        }
    };

    ShutterCallback myShutterCallback = new ShutterCallback() {

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub
        }
    };

    PictureCallback myPictureCallback_RAW = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub

        }
    };


    PictureCallback myPictureCallback_JPG = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            uri = Uri.fromFile(pictureFile);


            OutputStream imageFileOS;
            try {
                imageFileOS = getActivity().getContentResolver().openOutputStream(uri);
                imageFileOS.write(arg0);
                imageFileOS.flush();
                imageFileOS.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            visibleImagePreview();
            preview(uri);

        }
    };

    public void preview(Uri uri) {
        captureState = true;
        btnTakePicture.setEnabled(true);
        imgPreviewView.invalidate();
        File imgFile = new File(uri.getPath());

        if (imgFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getPath(), options);
            imgPreviewView.setImageBitmap(bitmap);

        }

    }

    void flashVisibilityStatus() {
        if (flashStatus) {
            flashOff();
            par = camera.getParameters();
            par.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(par);
        } else {
            if (checkFlashStatus()) {
                flashOn();
                par = camera.getParameters();
                par.setFlashMode(Parameters.FLASH_MODE_ON);
                camera.setParameters(par);
            } else {
                flashOff();
                par = camera.getParameters();
                par.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(par);
            }

        }
    }

    void flashOn() {
        btnFlashOn.setVisibility(View.VISIBLE);
        btnFlashOff.setVisibility(View.GONE);
    }

    void flashOff() {
        btnFlashOff.setVisibility(View.VISIBLE);
        btnFlashOn.setVisibility(View.GONE);
    }

    private static File getOutputMediaFile(int type) {
            /* ToDo need to check if the SD card is present or not.
               ToDo using Environment.getExternalStorageState() before doing this....*/

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "BNI");


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    @SuppressLint("NewApi")
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        if (previewing) {
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null) {
            try {
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera = Camera.open();
        flashVisibilityStatus();
        camera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTakepicture:
                if (captureState) {
                    captureState = false;
                    camera.autoFocus(myAutoFocusCallback);// Auto Focus callback will be called when the user click on the capture button
                    new Handler().postDelayed(new Runnable() {//After the autofocus end button capture call bak will be called.

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            camera.takePicture(myShutterCallback,
                                    myPictureCallback_RAW, myPictureCallback_JPG);

                        }
                    }, 0);//here 2000 is the auto focus time.
                }

                break;
            case R.id.btnFlashOn:
                flashStatus = true;
                flashVisibilityStatus();
                break;
            case R.id.btnFlashOff:
                flashStatus = false;
                flashVisibilityStatus();
                break;
            case R.id.btnImageOk:
            	if(edtLabel.getText().length()>0){
            	tempModels.setUri(uri);
            	DbUris dbUris= new DbUris();
            	dbUris.imgPath=uri.getPath();
            	dbUris.label=edtLabel.getText().toString();
            	dbUris.save();
            	Toast.makeText(getActivity(), "File Saved To Database", Toast.LENGTH_LONG).show();  
            	((MainActivity)getActivity()).backState();
            	}
            	else{
            		Toast.makeText(getActivity(), "Plase Enter the Label", Toast.LENGTH_LONG).show();
            	}
                break;

            case R.id.btnImageCancel:
                visibleSurfaceView();
                break;

            default:
                break;
        }

    }


    

    void visibleImagePreview() {
        rltImagePreview.setVisibility(View.VISIBLE);
        visibleStausOfSurfaceView();
    }

    void visibleSurfaceView() {
        rltImagePreview.setVisibility(View.GONE);
        surfaceView.setVisibility(View.VISIBLE);
        rltControls.setVisibility(View.VISIBLE);
    }

    void visibleStausOfSurfaceView() {
        surfaceView.setVisibility(View.GONE);
        rltControls.setVisibility(View.GONE);
    }

  

}