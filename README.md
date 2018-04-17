# Onion

[![Release](https://jitpack.io/v/cbdyzj/onion.svg)](https://jitpack.io/#cbdyzj/onion)

## Usage

```java
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
```
