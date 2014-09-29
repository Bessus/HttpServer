package netty.bess;

import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import netty.bess.handlers.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;



/**
 * Class for initialization ChannelPipeline
 * Created by Bess on 23.09.14.
 */
public class HttpNettyServerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("shaping-handler", new ChannelTrafficShapingHandler(1000));
        pipeline.addLast("server-codec", new HttpServerCodec());
        pipeline.addLast("hello-handler", new HelloHandler());
        pipeline.addLast("redirect-handler", new RedirectHandler());
        pipeline.addLast("status-handler", new StatusHandler());
        pipeline.addLast("error-handler", new ErrorHandler());
    }
}
