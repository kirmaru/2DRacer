import javax.swing.*;

public class Main {

//TODO
/*
 * СОЗДАТЬ МАГАЗИН
 * ДОРИСОВАТЬ КАРТИНКИ ДЛЯ ДРУГИХ МАШИН
 * СДЕЛАТЬ ЧТОБЫ ПАРАМЕТРЫ МАШИНЫ ИНТУИТИВНО ИЗМЕНЯЛИСЬ
 * УВЕЛИЧИТЬ РАЗМЕР ДОРОГИ В СРАВНЕНИИ С МАШИНОЙ 
 * СДЕЛАТЬ ЧТОБЫ ПОВОРОТ МАШИНЫ СО ВРЕМЕНЕМ ВОЗВРАЩАЛСЯ В НОЛЬ
 * ДОБАВИТЬ НОВЫЕ ВИДЫ ТАЙЛОВ:
 * -ВОЗВЫШЕНИЕ
 * -ГРАВИЙ 
 * -АСФАЛЬТ
 * СПАВН МАШИНЫ НА ОДИН БЛОК РАНЬШЕ ОТ СТАРТА
 * НАРИСОВАТЬ КОРОБКУ ПЕРЕДАЧ
 * ОФОРМИТЬ ИНТЕРФЕЙС
 * -РЕКОРДЫ
 * -РЕДАКТОР ТРАСС 
 * ДОБАВИТЬ ПАУЗУ 
 * ДОБАВИТЬ lРИФТ(ДОРАБОТАТЬ ИНЕРЦИЮ)
 * ДОБАВИТЬ РУЧНОЙ ТОРМОЗ
 * ДОБАВИТЬ НОЧНОЙ РЕЖИМ 
 * ДОБАВИТЬ НАГРАДЫ ЗА ПРОХОЖДЕНИЕ ТРАССЫ (МБ ДОБАВИТЬ МОНЕТКИ)
 * СДЕЛАТЬ СЛУЧАЙНУЮ ГЕНЕРАЦИЮ
 * ЧЕТО ЕЩЁ Я ХЗ
 * ИЗМЕНИТЬ НАЗВАНИЯ ПАПОК НА МОДЕЛ ВЬЮ
 * ТЕСТЫ
 * ЗАГРУЗИТЬ В ГИТ
 * ЗАГРУЗИТЬ В ТУ ПРОГУ ИЗ МЕТОДЫ
 * РАЗОБРАТЬСЯ В КОДЕ
 * ПОЧИСТИТЬ ПРОГРАММУ
 * ДОАБАВИТЬ БОЛЬШЕ МУЗЫКИ
 * ДОБАВИТЬ ЗВУКИ МАШИНЫ
 * ДОРАБОТАТЬ РЕКОРДЫ
 * СДЕЛАТЬ БОЛЬШЕ ТРЕКОВ
 * БОЛЬШЕ ТЕКСТУР
 */
   public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
           GameWindow window = new GameWindow();
           window.showWindow(); 
       });
   }
}