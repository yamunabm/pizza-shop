# pizza-shop
Pizza Online Application

**Requirement:**
Build next capabilities to pizza online shop:
    1. Customer can order a pizza in the online shop;
    2. Customer can select additional ingredients/toppings in the order;  
    3. Customer can select different online & offline payment methods for each order;
    4. Delivery address can be specified with every order;
    5. Customer is allowed to cancel the order if order is not yet sent for delivery;
    6. Customer can verify the status/details of the order.

**Solution** 
To accomplish the above requirement, I have created three micro services.
Order-service: Responsible for creating and updating order.
stock-service: Responsible for loading, updating, get stock details 
order-fulfillment: Responsible for managing the order like updating the status

In this repository Order-service code is present.
And for stock-service and order-fulfillment services code if present at:
https://github.com/yamunabm/stock-service
https://github.com/yamunabm/order-fulfillment

**Create Order:**
check order quantity
lock the order
make payment
save address if new
save order
reply user: with order id and url to check the status

**Create Order:**
check order status
rollback payment
update order status
restore stock

**Tech stack:**
Java 8
Spring boot
REST 
Kafka
H2 database
lombok
Junit

**API:**

Create Order

POST /pizza/v1/order HTTP/1.1
Host: http://localhost:8080
HTTP/1.1 200 OK
{
    "customerId": 2,
    "totalPrice": 672,
    "address": {
        "address" : " #101, 2nd street",
        "city": "Germany",
        "zipCode": "10101"
    },
    "items": [{
        "itemId": 2,
        "name": "pizza",
        "unitPrice": 56,        
        "quantity": 12,
        "pizzaSizeType": "SMALL",
        "pizzaCrustType": "THIN",
        "toppings": [1,3]

    }],
    "payment": {
        "paymentType": "CARD",
        "card": {
            "cardType": "VISA_CREDIT",
            "nameOnCard": "Yamuna",
            "cardNumber": 123456789,
            "expiryDate": "01/2030"
        }
    }
}

response:
{
    "orderId": "99ba39c3-d530-42ed-8e6e-48961cd7766f",
    "location": "http://localhost:8090/pizza/v1/orderfulfillment/99ba39c3-d530-42ed-8e6e-48961cd7766f"
}

Cancel Order

DELETE /pizza/v1/order/d865749e-9aa1-4170-b97c-cf79c954ce11 HTTP/1.1
Host: http://localhost:8080
HTTP/1.1 200 OK

response:
{
    "orderId": "d865749e-9aa1-4170-b97c-cf79c954ce11",
    "location": "http://localhost:8090/pizza/v1/orderfulfillment/d865749e-9aa1-4170-b97c-cf79c954ce11"
}
