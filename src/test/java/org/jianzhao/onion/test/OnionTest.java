package org.jianzhao.onion.test;

import org.jianzhao.onion.Onion;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@SuppressWarnings("WeakerAccess")
public class OnionTest {

    @Test
    public void test() {
        Onion.Middleware<Object> composed = Onion.compose(Collections.singletonList((ctx, nxt) -> nxt.next()));
    }
}
