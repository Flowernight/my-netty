package the.flash.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by xulh on 2019/10/12.
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter{

    /**
     * 当channel有数据读的时候触发该事件
     * @param ctx
     * @param msg
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.printf(new Date()+":服务端读到数据 -> \r"+byteBuf.toString(Charset.forName("utf-8")));

        //回复数据到客户端
        System.out.printf(new Date()+":服务端写出数据 \r");
        ByteBuf out = getByteBuf(ctx);
        ctx.channel().writeAndFlush(out);

    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx){

        byte[] bytes = "服务端返回消息".getBytes(Charset.forName("utf-8"));

        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);

        return buffer;
    }
}
