package edu.iis.mto.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    @Mock
    private Clock clockMock;
    private Order order;
    private final Instant date = Instant.parse("2005-01-01T10:00:00Z");

    @BeforeEach
    void setUp() {
        when(clockMock.getZone()).thenReturn(ZoneId.systemDefault());
        order = new Order(clockMock);
    }

    @Test
    void sameTimeOrder() {
        Instant expirationTime = date.plus(0, ChronoUnit.HOURS);
        when(clockMock.instant()).thenReturn(date).thenReturn(expirationTime);
        try {
            order.submit();
            order.confirm();
            assertSame(Order.State.CONFIRMED, order.getOrderState());
        } catch (OrderExpiredException e) {
            fail(e.getMessage());
        }
        Order.State orderState = order.getOrderState();
        assertEquals(orderState, Order.State.CONFIRMED);
    }

    @Test
    void expiredOrder() {
        Instant expirationTime = date.plus(9999999, ChronoUnit.HOURS);
        when(clockMock.instant()).thenReturn(date).thenReturn(expirationTime);
        order.addItem(new OrderItem());
        order.submit();
        Assertions.assertThrows(OrderExpiredException.class, () -> order.confirm());
        Order.State orderState = order.getOrderState();
        assertEquals(orderState, Order.State.CANCELLED);
    }

    @Test
    void hourPassed() {

        Instant expirationTime = date.plus(1, ChronoUnit.HOURS);
        when(clockMock.instant()).thenReturn(date).thenReturn(expirationTime);
        try {
            order.submit();
            order.confirm();
            assertSame(Order.State.CONFIRMED, order.getOrderState());
        } catch (OrderExpiredException e) {
            fail(e.getMessage());
        }
        Order.State orderState = order.getOrderState();
        assertEquals(orderState, Order.State.CONFIRMED);
    }
}
