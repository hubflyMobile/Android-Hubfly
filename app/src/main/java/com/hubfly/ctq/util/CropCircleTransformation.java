package com.hubfly.ctq.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class CropCircleTransformation implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;
    int Border_width = 3;
    int mColor;
    Paint mBorderPaint = new Paint();

    public CropCircleTransformation(Context context) {
        this(Glide.get(context).getBitmapPool());
    }

    public CropCircleTransformation(Context context, int bwidth, String bcolor) {
        this(Glide.get(context).getBitmapPool());
        Border_width = bwidth;
        mColor = Color.parseColor(bcolor);
        InitBorder();
    }

    public CropCircleTransformation(BitmapPool pool) {
        this.mBitmapPool = pool;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        int size = Math.min(source.getWidth(), source.getHeight());

        int width = (source.getWidth() - size) / 2;
        int height = (source.getHeight() - size) / 2;

        Bitmap bitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader =
                new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        if (width != 0 || height != 0) {
            // source isn't square, move viewport to center
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        float cx = r;
        float cy = r;

        canvas.drawCircle(cx, cy, r, paint);
        canvas.drawCircle(cx, cy, r - mBorderPaint.getStrokeWidth() / 2, mBorderPaint);
        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    @Override
    public String getId() {
        return "CropCircleTransformation()";
    }

    void InitBorder() {
        mBorderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mColor);
        mBorderPaint.setStrokeWidth(Border_width);
    }
}

