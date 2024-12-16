import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AnimatedTicTacToe extends JFrame {
    private static final int SIZE = 3;
    private String currentPlayer = "X";
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private String[][] board = new String[SIZE][SIZE];
    private JPanel panel;

    public AnimatedTicTacToe() {
        setTitle("Tic Tac Toe with Animations");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setLayout(new GridLayout(SIZE, SIZE));
        panel.setBackground(new Color(40, 44, 52));
        add(panel);
        initializeBoard();
        setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[row][col].setFocusPainted(false);
                buttons[row][col].setBackground(new Color(80, 80, 80));
                buttons[row][col].setForeground(Color.WHITE);
                buttons[row][col].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 4));
                buttons[row][col].addActionListener(new ButtonClickListener(row, col));
                addButtonHoverEffect(buttons[row][col]);
                panel.add(buttons[row][col]);
                board[row][col] = null;
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board[row][col] != null) {
                return;
            }
            board[row][col] = currentPlayer;
            animateButtonPress(buttons[row][col], currentPlayer);
            if (checkWinner()) {
                disableBoard();
                JOptionPane.showMessageDialog(AnimatedTicTacToe.this, "Player " + currentPlayer + " wins!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                resetBoard();
            } else if (isDraw()) {
                JOptionPane.showMessageDialog(AnimatedTicTacToe.this, "It's a draw!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                resetBoard();
            } else {
                currentPlayer = currentPlayer.equals("X") ? "O" : "X";
            }
        }
    }

    private void animateButtonPress(JButton button, String player) {
        Timer timer = new Timer(20, new ActionListener() {
            int alpha = 255;

            @Override
            public void actionPerformed(ActionEvent e) {
                alpha -= 15;
                if (alpha <= 0) {
                    ((Timer) e.getSource()).stop();
                    button.setText(player);
                    button.setForeground(player.equals("X") ? Color.RED : Color.BLUE);
                } else {
                    button.setBackground(new Color(255 - alpha, 255 - alpha, 255));
                }
            }
        });
        timer.start();
    }

    private void addButtonHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 100, 100));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.getText().equals("")) {
                    button.setBackground(new Color(80, 80, 80));
                }
            }
        });
    }

    private boolean checkWinner() {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] != null && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                highlightWinningLine(i, 0, i, 1, i, 2);
                return true;
            }
            if (board[0][i] != null && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                highlightWinningLine(0, i, 1, i, 2, i);
                return true;
            }
        }
        if (board[0][0] != null && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            highlightWinningLine(0, 0, 1, 1, 2, 2);
            return true;
        }
        if (board[0][2] != null && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            highlightWinningLine(0, 2, 1, 1, 2, 0);
            return true;
        }
        return false;
    }

    private void highlightWinningLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        JButton[] winningButtons = { buttons[row1][col1], buttons[row2][col2], buttons[row3][col3] };
        Timer timer = new Timer(100, new ActionListener() {
            boolean glow = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton button : winningButtons) {
                    button.setBackground(glow ? new Color(255, 215, 0) : new Color(255, 255, 102));
                }
                glow = !glow;
            }
        });
        timer.start();
    }

    private boolean isDraw() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
                buttons[row][col].setBackground(new Color(80, 80, 80));
                board[row][col] = null;
            }
        }
        currentPlayer = "X";
    }

    private void disableBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                buttons[row][col].setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnimatedTicTacToe());
    }
}
