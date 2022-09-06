package mutiny.zero.operators;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.Flow;
import java.util.function.Function;

public class Transform<I, O> implements Flow.Publisher<O> {

    private final Flow.Publisher<I> upstream;
    private final Function<I, O> function;

    public Transform(Flow.Publisher<I> upstream, Function<I, O> function) {
        this.upstream = requireNonNull(upstream, "The upstream cannot be null");
        ;
        this.function = requireNonNull(function, "The function cannot be null");
    }

    @Override
    public void subscribe(Flow.Subscriber<? super O> subscriber) {
        requireNonNull(subscriber, "The subscriber cannot be null");
        Processor processor = new Processor();
        processor.subscribe(subscriber);
        upstream.subscribe(processor);
    }

    private class Processor extends ProcessorBase<I, O> {

        @Override
        public void onNext(I item) {
            if (!cancelled()) {
                try {
                    O result = function.apply(item);
                    if (result == null) {
                        throw new NullPointerException("The function produced a null result for item " + item);
                    }
                    downstream().onNext(result);
                } catch (Throwable failure) {
                    cancel();
                    downstream().onError(failure);
                }
            }
        }
    }
}
