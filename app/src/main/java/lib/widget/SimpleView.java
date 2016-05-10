package lib.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.cwf.app.cwf.R;

/**
 * Created by n-240 on 2016/5/9.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class SimpleView extends View {

    private Paint paint;
    private float density;
    private int height;
    private int width;

    private int time;

    public SimpleView(Context context) {
        super(context);
        init();
    }

    public SimpleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SimpleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        density = getResources().getDisplayMetrics().density;
        height = getResources().getDisplayMetrics().heightPixels / 2;
        width = getResources().getDisplayMetrics().widthPixels;
        paint = new Paint();
        paint.setTextSize(16 * density);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(width / 100);
        paint.setStrokeCap(Paint.Cap.ROUND);
        time = 0;
        item1 = 0;
        mAnimation animation = new mAnimation();
        animation.setRepeatCount(Animation.INFINITE);
        setAnimation(animation);
        rectF = new RectF();
        rectF.set(width / 2, height / 2, width, height);
        angle = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    private int item1;
    private RectF rectF;
    private int angle;
    private int progress = 0;

    @Override
    protected void onDraw(final Canvas canvas) {

        Paint paint_blue = new Paint();
        paint_blue.setAntiAlias(true);
        paint_blue.setColor(Color.BLUE);
        paint_blue.setStyle(Paint.Style.STROKE);
        paint_blue.setStrokeWidth(width / 100);
        for (int i = 1; i < 10; i++) {
            for (int ii = 0; ii < 5; ii++) {
                canvas.drawCircle(width / 10 * i, height / 10 * ii, width / 20, paint_blue);
            }
        }
        canvas.drawArc(0f, 0f, width / 2, height / 2, 0, 360f / 10 * item1, true, paint);

        canvas.drawText("你好\n我是绘制", width / 2, height / 3 * 2, paint);
        canvas.drawLine(width / 10, height / 10, width / 10 * 9, height / 10 * 9, paint);
        canvas.drawLine(width / 10 * 9, height / 10, width / 10, height / 10 * 9, paint);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.gossip), 35, 340, paint);
        if (time < 50) {
            reset();
        }
        for (int j = 1; j <= item1; j++) {
            canvas.drawLine(width / 10 * j, height / 10, width / 2, height, paint);
        }

        /*绘制只包含边框的圆，圆形进度条*/
        Paint paint1 = new Paint();
        paint1.setColor(Color.YELLOW);
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(width / 50);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeCap(Paint.Cap.ROUND);
        float startAngle = 90 + angle;
        float sweepAngle = 360 - angle * 2;
        paint1.setColor(Color.RED);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, paint1);
        canvas.drawArc(rectF, startAngle, sweepAngle - 60, false, paint1);

//        canvas.save();
//        canvas.rotate(180, getWidth() / 2, getHeight() / 2);
        paint1.setColor(Color.GREEN);
        canvas.drawArc(rectF, 270 - angle, angle * 2, false, paint1);


        /*自定义的横线进度条*/
        Paint paintL1 = new Paint();
        paintL1.setAntiAlias(true);
        paintL1.setColor(Color.BLUE);
        paintL1.setStrokeWidth(width / 150);
        paintL1.setStyle(Paint.Style.STROKE);
        float xs = width / 10;
        float xe = xs + (progress % 100) * width / 1000 * 8;
        float y = height / 10 * 9;
        canvas.drawLine(xs, y, xe, y, paintL1);
        if (xe < width / 10 * 9) {
            Paint paintL2 = new Paint();
            paintL2.setAntiAlias(true);
            paintL2.setColor(Color.LTGRAY);
            paintL2.setStrokeWidth(width / 250);
            paintL2.setStyle(Paint.Style.STROKE);
            canvas.drawLine(xe, y, width / 10 * 9, y, paintL2);
        }

        if (xe > width / 10 * 9 - density * 8)
            xe = width / 10 * 9 - density * 8;
        paintL1.setStrokeWidth(density / 2);
        paintL1.setTextSize(density * 8);
        canvas.drawText(" " + progress % 100 + "% ", xe, y + width / 300, paintL1);

//        canvas.restore();
    }


    private void reset() {
        time++;
        paint.setColor(Color.argb(250, time * 5, 100, 100));
        invalidate();
    }


    private long current = System.currentTimeMillis();

    class mAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (System.currentTimeMillis() - current > 200 && item1 < 10) {
                current = System.currentTimeMillis();
                item1++;
                angle = item1 * 10;
                progress++;
                postInvalidate();
            }
            if (item1 >= 10)
                item1 = 0;
        }
    }
}
