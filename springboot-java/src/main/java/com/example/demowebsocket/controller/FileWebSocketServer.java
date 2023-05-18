package com.example.demowebsocket.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.example.demowebsocket.util.FormatUtil;
import com.example.demowebsocket.util.Message;
import com.example.demowebsocket.util.ServerConstant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName WebSocketServer
 * @date 2022/07/08 20:18:27
 */
@Component
@ServerEndpoint("/fileWebsocket/{bindIp}")
@Slf4j
public class FileWebSocketServer {

    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户名为key，WebSocket为对象保存起来
     */
    private static final Map<String, FileWebSocketServer> clients = new ConcurrentHashMap<String, FileWebSocketServer>();
    /**
     * 会话
     */
    private Session session;
    /**
     * 主机名
     */
    private String bindIp;
    /**
     * 文件名
     */
    private String fileName = null;

    /**
     * token
     */
    private String token;

    /**
     * 上传路径
     */
    private String uploadPath = null;

    /**
     * 文件大小
     */
    private long fileSize = 0L;

    private long passedlen = 0L;

    private long i = 0;

    private FileOutputStream fileOutputStream = null;

    /**
     * 建立连接
     *
     * @param bindIp
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("bindIp") String bindIp, Session session) {
        onlineNumber++;
        log.info("现在来连接的客户端：" + bindIp + "当前在线人数" + onlineNumber);
        this.bindIp = bindIp;
        this.session = session;
        try {
            //先给所有人发送通知，说我上线了
            //Message message = new Message();
            //message.setMsgType(ServerConstant.WEB_SOCKET_TYPE.ONLINE);
            //message.setMessage(bindIp);
            //sendMessageAll(JSONUtil.toJsonStr(message));
            //把自己的信息加入到map当中去
            clients.put(bindIp, this);
            //给自己发一条消息：告诉自己现在都有谁在线
            Message m = new Message();
            m.setMsgType(ServerConstant.WEB_SOCKET_TYPE.ONLINEUSER);
            //移除掉自己
            Set<String> set = clients.keySet();
            m.setMsg(set);
            sendMessageTo(JSONUtil.toJsonStr(m), bindIp);
        } catch (Exception e) {
            log.info(bindIp + "上线的时候通知所有人发生了错误");
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("服务端发生了错误", error);
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        onlineNumber--;
        clients.remove(bindIp);
        try {
            Message message = new Message();
            message.setMsgType(ServerConstant.WEB_SOCKET_TYPE.OFFLINE);
            message.setMsg(clients.keySet());
            message.setFrom(bindIp);
            sendMessageAll(JSONUtil.toJsonStr(message));
        } catch (IOException e) {
            log.error(bindIp + "下线的时候通知所有人发生了错误", e);
        }
        log.info("有连接关闭！ 当前在线人数" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            Message m = JSONUtil.toBean(message, Message.class);
            Object textMessage = m.getMsg();
            String frombindIp = m.getFrom();
            String tobindIp = m.getTo();
            ServerConstant.WEB_SOCKET_TYPE msgType = m.getMsgType();
            log.info("来自客户端" + bindIp + "消息，消息类型: " + msgType);
            Message msg = new Message();
            //心跳检测
            if (msgType == ServerConstant.WEB_SOCKET_TYPE.PING) {
                msg.setMsgType(ServerConstant.WEB_SOCKET_TYPE.PING);
                sendMessageTo(JSONUtil.toJsonStr(msg), frombindIp);
            } else if (msgType == ServerConstant.WEB_SOCKET_TYPE.FILE_UPLOAD_START) {
                if ("server".equals(tobindIp)) {
                    Map HashMap = BeanUtil.beanToMap(textMessage);
                    this.fileName = HashMap.get("fileName").toString();
                    this.uploadPath = HashMap.get("uploadPath").toString();
                    this.fileSize = Long.parseLong(HashMap.get("fileSize").toString());
                    String savePath = uploadPath + File.separatorChar + fileName;
                    this.fileOutputStream = new FileOutputStream(savePath);
                }

            } else if (msgType == ServerConstant.WEB_SOCKET_TYPE.FILE_UPLOAD_UPLOADING) {
                if ("server".equals(tobindIp)) {
                    Map HashMap = BeanUtil.beanToMap(textMessage);
                    byte[] buf = FormatUtil.toByteArray(HashMap.get("byte"));
                    int off = Integer.parseInt(HashMap.get("off").toString());
                    int len = Integer.parseInt(HashMap.get("len").toString());
                    this.fileOutputStream.write(buf, off, len);
                    this.passedlen += len;
                    if (this.i < (this.passedlen * 100L / this.fileSize)) {
                        this.i = this.passedlen * 100L / this.fileSize;
                        log.info("文件已经接收: " + this.i + "%");
                    }
                }
            } else if (msgType == ServerConstant.WEB_SOCKET_TYPE.FILE_UPLOAD_COMPLETED) {
                if ("server".equals(tobindIp)) {
                    if (this.fileOutputStream != null) {
                        this.fileOutputStream.close();
                        this.passedlen = 0L;
                        this.fileSize = 0L;
                        this.fileName = null;
                        this.uploadPath = null;
                    }
                    log.info("文件接收完成");
                }
            } else {
                msg.setMsg(textMessage);
                msg.setFrom(frombindIp);
                //如果不是发给所有，那么就发给某一个人
                msg.setMsgType(ServerConstant.WEB_SOCKET_TYPE.STRING);
                if ("All".equals(tobindIp)) {
                    msg.setTo("All");
                    sendMessageAll(JSONUtil.toJsonStr(msg));
                } else {
                    msg.setTo(tobindIp);
                    sendMessageTo(JSONUtil.toJsonStr(msg), tobindIp);
                }
            }
        } catch (Exception e) {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                this.passedlen = 0;
                this.fileSize = 0;
                this.fileName = null;
                this.uploadPath = null;
            } catch (Exception ex) {
                log.error("发生了错误了", ex);
            }

            log.error("发生了错误了", e);
        }

    }


    public void sendMessageTo(String message, String tobindIp) {
        for (FileWebSocketServer item : clients.values()) {
            if (item.bindIp.equals(tobindIp)) {
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }

    }

    public void sendMessageAll(String message) throws IOException {
        for (FileWebSocketServer item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

}