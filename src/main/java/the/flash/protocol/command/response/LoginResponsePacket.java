package the.flash.protocol.command.response;

import lombok.Data;
import the.flash.protocol.command.Command;
import the.flash.protocol.command.Packet;

/**
 * Created by xulh on 2019/10/14.
 */
@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
