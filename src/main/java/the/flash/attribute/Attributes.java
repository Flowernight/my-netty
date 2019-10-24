package the.flash.attribute;

import io.netty.util.AttributeKey;

/**
 * Created by xulh on 2019/10/14.
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
