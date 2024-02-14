# Инструкция по запуску
### Для запуска нужны JDK17, Maven, Docker, Git
___

Клонировать репозиторий, в терминале установить путь в корневой каталог проекта
и ввести следующие команды
- ```mvn clean package```
- ```docker compose -f .\docker-compose.yml up```
- ```mvn flyway:migrate```