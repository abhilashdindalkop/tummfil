package com.ecommerce.dao;

import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.ecommerce.models.sql.BoyAssignedOrders;
import com.ecommerce.models.sql.DeliveryBoys;
import com.ecommerce.models.sql.Orders;

import utils.MyConstants.OrderStatus;

public class BoyAssignedOrdersDAO {

	public BoyAssignedOrders add(DeliveryBoys deliveryBoy, Orders order) {
		BoyAssignedOrders newAssignment = new BoyAssignedOrders();
		newAssignment.setDeliveryBoy(deliveryBoy);
		newAssignment.setOrder(order);
		newAssignment.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
		newAssignment.setCreatedTime(new Date());
		newAssignment.save();
		return newAssignment;
	}

	public List<BoyAssignedOrders> getAssignedOrders(DeliveryBoys deliveryBoy, int orderStatus) {
		List<BoyAssignedOrders> assigneOrderList = Ebean.find(BoyAssignedOrders.class).where()
				.eq("deliveryBoy", deliveryBoy).eq("order_status", orderStatus).findList();
		return assigneOrderList;
	}

	public BoyAssignedOrders findByOrderId(Orders order) {
		return Ebean.find(BoyAssignedOrders.class).where().eq("orders", order).findUnique();
	}

	public void updateOrderStatus(Orders order, int orderStatus) {
		BoyAssignedOrders assignedOrder = findByOrderId(order);
		assignedOrder.setOrderStatus(orderStatus);
		assignedOrder.update();
	}
}
