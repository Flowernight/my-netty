package the.flash.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import the.flash.protocol.command.request.LoginRequestPacket;
import the.flash.protocol.command.response.LoginResponsePacket;
import the.flash.session.Session;
import the.flash.util.IDUtil;
import the.flash.util.SessionUtil;

/**
 * Created by xulh on 2019/10/28.
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(loginRequestPacket.getUsername());

        if(valid(loginRequestPacket)){
            loginResponsePacket.setSuccess(true);
            String userId = IDUtil.randomId();
            System.out.println(String.format("[%s]登录成功",loginRequestPacket.getUsername()));
            SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUsername()), ctx.channel());
        } else {
            loginResponsePacket.setReason("帐号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println("登录失败");
        }

        //登录响应
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket){
        return true;
    }
}
