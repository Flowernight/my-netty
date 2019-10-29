package the.flash.protocol.command.response;

import lombok.Data;
import the.flash.protocol.command.Command;
import the.flash.protocol.command.Packet;

import java.util.List;

/**
 * Created by xulh on 2019/10/29.
 */
@Data
public class CreateGroupResponsePacket extends Packet {

    private boolean success;

    private String groupId;

    private List<String> userNameList;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }
}
