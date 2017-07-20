package se.jayway.wallaceremotecontrol

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    var robot = WallaceRobotApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resetConnectionButton.setOnClickListener {
            robot.close()
            robot = WallaceRobotApi()
            Log.d("TAG", "Reset web-socket clicked.")
        }
        stopMotors.setOnClickListener {
            seekBarRight.progress = 255
            seekBarLeft.progress = 255
            writeMotorSpeedToSocket(0, 0)
        }

        val stringPublishSubject = PublishSubject.create<Unit>()
        seekBarLeft.setOnTouchListener({ _, _ ->
            stringPublishSubject.onNext(Unit)
            false
        })
        seekBarRight.setOnTouchListener({ _, _ ->
            stringPublishSubject.onNext(Unit)
            false
        })
        stringPublishSubject.sample(500, TimeUnit.MILLISECONDS)
                .subscribe({ _ -> writeMotorSpeedToSocket(seekBarLeft.progress - 255, seekBarRight.progress - 255) })

        Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe({ requestLidarData() })
    }

    override fun onDestroy() {
        super.onDestroy()
        robot.close()
    }

    private fun writeMotorSpeedToSocket(rightMotor: Int, leftMotor: Int) {
        var message = WallaceRobotApi.MOTOR_PREFIX + " left ${leftMotor} right ${rightMotor}"
        Log.d("TAG", message)
        robot.send(message)
    }

    private fun requestLidarData() {
        var message = WallaceRobotApi.LIDAR_DATA_PREFIX
        Log.d("TAG", message)
        robot.send(message)
    }

}
