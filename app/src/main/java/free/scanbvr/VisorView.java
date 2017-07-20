package free.scanbvr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tony.maulaz on 20/07/2017.
 */

public class VisorView extends View {

    float RectLeft, RectTop, RectRight, RectBottom;
    float devWidth;

    public VisorView(Context context){
        super(context);
    }

    public VisorView(Context context, AttributeSet attrs){
        super(context,attrs);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int mesH = getMeasuredHeight();
        int mesW = getMeasuredWidth();

        Paint  paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(3);
        RectLeft = 20;
        RectTop = 80 ;
        RectRight = mesW - 20 - RectLeft;
        RectBottom = RectTop + 40;
        Rect rec=new Rect((int) RectLeft,(int)RectTop,(int)RectRight,(int)RectBottom);
        canvas.drawRect(rec,paint);
    }
}
