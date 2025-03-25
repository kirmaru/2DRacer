import javax.swing.*;
import panels.GameWindow;

public class Main {

   public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
           GameWindow window = new GameWindow();
           window.showWindow(); 
       });
   }
}