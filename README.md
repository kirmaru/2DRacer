# 2DRacer: Простая игра на Java
2DRacer - Проект, написанный на ЯП Java, работа которого основывается на библиотеке графического интерфейса Java Swing.
![](src/main/resources/textures/background.png)
## Проект использует следующие готовые решения:
- Java Swing - библиотека графического интерфейса.
- Json - для работы с файлами формата json.
- JUnit - библиотека для автотестов.
## Описание игры
2DRacer - игра в жанре "гонки" с перспективой "top-down" в пиксельном стиле. Представляет собой небольшой пэт-проект, который был написан для изучения библиотеки Swing. Игра собрана в .jar при помощи Maven.

Функционал игры:
- Несколько различных машин на выбор
- Аркадное управление с механической коробкой передач
- Редактор уровней
- Сохранение логов прохождения трасс
- Внутриигровой магазин
- Потрясающая графика :)
- Саундтрек (by [MYRONE](https://myrone.bandcamp.com/))

## Обзор программного кода
Код разделён на разделы по архитектуре MVC (Model-View-Control).
## Model
Состоит из следующих классов:
 - Car - движок физики машины (движение, коллизии и т.д.)
 - Player - управление данными игрока
 - RaceTimer - логика таймера
 - ScoreManager - управление игровыми очками
 - Tile - логика единицы трассы ("Тайл"\"Плитка")
 - Track - логика трассы
## View
Состоит из следующих классов:
- CarView - отображение автомобиля
- HUDView - отображение элементов интерфейса
- TrackView - отображение трассы

Запуск программы происходит через класс Main
