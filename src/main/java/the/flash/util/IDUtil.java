package the.flash.util;

import java.util.UUID;

/**
 * Created by xulh on 2019/10/28.
 */
public class IDUtil {

    public static String randomId(){
        return UUID.randomUUID().toString().split("-")[0];
    }
}
