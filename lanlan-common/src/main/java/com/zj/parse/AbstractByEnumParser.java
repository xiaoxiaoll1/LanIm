package com.zj.parse;


import com.google.protobuf.Message;
import com.google.protobuf.ProtocolMessageEnum;
import com.zj.function.ImBiConsumer;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Date: 2019-05-23
 * Time: 18:40
 *
 * @author yrw
 */
public abstract class AbstractByEnumParser<E extends ProtocolMessageEnum, M extends Message> {

    private Map<E, ImBiConsumer<M, ChannelHandlerContext>> parseMap;

    public AbstractByEnumParser(int size) {
        this.parseMap = new HashMap<>(size);
    }

    public void register(E type, ImBiConsumer<M, ChannelHandlerContext> consumer) {
        parseMap.put(type, consumer);
    }

    /**
     * 获取枚举
     *
     * @param msg
     * @return
     */
    protected abstract E getType(M msg);

    public ImBiConsumer<M, ChannelHandlerContext> generateFun() {
        return (m, ctx) -> Optional.ofNullable(parseMap.get(getType(m)))
            .orElseThrow(() -> new IllegalArgumentException("Invalid msg enum"))
            .accept(m, ctx);
    }
}
