package mutiny.zero.operators;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.Flow;

import org.junit.jupiter.api.Test;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import mutiny.zero.ZeroPublisher;

class TransformTest {

    @Test
    void transformItems() {
        Flow.Publisher<Integer> source = ZeroPublisher.fromItems(1, 2, 3);
        Transform<Integer, String> operator = new Transform<>(source, n -> n + ":" + (n * 100));

        AssertSubscriber<Object> sub = AssertSubscriber.create(Long.MAX_VALUE);
        operator.subscribe(sub);

        sub.assertCompleted().assertItems("1:100", "2:200", "3:300");
    }

    @Test
    void rejectNullSource() {
        assertThatThrownBy(() -> new Transform<>(null, n -> n))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    void rejectNullFunction() {
        assertThatThrownBy(() -> new Transform<>(ZeroPublisher.empty(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    void handleThrowingFunction() {
        Flow.Publisher<Integer> source = ZeroPublisher.fromItems(1, 2, 3);
        Transform<Integer, String> operator = new Transform<>(source, n -> {
            throw new RuntimeException("yolo");
        });

        AssertSubscriber<Object> sub = AssertSubscriber.create(Long.MAX_VALUE);
        operator.subscribe(sub);

        sub.assertFailedWith(RuntimeException.class, "yolo");
    }

    @Test
    void handleNullProducingFunction() {
        Flow.Publisher<Integer> source = ZeroPublisher.fromItems(1, 2, 3);
        Transform<Integer, String> operator = new Transform<>(source, n -> null);

        AssertSubscriber<Object> sub = AssertSubscriber.create(Long.MAX_VALUE);
        operator.subscribe(sub);

        sub.assertFailedWith(NullPointerException.class, "The function produced a null result for item 1");
    }
}
