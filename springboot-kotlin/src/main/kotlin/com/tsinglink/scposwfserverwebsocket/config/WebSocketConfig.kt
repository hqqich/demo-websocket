package com.tsinglink.scposwfserverwebsocket.config

import com.tsinglink.scposwfserverwebsocket.controller.SocketController
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
open class WebSocketConfig: WebSocketConfigurer {

    private fun getSocketHandler(): WebSocketHandler = SocketController()

    // 注册路由
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(getSocketHandler(), "/ws").setAllowedOrigins("*")
    }
}