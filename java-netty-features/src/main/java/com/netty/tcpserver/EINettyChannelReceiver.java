package com.netty.tcpserver;

/**
 * Netty通道消息接收器
 */
public interface EINettyChannelReceiver<T> {
    /**
     * 接收通道消息
     *  @param channel
     * @param message
     */
    void onReceive(EINettyChannel<T> channel, final T message);
}
