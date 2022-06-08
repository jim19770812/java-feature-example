package com.netty.tcpserver.impl;

import com.example.netty.tcpserver.EINettyChannel;
import io.netty.channel.Channel;
import org.apache.logging.log4j.util.Strings;

/**
 * Netty同都包装器，对外隐藏通道细节
 *
 * @param <T>
 */
public class EINettyChannelWrapper<T> implements EINettyChannel<T> {
    private Channel channel;
    private String frameDelimiter;

    public EINettyChannelWrapper(Channel channel, final String frameDelimiter) {
        this.channel = channel;
        this.frameDelimiter = frameDelimiter;
    }

    /**
     * 向客户端发送消息
     *
     * @param message
     * @throws Exception
     */
    @Override
    public void send(T message) throws Exception {
        this.channel.write(message);
        this.channel.writeAndFlush(Strings.isBlank(this.frameDelimiter) ? "" : this.frameDelimiter);
    }

    /**
     * 关闭连接
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        this.channel.close();
    }

    /**
     * 通道的ID
     *
     * @return
     */
    public String id() {
        return channel.id().asShortText();
    }

    public boolean isOpen() {
        return channel.isOpen();
    }

    /**
     * 返回是否处于活动状态
     *
     * @return
     */
    public boolean isActive() {
        return channel.isActive();
    }

    /**
     * 通道断开链接
     *
     * @return
     */
    public void disconnect() {
        channel.disconnect();
    }
}
