package the.flash.protocol.command.request;

import lombok.Data;
import the.flash.protocol.command.Command;
import the.flash.protocol.command.Packet;

/**
 * Created by xulh on 2019/10/14.
 */
@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
