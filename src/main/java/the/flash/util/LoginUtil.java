package the.flash.util;


import io.netty.channel.Channel;
import io.netty.util.Attribute;
import the.flash.attribute.Attributes;

/**
 * Created by xulh on 2019/10/14.
 */
public class LoginUtil {

    public static void maskAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);

    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
        return loginAttr.get() != null;
    }
}
