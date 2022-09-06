package mutiny.zero.operators;

import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class Select<T> implements Flow.Publisher<T> {

    private final Flow.Publisher<T> upstream;
    private final Predicate<T> predicate;

    public Select(Flow.Publisher<T> upstream, Predicate<T> predicate) {
        this.upstream = requireNonNull(upstream, "The upstream cannot be null");
        this.predicate = requireNonNull(predicate, "The predicate cannot be null");
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        requireNonNull(subscriber, "The subscriber cannot be null");
        Processor processor = new Processor();
        processor.subscribe(subscriber);
        upstream.subscribe(processor);
    }

    private class Processor extends ProcessorBase<T, T> {

        @Override
        public void onNext(T item) {
            if (!cancelled()) {
                try {
                    if (predicate.test(item)) {
                        downstream().onNext(item);
                    }
                } catch (Throwable failure) {
                    cancel();
                    downstream().onError(failure);
                }
            }
        }
    }
}
