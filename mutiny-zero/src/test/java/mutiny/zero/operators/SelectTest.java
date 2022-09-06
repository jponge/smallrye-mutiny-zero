package mutiny.zero.operators;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import mutiny.zero.ZeroPublisher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SelectTest {

    @Test
    void filterElements() {
        Flow.Publisher<Integer> source = ZeroPublisher.fromItems(1, 2, 3, 4);
        Select<Integer> operator = new Select<>(source, n -> n % 2 == 0);

        AssertSubscriber<Object> sub = AssertSubscriber.create(Long.MAX_VALUE);
        operator.subscribe(sub);

        sub.assertCompleted().assertItems(2, 4);
    }

    @Test
    void rejectNullSource() {
        assertThatThrownBy(() -> new Select<>(null, o -> true))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    void rejectNullPredicate() {
        assertThatThrownBy(() -> new Select<>(ZeroPublisher.empty(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("cannot be null");
    }
}