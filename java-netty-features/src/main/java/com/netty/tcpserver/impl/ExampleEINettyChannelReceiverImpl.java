package com.netty.tcpserver.impl;

import com.example.netty.tcpserver.EINettyChannel;
import com.example.netty.tcpserver.EINettyChannelReceiver;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * EI通道接收器的范例，需要注释掉@Component
 *
 * @param <T>
 */
@Component
public class ExampleEINettyChannelReceiverImpl<T> implements EINettyChannelReceiver<T> {
    @Override
    @SneakyThrows
    public void onReceive(EINettyChannel<T> channel, T message) {
        channel.send(message); //直接向客户端返回客户端传来的信息
    }
}
