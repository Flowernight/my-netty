package the.flash.protocol.command.response;

import lombok.Data;
import the.flash.protocol.command.Command;
import the.flash.protocol.command.Packet;

/**
 * Created by xulh on 2019/10/14.
 */
@Data
public class MessageResponsePacket extends Packet {

    private String message;

    private String fromUserId;

    private String fromUserName;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
