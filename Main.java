import javax.swing.*;
import panels.GameWindow;

public class Main {

//TODO

   public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
           GameWindow window = new GameWindow();
           window.showWindow(); 
       });
   }
}