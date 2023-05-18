package com.example.demowebsocket.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class FormatUtil {

    /**
     * 对象转数组
     * @param obj
     * @return
     *
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

}
