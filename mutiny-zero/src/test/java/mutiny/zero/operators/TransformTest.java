package mutiny.zero.operators;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import mutiny.zero.ZeroPublisher;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Flow;

import static org.junit.jupiter.api.Assertions.*;

class TransformTest {

    @Test
    void transformItems() {
        Flow.Publisher<Integer> source = ZeroPublisher.fromItems(1, 2, 3);
        Transform<Integer, String> operator = new Transform<>(source, n -> n + ":" + (n * 100));

        AssertSubscriber<Object> sub = AssertSubscriber.create(Long.MAX_VALUE);
        operator.subscribe(sub);

        sub.assertCompleted().assertItems("1:100", "2:200", "3:300");
    }
}