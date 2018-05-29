package org.jianzhao.onion.test;

import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import org.jianzhao.onion.Onion;

import java.util.stream.Stream;

public class OnionTest {

    public static void main(String... args) {
        Onion<HttpServerExchange> onion = new Onion<>();

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

        onion.use(log).use(hi);

        Undertow.builder()
                .setHandler(onion::handle)
                .addHttpListener(8000, "0.0.0.0")
                .build().start();
    }
}
