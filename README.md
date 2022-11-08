# country-determination-service

Microservice to determine the country by phone number.
The user enters a phone number, the system validates it and displays the country or an error message.
For country codes, use the table on the page:
https://en.wikipedia.org/wiki/List_of_country_calling_codes
https://en.wikipedia.org/wiki/List_of_mobile_telephone_prefixes_by_country

## Requirements

- Java 11+
- Spring Boot 2
- Spring Data JPA - MySQL
- Maven/Gradle
- HTTP, RESTful сервис с JSON-форматом данных.

Вспомогательные библиотеки - на Ваше усмотрение, однако нельзя использовать те,
которые являются решением задачи (например, Google Phone Validator и т.д.)

Приложение должно собираться и запускаться из командной строки, на 8080 порту.
Также должна быть возможность запуска тестов и просмотра отчётов по ним.
Все обращения к приложению делаются через RESTful сервис с JSON в качестве формата данных.
Внешний вид интерфейса неважен, достаточно опрятного HTML.
Для запросов используйте любой fetch, AJAX-capable фреймворк, можно JQuery,
Валидация данных, тесты обязательны.

Срок выполнения задачи - 1 неделя с момента получения.

## Installation

Use [maven](https://maven.apache.org/users/index.html)

Unix:

```bash
./mvnw spring-boot:run
```

Windows:

```cmd
mvnw.cmd spring-boot:run
```

## Usage

Request

```
curl 'http://localhost:8080/api/v1/countries?mobile=+1(123)4567890'
```

## Copyright

[LICENSE](https://choosealicense.com/licenses/mit/)