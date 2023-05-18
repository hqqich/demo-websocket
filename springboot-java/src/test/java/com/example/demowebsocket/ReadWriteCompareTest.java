package com.example.demowebsocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.junit.jupiter.api.Test;

/**
 * Created by ChenHao on 2022/7/15 is 17:03.
 *
 * @author hqqich <hqqich1314@outlook.com>
 * @version V1.0.0
 * @description
 * @date 2022/7/15
 * @since 1.0
 */
public class ReadWriteCompareTest {

  @Test
  public void test01() throws IOException {
    FileInputStream fileInputStream = new FileInputStream("G:" + File.separator + "aaa.jpg");

    FileOutputStream fileOutputStream = new FileOutputStream("G:" + File.separator + "test.jpg");

    FileChannel inChannel = fileInputStream.getChannel();

    FileChannel outChannel = fileOutputStream.getChannel();

    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    // Direct Buffer的效率会更高。

    // ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

    long start = System.currentTimeMillis();

    while (true) {

      int eof = inChannel.read(byteBuffer);

      if (eof == -1) break;

      byteBuffer.flip();

      outChannel.write(byteBuffer);

      byteBuffer.clear();
    }

    System.out.println("spending : " + (System.currentTimeMillis() - start));

    inChannel.close();

    outChannel.close();
  }
}
