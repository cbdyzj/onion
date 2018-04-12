package org.jianzhao.onion.test;

import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import org.jianzhao.onion.Onion;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class OnionTest {

    @Test
    public void test() {
        Onion.Middleware<Object> composed = Onion.compose(Collections.singletonList((ctx, nxt) -> nxt.next()));
    }

    public static void main(String... args) {
        Onion<HttpServerExchange> app = new Onion<>();

        Onion.Middleware<HttpServerExchange> log = (ctx, nxt) -> {
            Stream.of("Before: ", ctx.getRequestPath(), "\n").forEach(System.out::print);
            nxt.next();
            Stream.of("After: ", ctx.getRequestPath(), "\n").forEach(System.out::print);
        };

        Onion.Middleware<HttpServerExchange> hi = (ctx, nxt) -> {
            System.out.println("Say Hi");
            ctx.getResponseSender().send("Hi");
            nxt.next();
        };

        app.use(log).use(hi);

        Undertow.builder()
                .setHandler(app.callback()::handle)
                .addHttpListener(8000, "0.0.0.0")
                .build().start();
    }
}
