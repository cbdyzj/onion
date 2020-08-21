package org.jianzhao.onion;

import java.util.Arrays;

/**
 * Onion is just like an Onion
 *
 * @param <T> Context
 * @author cbdyzj
 * @since 2018.3.23
 */
public final class Onion<T> {

    private Middleware<T> core = (ctx, nxt) -> nxt.next();

    public final void use(Middleware<T> middleware) {
        this.core = compose(this.core, middleware);
    }

    public void handle(T context) throws Exception {

        this.core.via(context, () -> {
        });
    }

    public interface Middleware<T> {

        void via(T context, Next next) throws Exception;
    }

    public interface Next {

        void next() throws Exception;
    }

    @SafeVarargs
    public static <U> Middleware<U> compose(Middleware<U>... middlewares) {
        return Arrays.stream(middlewares).reduce((ctx, nxt) -> nxt.next(),
                (before, after) -> (ctx, nxt) -> before.via(ctx, () -> after.via(ctx, nxt)));
    }
}
