package com.example.demowebsocket.util;

public class Message {

    /**
     * 消息内容
     */
    private Object msg;

    /**
     * 消息类型
     */
    private ServerConstant.WEB_SOCKET_TYPE msgType;

    /**
     * 发给谁
     */
    private String to;

    /**
     * 谁发的
     */
    private String from;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public ServerConstant.WEB_SOCKET_TYPE getMsgType() {
        return msgType;
    }

    public void setMsgType(ServerConstant.WEB_SOCKET_TYPE msgType) {
        this.msgType = msgType;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "Message{" +
            "msg=" + msg +
            ", msgType=" + msgType +
            ", to='" + to + '\'' +
            ", from='" + from + '\'' +
            '}';
    }
}
