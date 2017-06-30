package se.jayway.wallaceremotecontrol

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var robot = WallaceRobotApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resetConnectionButton.setOnTouchListener {
            v, e ->
            robot.close()
            robot = WallaceRobotApi()
            false
        }

        seekBarLeft.setOnTouchListener { v, e ->
            writeToSocket( seekBarLeft.progress - 100, seekBarRight.progress - 100)
            false
        }
        seekBarRight.setOnTouchListener { v, e ->
            writeToSocket( seekBarLeft.progress - 100, seekBarRight.progress - 100)
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        robot.close()
    }

    private fun writeToSocket( rightMotor: Int, leftMotor: Int) {
        var message = "motor: left ${leftMotor} right ${rightMotor}"
        Log.d("TAG", message)
        robot.send(message);
    }
}
