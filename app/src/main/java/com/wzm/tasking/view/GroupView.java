package com.wzm.tasking.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.wzm.tasking.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hiwhitley on 2016/3/10.
 */
public class GroupView extends ImageView {

    /**
     * 图片之间的距离
     */
    private int padding = 0;
    /**
     * 圆角值
     */
    private int cornor = 0;

    private int width, height;
    ;
    /**
     * 头像模式 圆的
     */
    public static final int FACETYPE_CIRCLE = 1;
    /**
     * 头像模式 方的 最多9个
     */
    public static final int FACETYPE_SQUARE = 2;

    private Context mContext;

    private int mViewType;//默认圆形
    private Bitmap[] mBitmaps;
    private int background;

    public GroupView(Context context, Bitmap[] bitmaps) {
        super(context);
        this.mBitmaps = bitmaps;
    }

    public GroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GroupView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.GroupView_type:
                    mViewType = a.getInt(R.styleable.GroupView_type, FACETYPE_CIRCLE);
                    break;
                case R.styleable.GroupView_cornor:
                    cornor = (int) a.getDimension(R.styleable.GroupView_cornor, 0);
                    break;
                case R.styleable.GroupView_padding:
                    padding = (int) a.getDimension(R.styleable.GroupView_padding, 3);
                case R.styleable.GroupView_backgroundGP:
                    background = a.getColor(R.styleable.GroupView_backgroundGP, Color.parseColor("#DDDFD4"));
                    break;
                default:
                    break;
            }
        }

        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        return width;
    }

    private int measureHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        return height;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        if (mBitmaps == null) {
            super.onDraw(canvas);
            return;
        }
        Paint paint = new Paint();
        try {
            canvas.drawBitmap(createGroupFace(mViewType, mContext, mBitmaps), 0, 0, paint);
        } catch (Exception e) {

        }
    }

    public void setImageBitmaps(Bitmap[] bitmaps) {
        mBitmaps = bitmaps;
    }

    public void setImageBitmaps(List<Bitmap> bitmaps) {
        mBitmaps = bitmaps.toArray(new Bitmap[bitmaps.size()]);
        setImageBitmap(null);

    }

    public Bitmap createGroupFace(int type, Context context,
                                  Bitmap[] bitmapArray) {
//        System.out.println("hiwhitley" + "type" + type);
        if (type == FACETYPE_CIRCLE) {
            return createGroupBitCircle(bitmapArray, context);
        }
        return createTogetherBit(bitmapArray, context);
    }

    private static Bitmap scaleBitmap(float paramFloat, Bitmap paramBitmap) {
        Matrix localMatrix = new Matrix();
        localMatrix.postScale(paramFloat, paramFloat);
        return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(),
                paramBitmap.getHeight(), localMatrix, true);
    }

    /**
     * 拼接群头像 圆形版的
     *
     * @param bitmapArray
     * @param context
     * @return
     */
    private Bitmap createGroupBitCircle(Bitmap[] bitmapArray,
                                        Context context) {
        if (bitmapArray == null) {
            return null;
        }
        if (bitmapArray.length < 1 && bitmapArray.length > 9) {
            return null;
        }
        // 先取一个获取宽和高
        Bitmap tempBitmap = (Bitmap) bitmapArray[0];
        if (tempBitmap == null) {
            return null;
        }
        // 画布的宽
        int tempWidth = width;
        // 画布的高
        int tempHeight = height;
        Bitmap canvasBitmap = Bitmap.createBitmap(tempWidth, tempHeight,
                Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(canvasBitmap);
        localCanvas.drawColor(background);
        JoinBitmaps.join(localCanvas, Math.min(tempWidth, tempHeight),
                Arrays.asList(bitmapArray));
        return canvasBitmap;
    }

    /**
     * 拼接群头像
     *
     * @param paramList
     * @param context
     * @return 头像本地路径
     */
    @SuppressWarnings("unused")
    private Bitmap createTogetherBit(Bitmap[] paramList,
                                     final Context context) {
        if (paramList.length < 1 && paramList.length > 9) {
            return null;
        }
        // 先取一个获取宽和高
        Bitmap tempBitmap = (Bitmap) paramList[0];
        if (tempBitmap == null) {
            return null;
        }
        // 画布的宽
        int tempWidth = width;
        // 画布的高
        int tempHeight = height;
        // 创建一个空格的bitmap
        Bitmap canvasBitmap = Bitmap.createBitmap(tempWidth, tempHeight,
                Bitmap.Config.ARGB_8888);
        // 头像的数量
        int bitmapCount = paramList.length;
        Canvas localCanvas = new Canvas(canvasBitmap);
        localCanvas.drawColor(background);
        int colum = 0;

        if (bitmapCount > 1 && bitmapCount < 5) {
            colum = 2;
        } else if (bitmapCount > 4 && bitmapCount < 10) {
            colum = 3;
        } else {
            colum = 1;
        }
        float scale = 1.0F / colum;
        // 根据列数缩小
        Bitmap scaledBitmap = scaleBitmap(scale, tempBitmap);
        if (padding > 0) {
            padding = dip2px(context, padding);
            // 如果有内边距 再次缩小
            float paddingScale = (float) (tempWidth - (colum + 1) * padding)
                    / colum / scaledBitmap.getWidth();
            scaledBitmap = scaleBitmap(paddingScale, scaledBitmap);
            scale = scale * paddingScale;
        }
        // 第一行的 头像个数
        int topRowCount = bitmapCount % colum;
        // 满行的行数
        int rowCount = bitmapCount / colum;
        if (topRowCount > 0) {
            // 如果第一行头像个数大于零 行数加1
            rowCount++;
        } else if (topRowCount == 0) {
            // 6 或者 9 第一行头像个数和列数一致
            topRowCount = colum;
        }
        // 缩小后头像的宽
        int scaledWidth = scaledBitmap.getWidth();
        // 缩小后头像的高
        int scaledHeight = scaledBitmap.getHeight();
        // 第一个头像与画布顶部的距离
        int firstTop = ((tempHeight - (rowCount * scaledHeight + (rowCount + 1)
                * padding)) / 2)
                + padding;
        // 第一个头像与画布左部的距离
        int firstLeft = ((tempWidth - (topRowCount * scaledWidth + (topRowCount + 1)
                * padding)) / 2)
                + padding;
        for (int i = 0; i < paramList.length; i++) {
            if (i == 9) {// 达到上限 停止
                break;
            }
            // 按照最终压缩比例压缩
            Bitmap bit = scaleBitmap(scale, (Bitmap) paramList[i]);
            if (cornor > 0) {
                // 圆角化
                bit = GetRoundedCornerBitmap(bit);
            }
            localCanvas.drawBitmap(bit, firstLeft, firstTop, null);
            firstLeft += (scaledWidth + padding);
            if (i == topRowCount - 1 | tempWidth - firstLeft < scaledWidth) {
                firstTop += (scaledHeight + padding);
                firstLeft = padding;
            }
            bit.recycle();
        }
        // 重置padding
        //padding = 2;
        localCanvas.save(Canvas.ALL_SAVE_FLAG);
        localCanvas.restore();
        return canvasBitmap;
    }

    /**
     * 圆角
     *
     * @param bitmap
     * @return
     */
    private Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, cornor, cornor, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    private int dip2px(Context context, float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, context.getResources().getDisplayMetrics()) + 0.5f);
    }

}
