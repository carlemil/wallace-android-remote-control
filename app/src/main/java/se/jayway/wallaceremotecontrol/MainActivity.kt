package se.jayway.wallaceremotecontrol

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
                .subscribe({ s -> writeMotorSpeedToSocket(seekBarLeft.progress - 255, seekBarRight.progress - 255) })

    }


    override fun onDestroy() {
        super.onDestroy()
        robot.close()
    }

    private fun writeMotorSpeedToSocket(rightMotor: Int, leftMotor: Int) {
        var message = "motor: left ${leftMotor} right ${rightMotor}"
        Log.d("TAG", message)
        robot.send(message);
    }

}
