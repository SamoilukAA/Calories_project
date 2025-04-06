# Calories_project
# 1. Описание решения
Реализован REST API сервис с использованием Spring Boot и Spring Data JPA для отслеживания дневной нормы калорий пользователя и учета съеденных блюд. Все данные хранятся в базе данных PostgeSQL.  
Используемые классы:
* User.java
  *  Поля класса: Long id, String userName, String email, int userAge, userWeight, double userHeight, String purpose - цель (Похудение, Поддержание, Набор массы), int caloriesDayNorm - дневная норма калорий.
  *  Класс содержит информацию о пользователе, id присваивается автоматически. Поле caloriesDayNorm рассчитывается автоматически по формуле Харриса-Бенедикта, после выполнения post-запроса о добавлении нового пользователя.
* Dish.java
  * Поля класса: long id, String dishName, int calories - количество калорий на порцию, String pfc - белки/жиры/углеводы (предполагаемый формат ввода - строка со значениями, разделенными символом '/').
  * Класс содержит информацию о блюде, id присваивается автоматически.
* Meal.java
  * Поля класса: long id, LocalDate mealDate - дата приема пищи, String mealName - название приема пищи (Завтрак, Обед, Ужин, Перекус), User user - информация о пользователе, соверщившим прием пищи, List<Dish> dishes - список блюд.
  * Класс содержит информацию о приеме пищи, id присваивается автоматически. Если в post-запросе на добавление приема пищи не указана дата, то полю mealDate присваивается текущая дата.

Поля этих классов помечены аннотациями для валидации входных данных, а сами классы помечены аннотацией @Entity и представляют собой таблицы в базе данных. Для связи приема пищи и пользователя (по id пользователя) используется аннотация @ManyToOne. Для того, чтобы связать прием пищи со списком блюд (по id блюд) используется связь @ManyToMany, при этом в базе данных создается промежуточная таблица "dishes_in_meal".  

Класс CaloriesController - обработчик входящих rest-запросов. В нем есть следующие методы:
* public UserCaloriesNorm createUser(@Valid @RequestBody User userRequest) - post-запрос на создание пользователя.
  * на вход принимает класс User в качестве тела запроса.
  * на выходе возвращает класс UserCaloriesNorm, содержащий два поля: long id - id созданного пользователя, int calorieDayNorm - рассчитанную дневную норму калорий пользователя.
* public ResponseEntity<User> getUser(@PathVariable long userId) - get-запрос на получение информации о пользователе по id.
* public String createDish(@Valid @RequestBody Dish dishRequest) - post-запрос на создание блюда.
  * на вход принимает класс Dish в качестве тела запроса.
  * на выходе возвращает строку с информацией о том, что блюдо создано.
* public ResponseEntity<Dish> getDish(@PathVariable long dishId) - get-запрос на получение информации о блюде по id.
* public String createMeal(@Valid @RequestBody Meal mealRequest) - post-запрос на добавление приема пищи.
 * на вход принимает класс Meal в качестве тела запроса. В теле запроса необходимо указать id пользователя и список id блюд.
 * на выходе возвращает строку с информацией о том, что прием пищи добавлен.
* public ResponseEntity<Meal> getMeal(@PathVariable long mealId) - get-запрос на получение информации о приеме пищи по id. 
* public DailyReport getReport(@PathVariable long userId, @PathVariable LocalDate mealDate) - get-запрос на получение отчета за день с суммой всех калорий и приемов пищи.
  * Выполняется по id пользователя и дате приема пищи.
  * Возвращает класс DailyReport, содержащий поля: int calories - сумма всех калорий за день, LinkedHashMap<String, ArrayList<String>> mealsPerDay - структура, содержащая информацию о всех приемах пищи в формате ключ-значение, где ключ - название приема пищи (Завтрак, Обед, Ужин, Перекус), значение - список всех блюд в конкретный прием пищи.
* public CaloriesChecker checkIntake(@PathVariable long userId, @PathVariable LocalDate mealDate) - get-запрос для проверки, уложился ли пользователь в свою дневную норму калорий.
  * Выполняется по id пользователя и дате приема пищи.
  * Возвращает класс CaloriesChecker, содержащий поля: int caloriesDayNorm - дневную норму калорий, int caloriesIntake - сумма всех калорий за день, String comment - комментарий (в случае, когда пользователь уложился в дневную норму, будет строка "Whithin the norm", в противном случае - "Out of the norm").
* public History getHistory(@PathVariable long userId) - get-запрос на получение история питания по дням.
  * Выполняется по id пользователя.
  * Возвращает класс History, содержащий поле LinkedHashMap<LocalDate, DailyReport> history - структуру в формате ключ-значение, где ключ-дата приема пищи, значение класс DailyReport, описанный выше.

В классе CaloriesServiceImpl реализована вся основная логика обработки входящих rest-запросов, обращение в базу данных и обработка ответов, приходящих из нее.  

Реализована обработка ошибок в случае, если пользователь неправильно указывает данные в пути запроса. Для этого созданы классы UserNotFoundException, DishNotFoundException, MealNotFoundException, DateNotFoundException, а так же класс ErrorController, осуществляющий обработку ошибок.

