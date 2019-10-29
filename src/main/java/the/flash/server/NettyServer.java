package the.flash.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import the.flash.codec.PacketDecoder;
import the.flash.codec.Spliter;
import the.flash.server.handler.AuthHandler;
import the.flash.server.handler.CreateGroupRequestHandler;
import the.flash.server.handler.LoginRequestHandler;
import the.flash.server.handler.MessageRequestHandler;

/**
 * Created by xulh on 2019/10/12.
 */
public class NettyServer {

    private static final int BEGIN_PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        final AttributeKey<Object> clientKey = AttributeKey.newInstance("clientKey");
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .attr(AttributeKey.newInstance("serverName"),"nettyServer")
                .childAttr(clientKey,"clientValue")
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch){
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new AuthHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new CreateGroupRequestHandler());

                        ch.pipeline().addLast(new ServerHandler());
                    }

                });

        bind(serverBootstrap, BEGIN_PORT);
    }

    /**
     * 绑定端口
     * @param serverBootstrap
     * @param prot
     */
    public static void bind(final ServerBootstrap serverBootstrap,final int prot){
        serverBootstrap.bind(prot).addListener(future -> {
            if (future.isSuccess()){
                System.out.printf("端口[%d]绑定成功!",prot);
            } else {
                System.out.printf("端口[%d]绑定失败", prot);
                bind(serverBootstrap,prot+1);
            }
        });
    }
}
