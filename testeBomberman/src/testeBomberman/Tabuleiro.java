package testeBomberman;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Tabuleiro {
    private int largura;
    private int altura;
    private List<Bomba> bombas;
    private List<Explosao> explosoes;
    private List<Jogador> jogadores;
    private boolean[][] paredes;
    private boolean[][] caixas;
    private Random random;

    public Tabuleiro(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        this.bombas = new ArrayList<>();
        this.explosoes = new ArrayList<>();
        this.jogadores = new ArrayList<>();
        this.paredes = new boolean[largura][altura];
        this.caixas = new boolean[largura][altura];
        this.random = new Random();

        preencherTabuleiro();
    }

    private void preencherTabuleiro() {
        // Preencher o tabuleiro com paredes nas posições [ímpar][ímpar] e caixas aleatórias
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                // Preencher apenas posições [ímpar][ímpar] com paredes
                if (x % 2 == 1 && y % 2 == 1) {
                    paredes[x][y] = true;
                } else if (random.nextDouble() < 0.2) { // 20% chance de ser uma caixa
                    caixas[x][y] = true;
                }
            }
        }

        // Garantir que o jogador tenha um espaço para começar
        // Limpar as posições críticas ao redor do jogador
        limparPosicoesCriticas();

        // Certificar-se de que a posição [0][0] está livre de paredes e caixas
        paredes[0][0] = false;
        caixas[0][0] = false;
    }

    private void limparPosicoesCriticas() {
        // Aqui você pode definir qualquer lógica para garantir que as áreas ao redor do jogador estejam livres
        // Por exemplo, garantir que as posições ao redor do jogador estejam livres para movimento
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = 1 + dx;
                int y = 1 + dy;
                if (x >= 0 && x < largura && y >= 0 && y < altura) {
                    paredes[x][y] = false;
                    caixas[x][y] = false;
                }
            }
        }
    }

    public void adicionarJogador(Jogador jogador) {
        jogadores.add(jogador);
    }

    public void colocarBomba(Bomba bomba) {
        bombas.add(bomba);
    }

    public void atualizar() {
        List<Bomba> bombasExplodidas = new ArrayList<>();
        for (Bomba bomba : bombas) {
            bomba.tick();
            if (bomba.estaExplodida()) {
                Explosao explosao = new Explosao(bomba.getPosicao().getX(), bomba.getPosicao().getY(), 2);
                explosoes.add(explosao);
                destruirCaixas(explosao);
                bombasExplodidas.add(bomba);
            }
        }
        bombas.removeAll(bombasExplodidas);

        // Atualizar explosões e remover as que expiraram
        Iterator<Explosao> iter = explosoes.iterator();
        while (iter.hasNext()) {
            Explosao explosao = iter.next();
            explosao.tick();
            if (explosao.estaExpirada()) {
                iter.remove();
            }
        }
    }

    private void destruirCaixas(Explosao explosao) {
        for (Posicao pos : explosao.getPosicoesAfetadas()) {
            if (pos.getX() >= 0 && pos.getX() < largura && pos.getY() >= 0 && pos.getY() < altura) {
                if (caixas[pos.getX()][pos.getY()]) {
                    caixas[pos.getX()][pos.getY()] = false;
                }
            }
        }
    }

    public List<Bomba> getBombas() {
        return bombas;
    }

    public List<Explosao> getExplosoes() {
        return explosoes;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public boolean isPosicaoValida(int x, int y) {
        // Verificar se está fora dos limites do tabuleiro
        if (x < 0 || y < 0 || x >= largura || y >= altura) {
            return false;
        }
        // Verificar colisão com paredes
        if (paredes[x][y]) {
            return false;
        }
        // Verificar colisão com caixas
        if (caixas[x][y]) {
            return false;
        }
        // Verificar colisão com bombas
        for (Bomba bomba : bombas) {
            if (bomba.getPosicao().getX() == x && bomba.getPosicao().getY() == y) {
                return false;
            }
        }
        return true;
    }

    public void renderizar(Graphics g) {
        // Desenha o tabuleiro
        for (int i = 0; i < largura; i++) {
            for (int j = 0; j < altura; j++) {
                if (paredes[i][j]) {
                    g.setColor(Color.GRAY);
                    g.fillRect(i * 40, j * 40, 40, 40);
                } else if (caixas[i][j]) {
                    g.setColor(Color.ORANGE);
                    g.fillRect(i * 40, j * 40, 40, 40);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(i * 40, j * 40, 40, 40);
                }
            }
        }

        // Desenha os jogadores
        g.setColor(Color.BLUE);
        for (Jogador jogador : jogadores) {
            g.fillRect(jogador.getPosicao().getX() * 40, jogador.getPosicao().getY() * 40, 40, 40);
        }

        // Desenha as bombas
        g.setColor(Color.BLACK);
        for (Bomba bomba : bombas) {
            g.fillRect(bomba.getPosicao().getX() * 40, bomba.getPosicao().getY() * 40, 40, 40);
        }

        // Desenha as explosões
        g.setColor(Color.RED);
        for (Explosao explosao : explosoes) {
            for (Posicao pos : explosao.getPosicoesAfetadas()) {
                g.fillRect(pos.getX() * 40, pos.getY() * 40, 40, 40);
            }
        }
    }
}
