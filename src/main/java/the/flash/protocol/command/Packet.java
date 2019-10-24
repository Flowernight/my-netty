package the.flash.protocol.command;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by xulh on 2019/10/14.
 */
@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize =  false)
    private Byte version = 1;

    @JSONField(serialize = false)
    public abstract Byte getCommand();
}
