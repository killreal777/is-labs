# Информационные системы

## Лабораторная работа №1

Реализовать информационную систему, которая позволяет взаимодействовать с объектами класса `SpaceMarine`, описание которого приведено ниже:

```java
public class SpaceMarine {
    private Long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private java.time.LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Chapter chapter; // Поле может быть null
    private Double health; // Поле не может быть null, Значение поля должно быть больше 0
    private boolean loyal;
    private Integer height; // Поле не может быть null
    private AstartesCategory category; // Поле может быть null
}
public class Coordinates {
    private Long x; // Поле не может быть null
    private Float y; // Поле не может быть null
}
public class Chapter {
    private String name; // Поле не может быть null, Строка не может быть пустой
    private String parentLegion;
    private long marinesCount; // Значение поля должно быть больше 0, Максимальное значение поля: 1000
}
public enum AstartesCategory {
    DREADNOUGHT,
    ASSAULT,
    INCEPTOR,
    TERMINATOR,
    HELIX;
}
```

### Разработанная система должна удовлетворять следующим требованиям:

*   Основное назначение информационной системы - управление объектами, созданными на основе заданного в варианте класса.
*   Необходимо, чтобы с помощью системы можно было выполнить следующие операции с объектами: создание нового объекта, получение информации об объекте по ИД, обновление объекта (модификация его атрибутов), удаление объекта. Операции должны осуществляться в отдельных окнах (интерфейсах) приложения. При получении информации об объекте класса должна также выводиться информация о связанных с ним объектах.
*   При создании объекта класса необходимо дать пользователю возможность связать новый объект с объектами вспомогательных классов, которые могут быть связаны с созданным объектом и уже есть в системе.
*   Выполнение операций по управлению объектами должно осуществляться на серверной части (не на клиенте), изменения должны синхронизироваться с базой данных.
*   На главном экране системы должен выводиться список текущих объектов в виде таблицы (каждый атрибут объекта - отдельная колонка в таблице). При отображении таблицы должна использоваться пагинация (если все объекты не помещаются на одном экране).
*   Нужно обеспечить возможность фильтровать/сортировать строки таблицы, которые показывают объекты (по значениям любой из строковых колонок). Фильтрация элементов должна производиться только по полному совпадению.
*   Переход к обновлению (модификации) объекта должен быть возможен из таблицы с общим списком объектов и из области с визуализацией объекта (при ее реализации).
*   При добавлении/удалении/изменении объекта, он должен автоматически появиться/исчезнуть/измениться в интерфейсах у других пользователей, авторизованных в системе.
*   Если при удалении объекта с ним связан другой объект, связанные объекты должны быть связаны с другим объектом (по выбору пользователя), а изначальный объект удален.
*   Пользователю системы должен быть предоставлен интерфейс для авторизации/регистрации нового пользователя. У каждого пользователя должен быть один пароль. Требования к паролю: пароль должен быть уникален. В системе предполагается использование следующих видов пользователей (ролей): обычные пользователи и администраторы. Если в системе уже создан хотя бы один администратор, зарегистрировать нового администратора можно только при одобрении одним из существующих администраторов (у администратора должен быть реализован интерфейс со списком заявок и возможностью их одобрения).
*   Редактировать и удалять объекты могут только пользователи, которые их создали, и администраторы (администраторы могут редактировать объекты, которые пользователь разрешил редактировать при создании).
*   Зарегистрированные пользователи должны иметь возможность просмотра всех объектов, но модифицировать (обновлять) могут только принадлежащие им (объект принадлежит пользователю, если он его создал). Для модификации объекта должно открываться отдельное диалоговое окно. При вводе некорректных значений в поля объекта должны появляться информативные сообщения о соответствующих ошибках.

### В системе должен быть реализован отдельный пользовательский интерфейс для выполнения специальных операций над объектами:

*   Сгруппировать объекты по значению поля creationDate, вернуть количество элементов в каждой группе.
*   Вернуть массив объектов, значение поля name которых содержит заданную подстроку.
*   Вернуть массив объектов, значение поля loyal которых больше заданного.
*   Добавить десантника в указанный орден.
*   Отчислить десантника из указанного ордена.

Представленные операции должны быть реализованы в рамках компонентов бизнес-логики приложения без прямого использования функций и процедур БД.

