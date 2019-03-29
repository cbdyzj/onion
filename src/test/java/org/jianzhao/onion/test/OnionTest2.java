package org.jianzhao.onion.test;

import org.jianzhao.onion.Onion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class OnionTest2 {

    public static void main(String[] args) throws Exception {
        Onion<Map<String, Long>> onion = new Onion<>();

        Onion.Middleware<Map<String, Long>> max = (ctx, nxt) -> {
            Long input = ctx.get("input");
            Long oldMax = ctx.get("max");
            ctx.replace("max", input > oldMax ? input : oldMax);
            nxt.next();
        };

        Onion.Middleware<Map<String, Long>> min = (ctx, nxt) -> {
            Long input = ctx.get("input");
            Long oldMin = ctx.get("min");
            ctx.replace("min", input < oldMin ? input : oldMin);
            nxt.next();
        };

        Onion.Middleware<Map<String, Long>> sum = (ctx, nxt) -> {
            Long input = ctx.get("input");
            Long oldSum = ctx.get("sum");
            ctx.replace("sum", oldSum + input);
            nxt.next();
        };

        int[] ints = ThreadLocalRandom.current()
                .ints()
                .filter(i -> i > 0)
                .limit(100)
                .toArray();

        Map<String, Long> context = new HashMap<>();
        context.put("max", Long.MIN_VALUE);
        context.put("min", Long.MAX_VALUE);
        context.put("sum", 0L);

        onion.use(max).use(min).use(sum);

        for (int i : ints) {
            context.put("input", (long) i);
            onion.handle(context);
        }
        System.out.println(Arrays.toString(ints));
        System.out.println(context);
    }
}
