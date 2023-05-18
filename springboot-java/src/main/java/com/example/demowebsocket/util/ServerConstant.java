package com.example.demowebsocket.util;

public interface ServerConstant {

    /**
     * 文件状态enum
     */
    public static enum FILE_STATE {
        /**
         * 文件上传完成
         */
        FILE_UPLOAD_COMPLETED,
        /**
         * 文件上传中
         */
        FILE_UPLOAD_UPLOADING,
        /**
         * 文件上传失败
         */
        FILE_UPLOAD_FAILED,
        /**
         * 文件未找到
         */
        FILE_NOT_FIND,
        /**
         * 文件已存在
         */
        FILE_EXSIT,
        /**
         * 文件准备开始传输
         */
        FILE_UPLOAD_START
    }

    /**
     * websocket
     */
    public static enum WEB_SOCKET_TYPE {
        /**
         * 系统消息 由服务器发给客户端
         */
        SYSTEM,

        /**
         * 客户端消息 由客户端发给服务器
         */
        USER,

        /**
         * 上线通知
         */
        ONLINE,

        /**
         * 下线通知
         */
        OFFLINE,

        /**
         * 在线名单
         */
        ONLINEUSER,

        /**
         * 普通消息
         */
        STRING,

        /**
         * 心跳检测
         */
        PING,

        /**
         * 文件准备开始传输
         */
        FILE_UPLOAD_START,
        /**
         * 文件上传完成
         */
        FILE_UPLOAD_COMPLETED,
        /**
         * 文件上传中
         */
        FILE_UPLOAD_UPLOADING,
        /**
         * 文件上传失败
         */
        FILE_UPLOAD_FAILED,
        /**
         * 文件未找到
         */
        FILE_NOT_FIND,
        /**
         * 文件已存在
         */
        FILE_EXSIT
    }

}