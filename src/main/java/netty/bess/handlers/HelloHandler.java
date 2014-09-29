package netty.bess.handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * for hello request
 * Created by Bess on 23.09.14.
 */
public class HelloHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
        if (req.getUri().equals("/hello") || req.getUri().equals("/hello/")) {
            StringBuilder  responseContent = new StringBuilder();
            responseContent.append("<html>\n " +
                    "<header><title>HttpNetty</title></header>\n " +
                    "<body>\n " + "Hello world\n " +
                    "</body>\n " + "</html>");
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FullHttpResponse response =  new DefaultFullHttpResponse(HTTP_1_1, OK,
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8)));
            response.headers().set(CONTENT_TYPE, "text/html");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            ctx.writeAndFlush(response);

        } else {
            ctx.fireChannelRead(req);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}