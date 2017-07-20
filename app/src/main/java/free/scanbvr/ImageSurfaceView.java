package free.scanbvr;

import android.content.res.Resources;
import android.hardware.Camera;
import android.content.Context;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.ViewGroup;

import java.io.IOException;
import java.security.Policy;
import java.util.List;

/**
 * Created by tony.maulaz on 19/07/2017.
 */

public class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceHolder surfaceHolder;

    public ImageSurfaceView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {

            int mesW = getMeasuredWidth();
            int screenH = Resources.getSystem().getDisplayMetrics().heightPixels;

            List<Camera.Size> supPrev = camera.getParameters().getSupportedPreviewSizes();

            int err = Integer.MAX_VALUE;
            Camera.Size size = null;
            int ind = 0;
            for(Camera.Size s : supPrev )
            {
                if( s.width < mesW )
                {
                    int dif = Math.abs( s.width - mesW );
                    if( dif < err )
                    {
                        size = s;
                        err = dif;
                    }
                }
                ind++;
            }

            float ratio = (float)size.height / (float)size.width;

            camera.setDisplayOrientation(90);

            Camera.Parameters param = camera.getParameters();
            String mode = param.getFocusMode();
            List<String> lm = param.getSupportedFocusModes();
            param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            param.setFlashMode( Camera.Parameters.FLASH_MODE_ON );

            param.setPreviewSize(size.width, size.height);
            camera.setParameters(param);
            Camera.Size s = camera.getParameters().getPreviewSize();

            ViewGroup.LayoutParams lp = getLayoutParams();

            int hSurf = ((int)(mesW/ratio));
            //Adatpte si la heuteur est trop grande
            /*if (hSurf > screenH - 300)
            {
                hSurf = screenH-300;
                lp.width = (int)(hSurf * ratio);
            }
            else*/
            {
                lp.width = mesW;
            }

            lp.height = hSurf;
            setLayoutParams(lp);

            this.camera.setPreviewDisplay(holder);
            this.camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if( holder.getSurface() == null )
            return;

       try {
           camera.stopPreview();
       }catch (Exception e){
       }

        try{
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
        }
    }

    public void Restart(){
        try{
            camera.startPreview();
        } catch (Exception e) {
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.camera.stopPreview();
        this.camera.release();
    }
}
