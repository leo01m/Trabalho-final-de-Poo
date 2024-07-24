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
    private List<Inimigo> inimigos;
    private boolean[][] paredes;
    private boolean[][] caixas;
    private boolean[][] bombasColid;
    private Random random;

    public Tabuleiro(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        bombas = new ArrayList<>();
        explosoes = new ArrayList<>();
        jogadores = new ArrayList<>();
        this.inimigos= new ArrayList<>();
        paredes = new boolean[largura][altura];
        caixas = new boolean[largura][altura];
        bombasColid= new boolean[largura][altura];
        random = new Random();
        gerarObstaculos();
        gerarInimigos();
    }
    private void gerarInimigos(){
        inimigos.add(new Inimigo(3, 3, 1));
        inimigos.add(new Inimigo(5, 5, 1));
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }


    public void adicionarJogador(Jogador jogador) {
        jogadores.add(jogador);
    }

    public List<Explosao> getExplosoes() {
        return explosoes;
    }
    public List<Inimigo> getInimigos(){
        return inimigos;
    }

    private void gerarObstaculos() {
        // Gerar paredes nas bordas e em posições fixas
        for (int i = 0; i < largura; i++) {
            for (int j = 0; j < altura; j++) {
                if (i == 0 || i == largura - 1 || j == 0 || j == altura - 1 || (i % 2 == 0 && j % 2 == 0)) {
                    paredes[i][j] = true;
                } else {
                    paredes[i][j] = false;
                }
            }
        }

        // Gerar caixas aleatórias, garantindo que não sobreponham jogadores
        for (int i = 1; i < largura - 1; i++) {
            for (int j = 1; j < altura - 1; j++) {
                if (!paredes[i][j] && random.nextFloat() < 0.2) { // 20% de chance de ter uma caixa
                    caixas[i][j] = true;
                }
            }
        }
    }

    public boolean isPosicaoValida(int x, int y) {
        return x >= 0 && x < largura && y >= 0 && y < altura && !paredes[x][y] && !caixas[x][y] && !bombasColid[x][y];
    }

    public void colocarBomba(Bomba bomba) {
        bombas.add(bomba);
        bombasColid[bomba.getPosicao().getX()][bomba.getPosicao().getY()]=true;
    }

    public void atualizar() {
        List<Bomba> bombasExplodidas = new ArrayList<>();
        for (Bomba bomba : bombas) {
            bomba.tick();
            if (bomba.estaExplodida()) {
                Explosao explosao = new Explosao(bomba.getPosicao().getX(), bomba.getPosicao().getY(), 2, this);
                explosoes.add(explosao);
                destruirCaixas(explosao);
                bombasExplodidas.add(bomba);
                bombasColid[bomba.getPosicao().getX()][bomba.getPosicao().getY()]= false;
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
        for(Inimigo inimigo: inimigos){
            inimigo.mover(this);
        }
    }

    private void destruirCaixas(Explosao explosao) {
        for (Posicao pos : explosao.getPosicoesAfetadas()) {
            int x = pos.getX();
            int y = pos.getY();
            if (caixas[x][y]) {
                caixas[x][y] = false; // Caixa destruída pela explosão
            }
        }
    }

    
    public boolean temParede(int x, int y) {
        return paredes[x][y];
    }

    public boolean temCaixa(int x, int y) {
        return caixas[x][y];
    }

    public void renderizar(Graphics g) {
        // Renderizar paredes
        g.setColor(Color.GRAY);
        for (int i = 0; i < largura; i++) {
            for (int j = 0; j < altura; j++) {
                if (paredes[i][j]) {
                    g.fillRect(i * 40, j * 40, 40, 40);
                }
            }
        }

        // Renderizar caixas
        g.setColor(Color.ORANGE);
        for (int i = 0; i < largura; i++) {
            for (int j = 0; j < altura; j++) {
                if (caixas[i][j]) {
                    g.fillRect(i * 40, j * 40, 40, 40);
                }
            }
        }

        // Renderizar bombas
        g.setColor(Color.BLACK);
        for (Bomba bomba : bombas) {
            Posicao pos = bomba.getPosicao();
            g.fillRect(pos.getX() * 40, pos.getY() * 40, 40, 40);
        }

        // Renderizar explosões
        g.setColor(Color.RED);
        for (Explosao explosao : explosoes) {
            for (Posicao pos : explosao.getPosicoesAfetadas()) {
                g.fillRect(pos.getX() * 40, pos.getY() * 40, 40, 40);
            }
        }

        // Renderizar jogador
        g.setColor(Color.BLUE);
        for (Jogador jogador : jogadores) {
            Posicao pos = jogador.getPosicao();
            g.fillRect(pos.getX() * 40, pos.getY() * 40, 40, 40);
        }

        g.setColor(Color.GREEN);
        for(Inimigo inimigo: inimigos){
            Posicao pos= inimigo.getPosicao();
            if(inimigo.isVivo()){
            g.fillRect(pos.getX() *40, pos.getY() *40, 40,40);
            }
        }
    }
}
