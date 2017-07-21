package free.scanbvr;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tony.maulaz on 21/07/2017.
 */

public class TessOCR {
    private final TessBaseAPI mTess;
    String datapath;
    Context context;

    public TessOCR(Context context) {
        this.context = context;
        datapath = Environment.getExternalStorageDirectory().getAbsolutePath();

        try{
            File dir = new File(datapath + "/tessdata");
            if( !dir.exists() ) {
                dir.mkdir();
            }

            File file = new File(datapath + "/tessdata/fra.traineddata");
            if ( !file.exists() ) {
                Log.d("mylog", "in file doesn't exist");
                copyFile(context);
            }
        }catch (Exception e){
            Log.e("Exc : ", e.getMessage());
        }


        mTess = new TessBaseAPI();
        String language = "fra";
        mTess.init(datapath, language);
        //Auto only        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_ONLY);
        // }
    }

    private void copyFile(Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream in = assetManager.open("tessdata/fra.traineddata");
            OutputStream out = new FileOutputStream(datapath + "/tessdata/fra.traineddata");
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
        } catch (Exception e) {
            Log.d("mylog", "couldn't copy with the following error : "+e.toString());
        }
    }

    public String getOCRResult(Bitmap bitmap) {
        mTess.setImage(bitmap);
        return mTess.getUTF8Text();
    }

    public void onDestroy() {
        if (mTess != null) mTess.end();
    }
}
