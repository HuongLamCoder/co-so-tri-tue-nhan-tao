import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GraphColoringApp extends JFrame {
    private final int WIDTH = 800, HEIGHT = 600, RADIUS = 20;
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private Map<Vertex, Integer> vertexColors = new HashMap<>();
    private int currentVertexIndex = 0;
    private boolean autoMode = false, isEnglish = true;
    private javax.swing.Timer timer;
    private JTextArea logArea;

    private JButton backButton, startButton, stepButton, clearButton, languageButton, helpButton;

    public GraphColoringApp() {
        setupFrame();
        DrawingPanel drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);

        logArea = new JTextArea(10, 40);
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.NORTH);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        setupDrawingPanelMouseListener(drawingPanel);
        setupButtonActions();
    }

    private void setupFrame() {
        setTitle("Graph Coloring - Greedy Algorithm");
        setSize(WIDTH, HEIGHT);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Back Step");
        startButton = new JButton("Start");
        stepButton = new JButton("Next Step");
        clearButton = new JButton("Clear");
        languageButton = new JButton("Language");
        helpButton = new JButton("Help");

        buttonPanel.add(backButton);
        buttonPanel.add(startButton);
        buttonPanel.add(stepButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(languageButton);
        buttonPanel.add(helpButton);
        return buttonPanel;
    }

    private void setupDrawingPanelMouseListener(DrawingPanel drawingPanel) {
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    addVertex(e);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    addEdge(e);
                }
            }
        });
    }

    private void addVertex(MouseEvent e) {
        vertices.add(new Vertex(e.getX(), e.getY(), vertices.size() + 1));
        logArea.append((isEnglish ? "Added Vertex " : "Thêm Đỉnh ") + vertices.size() +
                (isEnglish ? " at (" : " tại (") + e.getX() + ", " + e.getY() + ")\n");
        repaint();
    }

    private void addEdge(MouseEvent e) {
        if (vertices.size() >= 2) {
            Vertex v1 = getClosestVertex(e.getX(), e.getY());
            vertices.remove(v1);
            Vertex v2 = getClosestVertex(e.getX(), e.getY());
            vertices.add(v1);
            edges.add(new Edge(v1, v2));
            logArea.append((isEnglish ? "Added Edge between Vertex " : "Thêm Cạnh giữa Đỉnh ") +
                    v1.number + (isEnglish ? " and Vertex " : " và Đỉnh ") + v2.number + "\n");
            repaint();
        }
    }

    private void setupButtonActions() {
        stepButton.addActionListener(e -> stepColoring());
        backButton.addActionListener(e -> undoColoring());
        clearButton.addActionListener(e -> clearGraph());
        startButton.addActionListener(e -> toggleAutoMode());
        languageButton.addActionListener(e -> toggleLanguage());
        helpButton.addActionListener(e -> showHelp());
    }

    private void stepColoring() {
        if (currentVertexIndex < vertices.size()) {
            colorCurrentVertex();
        } else {
            JOptionPane.showMessageDialog(null, isEnglish ? "All vertices are colored." : "Tất cả các đỉnh đã được tô màu.");
        }
    }

    private void undoColoring() {
        if (currentVertexIndex > 0) {
            Vertex removedVertex = vertices.get(currentVertexIndex - 1);
            vertexColors.remove(removedVertex);
            logArea.append((isEnglish ? "Removed color from Vertex " : "Xóa màu của Đỉnh ") + removedVertex.number + "\n");
            currentVertexIndex--;
            repaint();
        }
    }

    private void clearGraph() {
        vertices.clear();
        edges.clear();
        vertexColors.clear();
        currentVertexIndex = 0;
        logArea.setText((isEnglish ? "Graph cleared.\n" : "Đã xóa đồ thị.\n"));
        repaint();
    }

    private void toggleAutoMode() {
        autoMode = !autoMode;
        startButton.setText(isEnglish ? (autoMode ? "Stop" : "Start") : (autoMode ? "Dừng" : "Bắt Đầu"));
        if (autoMode) {
            startColoring();
        } else if (timer != null) {
            timer.stop();
        }
    }

    private void toggleLanguage() {
        isEnglish = !isEnglish;
        changeLanguage();
        languageButton.setText(isEnglish ? "Language" : "Ngôn Ngữ");
    }

    private void showHelp() {
        String helpMessage = isEnglish
                ? "Left click to add vertex, right click to add edge between two closest vertices.\n"
                + "- Use the Next Step button to color each vertex one by one.\n"
                + "- Use the Back Step button to undo the last step.\n"
                + "- Use the Clear button to reset the graph.\n"
                + "- The Start/Stop button controls the automatic step mode.\n"
                + "- The Language button switches between English and Vietnamese."
                : "Nhấn chuột trái để thêm đỉnh, nhấn chuột phải để thêm cạnh giữa hai đỉnh gần nhất.\n"
                + "- Sử dụng nút Tiếp Theo để tô màu đỉnh kế tiếp.\n"
                + "- Sử dụng nút Quay Lại để hoàn tác bước phía trước.\n"
                + "- Sử dụng nút Xóa để đặt lại đồ thị.\n"
                + "- Nút Bắt Đầu/Dừng để điều khiển chế độ tô màu tự động.\n"
                + "- Nút Ngôn Ngữ chuyển đổi giữa Tiếng Anh và Tiếng Việt.";
        JOptionPane.showMessageDialog(null, helpMessage);
    }

    private void changeLanguage() {
        if (isEnglish) {
            setTitle("Graph Coloring - Greedy Algorithm");
            backButton.setText("Back Step");
            startButton.setText("Start");
            stepButton.setText("Next Step");
            clearButton.setText("Clear");
            helpButton.setText("Help");
        } else {
            setTitle("Tô Màu Đồ Thị - Thuật Toán Tham Lam");
            backButton.setText("Quay Lại");
            startButton.setText("Bắt Đầu");
            stepButton.setText("Tiếp Theo");
            clearButton.setText("Xóa");
            helpButton.setText("Trợ Giúp");
        }
    }

    private void colorCurrentVertex() {
        Vertex vertex = vertices.get(currentVertexIndex);
        HashSet<String> usedColors = new HashSet<>();

        for (Edge edge : edges) {
            if (edge.v1 == vertex && vertexColors.containsKey(edge.v2)) {
                usedColors.add(getColorName(vertexColors.get(edge.v2)));
            } else if (edge.v2 == vertex && vertexColors.containsKey(edge.v1)) {
                usedColors.add(getColorName(vertexColors.get(edge.v1)));
            }
        }

        String color = getColorName(findFirstUnusedColor(usedColors));
        vertexColors.put(vertex, findFirstUnusedColor(usedColors));

        logVertexColoring(vertex, usedColors, color);
        currentVertexIndex++;
        repaint();
    }

    private void logVertexColoring(Vertex vertex, HashSet<String> usedColors, String color) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append((isEnglish ? "Colored Vertex " : "Tô màu Đỉnh ") + vertex.number +
                (isEnglish ? " with color " : " với màu ") + color + ".\n");
        logMessage.append((isEnglish ? "Vertex " : "Đỉnh ") + vertex.number +
                (isEnglish ? " is near: " : " gần: "));

        boolean foundColoredNeighbor = false;

        for (Edge edge : edges) {
            if (edge.v1 == vertex) {
                if (vertexColors.containsKey(edge.v2)) {
                    logMessage.append((isEnglish ? "Vertex " : "Đỉnh ") + edge.v2.number +
                            " (" + getColorName(vertexColors.get(edge.v2)) + ") ");
                    foundColoredNeighbor = true;
                } else {
                    logMessage.append((isEnglish ? "Vertex " : "Đỉnh ") + edge.v2.number + " ");
                }
            } else if (edge.v2 == vertex) {
                if (vertexColors.containsKey(edge.v1)) {
                    logMessage.append((isEnglish ? "Vertex " : "Đỉnh ") + edge.v1.number +
                            " (" + getColorName(vertexColors.get(edge.v1)) + ") ");
                    foundColoredNeighbor = true;
                } else {
                    logMessage.append((isEnglish ? "Vertex " : "Đỉnh ") + edge.v1.number + " ");
                }
            }
        }

        if (foundColoredNeighbor) {
            logMessage.append((isEnglish ? ". Therefore, color " : ". Do đó, màu ") + color +
                    (isEnglish ? " is assigned based on adjacent colors." : " được gán dựa trên màu của các đỉnh gần.\n"));
        } else {
            logMessage.append((isEnglish ? ". Therefore, color " : ". Do đó, màu ") + color +
                    (isEnglish ? " is assigned since no adjacent vertices are colored." : " được gán vì không có đỉnh gần nào được tô màu.\n"));
        }

        logArea.append(logMessage.toString());
    }

    private String getColorName(int colorIndex) {
        String[] colorNamesEnglish = {"Red", "Green", "Blue", "Orange", "Cyan", "Magenta", "Pink", "Yellow"};
        String[] colorNamesVietnamese = {"Đỏ", "Xanh Lục", "Xanh Dương", "Cam", "Xanh Lam", "Tím", "Hồng", "Vàng"};
        return isEnglish ? colorNamesEnglish[colorIndex % colorNamesEnglish.length] : colorNamesVietnamese[colorIndex % colorNamesVietnamese.length];
    }

    private int findFirstUnusedColor(Set<String> usedColors) {
        String[] colorNames = isEnglish
                ? new String[]{"Red", "Green", "Blue", "Orange", "Cyan", "Magenta", "Pink", "Yellow"}
                : new String[]{"Đỏ", "Xanh Lục", "Xanh Dương", "Cam", "Xanh Lam", "Tím", "Hồng", "Vàng"};

        for (int i = 0; i < colorNames.length; i++) {
            if (!usedColors.contains(colorNames[i])) {
                return i;
            }
        }
        return 0;
    }

    private void startColoring() {
        timer = new javax.swing.Timer(600, e -> {
            if (currentVertexIndex < vertices.size() && autoMode) {
                colorCurrentVertex();
            } else {
                ((javax.swing.Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawEdges(g);
            drawVertices(g);
        }

        private void drawEdges(Graphics g) {
            for (Edge edge : edges) {
                g.setColor(vertices.indexOf(edge.v1) == currentVertexIndex || vertices.indexOf(edge.v2) == currentVertexIndex ? Color.RED : Color.BLACK);
                g.drawLine(edge.v1.x, edge.v1.y, edge.v2.x, edge.v2.y);
            }
        }

        private void drawVertices(Graphics g) {
            for (Vertex vertex : vertices) {
                int x = vertex.x - RADIUS, y = vertex.y - RADIUS;
                g.setColor(vertexColors.containsKey(vertex) ? getColor(vertexColors.get(vertex)) : Color.LIGHT_GRAY);
                g.fillOval(x, y, RADIUS * 2, RADIUS * 2);
                g.setColor(Color.BLACK);
                g.drawOval(x, y, RADIUS * 2, RADIUS * 2);
                drawVertexNumber(g, vertex);
            }
        }

        private void drawVertexNumber(Graphics g, Vertex vertex) {
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(String.valueOf(vertex.number));
            g.drawString(String.valueOf(vertex.number), vertex.x - textWidth / 2, vertex.y + fm.getAscent() / 2);
        }

        private Color getColor(int colorIndex) {
            Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.YELLOW};
            return colors[colorIndex % colors.length];
        }
    }

    private Vertex getClosestVertex(int x, int y) {
        Vertex closest = null;
        double minDist = Double.MAX_VALUE;
        for (Vertex vertex : vertices) {
            double dist = Math.hypot(vertex.x - x, vertex.y - y);
            if (dist < minDist) {
                minDist = dist;
                closest = vertex;
            }
        }
        return closest;
    }

    class Vertex {
        int x, y, number;

        Vertex(int x, int y, int number) {
            this.x = x;
            this.y = y;
            this.number = number;
        }
    }

    class Edge {
        Vertex v1, v2;

        Edge(Vertex v1, Vertex v2) {
            this.v1 = v1;
            this.v2 = v2;
        }
    }
}