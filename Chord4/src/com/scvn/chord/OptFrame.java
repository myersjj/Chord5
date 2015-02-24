/* 
 * The CHORD utility for guitar players is distributed under the terms of the
 * general GNU license. 
 * 
 * Copyright (C) 2001  Martin Leclerc & Mario Dorion
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.scvn.chord;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Locale;
import java.util.Observer;
import java.util.Observable;


// TODO: Make options/Settings persitent in a chord4.properties file
public class OptFrame extends JFrame implements ActionListener, ChordConstants, Observer {
    Chord4 c4;
    
    JPanel optPanel;
    JPanel fontPanel;
    JPanel miscPanel;
    JPanel copyrightPanel;
    JPanel okPanel;
    JPanel sub1, sub2, sub3, sub4, sub5, sub6;
    JComboBox iTextFont, iTextSize;
    JComboBox iChordFont, iChordSize;
    JComboBox iMonoFont, iMonoSize;
    JComboBox iTranspose;
    JComboBox iPagination;
    JComboBox iGridSize;
    JComboBox iLocale;
    JCheckBox iNoEasyGrids, iNoGrids, iLyricsOnly, iNumberLogical, iLeftFootEven;
    JCheckBox iDoToc, iDoSubToc, iShowSplash;
    JTextField iPageLabel;
    JTextArea iCopyright;

    JButton ok, cancel;
    
    String fontList[];
    int i;
    Settings settings;

    public Object defaultSignatureObject = null;
    public OptFrame(Chord4 c4) {
        super("Options");
        this.c4 = c4;
        settings = c4.getSettings();
        settings.addObserver(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2,5,2,5);
        fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String fontSizes[] = new String[] {"5", "8", "9", "10", "11", "12", "13", "14", "16", "18", "20", "24"};

        
        miscPanel = new JPanel();
        miscPanel.setLayout(new GridBagLayout());
        
        copyrightPanel = new JPanel();
        copyrightPanel.setLayout(new GridBagLayout());

        // fonts
        fontPanel = new JPanel();
        fontPanel.setLayout(new GridBagLayout());
        fontPanel.setBorder(new TitledBorder(new LineBorder(Color.blue), "Fonts"));
        
        // text font
        gbc.gridwidth = 1;
        fontPanel.add(new JLabel("Text", JLabel.RIGHT), gbc);
        iTextFont = new JComboBox(fontList);
        //System.out.println(settings.toString());
        fontPanel.add(iTextFont, gbc);
        
        iTextSize = new JComboBox(fontSizes);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        fontPanel.add(iTextSize, gbc);
        
        // chord font
        gbc.gridwidth = 1;
        fontPanel.add(new JLabel("Chord", JLabel.RIGHT), gbc);
        iChordFont = new JComboBox(fontList);
        fontPanel.add(iChordFont, gbc);
        
        iChordSize = new JComboBox(fontSizes);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        fontPanel.add(iChordSize, gbc);
        
        // mono font
        gbc.gridwidth = 1;
        fontPanel.add(new JLabel("Tab", JLabel.RIGHT), gbc);
        
        iMonoFont = new JComboBox(fontList);
        fontPanel.add(iMonoFont, gbc);
        
        iMonoSize = new JComboBox(fontSizes);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        fontPanel.add(iMonoSize, gbc);
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        miscPanel.add(fontPanel, gbc);
        
        // options
        optPanel = new JPanel();
        optPanel.setLayout(new GridLayout(0, 2));
        optPanel.setBorder(new TitledBorder(new LineBorder(Color.blue), "Options"));
        //		miscPanel.add(new JLabel("No grids for easy chords", JLabel.RIGHT));
        optPanel.add(iNoEasyGrids = new JCheckBox("No grids for easy chords"));
        iShowSplash = new JCheckBox("Show splash");
        optPanel.add(iShowSplash);

        iDoToc = new JCheckBox("Generate table of contents");
        iDoToc.addActionListener(this);
        optPanel.add(iDoToc);

        iDoSubToc = new JCheckBox("Generate table of subtitles");
        iDoSubToc.setEnabled(false);
        optPanel.add(iDoSubToc);

        iLeftFootEven = new JCheckBox("Even page number on left");
        optPanel.add(iLeftFootEven);
        
        iLyricsOnly = new JCheckBox("Only print lyrics");
        optPanel.add(iLyricsOnly);
        
        iNumberLogical = new JCheckBox("Number logical pages, not physical");
        optPanel.add(iNumberLogical);
        
        iNoGrids = new JCheckBox("No grids");
        optPanel.add(iNoGrids);
        optPanel.add(new JLabel("Starting page number : ", JLabel.RIGHT));
        
        sub1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iPageLabel = new JTextField(Integer.toString(settings.getFirstPageLabel()), 5);
        sub1.add(iPageLabel);
        optPanel.add(sub1);
        optPanel.add(new JLabel("Set chord grid size : ", JLabel.RIGHT));
        
        sub2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iGridSize = new JComboBox();
        for (i = 20; i < 50; i += 5) {
            iGridSize.addItem(Integer.toString(i));
        }
        sub2.add(iGridSize);
        optPanel.add(sub2);
        
        optPanel.add(new JLabel("Transpose by this many halftones : ", JLabel.RIGHT));
        
        sub3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iTranspose = new JComboBox();
        for (i = -11; i < 11; i++) {
            iTranspose.addItem(Integer.toString(i));
        }
        sub3.add(iTranspose);
        optPanel.add(sub3);
/*
        optPanel.add(new JLabel("Number of logical pages per physical page : ", JLabel.RIGHT));
        
        sub4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iPagination = new JComboBox();
        iPagination.addItem("1");
        iPagination.addItem("2");
        iPagination.addItem("4");
        iPagination.setSelectedItem(Integer.toString(settings.getPagination()));
        sub4.add(iPagination);
        optPanel.add(sub4);
 */
