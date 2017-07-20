package se.jayway.wallaceremotecontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * Created by carlemil on 7/19/17.
 */

class LidarVisualizer : View {

    private var lidarData: List<LidarData>? = null

    private var paint: Paint? = null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint!!.color = 0xffff00ff.toInt()
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeWidth = 4f

        WallaceRobotApi.lidarDataPublisher
                .subscribe { lidarData -> setLidarData(lidarData) }
    }


    fun setLidarData(lidarData: List<LidarData>) {
        this.lidarData = lidarData
        if (isAttachedToWindow) postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (lidarData != null && lidarData!!.size > 0) {

            val width = measuredWidth
            val xoffset = width / 2
            val hegiht = measuredHeight
            val yoffset = hegiht / 2

            var maxLidarValue = 8000f
//            for (ld in lidarData!!) {
//                maxLidarValue = if (maxLidarValue < ld.distance) ld.distance else maxLidarValue
//            }

            val maxSize = Math.min(width, hegiht)
            val scale = maxSize / maxLidarValue

            val path = Path()

            for (ld in lidarData!!) {
                val x = getX(ld, scale, xoffset)
                val y = getY(ld, scale, yoffset)
                if (ld.distance != 0f ){//&& ld.distance < 5500) {
                    if (path.isEmpty)
                        path.moveTo(x, y)
                    else
                        path.lineTo(x, y)
                } else {
                    canvas.drawPath(path, paint!!)
                    path.reset()
                }
            }

        }
    }

    private fun getY(ld: LidarData, scale: Float, yoffset: Int): Float {
        return Math.cos(ld.theta / 180 * Math.PI).toFloat() * ld.distance * scale + yoffset
    }

    private fun getX(ld: LidarData, scale: Float, xoffset: Int): Float {
        return Math.sin(ld.theta / 180 * Math.PI).toFloat() * ld.distance * scale + xoffset
    }
}
