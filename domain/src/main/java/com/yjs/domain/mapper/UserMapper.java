package com.yjs.domain.mapper;

import com.yjs.domain.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by lenovo on 2018/6/1.
 */


// @Mapper 这里可以使用@Mapper注解，但是每个mapper都加注解比较麻烦，所以统一配置@MapperScan在扫描路径在application类中
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(Long id);

    @Select("SELECT * FROM user")
    public List<User> getUserList();

    @Insert("insert into user(address,birth_day,sex,user_name) values(#{address}, #{birthDay}, #{sex}, #{userName})")
    public int add(User user);

    @Update("UPDATE user SET user_name = #{user.username} , birth_day = #{user.birthDay},, sex = #{user.sex} , address = #{user.address} WHERE id = #{id}")
    public int update(@Param("id") Long id, @Param("user") User user);

    @Delete("DELETE from user where id = #{id} ")
    public int delete(Long id);
}