package mutiny.zero.vertxpublishers;

import io.vertx.core.Future;
import io.vertx.core.streams.ReadStream;

import java.util.concurrent.Flow;
import java.util.function.Supplier;

class SuppliedFutureStreamPublisher<T> extends PublisherBase<T> {

    private final Supplier<Future<? extends ReadStream<T>>> futureStreamSupplier;

    SuppliedFutureStreamPublisher(Supplier<Future<? extends ReadStream<T>>> futureStreamSupplier) {
        this.futureStreamSupplier = futureStreamSupplier;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        Future<? extends ReadStream<T>> future = futureStreamSupplier.get();
        if (future == null) {
            subscriber.onSubscribe(new NoopSubscription());
            subscriber.onError(new NullPointerException("The future cannot be null"));
        } else {
            future.onSuccess(stream -> adapt(subscriber, stream));
            future.onFailure(err -> {
                subscriber.onSubscribe(new NoopSubscription());
                subscriber.onError(err);
            });
        }
    }
}
