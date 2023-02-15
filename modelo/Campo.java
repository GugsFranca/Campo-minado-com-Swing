package modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Campo {

    private boolean minado = false;
    private boolean aberto = false;
    private boolean marcado = false;

    private final int linha;
    private final int coluna;

    private List<Campo> vizinhos = new ArrayList<>();
    private Set<CampoObservador> observadores = new HashSet<>();

    Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public void registrarObservador(CampoObservador observador){
        observadores.add(observador);
    }

    private void notificarObservadores(CampoEvento evento){
        observadores.forEach(obs-> obs.eventoOcorreu(this, evento));
    }

    boolean adicionarVizinho(Campo vizinho) {
        // Mostra se as linhas são diferentes da aberta e na diagonal
        boolean linhaDiferente = this.linha != vizinho.linha;
        boolean colunaDiferente = this.coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;
        // se ele tiver em uma linha e coluna diferente ele ta na diagonal ou não é
        // vizinho

        // Subtrai as linhas anteriores com a aberta para entender se são ou não
        // vizinhas
        // delta == diferença
        int deltaLinha = Math.abs(linha - vizinho.linha);// Math.abs transforma negativa em positivo
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;// if soma == 2 ta na diagonal && if soma == 1 ta na horizontal

        if (deltaGeral == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else if (deltaGeral == 2 && diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else {
            return false;
        }
    }

    public void alternarMarcacao() {
        if (!aberto) {
            marcado = !marcado;

            if(marcado){
                notificarObservadores(CampoEvento.MARCAR);
            }
            else{
                notificarObservadores(CampoEvento.DESMARCAR);
            }
        }
    }

    public boolean abrir() {
        if (!aberto && !marcado) {
            if (minado) {
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }
            setAberto(true);
            if (vizinhacaSegura()) {
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        }else {
            return false;
        }
    }

    public boolean vizinhacaSegura() {
        return vizinhos.stream().noneMatch(v -> v.minado);
    }

    void minar() {
        minado = true;
    }

    public boolean isMinado(){
        return minado;
    }

    public boolean isMarcado() {//o get para boolean se marca com is
        return marcado;
    }
    
    void setAberto(boolean aberto) {
        this.aberto = aberto;
        if(aberto){
            notificarObservadores(CampoEvento.ABRIR);
        }
    }

    public boolean isAberto() {
        return aberto;
    }

    public boolean isFechado() {
        return !isAberto();
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
    boolean objetivoAlcancado(){
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }
    public int minasNaVizinhaca(){
        return (int)vizinhos.stream().filter(v -> v.minado).count();
    }
    void reiniciar(){
        aberto = false;
        minado = false;
        marcado = false;
        notificarObservadores(CampoEvento.REINICIAR);
    }

    public List<Campo> getVizinhos() {
        return vizinhos;
    }
}