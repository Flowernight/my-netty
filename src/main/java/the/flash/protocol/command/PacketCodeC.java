package the.flash.protocol.command;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import the.flash.protocol.command.request.LoginRequestPacket;
import the.flash.protocol.command.request.MessageRequestPacket;
import the.flash.protocol.command.response.LoginResponsePacket;
import the.flash.protocol.command.response.MessageResponsePacket;
import the.flash.serialize.Serializer;
import the.flash.serialize.impl.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xulh on 2019/10/14.
 */
public class PacketCodeC {

    public static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlogrithm(), serializer);
    }

    /**
     * 编码
     * @param packet
     * @return
     */
    public ByteBuf encode(ByteBuf byteBuf, Packet packet){

        //1. 序列化 java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        //2. 业务
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlogrithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 解码
     * @param byteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf){
        //跳过魔术
        byteBuf.skipBytes(4);
        //跳过版本号
        byteBuf.skipBytes(1);
        //获取序列化算法
        byte serializeAlgorithm = byteBuf.readByte();
        //获取指令
        byte command = byteBuf.readByte();
        //获取数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if(requestType != null && serializer != null){
            return serializer.deserialize(requestType,bytes);
        }

        return null;
    }

    private Class<? extends Packet> getRequestType(byte command){
        return packetTypeMap.get(command);
    }

    private Serializer getSerializer(byte serializeAlgorithm){
        return serializerMap.get(serializeAlgorithm);
    }


}
