package com.tsinglink.scposwfserverwebsocket.client

import java.net.URI
import java.util.*
import javax.websocket.*

@ClientEndpoint
class SocketClient(uri: URI, val openHandler: () -> Unit, val closeHandler: () -> Unit, val errorHandler: (t: Throwable) -> Unit, val msgHandler:(msg: String) -> Unit) {

    private var session: Session? = null

    init {
        try {
            val container = ContainerProvider.getWebSocketContainer()
            container.connectToServer(this, uri)
        } catch (th: Throwable) {
            throw RuntimeException(th)
        }
    }

    @OnOpen
    fun onOpen(session: Session) {
        this.session = session
        openHandler()
    }

    @OnClose
    fun onClose(session: Session, reason: CloseReason) {
        this.session = null
        closeHandler()
    }

    @OnMessage
    fun onMessage(message: String) {
        msgHandler(message)
    }

    @OnError
    fun onError(t: Throwable) {
        errorHandler(t)
    }

    fun sendMessage(message: String) {
        session?.basicRemote?.sendText(message)
    }
}

class SocketIO {

    private var endPoint: SocketClient? = null
    private var timerHeartbeat: Timer? = null
    private var working = false

    init {
        endPoint = SocketClient(URI("ws://127.0.0.1:9011/ws"),
            {
                // open callback
//                startHeartHeat()
            },
            {
                // close callback
                working = false
            },
            {
                // error callback
                println("SocketIO Error: $it")
            },
            {
                // message callback
                println("received: $it")
            })
    }
}

//private fun startHeartHeat() {
//    working = true
//    timerHeartbeat = Timer("_socket_heartbeat_")
//    timerHeartbeat?.schedule(timerTask {
//        if (!working) {
//            this.cancel()
//            timerHeartbeat?.cancel()
//            timerHeartbeat = null
//            return@timerTask
//        }
//        endPoint?.sendMessage(MSG_HEARTBEAT)
//    }, 0, 10000L)
//}
//}