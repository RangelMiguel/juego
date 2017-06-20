/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;


import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author miguel
 */
public class ViewGame extends javax.swing.JFrame {
    String[] words = new String[20];
    String[] dwords = new String[20];
    int nPalabra=-1,intentos=0,vidas=3;
    String word="",hword="",dword="";
    /**
     * Creates new form VAhorcado
     */
    public ViewGame() {
        initComponents();
        loadWords();
        centrar();
    }
    
    public void loadWords(){
        try {
            Scanner scw = new Scanner(new File("src/res/misc/diccionario.txt"));
            String[] wd = new String[2];
            for(int i = 0; i<20; i++){
                words[i] = "";
                wd = scw.nextLine().split(":");
                for(int j = 0; j < wd[0].length(); j++){
                    words[i] += wd[0].charAt(j)+" ";
                }
                words[i] = words[i].toUpperCase();
                dwords[i] = wd[1];
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ViewGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sort(String[] w, String[] d){
        String[] sortW = new String[20];
        String[] sortD = new String[20];
        int n;
        for(int i = 0; i<20; i++){
            n = (int)(Math.random()*(19-i));
            sortW[i] = w[n];
            sortD[i] = d[n];
            w = eliminar(w,n);
            d = eliminar(d,n);
        }
        words = sortW;
        dwords = sortD;
    }
    
    public String[] eliminar(String[] a, int index){
        boolean noPaso = true;
        String[] out = new String[a.length-1];
        for(int i = 0; i<a.length; i++){
            if(noPaso){
                if(i==index)
                    noPaso = false;
                else
                    out[i]=a[i];
            }else{
                out[i-1]=a[i];
            }
        }
        return out;
    }
    
    public void setWords(int index){
        word = words[index];
        dword = dwords[index];
        hword = "";
        for(int i = 0; i < word.length()/2; i++)
            hword += "_ ";
    }
    
    public void comenzarJuego(){
        nPalabra = 0;
        intentos = 0;
        sort(words,dwords);
        setWords(nPalabra);
        imgScreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgState"+intentos+".jpg")));
        txtGuess.setText(hword+"\n"+dword);
        txtAdivinar.setText("");
        lblMistakes.setText("");
        lblIntentos.setText("Errores:");
        txtAdivinar.requestFocus();
        switch(vidas){
            case 0:
                imgVidas0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgNoLife.jpg")));
                imgVidas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgNoLife.jpg")));
                imgVidas2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgNoLife.jpg")));break;
            case 1:
                imgVidas0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgLife.jpg")));
                imgVidas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgNoLife.jpg")));
                imgVidas2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgNoLife.jpg")));break;
            case 2:
                imgVidas0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgLife.jpg")));
                imgVidas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgLife.jpg")));
                imgVidas2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgNoLife.jpg")));break;
            case 3:
                imgVidas0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgLife.jpg")));
                imgVidas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgLife.jpg")));
                imgVidas2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgLife.jpg")));break;
        }
        
        
        
    }

    public void pruebaCaracter(String c){
        StringBuilder sbw = new StringBuilder(word);
        StringBuilder sbh = new StringBuilder(hword);
        int position = word.indexOf(c);
        if(position>=0){
            while(position>=0){
                sbh.replace(position, position+1, c);
                sbw.replace(position, position+1, "_");
                word = sbw.toString();
                hword = sbh.toString();
                position = word.indexOf(c);
            }
        }else{
            if(!(hword.contains(c)||lblMistakes.getText().contains(c))){
                lblMistakes.setText(lblMistakes.getText()+" "+c);
                intentos++;
            }
        }
        actualizar();
        seGano();
        sePerdio();
    }
    
    public void actualizar(){
        imgScreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgState"+intentos+".jpg")));
        
        txtGuess.setText(hword+"\n"+dword);
    }
    
    public void seGano(){
        if(words[nPalabra].equals(hword)){
            JOptionPane.showMessageDialog(null, "Advinaste la palabra " + (nPalabra+1));
            if(nPalabra==19){
                juegoTerminado();
            }
            nPalabra++;
            intentos = 0;
            lblMistakes.setText("");
            setWords(nPalabra);
            actualizar();
        }
    }
    
    public void sePerdio(){
        if(intentos==6){
            vidas--;
            JOptionPane.showMessageDialog(null, "Se perdio una vida, el juego se reiniciara");
            reiniciar();
        }
        if(vidas==0){
            JOptionPane.showMessageDialog(null, "Perdiste en la palabra numero " + nPalabra+1);
            juegoTerminado();
        }
    }
    
    public void juegoTerminado(){
        txtAdivinar.setEnabled(false);
        txtGuess.setEnabled(false);
        imgScreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgGameOver.jpg")));
    }
    
    public void reiniciar(){
        nPalabra = -1;
        comenzarJuego();
    }
    
    public void centrar(){
        Dimension screen = this.getToolkit().getScreenSize();
        Dimension window = this.getSize();
        
        double posX = (screen.width - window.width)/2;
        double posY = (screen.height - window.height)/2;
        
        setLocation((int) posX, (int) posY);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        imgVidas0 = new javax.swing.JLabel();
        imgVidas1 = new javax.swing.JLabel();
        imgVidas2 = new javax.swing.JLabel();
        btnRestart = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        imgScreen = new javax.swing.JLabel();
        lblMistakes = new javax.swing.JLabel();
        lblIntentos = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtGuess = new javax.swing.JTextArea();
        txtAdivinar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(34, 1, 1));

        jPanel1.setBackground(new java.awt.Color(1, 1, 1));

        imgVidas0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgNoLife.jpg"))); // NOI18N
        jPanel1.add(imgVidas0);

        imgVidas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgNoLife.jpg"))); // NOI18N
        jPanel1.add(imgVidas1);

        imgVidas2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgNoLife.jpg"))); // NOI18N
        jPanel1.add(imgVidas2);

        btnRestart.setText("Restart game");
        btnRestart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestartActionPerformed(evt);
            }
        });
        jPanel1.add(btnRestart);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setBackground(new java.awt.Color(140, 1, 17));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        imgScreen.setBackground(new java.awt.Color(46, 46, 46));
        imgScreen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imgScreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/img/imgStart.jpg"))); // NOI18N
        imgScreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imgScreenMouseClicked(evt);
            }
        });
        jPanel2.add(imgScreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, -1));

        lblMistakes.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        lblMistakes.setForeground(new java.awt.Color(254, 254, 254));
        lblMistakes.setText("Miguel Angel Rangel Martinez -GSI1333");
        jPanel2.add(lblMistakes, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, -1, -1));

        lblIntentos.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        lblIntentos.setForeground(new java.awt.Color(254, 254, 254));
        jPanel2.add(lblIntentos, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, -1, -1));

        txtGuess.setColumns(20);
        txtGuess.setRows(5);
        jScrollPane1.setViewportView(txtGuess);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 300, 50));

        txtAdivinar.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtAdivinarInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        txtAdivinar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdivinarKeyTyped(evt);
            }
        });
        jPanel2.add(txtAdivinar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 300, -1));

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void imgScreenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgScreenMouseClicked
        if(nPalabra==-1){
            comenzarJuego();
            return;
        }
    }//GEN-LAST:event_imgScreenMouseClicked

    private void txtAdivinarInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtAdivinarInputMethodTextChanged

    }//GEN-LAST:event_txtAdivinarInputMethodTextChanged

    private void txtAdivinarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdivinarKeyTyped
        if(nPalabra!=-1){
            try{
                Character c = evt.getKeyChar();
                if(Character.isAlphabetic(c)){
                    pruebaCaracter(c.toString().toUpperCase());                    
                }
            }catch(Exception e){}finally{txtAdivinar.setText("");}    
        }     
    }//GEN-LAST:event_txtAdivinarKeyTyped

    private void btnRestartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestartActionPerformed
        nPalabra = -1;
        vidas = 3;
        txtAdivinar.setEnabled(true);
        txtGuess.setEnabled(true);
        comenzarJuego();
    }//GEN-LAST:event_btnRestartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewGame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRestart;
    private javax.swing.JLabel imgScreen;
    private javax.swing.JLabel imgVidas0;
    private javax.swing.JLabel imgVidas1;
    private javax.swing.JLabel imgVidas2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblIntentos;
    private javax.swing.JLabel lblMistakes;
    private javax.swing.JTextField txtAdivinar;
    private javax.swing.JTextArea txtGuess;
    // End of variables declaration//GEN-END:variables
}
