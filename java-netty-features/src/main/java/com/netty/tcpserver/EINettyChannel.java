package com.netty.tcpserver;

/**
 * EI通道对象
 *
 * @param <T>
 */
public interface EINettyChannel<T> {
    /**
     * 向客户端发送消息
     *
     * @param message
     */
    void send(final T message) throws Exception;

    /**
     * 关闭连接
     *
     * @throws Exception
     */
    void close() throws Exception;

    /**
     * 通道的ID
     *
     * @return
     */
    String id();

    boolean isOpen();

    /**
     * 返回是否处于活动状态
     *
     * @return
     */
    boolean isActive();

    /**
     * 通道断开链接
     *
     * @return
     */
    void disconnect();
}
