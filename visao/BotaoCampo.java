package visao;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import java.awt.*;
import java.awt.event.*;

import modelo.Campo;
import modelo.CampoEvento;
import modelo.CampoObservador;

public class BotaoCampo extends JButton
implements CampoObservador, MouseListener{
    private final Color BG_PADRAO = new Color(184,184,184);
    private final Color BG_MARCAR = new Color(8,179,247);
    private final Color BG_EXPLODIR = new Color(189,66,68);
    private final Color TEXTO_VERDE = new Color(0,100,0);

    private Campo campo;

    public BotaoCampo(Campo campo){
        this.campo = campo;
        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createBevelBorder(0));
        addMouseListener(this);
        campo.registrarObservador(this);
    
    }
    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        switch (evento) {
            case ABRIR:
                aplicarEstiloAbrir();
                break;
                case MARCAR:
                aplicarEstilMarcar();
                break;
                case EXPLODIR:
                aplicarEstiloExplodir();
                break;
        
            default:
            aplicarEstilPadrao();
                break;
        }
        SwingUtilities.invokeLater(()->{
            repaint();
            validate();
        });
    }
    private void aplicarEstilPadrao() {
        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
    }
    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
        setForeground(Color.WHITE);
        setText("X");
    }
    private void aplicarEstilMarcar() {
        setBackground(BG_MARCAR);
        setForeground(Color.BLACK);
        setText("M");        
    }
    private void aplicarEstiloAbrir() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if (campo.isMinado()) {
            setBackground(BG_EXPLODIR);
            return;
        }
        setBackground(BG_PADRAO);
        
        switch (campo.minasNaVizinhaca()) {
            case 1:
                setForeground(TEXTO_VERDE);
                break;
            case 2:
                 setForeground(Color.BLUE);
                 break;
            case 3:
                 setForeground(Color.YELLOW);
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;                
            default:
                setForeground(Color.PINK);
        }
        String valor = !campo.vizinhacaSegura() ? campo.minasNaVizinhaca() + "" : "";
        setText(valor);
    }
    ///Interface dos eventos do Mouse
    @Override
    public  void mousePressed(MouseEvent e) {
        if(e.getButton() == 1){
            campo.abrir();
        }else{
            campo.alternarMarcacao();
        } 
    }
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
   

    
}
