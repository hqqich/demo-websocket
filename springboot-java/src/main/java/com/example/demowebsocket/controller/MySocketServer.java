package com.example.demowebsocket.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@ServerEndpoint("/myws/{msg}")
@Component
@Slf4j
public class MySocketServer {

  private String msg = "";

  /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
  private Session session;

  @OnOpen
  public void onOpen(Session session, @PathParam("msg") String msg) {
    this.session = session;
    this.msg = msg;

    System.out.println(msg);

    try {
      //for (int i = 0; i < 5; i++) {
        sendMessage("aaaaa");
        Thread.sleep(1000);
      //}
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * 实现服务器主动推送
   */
  public void sendMessage(String message) throws IOException {
    this.session.getBasicRemote().sendText(message);


    try (FileInputStream fileInputStream = new FileInputStream("G:" + File.separator + "aaa.jpg")) {
      FileChannel inChannel = fileInputStream.getChannel();
      ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
      long start = System.currentTimeMillis();
      while (true) {
        int eof = inChannel.read(byteBuffer);
        if (eof == -1) break;
        byteBuffer.flip();
        this.session.getBasicRemote().sendBinary(byteBuffer);
        byteBuffer.clear();
      }
      System.out.println("spending : " + (System.currentTimeMillis() - start));
      inChannel.close();
    }

    //new ByteBuffer()

    //this.session.getBasicRemote().sendBinary();
  }

  /** 连接关闭调用的方法 */
  @OnClose
  public void onClose() {
    System.out.println(msg + "关闭");
  }

  /**
   * 收到客户端消息后调用的方法
   *
   * @param message 客户端发送过来的消息
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    log.info("用户消息:" + msg + ",报文:" + message);
  }

  /**
   * @param session
   * @param error
   */
  @OnError
  public void onError(Session session, Throwable error) {
    System.out.println(msg + "错误");
  }
}
