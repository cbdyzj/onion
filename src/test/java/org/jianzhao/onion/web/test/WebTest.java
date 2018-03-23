package org.jianzhao.onion.web.test;

import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import org.jianzhao.onion.Onion;
import org.junit.jupiter.api.Test;

@SuppressWarnings("WeakerAccess")
public class WebTest {

    @Test
    public void test() {
        Onion<HttpServerExchange> app = new Onion<>();

        Onion.Middleware<HttpServerExchange> log = (ctx, nxt) -> {
            System.out.println(ctx);
            nxt.next();
        };

        Onion.Middleware<HttpServerExchange> hi = (ctx, nxt) -> ctx.getResponseSender().send("hi");

        app.use(log).use(hi);

        Undertow.builder()
                .setHandler(app.callback()::handle)
                .addHttpListener(8000, "0.0.0.0")
                .build().start();
    }
}
