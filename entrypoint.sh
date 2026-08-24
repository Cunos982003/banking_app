#!/bin/sh
set -e

DB_HOST=${DB_HOST:-core-db}
DB_PORT=${DB_PORT:-5432}
DB_USER=${SPRING_DATASOURCE_USERNAME:-bank_user}

echo "Waiting for database ${DB_HOST}:${DB_PORT} to be ready..."
# Use PGPASSWORD env if provided by compose
until pg_isready -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" >/dev/null 2>&1; do
  sleep 1
done

echo "Database is ready - starting application"
exec java -jar /app/app.jar
