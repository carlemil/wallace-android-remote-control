package se.jayway.wallaceremotecontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by carlemil on 6/28/17.
 */

class RobotController : View {
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup()
    }

    constructor(context: Context) : super(context) {
        setup()
    }

    val edgePaint = Paint()
    val controllerPaint = Paint()

    var x = 0
    var y = 0

    fun setup() {
        edgePaint.setColor(0xFF00FF00.toInt())
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.strokeWidth = 10F
        edgePaint.setAntiAlias(true);
        controllerPaint.setColor(0xFFFF0000.toInt())
        controllerPaint.setStyle(Paint.Style.FILL);
        controllerPaint.strokeWidth = 10F
        controllerPaint.setAntiAlias(true);

        setOnTouchListener { v, event ->
            x = event.x.toInt()
            y = event.y.toInt()
            Log.d("TAG", "x: ${x}, ${y}")
            invalidate()
            true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (widthMeasureSpec < heightMeasureSpec)
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        else
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        x = width / 2
        y = height / 2
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas);
        canvas?.drawColor(0xFF0000FF.toInt())
        canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), edgePaint)
        canvas?.drawCircle(x.toFloat(), y.toFloat(), 50F, controllerPaint)
        canvas?.drawLine((width / 2).toFloat(), (height / 2).toFloat(), x.toFloat(), y.toFloat(), controllerPaint)
    }

}