// Locale selection   
        optPanel.add(new JLabel("Locale : ", JLabel.RIGHT));
        
        sub5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Locale locales[] = Locale.getAvailableLocales();
        
        iLocale = new JComboBox(locales);
        Logger.debug("locale=["+Locale.getDefault()+"]");
        iLocale.setSelectedItem(Locale.getDefault());
        sub5.add(iLocale);
        optPanel.add(sub5);

       // Copyright text
        copyrightPanel.add(new JLabel("Copyright:", JLabel.LEFT));
        sub6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iCopyright = new JTextArea("# Copyright text here", 5, 40);
        sub6.add(iCopyright);
        copyrightPanel.add(sub6);
 // end on internal panels
        miscPanel.add(optPanel, gbc);
        miscPanel.add(copyrightPanel, gbc);
        okPanel = new JPanel();
        ok = new JButton("Ok");
        okPanel.add(ok);
        cancel = new JButton("Cancel");
        okPanel.add(cancel);
        getContentPane().add(miscPanel, BorderLayout.CENTER);
        getContentPane().add(okPanel, BorderLayout.SOUTH);
        
        ok.addActionListener(this);
        cancel.addActionListener(this);
        pack();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        // locate default sig file
        defaultSignatureObject = Params.getDefaultSignatureObject();
        update (null, null); // force init of gui values from Settings
    }
    

    public void update(Observable o, Object arg) {
        //Logger.debug("OptFrame.update: key =[" + arg+"]");
    	iShowSplash.setSelected(settings.getShowSplash());
        iTextFont.setSelectedItem(settings.getTextFontName());
        iTextSize.setSelectedItem(Integer.toString(settings.getTextSize()));
        iChordFont.setSelectedItem(settings.getChordFontName());
        iChordSize.setSelectedItem(Integer.toString(settings.getChordSize()));
        iMonoFont.setSelectedItem(settings.getMonoFontName());
        iMonoSize.setSelectedItem(Integer.toString(settings.getMonoSize()));
        iNoEasyGrids.setSelected(settings.getNoEasyGrids());
        iDoToc.setSelected(settings.getDoToc());
        iDoSubToc.setEnabled(settings.getDoToc());
        iDoSubToc.setSelected(settings.getDoSubToc());
        iLyricsOnly.setSelected(settings.getLyricsOnly());
        iLeftFootEven.setSelected(settings.getIsLeftFootEven());
        iNumberLogical.setSelected(settings.getNumberLogical());
        iGridSize.setSelectedItem(Integer.toString(settings.getGridSize()));
        iTranspose.setSelectedItem(Integer.toString(settings.getTransposition()));
    }


    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == iDoToc) {
            iDoSubToc.setEnabled(iDoToc.isSelected());
            if (! iDoToc.isSelected()) {
                iDoSubToc.setSelected(false);
            }
            return;
        }
        if (e.getSource() == ok) {
            setVisible(false);
            // populate the RT option set
            Settings settings = c4.getSettings();
            settings.set(RT, CHORDFONTNAME, (String)iChordFont.getSelectedItem(), false);
            settings.set(RT, CHORDSIZE, (String)iChordSize.getSelectedItem(), false);

            settings.set(RT, MONOFONTNAME, (String)iMonoFont.getSelectedItem(), false);
            settings.set(RT, MONOSIZE, (String)iMonoSize.getSelectedItem(), false);

            settings.set(RT, TEXTFONTNAME, (String)iTextFont.getSelectedItem(), false);
            settings.set(RT, TEXTSIZE, (String)iTextSize.getSelectedItem(), false);

            settings.set(RT, GRIDSIZE,    (String)iGridSize.getSelectedItem(), false);
            settings.set(RT, NOGRIDS,     String.valueOf(iNoGrids.isSelected()), false);
            settings.set(RT, NOEASYGRIDS, String.valueOf(iNoEasyGrids.isSelected()), false);
            settings.set(RT, SHOWSPLASH,  String.valueOf(iShowSplash.isSelected()), false);
            
            settings.set(RT, TRANSPOSITION,   (String)iTranspose.getSelectedItem(), false);
//            settings.set(RT, PAGINATION,      (String)iPagination.getSelectedItem(), false);
//            setting.set(RT, SIGNATUREOBJECT, (String)iSig.getSelectedItem(), false);
            settings.set(RT, DOTOC,           String.valueOf(iDoToc.isSelected()), false);
            settings.set(RT, DOSUBTOC,        String.valueOf(iDoSubToc.isSelected()), false);
            settings.set(RT, ISLEFTFOOTEVEN,  String.valueOf(iLeftFootEven.isSelected()), false);
            settings.set(RT, ISLYRICSONLY,    String.valueOf(iLyricsOnly.isSelected()), false);
            settings.set(RT, NUMBERLOGICAL,   String.valueOf(iNumberLogical.isSelected()), false);
//            String locName =  (String)iLocale.getSelectedItem();
//            Locale locale = Locale.getDefault();
            Locale locale = (Locale)iLocale.getSelectedItem();
            Logger.debug("Locale set to " + locale);
            Locale.setDefault(locale);
            
            //Logger.debug(settings.toString());
            return;
        }
        if (e.getSource() == cancel) {
            setVisible(false);
            return;
        }
    }

    public static void main(String[] args) {
        OptFrame of = new OptFrame(null);
        of.setVisible(true);
    }
    public void printDefines() {
//        isDefinesDump = true;
    }
    public void printGrids() {
        iDoToc.setEnabled(false);
        iDoSubToc.setEnabled(false);
        iNoGrids.setEnabled(false);
        iLyricsOnly.setEnabled(false);
        iTranspose.setEnabled(false);
//        isGridsDump = true;
//        setVisible(true);
    }
    
//    public void getOptions() {
//        iDoToc.setEnabled(true);
//        iDoSubToc.setEnabled(true);
//        iNoGrids.setEnabled(true);
//        iLyricsOnly.setEnabled(true);
//        iTranspose.setEnabled(true);
//        setVisible(true);
//    }
    
    /* setters sections */
}
