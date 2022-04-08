package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Or;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    Order testOrder;

    @BeforeEach
    void setUp(){

    }

    @Test
    void expiredEmptyOrder() {
        testOrder = new Order(LocalDateTime.now().plusDays(2));
        testOrder.submit();
        assertThrows(OrderExpiredException.class, () -> testOrder.confirm());
        assertEquals(Order.State.CANCELLED, testOrder.getOrderState());
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



}
