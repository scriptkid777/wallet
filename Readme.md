Wallet Service API
Микросервис для управления виртуальными кошельками с поддержкой высоких нагрузок (до 1000 RPS).

Вариант 1: Запуск через Docker Compose
bash
# 1. Клонируйте репозиторий
git clone <repository-url>
cd wallet-service

# 2. Создайте .env файл (опционально)
cp .env.example .env
# Отредактируйте .env при необходимости

# 3. Запустите сервис
docker-compose up --build

# Приложение будет доступно на http://localhost:8080

Вариант 2: Локальный запуск (для разработки)
bash
# 1. Установите зависимости
# PostgreSQL 15+
# Java 17+
# Maven 3.6+

# 2. Настройте PostgreSQL
sudo -u postgres psql -c "CREATE DATABASE walletdb;"
sudo -u postgres psql -c "CREATE USER walletuser WITH PASSWORD 'password';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE walletdb TO walletuser;"

# 3. Соберите проект
mvn clean package

# 4. Запустите приложение
java -jar target/wallet-service-0.0.1-SNAPSHOT.jar
