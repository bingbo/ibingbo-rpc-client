package com.ibingbo.rpc.client.controller;

import com.ibingbo.rpc.client.RpcProxy;
import com.ibingbo.rpc.service.IHelloService;
import com.ibingbo.rpc.service.IUserService;
import com.ibingbo.rpc.service.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by bing on 17/6/3.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RpcProxy proxy;

    @RequestMapping("/get")
    @ResponseBody
    public User get(String name) {
        IUserService service = this.proxy.create(IUserService.class);
        User user = service.getUser(name);
        LOGGER.debug("result is {}", user);
        return user;
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<User> list(int size) {
        IUserService service = this.proxy.create(IUserService.class);
        List<User> users = service.getUsers(size);
        LOGGER.debug("result is {}", users);
        return users;
    }

}
