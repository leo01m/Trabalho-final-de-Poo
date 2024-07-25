package testeBomberman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Main extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private Tabuleiro tabuleiro;
    private Jogador jogador;
    private Timer timer;
    private double cooldownPlayer;
    private boolean perdeu;
    private boolean venceu;
    private JPanel menuPanel;
    private JPanel gamePanel;
    
    public Main() {
        setLayout(new CardLayout());

        // Inicializa o painel do menu
        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());

        // Carrega a splash screen
        URL splashUrl = getClass().getResource("/Documentos/TitleBomberman.png");
        if (splashUrl != null) {
            JLabel splashLabel = new JLabel(new ImageIcon(splashUrl));
            splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
            menuPanel.add(splashLabel, BorderLayout.CENTER);
        } else {
            JLabel splashLabel = new JLabel("Bomberman");
            splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
            splashLabel.setFont(new Font("Arial", Font.BOLD, 50));
            menuPanel.add(splashLabel, BorderLayout.CENTER);
        }

        JButton jogarButton = new JButton("Jogar");
        jogarButton.setFont(new Font("Arial", Font.BOLD, 20));
        jogarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJogo();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(jogarButton);
        menuPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Inicializa o painel do jogo
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                tabuleiro.renderizar(g);
                if (perdeu) {
                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 50));
                    g.drawString("Você Perdeu!", 150, 300);
                } else if (venceu) {
                    g.setColor(Color.GREEN);
                    g.setFont(new Font("Arial", Font.BOLD, 50));
                    g.drawString("Você Venceu!", 150, 300);
                }
            }
        };
        gamePanel.setFocusable(true);
        gamePanel.setPreferredSize(new Dimension(520, 520)); // Ajuste o tamanho para o novo tabuleiro
        gamePanel.addKeyListener(new AdaptadorTeclado());

        // Adiciona os painéis ao layout principal
        add(menuPanel, "Menu");
        add(gamePanel, "Jogo");

        // Inicializa o estado do jogo
        tabuleiro = new Tabuleiro(13, 13); // Tabuleiro 13x13
        jogador = new Jogador(1, 1, 1); // Posição inicial (1, 1)
        tabuleiro.adicionarJogador(jogador);
    
        timer = new Timer(100, this); // Atualiza a cada 100ms
        perdeu = false;
        venceu = false;
        cooldownPlayer = 0;
        timer.start();

        // Exibe o menu inicial ao iniciar o programa
        mostrarMenu();
    }

    private void mostrarMenu() {
        CardLayout cl = (CardLayout) getLayout();
        cl.show(this, "Menu");
    }

    private void iniciarJogo() {
        CardLayout cl = (CardLayout) getLayout();
        cl.show(this, "Jogo");
        gamePanel.requestFocusInWindow(); // Garante que o painel receba foco para os eventos de teclado
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!perdeu && !venceu) {
            tabuleiro.atualizar();
            destruirInimigos();
            verificarPerda();
            verificarVitoria();
            repaint();
        }
    }

    private class AdaptadorTeclado extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!perdeu && !venceu) {
                if (cooldownPlayer > 0) {
                    cooldownPlayer--;
                } else {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_LEFT) {
                        jogador.moverParaEsquerda(tabuleiro);
                        cooldownPlayer = 0;
                    } else if (key == KeyEvent.VK_RIGHT) {
                        jogador.moverParaDireita(tabuleiro);
                        cooldownPlayer = 0;
                    } else if (key == KeyEvent.VK_UP) {
                        jogador.moverParaCima(tabuleiro);
                        cooldownPlayer = 0;
                    } else if (key == KeyEvent.VK_DOWN) {
                        jogador.moverParaBaixo(tabuleiro);
                        cooldownPlayer = 0;
                    } else if (key == KeyEvent.VK_SPACE) {
                        jogador.colocarBomba(tabuleiro);
                        cooldownPlayer = 0;
                    }
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
                    && jogador.getPosicao().getY() == inimigo.getPosicao().getY() && inimigo.isVivo()) {
                perdeu = true;
            }
        }
    }

    private void verificarVitoria() {
        // Verifica se todos os inimigos foram mortos
        for (Inimigo inimigo : tabuleiro.getInimigos()) {
            if (inimigo.isVivo()) {
                return; // Se algum inimigo ainda está vivo, o jogador não venceu
            }
        }
        venceu = true; // Todos os inimigos foram mortos
    }

    private void destruirInimigos() {
        for (Explosao explosao : tabuleiro.getExplosoes()) {
            for (Inimigo inimigo : tabuleiro.getInimigos()) {
                for (Posicao pos : explosao.getPosicoesAfetadas()) {
                    if (inimigo.getPosicao().getX() == pos.getX() && inimigo.getPosicao().getY() == pos.getY()) {
                        inimigo.morrer();
                        System.out.println("Morreu");
                    }
                }
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
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }
}
