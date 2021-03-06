package the.flash.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import the.flash.protocol.command.request.LoginRequestPacket;
import the.flash.protocol.command.Packet;
import the.flash.protocol.command.PacketCodeC;
import the.flash.protocol.command.response.LoginResponsePacket;
import the.flash.protocol.command.response.MessageResponsePacket;
import the.flash.util.LoginUtil;

import java.util.Date;
import java.util.UUID;

/**
 * Created by xulh on 2019/10/14.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println(new Date() + ":客户端开始登陆");

        //创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");

        //编码
//        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequestPacket);

        //写数据
//        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginResponsePacket){
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()){
                System.out.println(new Date() + "客户端登陆成功");
                LoginUtil.maskAsLogin(ctx.channel());
            } else {
                System.out.println(new Date() + "客户端登陆失败");
            }
        } else if(packet instanceof MessageResponsePacket){
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) msg;
            System.out.println(new Date() + "收到服务端的消息:"+messageResponsePacket.getMessage());
        }
    }
}
