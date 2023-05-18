package com.example.demowebsocket;

import com.google.common.primitives.Ints;
import org.junit.jupiter.api.Test;

/**
 * Created by ChenHao on 2022/7/15 is 13:15.
 *
 * @author hqqich <hqqich1314@outlook.com>
 * @version V1.0.0
 * @description
 * @date 2022/7/15
 * @since 1.0
 */
public class GuavaTest {

  @Test
  public void test01() {
    System.out.println(Ints.tryParse("99.0"));

    System.out.println(Double.valueOf("99.0").intValue());
  }
}
