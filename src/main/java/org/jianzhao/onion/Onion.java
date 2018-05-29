package org.jianzhao.onion;

/**
 * Onion is just like an Onion
 *
 * @param <T> Context
 * @author cbdyzj
 * @since 2018.3.23
 */
public final class Onion<T> {

    private Middleware<T> middleware = (ctx, nxt) -> nxt.next();

    public Onion<T> use(Middleware<T> middleware) {
        this.middleware = this.middleware.compose(middleware);
        return this;
    }

    public void handle(T context) throws Exception {

        this.middleware.via(context,() -> { });
    }

    public interface Middleware<T> {

        void via(T context, Next next) throws Exception;

        default Middleware<T> compose(Middleware<T> middleware) {
            return (ctx, nxt) -> this.via(ctx, () -> middleware.via(ctx, nxt));
        }
    }

    public interface Next {

        void next() throws Exception;
    }

}
