package com.netty.tcpserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class EiChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Resource
    private EINettyInboundHandler inboundHandler;
    /**
     * 每个帧的最大长度，默认10k
     */
    @Value("${ei.netty.frame.maxlength:10240}")
    private int frameMaxLength;
    /**
     * 每个帧的结束标识符，默认是\n
     */
    @Value("${ei.netty.frame.delimiter:\n}")
    private String frameDelimiter;

    /**
     * 创建专门用于根据结束标识符来处理tcp分包、粘包的解码器，从而解决了分包黏包的问题
     *
     * @return
     */
    protected ChannelHandler createDelimiterBasedFrameDecoder() {
        ByteBuf bf = Unpooled.wrappedBuffer(this.frameDelimiter.getBytes());
        return new DelimiterBasedFrameDecoder(this.frameMaxLength, bf);
    }

    protected ChannelHandler createStringDecoder() {
        return new StringDecoder();
    }

    protected ChannelHandler createStringEncoder() {
        return new StringEncoder();
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(this.createDelimiterBasedFrameDecoder());
        pipeline.addLast(this.createStringDecoder());
        pipeline.addLast(this.createStringEncoder());
        pipeline.addLast(this.inboundHandler);
    }
}
