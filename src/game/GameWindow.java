package game;

import javax.swing.*;
import java.util.Scanner;

public class GameWindow extends JFrame {


    public GameWindow(int maxDepth, boolean userPlaysFirst) {
        this.add(new GamePanel(maxDepth, userPlaysFirst));
        this.setTitle("Reversi v0.1");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    //default constractor se periptosi poy den perastoun parametroi
    public GameWindow() {
        this(4, true); //
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        //rotame ton xristi gia to vathos tis Minimax elexontas oti einai egkyri i apantisi toy
        int maxDepth = 4;  // default
        while (true) {
            System.out.print("Δώσε βάθος αναζήτησης για τον Minimax (1 - 10): ");
            String line = sc.nextLine().trim();

            try {
                int d = Integer.parseInt(line);
                if (d >= 1 && d <= 10) {
                    maxDepth = d;
                    break;
                } else {
                    System.out.println("Πρέπει να είναι ακέραιος από 1 μέχρι 10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Μη έγκυρη τιμή, δοκίμασε ξανά.");
            }
        }

        // rotame an thelei na paiksei protos
        boolean userPlaysFirst = true;
        while (true) {
            System.out.print("Θέλεις να παίξεις πρώτος (μαύρα); (y/n): ");
            String ans = sc.nextLine().trim().toLowerCase();

            if (ans.startsWith("y") || ans.startsWith("ν")) { // y / yes / ναι
                userPlaysFirst = true;
                break;
            } else if (ans.startsWith("n") || ans.startsWith("ο")) { // n / no / όχι
                userPlaysFirst = false;
                break;
            } else {
                System.out.println("Γράψε 'y' για ναι ή 'n' για όχι.");
            }
        }


        new GameWindow(maxDepth, userPlaysFirst);
    }
}
