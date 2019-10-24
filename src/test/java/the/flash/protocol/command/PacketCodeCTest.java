package the.flash.protocol.command;

import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;
import the.flash.protocol.command.request.LoginRequestPacket;
import the.flash.serialize.Serializer;
import the.flash.serialize.impl.JSONSerializer;

/**
 * Created by xulh on 2019/10/14.
 */
public class PacketCodeCTest {

    @Test
    public void encoe(){

        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion((byte)1);
        loginRequestPacket.setUserId("123");
        loginRequestPacket.setUsername("zhangsan");
        loginRequestPacket.setPassword("password");

        PacketCodeC packetCodeC = new PacketCodeC();
//        ByteBuf byteBuf = packetCodeC.encode(loginRequestPacket);
//        Packet decodePacket = packetCodeC.decode(byteBuf);

//        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket),serializer.serialize(decodePacket));

    }
}
