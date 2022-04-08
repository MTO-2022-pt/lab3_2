package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


class OrderTest {
    private LocalDateTime expired;
    private Order expiredOrderInstance;
    @BeforeEach
    void setUp() throws Exception {
        expired = LocalDateTime.now().plusDays(1);
        expiredOrderInstance = new Order(expired);
    }

    @Test
    void orderExpiredThrowsOrderExpiredException() {
        assertThrowsExactly(OrderStateException.class,()->expiredOrderInstance.confirm());
    }

}
