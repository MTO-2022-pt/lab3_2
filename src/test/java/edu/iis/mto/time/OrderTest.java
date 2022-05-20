package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    @Mock
    private Clock clock;
    private Order order;
    private final Instant date = Instant.parse("2005-01-01T10:00:00Z");

    @BeforeEach
    void setUp() {
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        order = new Order(clock);
    }

    @Test
    void sameTimeExpectedOrderConfirmed() {
        Instant expirationTime = date.plus(0, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(date).thenReturn(expirationTime);
        try {
            order.submit();
            order.confirm();
            assertSame(Order.State.CONFIRMED, order.getOrderState());
        } catch (OrderExpiredException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void differentTimeExpectedOrderCancelled() {
        Instant expirationTime = date.plus(9999999, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(date).thenReturn(expirationTime);
        
        order.addItem(new OrderItem());
        order.submit();
        Assertions.assertThrows(OrderExpiredException.class, () -> order.confirm());
        
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
    }

    @Test
    void differentTimeExpectedOrderConfirmed() {
        Instant expirationTime = date.plus(1, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(date).thenReturn(expirationTime);
        try {
            order.submit();
            order.confirm();
            assertSame(Order.State.CONFIRMED, order.getOrderState());
        } catch (OrderExpiredException e) {
            fail(e.getMessage());
        }
        
        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
    }
}
