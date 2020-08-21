package org.jianzhao.onion.test;

import org.jianzhao.onion.Onion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class OnionTest {

    @Test
    public void test() throws Exception {
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


        Map<String, Long> context = new HashMap<>();
        context.put("max", Long.MIN_VALUE);
        context.put("min", Long.MAX_VALUE);
        context.put("sum", 0L);

        Onion<Map<String, Long>> onion = new Onion<>();
        onion.use(max);
        onion.use(min);
        onion.use(sum);

        int TOTAl = 100;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int[] ints = random
                .ints().filter(i -> i > 0)
                .limit(100)
                .toArray();
        for (int i : ints) {
            context.put("input", (long) i);
            onion.handle(context);
        }

        Long maxValue = context.get("max");
        Long minValue = context.get("min");
        Long sumValue = context.get("sum");

        Assertions.assertTrue(minValue < maxValue);
        Assertions.assertTrue(minValue * TOTAl < sumValue);
        Assertions.assertTrue(maxValue * TOTAl > sumValue);
    }
}
