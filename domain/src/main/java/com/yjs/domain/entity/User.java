package com.yjs.domain.entity;

/**
 * Created by lenovo on 2018-5-2.
 */
import java.util.Date;
import lombok.Data;

@Data
public class User {

    private Long id;

    private String userName;

    private Date birthDay;

    private String sex;

    private String address;


}
