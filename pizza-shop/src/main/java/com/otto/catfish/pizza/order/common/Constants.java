package com.otto.catfish.pizza.order.common;

import java.util.UUID;

public interface Constants {

	public static final String ID_GENERATOR = "ID_GENERATOR";
	public static final String DEDUCT_STOCK = "DEDUCT_STOCK";
	public static final String RESTORE_STOCK = "RESTORE_STOCK";
	public static String UNIQUE_ID = UUID.randomUUID().toString();
	static final String PAYMENT_FAILED = "PAYMENT_FAILED";
	public static final String OUT_OF_STOCK = "OUT_OF_STOCK";

	interface Swagger {
		String PROJECT_NAME = "pizza-ship";
		String PROJECT_DESCRIPTION = "Service to handle pizza order";
		String TEAM_NAME = "pizza order";

		interface Tags {

			interface OrderController {
				String NAME = "Pizza";
				String DESCRIPTION = "Order manage";
			}
		}
	}

	interface Summary {
		String APP_NAME = "appName";
		String APP_VERSION = "appVersion";
		String APP_PROFILE = "activeProfile";
		String APP_PROCESS_ID = "processID";
	}

	interface Paths {
		String BASE_PATH = "/api/v1/";

		interface ApiPaths {
			String ORDER = "order";
			String CREATE_ORDER = "create";
		}
	}
}
