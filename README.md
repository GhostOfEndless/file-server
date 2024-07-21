# Тестовое задание на CaseLab Java
## Описание решения
 Стек технологий:
 * Spring Boot (Web, JPA, Validation, Test, Testcontainers)
 * Flyway
 * PostgreSQL
 * Gradle
 * Testcontainers
 * JUnit
 * Docker/Docker Compose

 В каталоге `src/main/java/com/example` лежат пакеты, реализующие основную логику работы приложения:
 * `controller` - здесь находится `UserFileRestController`, отвечающий за взаимодействие с пользователем 
 посредством HTTP-запросов. `BadRequestControllerAdvice` обрабатывает ошибки ввода пользователя и выводит 
 локализованное сообщение об ошибке. В пакете `payload` лежит record `UserFilePayload` - это объект, который получает 
 сервер от пользователя при создании файла.
 * `entity` - содержит единственный entity-класс `UserFile`. Объекты этого класса сохраняются в базу данных и 
 возвращаются пользователю в `UserFileRestController`.
 * `repository` - тут располагается интерфейс `UserFileRepository`, который работает с БД.
 * `service` - в этом пакете находится единственный интерфейс `UserFileService` и реализующий его класс 
 `DefaultUserFileService`. Этот сервис выступает прослойкой между слоем контроллеров и слоем репозиториев.

В `src/main/resources/db/migration` находится файл миграции БД `V1__Basic_schema.sql`, 
создающий в новой БД схему и таблицу. Тестовая директория миграций `src/test/resources/db/migration` содержит миграцию 
`V2__Test_data.sql` для создания тестовых данных.

Директория `src/test/java/com/example` содержит абстрактный класс `BaseTest`, настраивающий тестовый контейнер 
для базы данных, а в пакете `controller` располагается класс тестов `UserFileRestControllerTest`, покрывающий основные
случаи при использовании API приложения.

## Инструкция по запуску
**Требования: в системе должен быть установлен docker и docker-compose**

1. Скачайте архив с репозиторием в удобное место у себя на коммпьютере:
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

## Примеры тестовых запросов

Для демонстрации используем утилиту командной строки *curl*, 
но запросы можно также делать из любого другого http клиента.
Подразумевается, что приложение запущено на локальной машине, 
с которой делаются запросы (*localhost*).

* **Создание файла**
  * URI: *http://localhost:8080/file-server-api/files*
  * Пример запроса:
      ```
      curl --request POST --json '{
         "title":"Test file",
         "description":"Description of test file",
         "data": "fjdsfhlas3712nbn12831=",
         "creation_date":"2024-07-20 17:51:36"
      }' http://localhost:8080/file-server-api/files
      ```
  * Пример ответа:
      ```
      {"id":35}
      ```
  * Данные, поступающие приложению при создании файла, также 
  валидируются и, при некорректном вводе, сервер выдаёт соответствующие ошибки:
    ```
    {
     "type":"about:blank",
     "title":"Bad Request",
     "status":400,
     "detail":"Ошибка 400: некорректный запрос",
     "instance":"/file-server-api/files",
     "errors":[
        "Имя файла должно быть указано",
        "Данные должны быть указаны",
        "Дата создания должна быть указана",
        "Описание файла должно быть указано"
     ]
    }
    ```
* **Получение файла**
   * URI: *http://localhost:8080/file-server-api/files/{fileId}*
   * Пример запроса:
     ```
     curl --request GET http://localhost:8080/file-server-api/files/35
     ```
   * Пример ответа:
     ```
     {
       "id":35,
       "title":"Test file",
       "description":"Description of test file",
       "data":"fjdsfhlas3712nbn12831=",
       "creation_date":"2024-07-20 17:51:36"
     }
     ```
     Если файла с таким *fileId* не существует, то будет выведена соотвествующая ошибка:
     ```
     {
      "type":"about:blank",
      "title":"Not Found",
      "status":404,"detail":"Файл не найден",
      "instance":"/file-server-api/files/40"
     }
     ```
* **Получение страницы с файлами**
    * URI: *http://localhost:8080/file-server-api/files/page/{pageNumber}?size={pageSize}*
    * Пример запроса:
      ```
      curl --request GET http://localhost:8080/file-server-api/files/page/0?size=5
      ```
    * Пример ответа:
      ```
      {
       "totalElements":11,
       "totalPages":3,
       "pageable": {
          "pageNumber":0,
          "pageSize":5,
          "sort":{
             "unsorted":false,
             "sorted":true,
             "empty":false
          },
          "offset":0,
          "unpaged":false,
          "paged":true
       },
       "numberOfElements":5,
       "size":5,
       "content":[
          {
           "id":1,
           "title":"Test file 1",
           "description":"Description of test file 1",
           "data":"fjdsfhlas3712nbn12831=",
           "creation_date":"2024-07-20 17:51:36"
          },
          {
           "id":2,
           "title":"Test file 2",
           "description":"Description of test file 2",
           "data":"fjdsfhlas3712nbn12831=",
           "creation_date":"2024-06-20 17:51:36"
          },
          {
           "id":3,
           "title":"Test file 3",
           "description":"Description of test file 3",
           "data":"fjdsfhlas3712nbn12831=",
           "creation_date":"2024-05-20 17:51:36"
          },
          {
           "id":4,
           "title":"Test file 4",
           "description":"Description of test file 4",
           "data":"fjdsfhlas3712nbn12831=",
           "creation_date":"2024-04-20 17:51:36"},
          {
           "id":5,
           "title":"Test file 5",
           "description":"Description of test file 5",
           "data":"fjdsfhlas3712nbn12831=",
           "creation_date":"2024-03-20 17:51:36"
          }
       ],
       "number":0,
       "sort":{
          "unsorted":false,
          "sorted":true,
          "empty":false
       },
       "first":true,
       "last":false,
       "empty":false
      }
      ```