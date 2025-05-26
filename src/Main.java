import controller.MenuController;
import view.MenuWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuWindow menuWindow = new MenuWindow();
            new MenuController(menuWindow);
        });
    }
}