package the.flash.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by xulh on 2019/10/12.
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当连接上时触发的事件
     * @param ctx
     */
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + ":客户端写出数据");

        //获取数据
        ByteBuf buffer = getByteBuf(ctx);

        //写数据
        ctx.channel().writeAndFlush(buffer);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx){
        byte[] bytes = "你好".getBytes(Charset.forName("utf-8"));

        ByteBuf buffer = ctx.alloc().buffer();

        buffer.writeBytes(bytes);

        return buffer;

    }

    /**
     * 当有数据读的时候触发的事件
     * @param ctx
     * @param msg
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.printf(new Date() + ":客户端读到数据->\r"+byteBuf.toString(Charset.forName("utf-8")));
    }
}
