package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    Order testOrder;
    Instant testDate = Instant.parse("2005-04-02T21:37:00Z");
    @Mock
    Clock clock;

    @Test
    void expiredEmptyOrderThrows() {
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        testOrder = new Order(clock);
        when(clock.instant()).thenReturn(testDate).thenReturn(testDate.plus(2, ChronoUnit.DAYS));
        testOrder.submit();
        assertThrows(OrderExpiredException.class, () -> testOrder.confirm());
    }

    @Test
    void expiredEmptyOrderStateTest(){
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        testOrder = new Order(clock);
        when(clock.instant()).thenReturn(testDate).thenReturn(testDate.plus(2, ChronoUnit.DAYS));
        testOrder.submit();
        try{
            testOrder.confirm();
        }catch(OrderExpiredException e){
            assertEquals(Order.State.CANCELLED, testOrder.getOrderState());
        }
    }

    @Test
    void coupleHoursOldOrder(){
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        testOrder = new Order(clock);
        when(clock.instant()).thenReturn(testDate).thenReturn(testDate.plus(2, ChronoUnit.HOURS));
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
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        testOrder = new Order(clock);
        when(clock.instant()).thenReturn(testDate);
        testOrder.submit();
        testOrder.confirm();
        assertEquals(Order.State.CONFIRMED, testOrder.getOrderState());
    }

    @Test
    void validRealizedEmptyOrder(){
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        testOrder = new Order(clock);
        when(clock.instant()).thenReturn(testDate);
        testOrder.submit();
        testOrder.confirm();
        testOrder.realize();
        assertEquals(Order.State.REALIZED, testOrder.getOrderState());
    }

    @Test
    void orderStateAfterAddingItem(){
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        testOrder = new Order(clock);
        when(clock.instant()).thenReturn(testDate);
        testOrder.addItem(new OrderItem());
        assertEquals(Order.State.CREATED, testOrder.getOrderState());
        testOrder.submit();
        assertEquals(Order.State.SUBMITTED, testOrder.getOrderState());
        testOrder.addItem(new OrderItem());
        assertEquals(Order.State.CREATED, testOrder.getOrderState());
    }

    @Test
    void thoroughBehaviorTest(){
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        testOrder = new Order(clock);
        when(clock.instant()).thenReturn(testDate);
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
