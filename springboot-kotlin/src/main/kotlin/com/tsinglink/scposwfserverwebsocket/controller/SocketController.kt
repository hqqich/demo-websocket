package com.tsinglink.scposwfserverwebsocket.controller

import com.tsinglink.scposwfserverwebsocket.util.otherwise
import com.tsinglink.scposwfserverwebsocket.util.yes
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

@Component
open class SocketController : WebSocketHandler {


    lateinit var hashMap: ConcurrentHashMap<String, Any>


    init {
        println("SocketController init")
        hashMap = ConcurrentHashMap<String, Any>()
    }


    override fun handleTransportError(session: WebSocketSession, t: Throwable) {
        println("handleTransportError")
    }

    override fun afterConnectionClosed(session: WebSocketSession, cs: CloseStatus) {
        println("afterConnectionClosed")
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        println("handleMessage: $message")
        session.sendMessage(message)
    }

    // 用户进入系统监听
    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("afterConnectionEstablished ${session}")

        val uri: URI? = session.uri

        val toString: String = uri.toString()

        val param: java.util.HashMap<String, Any> = getParam(toString)

        param.containsKey("wtIndex").yes {
            // 是风机连上
            println("是风机连上")
        }.otherwise {
            param.containsKey("facIndex").yes {
                // 是风场连上
                println("是风场连上")
            }.otherwise {
                // 代表全省大屏
                println("代表全省大屏")

            }
        }




        println(param)

    }

    override fun supportsPartialMessages() = false


    fun getParam(url: String): HashMap<String, Any> {
        /**
         * 例如：http://qqq/book?id=1&name=烫烫烫
         */
//        var url="http://qqq/book?id=1&name=烫烫烫"
        val urlValue = url.substringAfter('?', "")//获取?后的串

        /**
         * 切割字符串得到n组k-v型列表
         */
        val urlParams = urlValue.split("&")
//        val urlParams = Bundle()

        val result: HashMap<String, Any> = HashMap()

        urlParams.forEach {
            val param = it.split("=")
            if (param.size == 2) {
                result[param[0]] = param[1]
            }
        }


        println(result)

        return result

    }

}