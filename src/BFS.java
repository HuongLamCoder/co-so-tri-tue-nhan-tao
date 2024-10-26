import java.util.*;

public class BFS {
    // Lớp biểu diễn một đồ thị sử dụng danh sách kề
    static class Graph {
        private int numVertices; // Số đỉnh
        private LinkedList<Integer>[] adjLists; // Danh sách kề

        // Constructor: Khởi tạo đồ thị
        Graph(int vertices) {
            numVertices = vertices;
            adjLists = new LinkedList[vertices];
            for (int i = 0; i < vertices; i++) {
                adjLists[i] = new LinkedList<>();
            }
        }

        // Thêm cạnh vào đồ thị
        void addEdge(int src, int dest) {
            adjLists[src].add(dest);
        }

        // Thực hiện BFS từ một đỉnh bắt đầu và hiển thị từng bước
        List<Integer> BFS(int startNode) {
            boolean[] visited = new boolean[numVertices]; // Mảng đánh dấu đỉnh đã thăm
            Queue<Integer> queue = new LinkedList<>(); // Hàng đợi cho BFS
            List<Integer> traversalOrder = new ArrayList<>(); // Lưu thứ tự duyệt

            // Thêm đỉnh bắt đầu vào hàng đợi và đánh dấu đã thăm
            visited[startNode] = true;
            queue.add(startNode);

            System.out.println("Bat dau BFS tu dinh " + startNode);

            while (!queue.isEmpty()) {
                int u = queue.poll();
                System.out.println("Da tham dinh " + u);
                traversalOrder.add(u); // Lưu lại đỉnh đã thăm

                for (int v : adjLists[u]) {
                    if (!visited[v]) {
                        visited[v] = true; // Đánh dấu đã thăm
                        queue.add(v); // Thêm vào hàng đợi
                        System.out.println("  -> Dinh " + v + " Duoc them vao hang doi");
                    }
                }
                System.out.println("Hang doi hien tai: " + queue);
            }
            System.out.println("BFS hoan tat!");
            return traversalOrder; // Trả về thứ tự duyệt BFS
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int numVertices = 0, numEdges = 0;

        // Nhập số đỉnh với kiểm tra hợp lệ
        while (true) {
            try {
                System.out.print("Nhap so dinh cua do thi: ");
                numVertices = scanner.nextInt();
                if (numVertices > 0) break;
                else System.out.println("So dinh phai lon hon 0.");
            } catch (InputMismatchException e) {
                System.out.println("Vui long nhap mot so nguyen.");
                scanner.next(); // Xóa đầu vào không hợp lệ
            }
        }

        Graph g = new Graph(numVertices);

        // Nhập số cạnh với kiểm tra hợp lệ
        while (true) {
            try {
                System.out.print("Nhap so canh cua do thi: ");
                numEdges = scanner.nextInt();
                if (numEdges >= 0) break;
                else System.out.println("So canh khong duoc am.");
            } catch (InputMismatchException e) {
                System.out.println("Vui long nhap mot so nguyen.");
                scanner.next(); // Xóa đầu vào không hợp lệ
            }
        }

        // Nhập các cạnh với kiểm tra phạm vi
        System.out.println("Nhap cac canh duoi dang (dinh nguon dinh dich): ");
        for (int i = 0; i < numEdges; i++) {
            int src = -1, dest = -1;
            while (true) {
                try {
                    System.out.print("Canh " + (i + 1) + ": ");
                    src = scanner.nextInt();
                    dest = scanner.nextInt();
                    if (src >= 0 && src < numVertices && dest >= 0 && dest < numVertices) {
                        g.addEdge(src, dest);
                        break;
                    } else {
                        System.out.println("Cac dinh phai nam trong khoang [0, " + (numVertices - 1) + "].");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Vui long nhap hai so nguyen.");
                    scanner.next(); // Xóa đầu vào không hợp lệ
                }
            }
        }

        // Nhập đỉnh bắt đầu với kiểm tra hợp lệ
        int startNode = -1;
        while (true) {
            try {
                System.out.print("Nhap dinh bat dau duyet BFS: ");
                startNode = scanner.nextInt();
                if (startNode >= 0 && startNode < numVertices) break;
                else System.out.println("Dinh phai nam trong khoang [0, " + (numVertices - 1) + "].");
            } catch (InputMismatchException e) {
                System.out.println("Vui long nhap mot so nguyen.");
                scanner.next(); // Xóa đầu vào không hợp lệ
            }
        }

        System.out.println("------ Bat dau duyet BFS ------");
        List<Integer> result = g.BFS(startNode); // Gọi hàm BFS và lưu kết quả

        // In kết quả cuối cùng
        System.out.println("Ket qua cuoi cung sau khi BFS: " + result + "\n");
    }
}