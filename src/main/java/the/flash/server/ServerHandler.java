package the.flash.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import the.flash.protocol.command.request.LoginRequestPacket;
import the.flash.protocol.command.Packet;
import the.flash.protocol.command.PacketCodeC;
import the.flash.protocol.command.request.MessageRequestPacket;
import the.flash.protocol.command.response.LoginResponsePacket;
import the.flash.protocol.command.response.MessageResponsePacket;

import java.util.Date;

/**
 * Created by xulh on 2019/10/14.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new Date() + "客户端开始登陆");
        ByteBuf requestByteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(requestByteBuf);

        if (packet instanceof LoginRequestPacket){
            //登陆流程
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            if(valid(loginRequestPacket)){
                loginResponsePacket.setSuccess(true);
                System.out.println("登陆成功");
            } else {
                loginResponsePacket.setReason("账户或密码错误");
                loginResponsePacket.setSuccess(false);
                System.out.println("客户端登陆失败");
            }
            //登录响应
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(),loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        } else if (packet instanceof MessageRequestPacket){
            //客户端发来消息
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) msg;

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            System.out.println(new Date() + "收到客户端消息:"+messageRequestPacket.getMessage());
            messageResponsePacket.setMessage("服务端回复:"+messageRequestPacket.getMessage());
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket){
        return true;
    }
}
