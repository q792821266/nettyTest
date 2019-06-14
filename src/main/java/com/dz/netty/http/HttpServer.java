package com.dz.netty.http;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServer {

    private final int port;

    public HttpServer(int port){
        this.port = port;
    }

    public static void main(String[] args) {
        if(args.length!=1){
            System.err.println("Usage:"+HttpServer.class.getSimpleName()+"<port>");
        }
        int port = Integer.getInteger(args[0]);
        new HttpServer(port).start();
        System.out.println("Hello World!");
    }

    private void start() {
        ServerBootstrap sb = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        sb.group(group).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        System.out.println("initChannel ch:"+socketChannel);
                        socketChannel.pipeline()
                                .addLast("decoder",new HttpRequestDecoder())
                                .addLast("endocder",new HttpResponseEncoder())
                                .addLast("aggragator",new HttpObjectAggregator(512*1024))//是消息合并的数据大小,消息内容长度不超过512kb
                                .addLast("handler",new HttpHandler());//Handler;
                    }
                }).option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,Boolean.TRUE);
    }


}
