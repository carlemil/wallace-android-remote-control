package se.jayway.wallaceremotecontrol

import android.util.Log
import okhttp3.*
import okio.ByteString


/**
 * Created by carlemil on 6/30/17.
 */
class WallaceRobotApi() {

    private var client = OkHttpClient()
    private val listener = EchoWebSocketListener()
    private var ws: WebSocket?

    init {
        val request = Request.Builder().url("ws://10.0.100.84:8887").build()
        ws = client.newWebSocket(request, listener)
        //client.dispatcher().executorService().shutdown()

    }

    internal class EchoWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            //        webSocket.send("Hello, it's SSaurel !");
            //        webSocket.send("What's up ?");
            //        webSocket.send(ByteString.decodeHex("deadbeef"));
            //        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
            Log.d("TAG", "Web socket opened")
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            Log.d("TAG", "Receiving : " + text!!)
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
            Log.d("TAG", "Receiving bytes : " + bytes!!.hex())
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            webSocket!!.close(NORMAL_CLOSURE_STATUS, null)
            Log.d("TAG", "Closing : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
            Log.d("TAG", "Error : " + t!!.message)
        }

        companion object {

            private val NORMAL_CLOSURE_STATUS = 1000
        }

    }

    fun send(message: String) {
        ws?.send(message)
    }

    fun close() {
        ws?.close(1000, "Exiting")

    }
}

