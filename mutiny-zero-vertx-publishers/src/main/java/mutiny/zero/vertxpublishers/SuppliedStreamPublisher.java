package mutiny.zero.vertxpublishers;

import io.vertx.core.streams.ReadStream;

import java.util.concurrent.Flow;
import java.util.function.Supplier;

class SuppliedStreamPublisher<T> extends PublisherBase<T> {

    private final Supplier<ReadStream<T>> streamSupplier;

    SuppliedStreamPublisher(Supplier<ReadStream<T>> streamSupplier) {
        this.streamSupplier = streamSupplier;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        ReadStream<T> stream = streamSupplier.get();
        if (stream == null) {
            subscriber.onSubscribe(new NoopSubscription());
            subscriber.onError(new NullPointerException("The stream cannot be null"));
        } else {
            adapt(subscriber, stream);
        }
    }
}
