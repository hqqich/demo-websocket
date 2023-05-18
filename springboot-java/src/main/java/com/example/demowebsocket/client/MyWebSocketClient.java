package com.example.demowebsocket.client;

import cn.hutool.json.JSONUtil;
import com.example.demowebsocket.util.Message;
import com.example.demowebsocket.util.ServerConstant;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

@Slf4j
public class MyWebSocketClient extends WebSocketClient {

    /**
     * description 客户端连接状态
     */
    private boolean isConnect = false;


    public void setConnectState(boolean isConnect) {
        this.isConnect = isConnect;
    }

    public boolean getConnectState() {
        return this.isConnect;
    }

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
        connect();
    }

    @Override
    public void connect() {
        super.connect();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("开始连接...");
        setConnectState(true);
        //for(Iterator<String> it = serverHandshake.iterateHttpFields(); it.hasNext();) {
        //    String key = it.next();
        //    System.out.println(key+":"+serverHandshake.getFieldValue(key));
        //}
    }

    @Override
    public void onMessage(String s) {
        log.info("接收到消息：" + s);
    }

    /***检测到连接关闭之后，会更新连接状态以及尝试重新连接***/
    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("连接关闭 {}", s);
        setConnectState(false);
        //rec();
    }

    /***检测到错误，更新连接状态***/
    @Override
    public void onError(Exception e) {
        log.error("连接错误 ", e);
        setConnectState(false);
        //rec();
    }


    /**
     * 重连
     */
    @Override
    public void reconnect() {
        super.reconnect();
        log.warn("重连中 .................");
    }

    //public void rec() {
    //    try{
    //        if(!getConnectState()){
    //            int i = 0;
    //            while(true){
    //                reconnect();
    //                if(getConnectState()){
    //                    log.info("连接成功");
    //                    break;
    //                }
    //                Thread.sleep(1000);
    //            }
    //        }
    //    }catch (InterruptedException e){
    //        log.error("",e);
    //    }
    //}
    public static void main(String[] args) throws InterruptedException, IOException {
        String username = "aaa" + 11;
        String uri = "ws://127.0.0.1:8888/demo/fileWebsocket/" + username;
        MyWebSocketClient myWebSocketClient = new MyWebSocketClient(URI.create(uri));
        //必须延时否则可能会无法收到消息
        //Thread.sleep(1000);
        log.info(uri);
        String s = "";
    File file = new File("G:\\aaa.txt");
        //获得要发送文件的长度
        long length = file.length();
        Message message = new Message();
        message.setMsgType(ServerConstant.WEB_SOCKET_TYPE.FILE_UPLOAD_START);
        message.setFrom(username);
        message.setTo("server");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("fileName",file.getName());
        map.put("fileSize",length);
        map.put("uploadPath", "F:\\");
        message.setMsg(map);
        s = JSONUtil.toJsonStr(message);
        log.info("发送开始: " + s);
        myWebSocketClient.send(s);

        Message m = new Message();
        byte[] buf = new byte[1024];
        FileInputStream fileInputStream = new FileInputStream(file.getPath());
        int read = 0;
        long i = 0;
        int passedlen = 0;
        while ((read = fileInputStream.read(buf)) != -1) {
            m.setMsgType(ServerConstant.WEB_SOCKET_TYPE.FILE_UPLOAD_UPLOADING);
            m.setFrom(username);
            m.setTo("server");
            Map<String,Object> fileMap = new HashMap<String,Object>();
            //byte b[], int off, int len
            fileMap.put("byte",buf);
            fileMap.put("off",0);
            fileMap.put("len",read);
            m.setMsg(fileMap);
            s = JSONUtil.toJsonStr(m);
            myWebSocketClient.send(s);
            passedlen += read;
            if (i < passedlen * 100L / length) {
                i = (passedlen * 100L / length);
                log.info("已经发送文件: " + i + "%");
            }
            //不加延时传送大文件会出异常原因未知 小文件可以注释
            //Thread.sleep(30);
        }
        m.setMsgType(ServerConstant.WEB_SOCKET_TYPE.FILE_UPLOAD_COMPLETED);
        m.setFrom(username);
        m.setTo("server");
        m.setMsg("");
        s = JSONUtil.toJsonStr(m);
        myWebSocketClient.send(s);
        log.info("发送完成: " + file.getPath());
        //while (true) {
        //    if(myWebSocketClient.getConnectState()){
        //        //Message message = new Message();
        //        //message.setMsgType(ServerConstant.WEB_SOCKET_TYPE.PING);
        //        //message.setFrom(username);
        //        //s = JSONUtil.toJsonStr(message);
        //        //myWebSocketClient.send(s);
        //
        //    }
        //    log.info(s);
        //    Thread.sleep(3000);
        //}
    }

}