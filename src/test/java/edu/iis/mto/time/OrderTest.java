package edu.iis.mto.time;

import static edu.iis.mto.time.Order.State.CANCELLED;
import static edu.iis.mto.time.Order.State.CREATED;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


class OrderTest {
    private LocalDateTime expired;
    private Order expiredOrderInstance;
    private Order validOrderInstance;
    @BeforeEach
    void setUp() throws Exception {
        expired = LocalDateTime.now().plusHours(25).plusMinutes(1);
        expiredOrderInstance = new Order(expired);
        validOrderInstance = new Order();
    }

    @Test
    void orderExpiredThrowsOrderExpiredException() {
        expiredOrderInstance.submit();
        assertThrowsExactly(OrderExpiredException.class,()->expiredOrderInstance.confirm());
    }

    @Test
    void orderExpiredExpectingCancelledState() {
        try{
            expiredOrderInstance.submit();
            expiredOrderInstance.confirm();
        }catch(OrderExpiredException e){
            assertEquals(CANCELLED,expiredOrderInstance.getOrderState());
        }
    }

    @Test
    void orderCreatedExpectingCreatedState() {
        assertEquals(CREATED,expiredOrderInstance.getOrderState());
    }

    @Test
    void addedItemExpectingCreatedState() {
        validOrderInstance.submit();
        validOrderInstance.addItem(new OrderItem());
        assertEquals(CREATED,expiredOrderInstance.getOrderState());
    }

}
