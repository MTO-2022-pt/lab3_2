package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.*;

class OrderTest {
    Order testOrder;

    @Test
    void expiredEmptyOrderThrows() {
        testOrder = new Order(LocalDateTime.now().plusDays(2));
        testOrder.submit();
        assertThrows(OrderExpiredException.class, () -> testOrder.confirm());
    }

    @Test
    void expiredEmptyOrderStateTest(){
        testOrder = new Order(LocalDateTime.now().plusDays(2));
        testOrder.submit();
        try{
            testOrder.confirm();
        }catch(OrderExpiredException e){
            assertEquals(Order.State.CANCELLED, testOrder.getOrderState());
        }
    }

    @Test
    void coupleHoursOldOrder(){
        testOrder = new Order(LocalDateTime.now().plusHours(5));
        testOrder.submit();
        try{
            testOrder.confirm();
        }catch(OrderExpiredException e){
            fail(e.getMessage());
        }
        assertEquals(Order.State.CONFIRMED, testOrder.getOrderState());
    }

    @Test
    void validConfirmedEmptyOrder(){
        testOrder = new Order(LocalDateTime.now());
        testOrder.submit();
        testOrder.confirm();
        assertEquals(Order.State.CONFIRMED, testOrder.getOrderState());
    }

    @Test
    void validRealizedEmptyOrder(){
        testOrder = new Order(LocalDateTime.now());
        testOrder.submit();
        testOrder.confirm();
        testOrder.realize();
        assertEquals(Order.State.REALIZED, testOrder.getOrderState());
    }

    @Test
    void orderStateAfterAddingItem(){
        testOrder = new Order(LocalDateTime.now());
        testOrder.addItem(new OrderItem());
        assertEquals(Order.State.CREATED, testOrder.getOrderState());
        testOrder.submit();
        assertEquals(Order.State.SUBMITTED, testOrder.getOrderState());
        testOrder.addItem(new OrderItem());
        assertEquals(Order.State.CREATED, testOrder.getOrderState());
    }

    @Test
    void thoroughBehaviorTest(){
        testOrder = new Order(LocalDateTime.now());
        assertEquals(Order.State.CREATED, testOrder.getOrderState());
        testOrder.addItem(new OrderItem());
        assertEquals(Order.State.CREATED, testOrder.getOrderState());
        testOrder.submit();
        assertEquals(Order.State.SUBMITTED, testOrder.getOrderState());
        testOrder.confirm();
        assertEquals(Order.State.CONFIRMED, testOrder.getOrderState());
        testOrder.realize();
        assertEquals(Order.State.REALIZED, testOrder.getOrderState());
    }




}
