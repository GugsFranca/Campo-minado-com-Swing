package visao;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.*;

import modelo.Tabuleiro;

public class PainelTabuleiro extends JPanel{

    public PainelTabuleiro(Tabuleiro tabuleiro) {

        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
        tabuleiro.paraCada(c -> add(new BotaoCampo(c)));
        tabuleiro.registrarObservador(e-> {
            SwingUtilities.invokeLater(() -> {
                if (e.isGanhou()) {
                    JOptionPane.showMessageDialog(this, "Ganhou!!!");
                }else{
                    JOptionPane.showMessageDialog(this, "Perdeu!:(");
                }
                tabuleiro.reiniciar();
            });
        });
            
        }
    }

    
    
