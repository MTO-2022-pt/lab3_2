package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTest {

    private FakeClock fakeClock;

    @BeforeEach
    void setUp() throws Exception {
        fakeClock = new FakeClock();
    }

    @Test
    void shouldThrowOrderExpiredException() {
        long nextDaySec = 48 * 60 * 60;
        Order order = new Order(fakeClock);

        order.addItem(new OrderItem());
        order.submit();

        fakeClock.nextInstant(nextDaySec);
        assertThrows(OrderExpiredException.class, () -> {
            order.confirm();
        });
    }

    @Test
    void shouldNotThrowOrderExpiredException() {
        long nextDaySec = 10 * 60 * 60;
        Order order = new Order(fakeClock);

        order.addItem(new OrderItem());
        order.submit();

        fakeClock.nextInstant(nextDaySec);
        assertDoesNotThrow(() -> {
            order.confirm();
        });
    }

}
