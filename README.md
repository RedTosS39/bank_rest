Отлично! Вот готовый README.md для вашего Swagger API:

Bank Cards REST API
 Описание
REST API для управления банковскими картами, пользователями и транзакциями. Система поддерживает ролевую модель (ADMIN/USER) с различными уровнями доступа.

 Аутентификация
API использует JWT токены для аутентификации. Добавьте токен в заголовок запроса:

text
Authorization: Bearer <your_jwt_token>
 Base URL
text
http://localhost:8080
📋 Основные endpoints
 Аутентификация
Метод	Endpoint	Описание	Доступ
POST	/api/auth/registration	Регистрация нового пользователя	PUBLIC
POST	/api/auth/login	Вход в систему	PUBLIC
GET	/api/auth	Страница регистрации	PUBLIC
 Пользователь (USER role)
Метод	Endpoint	Описание
GET	/user/cards	Получить список своих карт (с пагинацией)
GET	/user/balance	Показать баланс конкретной карты
POST	/transfer	Перевод между картами
 Администратор (ADMIN role)
Метод	Endpoint	Описание
GET	/admin/cards	Получить все карты в системе
GET	/admin/create_card	Создать новую карту
GET	/admin/{id}/user	Получить информацию о пользователе
PUT	/admin/{id}/assign	Привязать карту к пользователю
PATCH	/admin/{id}/block	Изменить статус карты
DELETE	/admin/{id}/cards	Удалить карту
 Модели данных
 UserDto
json
{
  "id": 0,
  "username": "string",
  "password": "string",
  "role": "ADMIN"
}
 CardDto
json
{
  "id": 0,
  "cardNumber": "string",
  "expiredDate": "2024-01-01",
  "balance": 0,
  "status": "ACTIVE"
}
AuthRequest
json
{
  "username": "string",
  "password": "string"
}
 Параметры запросов
Пагинация
page - номер страницы (по умолчанию: 0)

size - размер страницы (по умолчанию: 5)

searchingCard - поиск по номеру карты

Статусы карт
ACTIVE - активна

BLOCKED - заблокирована

EXPIRED - просрочена

Роли пользователей
ADMIN - администратор

USER - обычный пользователь

 Быстрый старт
1. Регистрация пользователя
bash
curl -X POST "http://localhost:8080/api/auth/registration" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user123",
    "password": "password123",
    "role": "USER"
  }'
2. Авторизация
bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user123",
    "password": "password123"
  }'
3. Использование с JWT токеном
bash
curl -X GET "http://localhost:8080/user/cards?page=0&size=10" \
  -H "Authorization: Bearer <your_jwt_token>"
 Дополнительная информация
Swagger UI
Интерактивная документация доступна по адресу:

text
http://localhost:8080/swagger-ui.html
API Docs
Спецификация OpenAPI 3.1:

text
http://localhost:8080/v3/api-docs

Важные заметки
Для операций с картами требуется роль ADMIN

Пользователи могут просматривать только свои карты

Переводы возможны только между активными картами

JWT токен действителен 24 часа
