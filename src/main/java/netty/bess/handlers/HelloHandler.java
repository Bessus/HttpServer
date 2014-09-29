package netty.bess.handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import netty.bess.stat.StatisticsController;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * for hello request
 * Created by Bess on 23.09.14.
 */
public class HelloHandler extends SimpleChannelInboundHandler<HttpRequest> {
    private static StatisticsController controller = new StatisticsController();
    private static ChannelGroup openChannelsSet =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE); // A thread-safe Set that contains open Channels
    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
        openChannelsSet.add(ctx.channel());
        if (req.getUri().equals("/hello") || req.getUri().equals("/hello/")) {
            String  responseContent = "<html>\n " + "<header><title>HttpNetty</title></header>\n " +
                    "<body>\n " + "Hello world\n " + "</body>\n " + "</html>";
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FullHttpResponse response =  new DefaultFullHttpResponse(HTTP_1_1, OK,
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(responseContent, CharsetUtil.UTF_8)));
            response.headers().set(CONTENT_TYPE, "text/html");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            ctx.writeAndFlush(response);
            String url = req.getUri();
            controller.IncreaseCount();
            controller.addToIpMap(ctx);
            controller.addToConnectionDeque(ctx, url);

        } else {
            ctx.fireChannelRead(req);
        }
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