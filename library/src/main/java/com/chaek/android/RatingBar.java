package com.chaek.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.chaek.android.ratingbar.R;

/**
 * rating bar
 *
 * @author Chaek
 */
public class RatingBar extends View {
    public static final int STAR_COUNT = 5;
    public static final int TEN = 10;
    private int maxScore;
    private int score;

    private int srcWidth;
    private int srcHeight;
    private int starMargin;

    private int starColor;
    private int emptyColor;

    private Bitmap srcBitmap;
    private Bitmap emptyBitmap;
    private Bitmap halfBitmap;

    private int starDrawable;
    private int emptyDrawable;
    private int halfDrawable;

    private boolean isTint;

    private Paint paint;
    private Xfermode xfermode;

    private RatingBarListener ratingBarListener;


    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        starDrawable = a.getResourceId(R.styleable.RatingBar_rating_star_src, R.drawable.default_star);
        emptyDrawable = a.getResourceId(R.styleable.RatingBar_rating_star_empty, 0);
        halfDrawable = a.getResourceId(R.styleable.RatingBar_rating_star_half, 0);
        srcWidth = (int) a.getDimension(R.styleable.RatingBar_rating_star_width, dpToPx(10));
        srcHeight = (int) a.getDimension(R.styleable.RatingBar_rating_star_height, dpToPx(10));
        starMargin = (int) a.getDimension(R.styleable.RatingBar_rating_star_margin, dpToPx(4));
        emptyColor = a.getColor(R.styleable.RatingBar_rating_star_empty_color, 0xffa1a0a0);
        starColor = a.getColor(R.styleable.RatingBar_rating_star_color, 0xfffdd71b);
        maxScore = a.getInt(R.styleable.RatingBar_rating_flag, STAR_COUNT);
        score = a.getInt(R.styleable.RatingBar_rating_start_count, 5);
        isTint = a.getBoolean(R.styleable.RatingBar_rating_star_tint, false);
        a.recycle();

        initView();
    }

    /**
     * init
     */
    private void initView() {
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
        paint = new Paint();
        paint.setAntiAlias(true);
        //init bitmap
        srcBitmap = createDrawableBitmap(starDrawable, srcWidth, srcHeight);
        if (emptyDrawable != 0) {
            emptyBitmap = createDrawableBitmap(emptyDrawable, srcWidth, srcHeight);
        }
        if (halfDrawable != 0) {
            halfBitmap = createDrawableBitmap(halfDrawable, srcWidth, srcHeight);
        }
        if (halfBitmap == null && emptyBitmap != null && maxScore == TEN) {
            halfBitmap = createHalfBitmap();
        }
    }

    private Bitmap createDrawableBitmap(int src, int srcWidth, int srcHeight) {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), src), srcWidth, srcHeight, true);
    }

    private Bitmap createHalfBitmap() {
        Bitmap temp = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(temp);
        canvas.save();
        canvas.clipRect(0, 0, srcWidth / 2, srcHeight);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        canvas.restore();
        canvas.save();
        canvas.clipRect(srcWidth / 2, 0, srcWidth, srcHeight);
        canvas.drawBitmap(emptyBitmap, 0, 0, null);
        canvas.restore();
        return temp;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (ratingBarListener != null) {
            final int action = event.getAction();
            final float x = event.getX();
            score = (int) (x / (getWidth() / maxScore));
            if (score <= 0) {
                score = 0;
            } else if (score >= maxScore) {
                score = maxScore;
            }
            switch (action) {
                case MotionEvent.ACTION_UP:
                    invalidate();
                    performClick();
                    ratingBarListener.setRatingBar(score);
                    break;
                default:
                    ratingBarListener.setRatingBar(score);
                    invalidate();
                    break;
            }
        }
        return true;
    }

    /**
     * add ratingBar touch change score listener
     *
     * @param ratingBarListener listener
     */
    public void setRatingBarListener(RatingBarListener ratingBarListener) {
        this.ratingBarListener = ratingBarListener;
    }

    /**
     * dp to px
     */
    private int dpToPx(int num) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, getContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (srcWidth + starMargin) * STAR_COUNT - starMargin;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = srcHeight;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private boolean isDrawTint() {
        return halfBitmap == null && emptyBitmap == null || isTint;
    }

    private boolean isStarFill(int i) {
        return (i < score && maxScore == 5) || emptyBitmap == null || (maxScore == 10 && i < score / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saveLayerCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);

        for (int i = 0; i < STAR_COUNT; i++) {
            Bitmap bitmap;
            if (isStarFill(i)) {
                bitmap = srcBitmap;
            } else {
                if (maxScore == TEN && halfBitmap != null && i * 2 < score) {
                    //is max score 10  half not null
                    bitmap = halfBitmap;
                } else {
                    bitmap = emptyBitmap;
                }
            }
            canvas.drawBitmap(bitmap, i * (srcWidth + starMargin), 0, paint);
        }


        if (isDrawTint()) {
            paint.setXfermode(xfermode);
            int right = (srcWidth + starMargin) * score;
            if (maxScore == TEN) {
                right = (srcWidth + starMargin) * (score / 2) + (score % 2) * (srcWidth / 2);
            }
            paint.setColor(starColor);

            canvas.drawRect(0, 0, right, srcHeight, paint);
            paint.setColor(emptyColor);
            canvas.drawRect(right, 0, getWidth(), srcHeight, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(saveLayerCount);
        }

    }


    public void setEmptyDrawable(@IntegerRes int emptyDrawable) {
        this.emptyDrawable = emptyDrawable;
        initView();
        invalidate();
    }

    /**
     * 设置未选中的图片着色颜色
     *
     * @param emptyColor 颜色
     */
    public void setEmptyColor(@IntegerRes int emptyColor) {
        this.emptyColor = emptyColor;
        invalidate();
    }


    public void setStarColor(int starColor) {
        this.starColor = starColor;
        invalidate();
    }

    public void setScore(float score) {
        this.score = (int) score;
        invalidate();
    }

    public void setHalfDrawable(int halfDrawable) {
        this.halfDrawable = halfDrawable;
        if (halfDrawable != 0) {
            halfBitmap = createDrawableBitmap(halfDrawable, srcWidth, srcHeight);
        }
        invalidate();
    }


    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
        invalidate();
    }

    public void setSrcHeight(int srcHeight) {
        this.srcHeight = srcHeight;
        initView();
        requestLayout();
        invalidate();
    }

    public void setSrcWidth(int srcWidth) {
        this.srcWidth = srcWidth;
        initView();
        requestLayout();
        invalidate();
    }

    public void setTint(boolean tint) {
        isTint = tint;
        invalidate();
    }


    public interface RatingBarListener {
        void setRatingBar(int score);
    }

}