package se.jayway.wallaceremotecontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by carlemil on 7/19/17.
 */

public class LidarVisualizer extends View {

    private List<LidarData> lidarData = null;

    private Paint paint;

    public LidarVisualizer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LidarVisualizer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LidarVisualizer(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xffff00ff);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        WallaceRobotApi.Companion.getLidarDataPublisher()
                .subscribe(new Action1<List<LidarData>>() {
                    @Override
                    public void call(List<LidarData> lidarData) {
                        setLidarData(lidarData);
                    }
                });
    }


    public void setLidarData(List<LidarData> lidarData) {
        this.lidarData = lidarData;
        if (isAttachedToWindow()) postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (lidarData != null && lidarData.size() > 0) {

            int width = getMeasuredWidth();
            int xoffset = width / 2;
            int hegiht = getMeasuredHeight();
            int yoffset = hegiht / 2;

            float maxLidarValue = 0;
            for (LidarData ld : lidarData) {
                maxLidarValue = maxLidarValue < ld.getDistance() ? ld.getDistance() : maxLidarValue;
            }

            int maxSize = Math.min(width, hegiht);
            float scale = maxSize / maxLidarValue;

            Path path = new Path();

            for (LidarData ld : lidarData) {
                float x = getX(ld, scale, xoffset);
                float y = getY(ld, scale, yoffset);
                if (ld.getDistance() != 0 && ld.getDistance() < 5500) {
                    if (path.isEmpty())
                        path.moveTo(x, y);
                    else
                        path.lineTo(x, y);
                } else {
                    canvas.drawPath(path, paint);
                    path.reset();
                }
            }

        }
    }

    private float getY(LidarData ld, float scale, int yoffset) {
        return (float) Math.cos(ld.getTheta() / 180 * Math.PI) * ld.getDistance() * scale + yoffset;
    }

    private float getX(LidarData ld, float scale, int xoffset) {
        return (float) Math.sin(ld.getTheta() / 180 * Math.PI) * ld.getDistance() * scale + xoffset;
    }
}
