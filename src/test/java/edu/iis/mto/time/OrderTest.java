package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;


class OrderTest {

    @BeforeEach
    void setUp() throws Exception {}

    @Test
    void test() {
        Order order = new Order(LocalDateTime.of(2023, Month.APRIL, 29, 23, 12, 12));
        System.out.println(order.getOrderState());
        order.addItem(new OrderItem());
        order.submit();
        order.confirm();
    }

}
