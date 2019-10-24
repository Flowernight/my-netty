package the.flash.serialize.impl;

import com.alibaba.fastjson.JSON;
import the.flash.serialize.Serializer;
import the.flash.serialize.SerializerAlogrithm;

/**
 * Created by xulh on 2019/10/14.
 */
public class JSONSerializer implements Serializer {


    @Override
    public byte getSerializerAlogrithm() {
        return SerializerAlogrithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
