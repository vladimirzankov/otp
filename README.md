# SecureAuth OTP Сервис

## Обзор
SecureAuth OTP Сервис - это комплексное решение для генерации и валидации одноразовых паролей (OTP) с поддержкой различных каналов доставки, включая Email, Telegram, SMS и сохранение в файл. Сервис реализует ролевую модель доступа (ADMIN, USER) и аутентификацию на основе JWT.

## Технический стек
- **Основной фреймворк**: Spring Boot 3.4.4
- **Язык программирования**: Java 23
- **Безопасность**: Spring Security 6.4.4, JWT (jjwt)
- **База данных**: PostgreSQL с Spring Data JPA
- **Коммуникации**: 
  - Telegram Bots API
  - Twilio API для SMS
  - Jakarta Mail для email
- **Сборка**: Maven
- **Утилиты**: Lombok

## Системные требования
- Java 23+
- Maven 3.8+
- PostgreSQL 15+
- Токен Telegram бота
- Аккаунт Twilio (для SMS)
- SMTP сервер (для email)

## Быстрый старт

### Настройка окружения
Создайте файл `.env` со следующей конфигурацией:
```env
# Безопасность
JWT_SECRET=ваш-секретный-ключ
JWT_EXPIRATION=86400000

# Настройки базы данных
DB_URL=jdbc:postgresql://localhost:5432/verification_db
DB_USERNAME=postgres
DB_PASSWORD=ваш-пароль

# Настройки почты
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=ваш-email@gmail.com
MAIL_PASSWORD=ваш-пароль-приложения

# Настройки Telegram
TELEGRAM_BOT_TOKEN=токен-вашего-бота

# Настройки Twilio
TWILIO_ACCOUNT_SID=ваш-account-sid
TWILIO_AUTH_TOKEN=ваш-auth-token
TWILIO_PHONE_NUMBER=ваш-twilio-номер
```

### Шаги установки
1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/your-username/secureauth-verification.git
   cd secureauth-verification
   ```

2. Соберите проект:
   ```bash
   mvn clean install
   ```

3. Запустите приложение:
   ```bash
   mvn spring-boot:run
   ```

## Функциональные возможности

### Возможности пользователя
- **Аутентификация**
  - Регистрация и вход на основе JWT
  - Привязка аккаунта Telegram
  - Управление номером телефона

- **Операции с OTP**
  - Генерация кодов через различные каналы (Email, Telegram, SMS, Файл)
  - Валидация кодов
  - Управление сессиями

### Административные возможности
- **Конфигурация системы**
  - Настройка длины OTP кода
  - Управление периодом действия токенов
  - Управление пользователями
  - Мониторинг сервиса

## API Документация

### Эндпоинты аутентификации
```http
POST /api/v1/auth/register
Content-Type: application/json

{
    "login": "user@example.com",
    "password": "password123",
    "email": "user@example.com",
    "phoneNumber": "+1234567890"
}

POST /api/v1/auth/login
Content-Type: application/json

{
    "login": "user@example.com",
    "password": "password123"
}
```

### Управление пользователем
```http
PUT /api/v1/users/phone
GET /api/v1/users/profile
```

### Операции с токенами
```http
POST /api/v1/tokens/generate
POST /api/v1/tokens/validate
```

### Административные функции
```http
PUT /api/v1/admin/config
GET /api/v1/admin/config
```

## Меры безопасности
- JWT аутентификация для всех эндпоинтов, кроме `/api/v1/auth/**`
- Хеширование паролей с использованием BCrypt
- Ограниченный срок действия OTP токенов
- Защита от брутфорса
- Безопасная передача токенов через заголовок `Authorization: Bearer <token>`

## Мониторинг и логирование

### Возможности логирования
- Логирование запросов/ответов через `LoggingInterceptor`
- Детальные метрики, включающие:
  - Метод и URI запроса
  - Статус ответа
  - Время выполнения
  - Отслеживание ошибок

### Мониторинг системы
- Интеграция со Spring Boot Actuator
- Эндпоинт метрик: `/actuator/metrics`
- Проверка состояния: `/actuator/health`

## Структура проекта
```
src/main/java/com/secureauth/verification/
├── config/          # Конфигурации Spring
├── controller/      # REST эндпоинты
├── dto/            # Объекты передачи данных
├── interceptor/    # HTTP перехватчики
├── model/          # JPA сущности
├── repository/     # Репозитории данных
├── security/       # Конфигурации безопасности
└── service/        # Бизнес-логика
```

## Разработка

### Тестирование
```bash
# Запуск всех тестов
mvn test

# Запуск конкретного теста
mvn test -Dtest=TokenServiceTest
```

## Лицензия
MIT License




