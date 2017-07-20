package se.jayway.wallaceremotecontrol

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okio.ByteString
import rx.subjects.PublishSubject


/**
 * Created by carlemil on 6/30/17.
 */
class WallaceRobotApi() {

    private val NORMAL_CLOSURE_STATUS = 1000

    private val ws = OkHttpClient().newWebSocket(
            Request.Builder().url("ws://10.0.201.80:8887").build(),
            WallaceWebSocketListener())

    internal class WallaceWebSocketListener : WebSocketListener() {
        internal var gson = Gson()
        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            Log.d("TAG", "Web socket opened " + webSocket.toString())
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            Log.d("TAG", "Receiving : " + text!!)
            when {
                text.startsWith(Companion.LIDAR_DATA_PREFIX) -> {
                    lidarDataPublisher.onNext(gson.fromJson<List<LidarData>>(text.substring(WallaceRobotApi.LIDAR_DATA_PREFIX.length), object : TypeToken<List<LidarData>>() {}.type))
                }
            }
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
            Log.d("TAG", "Receiving bytes : " + bytes!!.hex())
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            Log.d("TAG", "Closing : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
            Log.d("TAG", "Error : " + t!!.message)
        }
    }

    fun send(message: String) {
        ws?.send(message)
    }

    fun close() {
        ws?.close(NORMAL_CLOSURE_STATUS, "Exiting")
    }

    companion object {
        val MOTOR_PREFIX = "set_motor_speeds:"
        val LIDAR_DATA_PREFIX = "get_lidar_data:"

        val lidarDataPublisher = PublishSubject.create<List<LidarData>>()
    }
}

