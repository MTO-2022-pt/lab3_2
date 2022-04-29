package edu.iis.mto.time;

import static edu.iis.mto.time.Order.State.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    private static final int EXPIRED_HOURS = 25;
    private static final int NON_EXPIRED_HOURS = 1;
    private Order mockedOrderInstance;

    @Mock
    private Clock mockedClock;

    @BeforeEach
    void setUp() {
        lenient().when(mockedClock.getZone()).thenReturn(ZoneId.systemDefault());
        mockedOrderInstance = new Order(mockedClock);
    }

    @Test
    void orderExpiredThrowsOrderExpiredException() {
        setHoursForNextCall(EXPIRED_HOURS);
        mockedOrderInstance.submit();
        assertThrowsExactly(OrderExpiredException.class, () -> mockedOrderInstance.confirm());
    }

    @Test
    void orderExpiredExpectingCancelledState() {
        setHoursForNextCall(EXPIRED_HOURS);
        try {
            mockedOrderInstance.submit();
            mockedOrderInstance.confirm();
        } catch (OrderExpiredException e) {
            assertEquals(CANCELLED, mockedOrderInstance.getOrderState());
        }
    }

    @Test
    void orderCreatedExpectingCreatedState() {
        assertEquals(CREATED, mockedOrderInstance.getOrderState());
    }

    @Test
    void addedItemExpectingCreatedState() {
        setHoursForNextCall(NON_EXPIRED_HOURS);
        mockedOrderInstance.submit();
        mockedOrderInstance.addItem(new OrderItem());
        assertEquals(CREATED, mockedOrderInstance.getOrderState());
    }

    @Test
    void validSubmisionExpectingSubmittedState() {
        setHoursForNextCall(NON_EXPIRED_HOURS);
        mockedOrderInstance.addItem(new OrderItem());
        mockedOrderInstance.submit();
        assertEquals(SUBMITTED, mockedOrderInstance.getOrderState());
    }

    @Test
    void validConfirmExpectingConfirmedState() {
        setHoursForNextCall(NON_EXPIRED_HOURS);
        mockedOrderInstance.addItem(new OrderItem());
        mockedOrderInstance.submit();
        mockedOrderInstance.confirm();
        assertEquals(CONFIRMED, mockedOrderInstance.getOrderState());
    }

    @Test
    void addingItemToExpiredOrderExpectingOrderStateException() {
        setHoursForNextCall(EXPIRED_HOURS);
        try {
            mockedOrderInstance.submit();
            mockedOrderInstance.confirm();
        } catch (OrderExpiredException e) {
            assertThrowsExactly(OrderStateException.class, () -> mockedOrderInstance.addItem(new OrderItem()));
        }
    }

    @Test
    void validRealizeCallExpectingRealizedState() {
        setHoursForNextCall(NON_EXPIRED_HOURS);
        mockedOrderInstance.addItem(new OrderItem());
        mockedOrderInstance.submit();
        mockedOrderInstance.confirm();
        mockedOrderInstance.realize();
        assertEquals(REALIZED, mockedOrderInstance.getOrderState());
    }

    @Test
    void invalidRealizeCallExpectingOrderStateException() {
        setHoursForNextCall(NON_EXPIRED_HOURS);
        mockedOrderInstance.addItem(new OrderItem());
        mockedOrderInstance.submit();

        assertThrowsExactly(OrderStateException.class, () -> mockedOrderInstance.realize());
    }

    private void setHoursForNextCall(int hours) {
        Instant instant = Instant.now();
        when(mockedClock.instant()).thenReturn(instant).thenReturn(instant.plus(hours, ChronoUnit.HOURS));
    }
}
