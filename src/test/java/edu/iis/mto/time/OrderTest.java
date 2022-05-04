package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.time.*;


class OrderTest {

    @Mock
    Clock clock;

    @BeforeEach
    void setUp(){
        clock = Mockito.mock(Clock.class);
    }

    @Test
    void noOffsetEmptyOrderItemStateTest() {
        Order order = new Order();
        assertEquals(order.getOrderState(), Order.State.CREATED);
    }

    @Test
    void noOffsetOneItemOrderItemStateTest() {
        Order order = new Order();
        order.addItem(new OrderItem());
        assertEquals(order.getOrderState(), Order.State.CREATED);
    }

    @Test
    void noOffsetFiveItemsOrderItemStateTest() {
        Order order = new Order();
        for(int i = 0; i < 5; i++){
            order.addItem(new OrderItem());
        }
        assertEquals(order.getOrderState(), Order.State.CREATED);
    }

    @Test
    void noOffsetFiveItemsOrderItemSubmittedStateTest() {
        Order order = new Order();
        for(int i = 0; i < 5; i++){
            order.addItem(new OrderItem());
        }
        order.submit();
        assertEquals(order.getOrderState(), Order.State.SUBMITTED);
    }

    @Test
    void noOffsetFiveItemsOrderItemConfirmedStateTest() {
        Order order = new Order();
        for (int i = 0; i < 5; i++) {
            order.addItem(new OrderItem());
        }
        order.submit();
        order.confirm();
        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
    }

    @Test
    void noOffsetFiveItemsOrderItemRealizedStateTest() {
        Order order = new Order();
        for (int i = 0; i < 5; i++) {
            order.addItem(new OrderItem());
        }
        order.submit();
        order.confirm();
        order.realize();
        assertEquals(order.getOrderState(), Order.State.REALIZED);
    }

    @Test
    void aFewOursItemsOrderItemRealizedStateTest() {
        Instant instant1 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withHour(5).toInstant();
        Instant instant2 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withHour(7).toInstant();
        Mockito.when(clock.instant())
                .thenReturn(instant1)
                .thenReturn(instant2);
        Mockito.when(clock.getZone())
                .thenReturn(ZoneOffset.UTC);
        Order order = new Order(clock);
        for (int i = 0; i < 5; i++) {
            order.addItem(new OrderItem());
        }
        order.submit();
        order.confirm();
        order.realize();
        assertEquals(order.getOrderState(), Order.State.REALIZED);
    }

    @Test
    void oneYearEmptyOrderItemStateTest() {
        Instant instant1 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withYear(1999).toInstant();
        Instant instant2 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withYear(2000).toInstant();
        Mockito.when(clock.instant())
                .thenReturn(instant1)
                .thenReturn(instant2);
        Mockito.when(clock.getZone())
                .thenReturn(ZoneOffset.UTC);
        Order order = new Order(clock);
        order.submit();
        assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
    }

    @Test
    void aFewMonthsEmptyOrderItemStateTest() {
        Instant instant1 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withDayOfYear(100).toInstant();
        Instant instant2 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withDayOfYear(200).toInstant();
        Mockito.when(clock.instant())
                .thenReturn(instant1)
                .thenReturn(instant2);
        Mockito.when(clock.getZone())
                .thenReturn(ZoneOffset.UTC);
        Order order = new Order(clock);
        order.submit();
        assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
    }

    @Test
    void oneWeekEmptyOrderItemStateTest() {
        Instant instant1 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withDayOfYear(7).toInstant();
        Instant instant2 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withDayOfYear(14).toInstant();
        Mockito.when(clock.instant())
                .thenReturn(instant1)
                .thenReturn(instant2);
        Mockito.when(clock.getZone())
                .thenReturn(ZoneOffset.UTC);
        Order order = new Order(clock);
        order.submit();
        assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
    }

    @Test
    void oneDayAndABitMoreEmptyOrderItemStateTest() {
        Instant instant1 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withDayOfYear(7).toInstant();
        Instant instant2 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withDayOfYear(8).withHour(1).withSecond(1).toInstant();
        Mockito.when(clock.instant())
                .thenReturn(instant1)
                .thenReturn(instant2);
        Mockito.when(clock.getZone())
                .thenReturn(ZoneOffset.UTC);
        Order order = new Order(clock);
        order.submit();
        assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
    }

    @Test
    void oneDayAndABitMoreFiveItemsOrderItemStatesTest() {
        Instant instant1 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withDayOfYear(7).toInstant();
        Instant instant2 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withDayOfYear(8).withHour(1).withSecond(1).toInstant();
        Mockito.when(clock.instant())
                .thenReturn(instant1)
                .thenReturn(instant2);
        Mockito.when(clock.getZone())
                .thenReturn(ZoneOffset.UTC);
        Order order = new Order(clock);
        for(int i = 0 ; i < 5; i++){
            order.addItem(new OrderItem());
        }
        order.submit();
        assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
        assertThrows(OrderStateException.class, order::realize);
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
    }

    @Test
    void oneDayEmptyOrderItemStateTest() { //one day passed... and, still alright?
        Instant instant1 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withDayOfYear(7).toInstant();
        Instant instant2 = (Instant.EPOCH).atZone(ZoneOffset.UTC).withDayOfYear(8).toInstant();
        Mockito.when(clock.instant())
                .thenReturn(instant1)
                .thenReturn(instant2);
        Mockito.when(clock.getZone())
                .thenReturn(ZoneOffset.UTC);
        Order order = new Order(clock);
        order.submit();
        assertEquals(order.getOrderState(), Order.State.SUBMITTED);
    }

}
