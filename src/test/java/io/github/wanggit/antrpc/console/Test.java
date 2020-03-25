package io.github.wanggit.antrpc.console;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            users.add(new User());
        }
        String json = JSONObject.toJSONString(users);
        System.out.println(json);
        JSON jsonObject = JSONObject.parseObject(json, JSON.class);
        System.out.println(jsonObject);

        json = JSONObject.toJSONString(new User());
        System.out.println(json);
        jsonObject = JSONObject.parseObject(json, JSON.class);
        System.out.println(jsonObject);
    }

    public static class User {
        private Long id;
        private String name;

        User() {
            id = RandomUtils.nextLong();
            name = RandomStringUtils.randomAlphanumeric(10);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
