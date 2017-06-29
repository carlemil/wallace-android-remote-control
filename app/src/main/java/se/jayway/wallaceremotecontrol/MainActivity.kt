package se.jayway.wallaceremotecontrol

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarLeft.setOnTouchListener{v, e ->
            writeToSocket(seekBarLeft.progress-100, seekBarRight.progress-100)
            false
        }
        seekBarRight.setOnTouchListener{v, e ->
            writeToSocket(seekBarLeft.progress-100, seekBarRight.progress-100)
            false
        }

    }

    private fun writeToSocket(rightMotor: Int, leftMotor: Int) {
        
    }
}
