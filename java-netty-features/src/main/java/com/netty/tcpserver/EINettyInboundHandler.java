package com.netty.tcpserver;

import com.example.netty.tcpserver.impl.EINettyChannelWrapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * ei服务处理器
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class EINettyInboundHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 自定义的消息接收器
     */
    @Autowired(required = false)
    private com.example.netty.tcpserver.EINettyChannelReceiver<String> receiver;
    /**
     * 每个帧的结束标识符，默认是\n
     */
    @Value("${ei.netty.frame.delimiter:\n}")
    private String frameDelimiter;

    @Override
    @SneakyThrows
    protected void channelRead0(ChannelHandlerContext ctx, String message) {
        Assert.notNull(this.receiver, "未注册EINettyChannelReceiver消息接收器实例，EINettyInboundHandler无法有效转发消息");
//        byte[] bts=message.getBytes(StandardCharsets.UTF_8);
//        StringBuilder sb=new StringBuilder();
//        for(byte b : bts){
//            sb.append(b);
//        }
//        log.debug("通道[{}]:{}", ctx.channel().id().toString(), message + " " + sb.toString());
        log.debug("通道[{}]:{}", ctx.channel().id().toString(), message);
        com.example.netty.tcpserver.EINettyChannel<String> channel = new EINettyChannelWrapper<String>(ctx.channel(), this.frameDelimiter);
        if (receiver != null) {
            receiver.onReceive(channel, message);
        }
//        ctx.flush();
    }

    /**
     * 通道注册
     *
     * @param ctx
     */
    @Override
    @SneakyThrows
    public void channelRegistered(ChannelHandlerContext ctx) {
        super.channelRegistered(ctx);
        log.debug("通道[{}]注册完成", ctx.channel().id().asShortText());
    }

    /**
     * 通道反注册
     *
     * @param ctx
     */
    @Override
    @SneakyThrows
    public void channelUnregistered(ChannelHandlerContext ctx) {
        super.channelUnregistered(ctx);
        log.debug("通道[{}]反注册完成", ctx.channel().id().asShortText());
    }

    /**
     * 通道建立连接
     *
     * @param ctx
     */
    @Override
    @SneakyThrows
    public void channelActive(ChannelHandlerContext ctx) {
        super.channelActive(ctx);
        ctx.fireChannelActive();
        log.debug(MessageFormatter.format("通道已经建立连接，客户端：{}", ctx.channel().remoteAddress() + "").getMessage());
        ctx.writeAndFlush("connected\n");
    }

    /**
     * 通道断开
     *
     * @param ctx
     */
    @Override
    @SneakyThrows
    public void channelInactive(ChannelHandlerContext ctx) {
        super.channelInactive(ctx);
        log.debug("通道[{}]连接断开", ctx.channel().id().toString());
    }

    /**
     * 通道异常处理
     *
     * @param ctx
     * @param cause
     */
    @Override
    @SneakyThrows
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //super.exceptionCaught(ctx, cause);
        log.error("通道[{}]有异常发生：{}", ctx.channel().id().toString(), cause.toString());
    }
}
