package io.flowing.retail.zeebe.order.persistence;

import org.springframework.data.repository.CrudRepository;

import io.flowing.retail.zeebe.order.domain.Order;

public interface OrderRepository extends CrudRepository<Order, String> {

}
