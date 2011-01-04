/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PinyinFrame.java
 *
 * Created on Jul 5, 2010, 4:48:11 PM
 */
package com.apc.pinyin;

import hmm.Flag;
import hmm.HmmResult;
import hmm.Node;
import hmm.ObserveListException;
import hmm.Viterbi;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ray
 */
public class PinyinFrame extends javax.swing.JFrame {

    PinyinToWord ptw = new PinyinToWord();
    PinyinTokenizer tokenizer = new PinyinTokenizer("pinyin.dic");
    WordToPinyin wtp = new WordToPinyin();

    /** Creates new form PinyinFrame */
    public PinyinFrame() {
        initComponents();
        ptw.load("ptw.m");
        wtp.load("wtp.m");
        loadUserDict("userdict.txt");
        ptw.init();
        wtp.init();
        if (2 == Flag.n) {
            ptw.setN(2);
            jRadioButton1.setSelected(true);
            jRadioButton2.setSelected(false);
            jRadioButton2.enableInputMethods(false);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jRadioButton1.setText("bigram");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setSelected(true);
        jRadioButton2.setText("trigram");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("display all");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addContainerGap(144, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        char ch = evt.getKeyChar();

        boolean isPinyin = true;
        isPinyin = !jTextField1.getText().matches(".*[一-龥].*");
        if (0x0A == ch) {
            jTextArea1.setText("");

            if (isPinyin) {
                pinyinToWord();
            } else {
                wordToPinyin();
            }
        } else {
            if (isPinyin && Character.isLetter(ch)) {
                String[] o = tokenizer.tokenize(jTextField1.getText().replaceAll("'", "").trim());
                if (o.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (String s : o) {
                        sb.append(s).append("'");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    int p = jTextField1.getCaretPosition();
                    int l = jTextField1.getText().length();
                    jTextField1.setText(sb.toString());
                    jTextField1.setCaretPosition(p + sb.length() - l);
                }
            }
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        ptw.setN(3);
        jRadioButton1.setSelected(false);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        ptw.setN(2);
        jRadioButton2.setSelected(false);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private String correct(String pinyin, String txt) {
        String ret = null;
        String[] o = tokenizer.tokenize(pinyin);
        List<Node<Character>> res = null;
        try {
            res = ptw.classify(o);
        } catch (ObserveListException ex) {
            System.out.println(ex.getMessage());
        }
        String str = "";
        if (null != res) {
            for (Node<Character> node : res) {
                str += node.getName();
            }

            Distance dis = new Distance();
            int n = dis.LD(str, txt);
            if (n == 1) {
                ret = str;
            }
        }
        return ret;
    }

    private void pinyinToWord() {
        String[] o = tokenizer.tokenize(jTextField1.getText().trim());

        HmmResult ret = null;
        try {
            ret = ptw.viterbi(o);
        } catch (ObserveListException ex) {
            System.out.println(ex.getMessage());
        }
        Map<Double, String> results = new HashMap<Double, String>();
        if (null != ret && ret.states != null) {
            for (int pos = 0; pos < ret.states[o.length - 1].length; pos++) {
                StringBuilder sb = new StringBuilder();
                int[] statePath = Viterbi.getStatePath(ret.states, ret.psai, o.length - 1, o.length, pos);
                for (int state : statePath) {
                    Character name = ptw.getStateBank().get(state).getName();
                    sb.append(name).append(" ");
                }
                sb.append(String.valueOf(ret.delta[o.length - 1][pos]));
                results.put(ret.delta[o.length - 1][pos], sb.toString());
            }
            List<Double> list = new ArrayList<Double>(results.keySet());
            Collections.sort(list);
            Collections.reverse(list);
            int end = jCheckBox1.isSelected()
                    ? ret.states[o.length - 1].length
                    : ret.states[o.length - 1].length >= 7 ? 7 : ret.states[o.length - 1].length;
            int i = 0;
            for (Double d : list) {
                jTextArea1.append(results.get(d));
                jTextArea1.append("\n");
                if (++i >= end) {
                    break;
                }
            }
        }
    }

    private void wordToPinyin() {
        String txt = jTextField1.getText().replaceAll("[\\w\\d\\s\\pP~]+", "");
        char[] o = txt.toCharArray();

        HmmResult ret = null;
        try {
            ret = wtp.viterbi(o);
        } catch (ObserveListException ex) {
            System.out.println(ex.getMessage());
        }
        Map<Double, String> results = new HashMap<Double, String>();
        if (null != ret && ret.states != null) {
            for (int pos = 0; pos < ret.states[o.length - 1].length; pos++) {
                StringBuilder sb = new StringBuilder();
                int[] statePath = Viterbi.getStatePath(ret.states, ret.psai, o.length - 1, o.length, pos);
                for (int state : statePath) {
                    String name = wtp.getStateBank().get(state).getName();
                    sb.append(name).append(" ");
                }
                sb.append(String.valueOf(ret.delta[o.length - 1][pos]));
                results.put(ret.delta[o.length - 1][pos], sb.toString());
            }
            List<Double> list = new ArrayList<Double>(results.keySet());
            Collections.sort(list);
            Collections.reverse(list);

            String pinyin = results.get(list.get(0)).replaceAll("[-\\d\\.]+", "").trim();
            String correct = correct(pinyin, txt);


            if (null != correct) {
                jTextArea1.append(correct);
                jTextArea1.append("\n");
            }

            int end = jCheckBox1.isSelected()
                    ? ret.states[o.length - 1].length
                    : ret.states[o.length - 1].length >= 7 ? 7 : ret.states[o.length - 1].length;
            int i = 0;
            for (Double d : list) {
                jTextArea1.append(results.get(d));
                jTextArea1.append("\n");
                if (++i >= end) {
                    break;
                }
            }
        }
    }

    public void loadUserDict(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                        new FileInputStream(file), "UTF-8"));
                String line = br.readLine();

                while (null != line) {
                    line = line.trim();
                    if (!"".equals(line)) {
                        String[] array = line.split("\\s+");
                        ptw.addUserDict(array[0], array[1]);
                    }
                    line = br.readLine();
                }
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new PinyinFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
