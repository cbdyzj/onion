# Onion

[![Release](https://jitpack.io/v/cbdyzj/onion.svg)](https://jitpack.io/#cbdyzj/onion)

## Usage

```java
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
```
## Better way

```java
Onion.Middleware<T extends Onion.Next> ware = ctx -> {
    // ctx.doSomething();
    ctx.next();
}
```

