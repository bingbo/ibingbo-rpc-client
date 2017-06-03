package com.ibingbo.rpc.client.controller;

import com.ibingbo.rpc.client.RpcProxy;
import com.ibingbo.rpc.service.IHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by bing on 17/6/3.
 */
@Controller
public class HelloController {

    @Autowired
    private RpcProxy proxy;

    @RequestMapping("/hello")
    public void hello(String name) throws Exception {
        IHelloService service = this.proxy.create(IHelloService.class);
        String result = service.hello(name);
        System.out.println(result);
//        RpcClient client = new RpcClient("127.0.0.1", 8000);
//        RpcRequest request = new RpcRequest();
//        request.setRequestId(UUID.randomUUID().toString());
//        request.setClassName("HelloService");
//        request.setMethodName("hello");
//        request.setParameterTypes(new Class<?>[]{String.class});
//        request.setParameters(new Object[]{name});
//        RpcResponse response = client.send(request);
//        System.out.println(response.getResult());
    }
}

