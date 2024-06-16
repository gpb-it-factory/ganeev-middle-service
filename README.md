# ganeev-middle-service
Репозиторий содержит Middle-сервис общей структуры приложения "Мини-банк"

### Основные функции сервиса:
- Получение запросов от телеграм-бота
- Валидация запросов
- Маршрутизация запросов на Backend-сервис

### Для локального запуска требуется:
1) Склонировать репозиторий git clone https://github.com/gpb-it-factory/ganeev-middle-service.git
2) Перейти в директорию с проектом командой cd.
3) Собираем проект командой
    - Для linux ./gradlew build
    - Для windows gradlew.bat build
4) Запускаем проект
    - Для linux ./gradlew run
    - Для windows gradlew.bat 