package com.wzm.tasking.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class CircleImageView extends ImageView {
    private Paint paint;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = getRoundRectBitmap(bitmap);

            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
            paint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    private Bitmap getRoundRectBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = width > height ? height : width;
        Bitmap backGround = Bitmap.createBitmap(r, r, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backGround);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        canvas.drawRoundRect(rect, r >> 1, r >> 1, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        return backGround;
    }

}
