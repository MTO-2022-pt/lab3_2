package edu.iis.mto.time;

import static edu.iis.mto.time.Order.State.CANCELLED;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


class OrderTest {
    private LocalDateTime expired;
    private Order expiredOrderInstance;
    @BeforeEach
    void setUp() throws Exception {
        expired = LocalDateTime.now().plusHours(25).plusMinutes(1);
        expiredOrderInstance = new Order(expired);
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

}
