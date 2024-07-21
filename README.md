## Инструкция по запуску
*Требования: в системе должен быть установлен docker и docker-compose*

1. Скопируйте архив с репозиторием в удобное место у себя на коммпьютере:
    ```
    git clone https://github.com/GhostOfEndless/file-server.git
    ```
2. Далее перейдите в директорию с файлом *docker-compose.yml*:
    ```
    cd file-server/src/main/docker
    ```
3. Теперь можно запустить приложение:
    * Для linux систем:
      ```
      docker compose up
      ```
    * Для windows систем:
      ```
      docker-compose up
      ```