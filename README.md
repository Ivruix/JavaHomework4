# ДЗ 4 по КПО

## Краткое условие

**Язык реализации:** Java

**Цель:** Разработать два отдельных микросервиса на основе RESTful API для системы обработки заказов в ресторане, первый из которых реализует авторизацию пользователей с различными ролями, а второй – управляет заказами и отслеживает запас блюд.

## Архитектура системы

1. **Контроллеры**: Обрабатывают входящие HTTP-запросы и управляют потоком данных. Они принимают запросы от клиентов, проверяют входные данные, вызывают соответствующие методы сервисов и возвращают HTTP-ответы с соответствующими данными или ошибками.

2. **Сервисы**: Реализуют бизнес-логику системы. Они обрабатывают запросы от контроллеров, выполняют необходимые операции с данными, взаимодействуют с репозиториями для доступа к данным и генерируют соответствующие ответы.

3. **Репозитории**: Обеспечивают доступ к данным системы, таким как пользователи, блюда, заказы и сеансы. Они предоставляют методы для поиска, создания, обновления и удаления данных в базе данных.

4. **Модели**: Представляют объекты данных, используемые в системе, такие как пользователи, блюда, заказы и сеансы. Они содержат поля и методы для работы с соответствующими данными.

5. **Утилиты**: Содержат вспомогательные функции и классы, используемые в системе. Хэш-утилита используется для хеширования паролей пользователей, а утилита токена JWT используется для генерации и проверки токенов аутентификации.

6. **DTO (Data Transfer Objects)**: Используются для передачи данных между клиентом и сервером.

## Postman

Коллекции Postman для обоих микросервисов можно найти [здесь](./postman).

## Спецификация API микросервиса авторизации

1. **Регистрация пользователя**

   - URL: `/api/users/register`
   - Метод: `POST`
   - Описание: Регистрирует нового пользователя в системе.
   - Тело запроса (JSON):
     ```
     {
       "username": "string",
       "email": "string",
       "password": "string",
       "role": "string" (optional)
     }
     ```

2. **Вход пользователя**

   - URL: `/api/users/login`
   - Метод: `POST`
   - Описание: Проверяет учетные данные пользователя и возвращает токен аутентификации.
   - Тело запроса (JSON):
     ```
     {
       "email": "string",
       "password": "string"
     }
     ```

3. **Получение информации о пользователе**

   - URL: `/api/users/info`
   - Метод: `GET`
   - Описание: Возвращает информацию о текущем пользователе на основе токена аутентификации в заголовке запроса.
   - Заголовок запроса:
     ```
     Authorization: <token>
     ```

## Спецификация API микросервиса заказов

Можно посмотреть в колекции Postman [здесь](./postman/Orders.postman_collection.json).