package testeBomberman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private Tabuleiro tabuleiro;
    private Jogador jogador;
    private Timer timer;
    private boolean perdeu;
    public Main() {
        tabuleiro = new Tabuleiro(13, 13); // Tabuleiro 13x13
        jogador = new Jogador(1, 1, 1); // Posição inicial (1, 1)
        tabuleiro.adicionarJogador(jogador);
        timer = new Timer(100, this); // Atualiza a cada 100ms
        addKeyListener(new AdaptadorTeclado());
        setFocusable(true);
        setPreferredSize(new Dimension(1280, 720)); // Ajuste o tamanho para o novo tabuleiro
        perdeu = false;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!perdeu) {
            tabuleiro.atualizar();
            verificarPerda();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        tabuleiro.renderizar(g);
        if (perdeu) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Você Perdeu!", 150, 300);
        }
    }

    private class AdaptadorTeclado extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!perdeu) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) {
                    jogador.moverParaEsquerda(tabuleiro);
                } else if (key == KeyEvent.VK_RIGHT) {
                    jogador.moverParaDireita(tabuleiro);
                } else if (key == KeyEvent.VK_UP) {
                    jogador.moverParaCima(tabuleiro);
                } else if (key == KeyEvent.VK_DOWN) {
                    jogador.moverParaBaixo(tabuleiro);
                } else if (key == KeyEvent.VK_SPACE) {
                    jogador.colocarBomba(tabuleiro);
                }
            }
        }
    }

    private void verificarPerda() {
        for (Explosao explosao : tabuleiro.getExplosoes()) {
            for (Posicao pos : explosao.getPosicoesAfetadas()) {
                if (jogador.getPosicao().getX() == pos.getX() && jogador.getPosicao().getY() == pos.getY()) {
                    perdeu = true;
                }
            }
        }
        for (Inimigo inimigo : tabuleiro.getInimigos()) {
            if (jogador.getPosicao().getX() == inimigo.getPosicao().getX()
                    && jogador.getPosicao().getY() == inimigo.getPosicao().getY()) {
                perdeu = true;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bomberman");
        Main main = new Main();
        frame.add(main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        main.requestFocusInWindow(); // Garante que o painel receba foco para os eventos de teclado
    }
}
