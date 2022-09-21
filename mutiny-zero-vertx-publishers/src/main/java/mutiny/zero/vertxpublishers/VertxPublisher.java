package mutiny.zero.vertxpublishers;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.Flow.Publisher;
import java.util.function.Supplier;

import io.vertx.core.Future;
import io.vertx.core.streams.ReadStream;

public interface VertxPublisher {

    static <T> Publisher<T> fromSupplier(Supplier<ReadStream<T>> streamSupplier) {
        requireNonNull(streamSupplier, "The stream supplier cannot be null");
        return new SuppliedStreamPublisher<>(streamSupplier);
    }

    static <T> Publisher<T> fromFuture(Supplier<Future<? extends ReadStream<T>>> futureStreamSupplier) {
        requireNonNull(futureStreamSupplier, "The future supplier cannot be null");
        return new SuppliedFutureStreamPublisher<>(futureStreamSupplier);
    }
}
