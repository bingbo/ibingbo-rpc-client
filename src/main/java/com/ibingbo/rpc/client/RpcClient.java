package com.ibingbo.rpc.client;

import com.ibingbo.rpc.common.RpcDecoder;
import com.ibingbo.rpc.common.RpcEncoder;
import com.ibingbo.rpc.common.RpcRequest;
import com.ibingbo.rpc.common.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bing on 17/6/3.
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);
    private String host;
    private int port;

    private RpcResponse response;

    private final Object object = new Object();

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        this.response = response;

        synchronized (object) {
            object.notifyAll();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }

    public RpcResponse send(RpcRequest rpcRequest) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcEncoder(RpcRequest.class))
                                    .addLast(new RpcDecoder(RpcResponse.class))
                                    .addLast(RpcClient.this);
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.connect(this.host, this.port).sync();
            future.channel().writeAndFlush(rpcRequest).sync();

            synchronized (this.object) {
                this.object.wait();
            }

            if (this.response != null) {
                future.channel().closeFuture().sync();
            }
            return this.response;
        } finally {
            group.shutdownGracefully();
        }
    }
}
