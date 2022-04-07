package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.time.LocalDateTime;
import java.time.Month;


class OrderTest {

    Order order;

    @BeforeEach
    void setUp(){
    }

    @Test
    void OneSecondFromNowOneOrderItemStateTest() {
        Order order = new Order(LocalDateTime.now().plusSeconds(1));
        order.addItem(new OrderItem());
        assertEquals(order.getOrderState(), Order.State.CREATED);
    }

    @Test
    void tenMinutesFromNowOneOrderItemStateTest() {
        Order order = new Order(LocalDateTime.now().plusMinutes(10));
        order.addItem(new OrderItem());
        order.submit();
        assertEquals(order.getOrderState(), Order.State.SUBMITTED);
    }

    @Test
    void sevenHoursFromNowFiveOrderItemsStateTest() {
        Order order = new Order(LocalDateTime.now().plusHours(7));
        for(int i = 0; i< 5; i++){
            order.addItem(new OrderItem());
        }
        order.submit();
        order.confirm();
        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
    }

    @Test
    void moreThanTwelveHoursFromNowFiveOrderItemsStateTest() {
        Order order = new Order(LocalDateTime.now().plusHours(14));
        for(int i = 0; i< 5; i++){
            order.addItem(new OrderItem());
        }
        order.submit();
        order.confirm();
        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
    }

    @Test
    void OneDayFromNowFiveOrderItemsStateTest() { //almost a day passed
        Order order = new Order(LocalDateTime.now().plusDays(1));
        for(int i = 0; i< 5; i++){
            order.addItem(new OrderItem());
        }
        order.submit();
        order.confirm();
        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
    }



}
