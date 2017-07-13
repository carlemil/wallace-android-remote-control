package se.jayway.wallaceremotecontrol

import android.util.Log
import okhttp3.*
import okio.ByteString


/**
 * Created by carlemil on 6/30/17.
 */
class WallaceRobotApi() {

    private val NORMAL_CLOSURE_STATUS = 1000

    private val ws = OkHttpClient().newWebSocket(
            Request.Builder().url("ws://10.0.201.80:8887").build(),
            WallaceWebSocketListener())

    internal class WallaceWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            Log.d("TAG", "Web socket opened "+webSocket.toString())
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            Log.d("TAG", "Receiving : " + text!!)
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
            Log.d("TAG", "Receiving bytes : " + bytes!!.hex())
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            //webSocket?.close(NORMAL_CLOSURE_STATUS, null)

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
}

