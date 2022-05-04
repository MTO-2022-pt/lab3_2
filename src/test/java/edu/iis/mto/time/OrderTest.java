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
    void NoOffsetEmptyOrderItemStateTest() {
        Order order = new Order();
        assertEquals(order.getOrderState(), Order.State.CREATED);
    }

    @Test
    void OneYearEmptyOrderItemStateTest() {
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

//
//    @Test
//    void OneSecondFromNowOneOrderItemStateTest() {
//        Order order = new Order(LocalDateTime.now().plusSeconds(1));
//        order.addItem(new OrderItem());
//        assertEquals(order.getOrderState(), Order.State.CREATED);
//    }
//
//    @Test
//    void tenMinutesFromNowOneOrderItemStateTest() {
//        Order order = new Order(LocalDateTime.now().plusMinutes(10));
//        order.addItem(new OrderItem());
//        order.submit();
//        assertEquals(order.getOrderState(), Order.State.SUBMITTED);
//    }
//
//    @Test
//    void tenMinutesFromNowFiveOrderItemsStateTest() {
//        Order order = new Order(LocalDateTime.now().plusMinutes(10));
//        for(int i = 0; i< 5; i++){
//            order.addItem(new OrderItem());
//        }
//        order.submit();
//        assertEquals(order.getOrderState(), Order.State.SUBMITTED);
//    }
//
//    @Test
//    void sevenHoursFromNowFiveOrderItemsStateTest() {
//        Order order = new Order(LocalDateTime.now().plusHours(7));
//        for(int i = 0; i< 5; i++){
//            order.addItem(new OrderItem());
//        }
//        order.submit();
//        order.confirm();
//        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
//    }
//
//    @Test
//    void moreThanTwelveHoursFromNowFiveOrderItemsStateTest() {
//        Order order = new Order(LocalDateTime.now().plusHours(14));
//        for(int i = 0; i< 5; i++){
//            order.addItem(new OrderItem());
//        }
//        order.submit();
//        order.confirm();
//        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
//    }
//
//    @Test
//    void OneDayFromNowFiveOrderItemsStateTest() { //almost a day passed
//        Order order = new Order(LocalDateTime.now().plusDays(1));
//        for(int i = 0; i< 5; i++){
//            order.addItem(new OrderItem());
//        }
//        order.submit();
//        order.confirm();
//        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
//    }
//
//    @Test
//    void OneDayAndABitMoreFromNowFiveOrderItemsStateTest() { //more than 24 hours... correct?
//        Order order = new Order(LocalDateTime.now().plusDays(1).plusHours(1));
//        for(int i = 0; i< 5; i++){
//            order.addItem(new OrderItem());
//        }
//        order.submit();
//        order.confirm();
//        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
//    }
//
//    @Test
//    void timeoutOrderStatesTest() {
//        Order order = new Order(LocalDateTime.now().plusDays(1).plusHours(1).plusSeconds(1));
//        for(int i = 0; i< 5; i++){
//            order.addItem(new OrderItem());
//        }
//        order.submit();
//        assertThrows(OrderExpiredException.class, order::confirm);
//        assertEquals(order.getOrderState(), Order.State.CANCELLED);
//    }
//
//    @Test
//    void OneDayAndTwoHoursFromNowFiveOrderItemsStateTest() {
//        Order order = new Order(LocalDateTime.now().plusDays(1).plusHours(2));
//        for(int i = 0; i< 5; i++){
//            order.addItem(new OrderItem());
//        }
//        order.submit();
//        assertThrows(OrderExpiredException.class, order::confirm);
//        assertEquals(order.getOrderState(), Order.State.CANCELLED);
//        assertThrows(OrderStateException.class, order::realize);
//        assertEquals(order.getOrderState(), Order.State.CANCELLED);
//    }
//
//    @Test
//    void tenMinutesFromNowFiveOrderItemsRealizedStateTest() {
//        Order order = new Order(LocalDateTime.now().plusMinutes(10));
//        for(int i = 0; i< 5; i++){
//            order.addItem(new OrderItem());
//        }
//        order.submit();
//        order.confirm();
//        order.realize();
//        assertEquals(order.getOrderState(), Order.State.REALIZED);
//    }

}
