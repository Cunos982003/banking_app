@echo off
setlocal enabledelayedexpansion

echo === Banking System Orchestration ===
echo Starting automatic setup...
echo.

echo Step 1: Shutting down existing containers...
docker compose down
echo Done.
echo.

echo Step 2: Building all services...
docker compose build --no-cache
if !errorlevel! neq 0 (
    echo Build failed!
    exit /b 1
)
echo Done.
echo.

echo Step 3: Starting all services...
docker compose up -d
echo Done.
echo.

echo Step 4: Waiting for services to reach healthy state...
timeout /t 5 /nobreak
docker compose ps

echo.
echo === System Status ===
echo [+] API Gateway (port 80): http://localhost
echo [+] Account Service (internal): http://account-service:8080
echo [+] Transaction Service (internal): http://transaction-service:8081
echo [+] Database (internal): postgres://core-db:5432
echo.
echo === Test Commands ===
echo List accounts: curl http://localhost/api/v1/accounts
echo Create account: curl -X POST -H "Content-Type: application/json" -d "{\"ownerName\":\"Alice\",\"initialBalance\":1000}" http://localhost/api/v1/accounts
echo.
echo System is ready!
pause
