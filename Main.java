import javax.swing.*;
import panels.GameWindow;

public class Main {

//TODO


 //TODO 2
 /*
ПРОБЛЕМЫ:
    - ВО ВРЕМЯ ДРИФТА НЕТ КОЛЛИЗИЙ С БАРЬЕРОМ
ОБЯЗАТЕЛЬНО СДЕЛАТЬ:
    - НАСТРОИТЬ ТЕСТЫ
    - НАСТРОИТЬ ПОКАЗАТЕЛИ МАШИНЫ
    - ДОБАВИТЬ БОЛЬШЕ ТРАСС
    - ДОБАВИТЬ БОЛЬШЕ МАШИН
    - ДОРИСОВАТЬ ИНТЕРФЕЙС
    - СДЕЛАТЬ ПАУЗУ
    - РЕКОРДЫ СОРТИРОВКА ПО ВРЕМЕНИ
    - ПЕРЕИМЕНОВАТЬ ПАКЕТЫ 
    - ???
  */
   public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
           GameWindow window = new GameWindow();
           window.showWindow(); 
       });
   }
}