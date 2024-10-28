import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class Menu {
    private boolean isRunning = true;
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        PuzzleSolver pz = new PuzzleSolver();
        GraphColoringApp graphColoring = new GraphColoringApp();
        BFS bfs = new BFS();

        // Add listener to handle window close event
        graphColoring.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        graphColoring,
                        "Are you sure you want to close this window?",
                        "Confirm Close",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    System.out.println("Cửa sổ Tô màu đồ thị đã đóng. Bạn có thể nhập lựa chọn khác trong terminal.\n");
                    isRunning = false; // Update flag to false
                    graphColoring.setVisible(false); // Hide the window
                }
            }
        });
        int choice;
        loop:
        while (true) {
            if (!isRunning) {
                // Prevents showing the menu until the window is fully closed
                isRunning = true; // Reset flag for next loop iteration
            } else {
                System.out.println("----------------------Menu---------------------------");
                System.out.println("0. Thoát khỏi chương trình.");
                System.out.println("1. Thuật toán tham lam cho bài toán tô màu đồ thị.");
                System.out.println("2. Thuật toán tìm kiếm theo chiều rộng của đồ thị.");
                System.out.println("3. Thuật toán AKT giải bài toán TACI (8-puzzle).");
                System.out.println("-----------------------------------------------------");
                System.out.print("Mời nhập lựa chọn: ");
                choice = sc.nextInt();
                System.out.print("\n");
                sc.nextLine();

                switch (choice) {
                    case 1:
                        isRunning = true;
                        graphColoring.setVisible(true);
                        while (isRunning) {
                            try {
                                Thread.sleep(100); // Small delay to prevent high CPU usage
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                    case 2:
                        bfs.start();
                        break;
                    case 3:
                        pz.start();
                        break;
                    case 0:
                        System.out.println("Thoát khỏi chương trình!");
                        break loop;
                    default:
                        System.out.println("Bạn đã nhập sai lựa chọn!");
                        break;
                }
            }
        }
    sc.close();
    }
}