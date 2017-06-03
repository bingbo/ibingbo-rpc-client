package com.ibingbo.rpc.client.controller;

import com.ibingbo.rpc.client.RpcProxy;
import com.ibingbo.rpc.service.IHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by bing on 17/6/3.
 */
@Controller
public class HelloController {

    @Autowired
    private RpcProxy rpcProxy;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(String name){
        IHelloService service = rpcProxy.create(IHelloService.class);
        String result = service.hello(name);
        System.out.println(result);
        return result;
    }

}