### Особенности хранения объектов, которые должны быть реализованы в системе:

*   Организовать хранение данных об объектах в реляционной СУБД (PostgreSQL). Каждый объект, с которым работает ИС, должен быть сохранен в базе данных.
*   Все требования к полям класса (указанные в виде комментариев к описанию классов) должны быть выполнены на уровне ORM и БД.
*   Для генерации поля id использовать средства базы данных.
*   Пароли при хранении хэшировать алгоритмом MD5.
*   При хранении объектов сохранять информацию о пользователе, который создал этот объект, а также фиксировать даты и пользователей, которые обновляли и изменяли объекты. Для хранения информации о пользователях и об изменениях объектов нужно продумать и реализовать соответствующие таблицы.
*   Таблицы БД, не отображающие заданные классы объектов, должны содержать необходимые связи с другими таблицами и соответствовать 3НФ.
*   Для подключения к БД на кафедральном сервере использовать хост `pg`, имя базы данных - `studs`, имя пользователя/пароль совпадают с таковыми для подключения к серверу.

### При создании системы нужно учитывать следующие особенности организации взаимодействия с пользователем:

*   Система должна реагировать на некорректный пользовательский ввод, ограничивая ввод недопустимых значений и информируя пользователей о причине ошибки.
*   Переходы между различными логически обособленными частями системы должны осуществляться с помощью меню.
*   Во всех интерфейсах системы должно быть реализовано отображение информации о текущем пользователе (кто авторизован) и предоставляться возможность изменить текущего пользователя.
*   \[Опциональное задание - +2 балла\] В отдельном окне ИС должна осуществляться визуализация объектов коллекции. При визуализации использовать данные о координатах и размерах объекта. Объекты от разных пользователей должны быть нарисованы разными цветами. При нажатии на объект должна выводиться информация об этом объекте.
*   При добавлении/удалении/изменении объекта, он должен автоматически появиться/исчезнуть/измениться на области у всех других клиентов.

### При разработке ИС должны учитываться следующие требования:

*   В качестве основы для реализации ИС необходимо использовать Spring MVC.
*   Для создания уровня хранения необходимо использовать JPA + Hibernate.
*   Разные уровни приложения должны быть отделены друг от друга, разные логические части ИС должны находиться в отдельных компонентах.

## Лабораторная работа №2

Доработать ИС из ЛР1 следующим образом:

*   Добавить в систему возможность массового добавления объектов при помощи импорта файла. Формат для импорта необходимо согласовать с преподавателем. Импортируемый файл должен загружаться на сервер через интерфейс разработанного веб-приложения.
*   При реализации логики импорта объектов необходимо реализовать транзакцию таким образом, чтобы в случае возникновения ошибок при импорте, не был создан ни один объект.
*   При импорте должна быть реализована проверка пользовательского ввода в соответствии с ограничениями предметной области из ЛР1.
*   При наличии вложенных объектов в основной объект из ЛР1 необходимо задавать значения полей вложенных объектов в той же записи, что и основной объект.
*   Необходимо добавить в систему интерфейс для отображения истории импорта (обычный пользователь видит только операции импорта, запущенные им, администратор - все операции).
*   В истории должны отображаться id операции, статус ее завершения, пользователь, который ее запустил, число добавленных объектов в операции (только для успешно завершенных).
*   Согласовать с преподавателем и добавить в модель из первой лабораторной новые ограничения уникальности, проверяемые на программном уровне (эти новые ограничения должны быть реализованы в рамках бизнес-логики приложения и не должны быть отображены/реализованы в БД).
*   Реализовать сценарий с использованием Apache JMeter, имитирующий одновременную работу нескольких пользователей с ИС, и проверить корректность изоляции транзакций, используемых в ЛР. По итогам исследования поведения системы при ее одновременном использовании несколькими пользователями изменить уровень изоляции транзакций там, где это требуется. Обосновать изменения.
*   Реализованный сценарий должен покрывать создание, редактирование, удаление и импорт объектов.
*   Реализованный сценарий должен проверять корректность поведения системы при попытке нескольких пользователей обновить и\или удалить один и тот же объект (например, двух администраторов).
*   Реализованный сценарий должен проверять корректность соблюдения системой ограничений уникальности предметной области при одновременной попытке нескольких пользователей создать объект с одним и тем же уникальным значением.