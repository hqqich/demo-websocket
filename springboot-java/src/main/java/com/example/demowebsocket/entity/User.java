package com.example.demowebsocket.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Administrator on 2022/7/5 is 22:36.
 *
 * @Description
 * @Author hqqich <hqqich1314@outlook.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/7/5
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User implements Serializable {

  private String name;

  private String sex;

}
