package com.netty.tcpserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import java.net.InetSocketAddress;

@Component
@Slf4j
public class EINettyBootstrap {
    private Channel serverChannel;
    @Value("${netty.server.host:127.0.0.1}")
    private String host;
    @Value("${netty.server.port:9997}")
    private int port;

    public void start() {
        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            InetSocketAddress tcpPort=new InetSocketAddress(this.host, this.port);
            ChannelFuture cf = serverBootstrap.bind(tcpPort).sync();
            log.info(MessageFormatter.format("EI服务已经启动，监听：{}:{}", tcpPort.getHostString(), tcpPort.getPort()).getMessage());
            this.serverChannel = cf.channel().closeFuture().sync().channel();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @PreDestroy
    public void stop() {
        if (this.serverChannel != null) {
            this.serverChannel.close();
            serverChannel.parent().close();
            log.info("EI服务器已经关闭");
        }
    }
}
