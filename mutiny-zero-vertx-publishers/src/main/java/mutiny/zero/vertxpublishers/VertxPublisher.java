package mutiny.zero.vertxpublishers;

import io.vertx.core.Future;
import io.vertx.core.streams.ReadStream;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Publisher;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public interface VertxPublisher {

    static <T> Publisher<T> fromSupplier(Supplier<ReadStream<T>> streamSupplier) {
        requireNonNull(streamSupplier, "The stream supplier cannot be null");
        return new SuppliedStreamPublisher<>(streamSupplier);
    }

    static <T> Publisher<T> fromFuture(Supplier<Future<? extends ReadStream<T>>> futureStreamSupplier) {
        requireNonNull(futureStreamSupplier, "The future supplier cabbot be null");
        return new SuppliedFutureStreamPublisher<>(futureStreamSupplier);
    }
}
