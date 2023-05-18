package com.example.demowebsocket;


import com.example.demowebsocket.entity.User;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteOptions;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.junit.jupiter.api.Test;


/**
 * 对象 <br>
 * 需要实现序列化接口
 */
public class LevelDBDataTest {

  private static final String PATH = "./data/level.db";
  private static final File FILE = new File(PATH);
  private static final Charset CHARSET = StandardCharsets.UTF_8;

  @Test
  public void writeObject() {
    Options options = new Options();
    DBFactory factory = Iq80DBFactory.factory;
    DB db = null;
    try {
      db = factory.open(FILE, options);
      User user = new User();
      user.setName("admin");
      user.setSex("男");
      WriteOptions writeOptions = new WriteOptions();
      writeOptions.snapshot(true);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(user);

      db.put(user.getName().getBytes(CHARSET), baos.toByteArray());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (db != null) {
        try {
          db.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Test
  public void readObject() {
    Options options = new Options();
    DBFactory factory = Iq80DBFactory.factory;
    DB db = null;
    try {
      db = factory.open(FILE, options);
      byte[] valueByte = db.get("admin".getBytes(CHARSET));
      ByteArrayInputStream bais = new ByteArrayInputStream(valueByte);
      ObjectInputStream ois = new ObjectInputStream(bais);
      User user = (User) ois.readObject();
      System.out.println(user);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (db != null) {
        try {
          db.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }



  @Test
  public void writeObjectList() {
    Options options = new Options();
    DBFactory factory = Iq80DBFactory.factory;
    DB db = null;
    try {
      db = factory.open(FILE, options);
      List<User> userList = new ArrayList<User>();
      User user = new User();
      user.setName("admin");
      user.setSex("男");
      User user2 = new User();
      user2.setName("root");
      user2.setSex("女");
      userList.add(user);
      userList.add(user2);
      WriteOptions writeOptions = new WriteOptions();
      writeOptions.snapshot(true);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(userList);

      db.put("userList".getBytes(CHARSET), baos.toByteArray());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (db != null) {
        try {
          db.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  @Test
  public void readObjectList() {
    Options options = new Options();
    DBFactory factory = Iq80DBFactory.factory;
    DB db = null;
    try {
      db = factory.open(FILE, options);
      byte[] valueByte = db.get("userList".getBytes(CHARSET));
      ByteArrayInputStream bais = new ByteArrayInputStream(valueByte);
      ObjectInputStream ois = new ObjectInputStream(bais);
      List<User> userList = new ArrayList<User>();
      userList = (List<User>) ois.readObject();
      System.out.println(userList);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (db != null) {
        try {
          db.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }

}