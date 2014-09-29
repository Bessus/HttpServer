package netty.bess.handlers;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.concurrent.GlobalEventExecutor;
import netty.bess.stat.StatisticsController;
import io.netty.channel.ChannelHandlerContext;

/**
 * counts bytes and speed
 * Created by Bess on 23.09.14.
 */
public class CollectorHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private static StatisticsController controller = new StatisticsController();
    private static ChannelGroup openChannelsSet =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE); // A thread-safe Set that contains open Channels

    public CollectorHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
        openChannelsSet.add(ctx.channel());
        String url = msg.getUri();
        controller.IncreaseCount();
        controller.ProcessRequestParams(ctx);
        controller.processConnectionsLog(ctx,url);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //return number of current active channels
    public static int getConnectionsCount(){
        return openChannelsSet.size();
    }
}
