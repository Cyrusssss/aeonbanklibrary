
# Library System

This project is a backend service to provide library functionality via Restful API as follows.

- [Library System](#library-system)
   * [Prerequisite](#prerequisite)
   * [How to build](#how-to-build)
   * [How to run in localhost](#how-to-run-in-localhost)
   * [API documentation](#api-documentation)
      - [Response Body](#response-body)
      - [Response Status Code](#response-status-code)
      + [Book API](#book-api)
         - [1. list](#1-list)
         - [2. get](#2-get)
         - [3. add](#3-add)
         - [4. update](#4-update)
         - [5. delete](#5-delete)
         - [6. borrow book](#6-borrow-book)
         - [7. return book](#7-return-book)
      + [Borrower API](#borrower-api)
         - [1. list](#1-list-1)
         - [2. get](#2-get-1)
         - [3. add](#3-add-1)
         - [4. update](#4-update-1)
         - [5. delete](#5-delete-1)

## Prerequisite

1. Java 21
2. Mysql 8.0 Server
   - Kindly refer to `db` folder in project root directory to init the database and tables
3. Docker
   - to build docker image or run the image as container locally

## How to build

1. `mvn clean install -Pdev`
   - `-Pdev` mean build the app using profile `dev`, you may change to `prd` or any other available profiles.
2. `docker build -t aeonbanklibrary-app .`
   - build the project as docker image
3. `docker save -o aeonbanklibrary-app.tar aeonbanklibrary-app:latest`
   - now you have the docker image .tar file, which can used to do deployment, for example upload to AWS ECR.

## How to run in localhost
1. `docker rm -f aeonbanklibrary-container`
   - this will remove existing container
2. `docker run -d -p 8080:8080 --name aeonbanklibrary-container aeonbanklibrary-app`
   - this will run the docker image in docker container, and exposing the port 8080.

## API documentation

Spring Doc Swagger documentation is available at `/swagger-ui/index.html` but with limited information due to system design, the request data object information is missing.

#### Response Body

API response body format is fixed, which contain response data object, and status information.

**Response Data and Type:**

- Object with following fields

| Field | Type | Desc |
| - | - | - |
| request | Object | contain request input data |
| data | Object | the response data, can be anything: List/Object/etc |
| code | String | status code - to indicate if the request is processed successfully or not |
| message | String | status message |
| detail | String | status details |
| pagination | Object | contain pagination info, typically used when requesting data list |

**Sample Response Body:**

```json
{
    "request": <Request Data Object>,
    "data": <Can be Anything>,
    "code": "000000",
    "message": "Operation success",
    "pagination": {
        "pageNo": 1,
        "pageSize": 25
    }
}
```

#### Response Status Code

- Any code other than `000000` mean the request is failed.

| Code | Message | Desc |
| - | - | - |
| 000000 | Operation success | Request successfully processed |
| 999999 | Something went wrong | Typically caused by system exception |
| 999998 | Invalid input | Input data is invalid |
| 999997 | Data not found | Unable to get the respective data in db to process the request |
| 999996 | Book is not available | Book is borrowed and unable to be borrowed again |
| 999995 | Transaction not found | Every book-borrowing will create a transaction in db. When returning a book, we need to mark the transaction as completed. This error is due to the respective transaction is not found. |
| 999994 | Data not tally | Book with same ISBN must have same Title and Author. This error indicate user trying to insert a same book to the system without same Title and Author. |
| 999993 | Book not returned | Book is currently borrowed. |

### Book API

#### 1. list

**Http Method:** `GET`

**URL:** `/api/v1/book`

**Response Data and Type:**

- List of objects, with following fields

| Field | Type |
| - | - |
| id | Long |
| isbn | String |
| title | String |
| author | String |
| borrowerId | Long |
| createdBy | String |
| createdDate | Date |
| updatedBy | String |
| updatedDate | Date |

**Sample Response:**

```json
{
    "request": {},
    "data": [
        {
            "id": 3,
            "createdBy": "system",
            "createdDate": "2024-08-06T05:05:38.000+00:00",
            "updatedDate": "2024-08-06T08:38:12.000+00:00",
            "isbn": "9780072673425",
            "title": "333 title",
            "author": "333 author",
            "borrowerId": 3
        }
    ],
    "code": "000000",
    "message": "Operation success",
    "pagination": {
        "pageNo": 1,
        "pageSize": 25
    }
}
```

#### 2. get

**Http Method:** `GET`

**URL:** `/api/v1/book/<id>`

**URL Path Variable:**

| Field | Type | Desc |
| - | - | - |
| id | Long | id of book |

**Response Data and Type:**

- Object, with following fields

| Field | Type |
| - | - |
| id | Long |
| isbn | String |
| title | String |
| author | String |
| borrowerId | Long |
| createdBy | String |
| createdDate | Date |
| updatedBy | String |
| updatedDate | Date |

**Sample Response:**

```json
{
    "request": {
        "id": 1
    },
    "data": {
        "id": 1,
        "createdBy": "system",
        "createdDate": "2024-08-06T05:05:27.000+00:00",
        "updatedDate": "2024-08-06T05:13:36.000+00:00",
        "isbn": "1687800014",
        "title": "111 title1",
        "author": "111 author1",
        "borrowerId": 2
    },
    "code": "000000",
    "message": "Operation success",
    "pagination": {}
}
```

#### 3. add

**Http Method:** `POST`

**URL:** `/api/v1/book`

**Request Data Fields and Type:**

| Field | Type | Mandatory |
| - | - | - |
| isbn | String | true |
| title | String | true |
| author | String | true |

**Sample Request:**

```json
{
    "isbn": "9780072673425",
    "title": "333 title",
    "author": "333 author"
}
```

**Response Data Fields and Type:**

- No data to be returned, kindly refer to the `code` to check if operation is success. Anything other than `000000` mean the request is failed.

**Sample Response:**

```json
{
    "request": {
        "isbn": "9780072673425",
        "title": "333 title",
        "author": "333 author"
    },
    "code": "000000",
    "message": "Operation success",
    "pagination": {}
}
```

#### 4. update

**Http Method:** `PUT`

**URL:** `/api/v1/book/<id>`

**URL Path Variable:**

| Field | Type | Desc |
| - | - | - |
| id | Long | id of book |

**Request Data Fields and Type:**

| Field | Type | Mandatory | Note |
| - | - | - | - |
| isbn | String | true |  |
| title | String | true |  |
| author | String | true |  |
| borrowerId | Long | false | if book is borrowed but null value passed in, book will be returned. |

**Sample Request:**

```json
{
    "isbn": "9780072673425",
    "title": "333 title",
    "author": "333 author",
    "borrowerId": 3
}
```

**Response Data Fields and Type:**

- No data to be returned, kindly refer to the `code` to check if operation is success. Anything other than `000000` mean the request is failed.

**Sample Response:**

```json
{
    "request": {
        "id": 3,
        "isbn": "9780072673425",
        "title": "333 title",
        "author": "333 author",
        "borrowerId": 3
    },
    "code": "000000",
    "message": "Operation success",
    "pagination": {}
}
```

#### 5. delete

**Http Method:** `DELETE`

**URL:** `/api/v1/book/<id>`

**URL Path Variable:**

| Field | Type | Desc |
| - | - | - |
| id | Long | id of book |

**Response Data Fields and Type:**

- No data to be returned, kindly refer to the `code` to check if operation is success. Anything other than `000000` mean the request is failed.

**Sample Response:**

```json
{
    "request": {
        "id": 1
    },
    "code": "000000",
    "message": "Operation success",
    "pagination": {}
}
```

#### 6. borrow book

**Http Method:** `POST`

**URL:** `/api/v1/book/<id>/borrow-book`

**URL Path Variable:**

| Field | Type | Desc |
| - | - | - |
| id | Long | id of book |

**Request Data Fields and Type:**

| Field | Type | Mandatory |
| - | - | - |
| borrowerId | Long | true |

**Sample Request:**

```json
{
    "borrowerId": 3
}
```

**Response Data Fields and Type:**

- No data to be returned, kindly refer to the `code` to check if operation is success. Anything other than `000000` mean the request is failed.

**Sample Response:**

```json
{
    "request": {
        "borrowerId": 3,
        "bookId": 2
    },
    "code": "000000",
    "message": "Operation success",
    "pagination": {}
}
```

#### 7. return book

**Http Method:** `POST`

**URL:** `/api/v1/book/<id>/return-book`

**URL Path Variable:**

| Field | Type | Desc |
| - | - | - |
| id | Long | id of book |

**Response Data Fields and Type:**

- No data to be returned, kindly refer to the `code` to check if operation is success. Anything other than `000000` mean the request is failed.

**Sample Response:**

```json
{
    "request": {
        "id": 2
    },
    "code": "000000",
    "message": "Operation success",
    "pagination": {}
}
```

### Borrower API

#### 1. list

**Http Method:** `GET`

**URL:** `/api/v1/borrower`

**Response Data and Type:**

- List of objects, with following fields

| Field | Type |
| - | - |
| id | Long |
| name | String |
| email | String |
| createdBy | String |
| createdDate | Date |
| updatedBy | String |
| updatedDate | Date |

**Sample Response:**

```json
{
    "data": [
        {
            "id": 3,
            "createdBy": "system",
            "createdDate": "2024-08-06T05:12:23.000+00:00",
            "name": "ccc name",
            "email": "ccc@email.com"
        }
    ],
    "code": "000000",
    "message": "Operation success",
    "pagination": {
        "pageNo": 1,
        "pageSize": 25
    }
}
```

#### 2. get

**Http Method:** `GET`

**URL:** `/api/v1/borrower/<id>`

**URL Path Variable:**

| Field | Type | Desc |
| - | - | - |
| id | Long | id of borrower |

**Response Data and Type:**

- Object, with following fields

| Field | Type |
| - | - |
| id | Long |
| name | String |
| email | String |
| createdBy | String |
| createdDate | Date |
| updatedBy | String |
| updatedDate | Date |

**Sample Response:**

```json
{
    "request": {
        "id": 3
    },
    "data": {
        "id": 3,
        "createdBy": "system",
        "createdDate": "2024-08-06T05:12:23.000+00:00",
        "name": "ccc name",
        "email": "ccc@email.com"
    },
    "code": "000000",
    "message": "Operation success",
    "pagination": {}
}
```

#### 3. add

**Http Method:** `POST`

**URL:** `/api/v1/borrower`

**Request Data Fields and Type:**

| Field | Type | Mandatory |
| - | - | - |
| name | String | true |
| email | String | true |

**Sample Request:**

```json
{
    "name": "ccc name",
    "email": "ccc@email.com"
}
```

**Response Data Fields and Type:**

- No data to be returned, kindly refer to the `code` to check if operation is success. Anything other than `000000` mean the request is failed.

**Sample Response:**

```json
{
    "request": {
        "name": "ccc name",
        "email": "ccc@email.com"
    },
    "code": "000000",
    "message": "Operation success",
    "pagination": {}
}
```

#### 4. update

**Http Method:** `PUT`

**URL:** `/api/v1/borrower/<id>`

**URL Path Variable:**

| Field | Type | Desc |
| - | - | - |
| id | Long | id of borrower |

**Request Data Fields and Type:**

| Field | Type | Mandatory |
| - | - | - |
| name | String | true |
| email | String | true |

**Sample Request:**

```json
{
    "name": "aaa1 name",
    "email": "aaa1@email.com"
}
```

**Response Data Fields and Type:**

- No data to be returned, kindly refer to the `code` to check if operation is success. Anything other than `000000` mean the request is failed.

**Sample Response:**

```json
{
    "request": {
        "id": 1,
        "name": "aaa1 name",
        "email": "aaa1@email.com"
    },
    "code": "000000",
    "message": "Operation success",
    "pagination": {}
}
```

#### 5. delete

**Http Method:** `DELETE`

**URL:** `/api/v1/borrower/<id>`

**URL Path Variable:**

| Field | Type | Desc |
| - | - | - |
| id | Long | id of borrower |

**Response Data Fields and Type:**

- No data to be returned, kindly refer to the `code` to check if operation is success. Anything other than `000000` mean the request is failed.

**Sample Response:**

```json
{
  "request": {
    "id": 2
  },
  "code": "000000",
  "message": "Operation success",
  "pagination": {}
}
```
