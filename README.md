# pizza-shop
Pizza Online Application

## Requirement
Build next capabilities to pizza online shop:  
    1. Customer can order a pizza in the online shop;  
    2. Customer can select additional ingredients/toppings in the order;  
    3. Customer can select different online & offline payment methods for each order;  
    4. Delivery address can be specified with every order;  
    5. Customer is allowed to cancel the order if order is not yet sent for delivery;  
    6. Customer can verify the status/details of the order.  

## Solution
To accomplish the above requirement, I have created two microservices.  

Order-service: Responsible for creating and updating order.  
stock-service: Responsible for loading, updating, get stock details  


**Create Order:**<br />
check order quantity
lock the order
make payment
save address if new
save order
reply user: with order id and url to check the status

**Cancel Order:**<br />
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
Postman<br />

##  List of APIs

## Create Order<br />

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

## Cancel Order

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

## get order

```json
GET /pizza/v1/order/d865749e-9aa1-4170-b97c-cf79c954ce11 HTTP/1.1
Host: http://localhost:8080
HTTP/1.1 200 OK
```

response:
```json
[
    {
        "id": 1,
        "orderId": "d865749e-9aa1-4170-b97c-cf79c954ce11",
        "customerId": 1,
        "address": {
            "addressId": 3,
            "address": " #101, 2nd street",
            "city": "Germany",
            "zipCode": "10101"
        },
        "payment": {
            "paymentId": 1,
            "paymentType": "CASH"
        },
        "totalPrice": 112.0,
        "orderStatus": "NEW",
        "orderTimestamp": "2022-03-06T12:22:15.71",
        "items": [
            {
                "itemId": 1,
                "name": "Pizza",
                "unitPrice": 150.0,
                "pizzaSizeType": "SMALL",
                "pizzaCrustType": "THIN"
            }
        ]
    }
]
```

## How to run:<br />

1. microservices are dockerized and using docker-compose for the same.<br />
2. Clone the projects:<br />
   https://github.com/yamunabm/pizza-shop
3. goto order-service project home directory and run build-order-service.sh<br />
> ./build-order-service.sh<br />
4. goto stock-service project home directory and run build-order-service.sh<br />
> ./build-stock-service.sh<br />
5. run start.sh from order-service project home directory <br />
> ./start.sh<br />
6. Hit the above APIs and test it.<br />



Note: 
1. Since no customer and payment services are present, all the information related to customer and payment are stored in order-service temporarily.
   But in the real case customer, payment services will be seperated.<br />
2. Here both the projects are present in the same repositories. but practically, there will be each repository for each project.   
3. No validations are taken care, like checking if order id or address id or payment id or customer id is present or not. Because assuming that when user has selected any address, or user has logged in then by default user will have customer id and other neccessary information.**


