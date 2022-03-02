package com.otto.catfish.pizza.order.common;

public enum OrderEventType {
	NEW("CAN_BE_CANNCELLED", 1), 
	PAYMENT_FAILED("PAYMENT_FAILED", 2), 
	CREATED("CAN_BE_CANNCELLED", 3), 
	PENDING("CAN_BE_CANNCELLED", 4),
	PREPARING("CAN_BE_CANNCELLED", 5), 
	DISPATCHED("CANNOT_BE_CANNCELLED", 6), 
	DELIVERED("CANNOT_BE_CANNCELLED", 7),
	CANCELLED("CANNOT_BE_CANNCELLED", 8);

	private final String status;
	private final int statusId;

	OrderEventType(String status, int statusId) {
		this.status = status;
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public int getStatusId() {
		return statusId;
	}

	public static boolean isAllwedToCancel(int statusId) {
		if (statusId == OrderEventType.DISPATCHED.getStatusId() 
				|| statusId == OrderEventType.DELIVERED.getStatusId()
				|| statusId == OrderEventType.CANCELLED.getStatusId()) {
			return false;
		}
		return true;
	}

}
