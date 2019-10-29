package the.flash.protocol.command.request;

import lombok.Data;
import the.flash.protocol.command.Command;
import the.flash.protocol.command.Packet;

import java.util.List;

/**
 * Created by xulh on 2019/10/29.
 */
@Data
public class CreateGroupRequestPacket extends Packet {

    private List<String> userIdList;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }
}