Основная логика покрыта юнит-тестами в классе CaloriesApplicationTests.java.
![image](https://github.com/user-attachments/assets/90a8b795-baa4-46b5-9299-5fddef53416e)

В файле initDB.sql содержится информация о таблицах и их полях в базе данных.

# 2. Инструкция по запуску приложения
Программа выполнена в среде разработки IntelliJ IDEA Ultimate c использованием JDK Development Kit 22.0.2.  
Для корректной работы программы необходима БД с названием "Calories", владельцем которой является пользователь root (в pgAdmin 4 создать новую Роль входа/группы с именем "root" и паролем "qwerty123", далее создать базу данных).  
Для создания исполняемого jar-файла выполнить команду mvn package в командной строке в папке с файлами программы.  
Для запуска приложения выполнить команду java -jar <путь до директории, где находится файл>/Calories-0.0.1-SNAPSHOT.jar.

# 3. Примеры тестовых запросов для проверки API-методов
Примеры тестовых запросов добавлены в Postman-коллекцию с названием CaloriesCollection.json.  
### Запрос на создание нового пользователя в БД
Post-запрос выполняется по адресу http://localhost:8080/calories_checker/new_user  
Тело запроса:  
{  
    "userName" : "nastya",  
    "email" : "nastya@gmail.com",  
    "userAge" : 23,  
    "userWeight" : 50.9,  
    "userHeight" : 163.0,  
    "purpose" : "Похудение"  
}  
В случае успеха в ответ получаем http-статус 201 и json c информацией о созданной записи:  
{  
    "id": 1,  
    "calorieDayNorm": 1208  
}  
![image](https://github.com/user-attachments/assets/9723bf94-d7ae-4ccb-952c-97f21234898b)  

### Запрос на получение информации о пользователе
Get-запрос выполняется по адресу http://localhost:8080/calories_checker/users/{userId:\d+}, где userId - id пользователя  
Пример запроса: http://localhost:8080/calories_checker/users/2  
В случае успеха ответ получим в виде:  
{  
    "id": 2,  
    "userName": "nickita",  
    "email": "nickita@yandex.ru",  
    "userAge": 23,  
    "userWeight": 70.3,  
    "userHeight": 180.0,  
    "purpose": "Набор массы",  
    "caloriesDayNorm": 2028  
}
![image](https://github.com/user-attachments/assets/fdc36fae-ade3-46a1-9c06-1c4ac70016bd)  
В случае, когда пользователя с указанным id нет, получим:  
{  
    "message": "User 100 not found"  
}  
![image](https://github.com/user-attachments/assets/61cb7c8e-fbf3-4a26-9683-bbb98496edd5)  

### Добавление и получение информации о блюдах и приемах пищи
Аналогичная ситуация для добавления и получения информации о блюдах и приемах пищи.  
Добавление блюда выполняется по адресу http://localhost:8080/calories_checker/new_dish  
Пример тела запроса:  
{  
    "dishName": "Овсянка с ягодами",  
    "calories": 300,  
    "pfc": "10/5/55"  
}  
Получение информации о блюде выполняется по адресу http://localhost:8080/calories_checker/dishes/{dishId:\d+}, где dishId - id блюда  

Добавление приема пищи выполняется по адресу http://localhost:8080/calories_checker/new_meal  
Пример тела запроса:  
{  
    "mealName" : "Завтрак",  
    "user": {"id": 1},  
    "dishes": [{"id": 1}]  
}  
Также в теле запроса можно указать дату:  
{  
    "mealName" : "Завтрак",  
    "mealDate" : "2025-04-06",  
    "user": {"id": 1},  
    "dishes": [{"id": 2}, {"id": 11}]  
}  
Получение информации о блюде выполняется по адресу http://localhost:8080/calories_checker/meals/{mealId:\d+}, где mealId - id приема пищи  
![image](https://github.com/user-attachments/assets/b5af0d5c-a239-41ef-a7a9-63d2d4a8e840)


### Получение отчета за день с суммой всех калорий и приемов пищи
Get-запрос выполняется по адресу http://localhost:8080/calories_checker/report/{userId:\d+}/{mealDate:\d{4}-\d{2}-\d{2}}, где userId - id пользователя, mealDate - дата приема пищи  
Пример запроса http://localhost:8080/calories_checker/report/1/2025-04-06  
В случае успеха ответ получим в виде:  
![image](https://github.com/user-attachments/assets/b5c53d1c-28a5-487f-a237-75ddd51c9a48)


### Проверка, уложился ли пользователь в свою дневную норму калорий
Get-запрос выполняется по адресу http://localhost:8080/calories_checker/check/{userId:\d+}/{mealDate:\d{4}-\d{2}-\d{2}}, где userId - id пользователя, mealDate - дата приема пищи  
Пример запроса http://localhost:8080/calories_checker/check/1/2025-04-06  
В случае успеха ответ получим в виде:  
![image](https://github.com/user-attachments/assets/259e8b86-e39b-4fb8-b34e-c9a9546d38de)


### История питания по дням
Get-запрос выполняется по адресу http://localhost:8080/calories_checker/history/{userId:\d+}, где userId - id пользователя  
Пример запроса http://localhost:8080/calories_checker/history/1  
В случае успеха ответ получим в виде:  
![image](https://github.com/user-attachments/assets/b342d13c-f0f5-4023-9110-bb974a138856)

