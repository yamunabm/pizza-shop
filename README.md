# pizza-shop
Pizza Online Application

**Requirement:**. <br />
Build next capabilities to pizza online shop:  
    1. Customer can order a pizza in the online shop;  
    2. Customer can select additional ingredients/toppings in the order;  
    3. Customer can select different online & offline payment methods for each order;  
    4. Delivery address can be specified with every order;  
    5. Customer is allowed to cancel the order if order is not yet sent for delivery;  
    6. Customer can verify the status/details of the order.  

**Solution**. <br />
To accomplish the above requirement, I have created three micro services.  

Order-service: Responsible for creating and updating order.  
stock-service: Responsible for loading, updating, get stock details   
order-fulfillment: Responsible for managing the order like updating the status. 

In this repository Order-service code is present.  
And for stock-service and order-fulfillment services code if present at:  

https://github.com/yamunabm/stock-service. <br />
https://github.com/yamunabm/order-fulfillment. 

**Create Order:**<br />
check order quantity
lock the order
make payment
save address if new
save order
reply user: with order id and url to check the status

**Create Order:**<br />
check order status<br />
rollback payment<br />
update order status<br />
restore stock<br />

**Tech stack:**<br />
Java 8<br />
Spring boot<br />
REST <br />
Kafka<br />
H2 database<br />
lombokv
Junit<br />

**API:**<br />

Create Order<br />

```json
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
```

response:
```json
{
    "orderId": "99ba39c3-d530-42ed-8e6e-48961cd7766f",
    "location": "http://localhost:8090/pizza/v1/orderfulfillment/99ba39c3-d530-42ed-8e6e-48961cd7766f"
}
```

Cancel Order

```json
DELETE /pizza/v1/order/d865749e-9aa1-4170-b97c-cf79c954ce11 HTTP/1.1
Host: http://localhost:8080
HTTP/1.1 200 OK
```

response:
```json
{
    "orderId": "d865749e-9aa1-4170-b97c-cf79c954ce11",
    "location": "http://localhost:8090/pizza/v1/orderfulfillment/d865749e-9aa1-4170-b97c-cf79c954ce11"
}
```

How to run:
Make sure Zookeeper and kafka are running on default ports.
hit the below command in the project home directory in the respective project

java -jar target/order-service-0.0.1-SNAPSHOT.jar
java -jar target/stock-service-0.0.1-SNAPSHOT.jar
java -jar target/order-fulfillment-service-0.0.1-SNAPSHOT.jar

