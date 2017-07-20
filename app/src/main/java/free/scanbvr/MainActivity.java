package free.scanbvr;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageSurfaceView mCameraView;
    private VisorView vis;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout cameraView = (FrameLayout) findViewById(R.id.Layout_Camera);
        camera = checkDeviceCamera();

        mCameraView = new ImageSurfaceView(this, camera);
        cameraView.addView(mCameraView);

        Button captureButton = (Button)findViewById(R.id.button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(autoFocusCallback);
                camera.takePicture(null, null, pictureCallback);
            }
        });
    }



    private Camera checkDeviceCamera(){
        Camera mCamera = null;
        try {
            int nbr = Camera.getNumberOfCameras();
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {
            return;
        }
    };

    PictureCallback pictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if(bitmap==null){
                Toast.makeText(MainActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }

            FrameLayout fl = (FrameLayout)findViewById(R.id.Layout_Camera);
            ImageView i = (ImageView)findViewById(R.id.captured_image);

            int ws = Resources.getSystem().getDisplayMetrics().widthPixels;

            int wb = bitmap.getWidth();
            int hb = bitmap.getHeight();
            int wSurf = fl.getWidth();


            Matrix mat = new Matrix();
            mat.postRotate(90);

            //Bitmap crop = Bitmap.createBitmap(bitmap, 0, 0, wb/4, hb/4);
            Bitmap crop = Bitmap.createScaledBitmap(bitmap, wb, hb, true);
            crop = Bitmap.createBitmap(crop,0,0,crop.getWidth(),crop.getHeight(),mat,true);

            float rap =  (float)crop.getWidth() / (float)wSurf;
            int posTopCrop = (int)(80 * rap);
            int heightCrop = (int)(40 * rap);

            // Image size rotate 90° :
            // width = 2448
            // height = 3264
            crop = Bitmap.createBitmap(crop,0,posTopCrop,hb,heightCrop);

            i.setImageBitmap(crop);

            bitmap.recycle();
            //i.setImageBitmap(scaleDownBitmapImage(bitmap,300,200));

            //capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap, 300, 200 ));

            mCameraView.Restart();
        }
    };

        private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            return resizedBitmap;
        }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}

