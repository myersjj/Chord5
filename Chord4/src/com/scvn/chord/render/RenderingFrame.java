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
 
package com.scvn.chord.render;

import com.scvn.chord.grop.*;
import com.scvn.chord.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.print.*;



/**
 * Insert the type's description here.
 * Creation date: (99-07-20 17:05:49)
 * @author
 */
public class RenderingFrame extends JFrame implements ActionListener, KeyListener {
    Chord4 c4;
    C4Renderer rend;
    JLabel label;
    JPanel imagePanel;
    CardLayout cardLayout;
    JScrollPane scrollPane;
    JMenuBar mb;
    JMenu ViewMenu;
    JMenuItem miNextPage, miPreviousPage, miGotoPage, miPrint, miPageSetup, miRefresh, miQuit;
    BufferedImage bi;
    int cpage;
    RenderingPane rPane;
    JToolBar toolBar;
    Action actionNextPage, actionPrevPage, actionRefreshPage;
    /**
     * Insert the method's description here.
     * Creation date: (99-07-20 17:08:15)
     */
    public RenderingFrame(Chord4 c4, C4Renderer rend) {
        super("Chord4 Renderer");
        this.c4 = c4;
        this.rend = rend;

        toolBar = new JToolBar();
        ViewMenu = new JMenu("View");
        ViewMenu.add(miNextPage = new JMenuItem("Next Page"));
        actionNextPage = new NextPageAction("Next Page", "graphics/Arrow (Down)_24x24.png");
        toolBar.add(actionNextPage).setToolTipText("Next Page");
        miNextPage.addActionListener(this);
        ViewMenu.add(miPreviousPage = new JMenuItem("Previous Page"));
        actionPrevPage = new PrevPageAction("Previous Page", "graphics/Arrow (Up)_24x24.png");
        toolBar.add(actionPrevPage).setToolTipText("Previous Page");
        miPreviousPage.addActionListener(this);
        ViewMenu.add(miGotoPage = new JMenuItem("Go to page..."));
        miGotoPage.addActionListener(this);
        
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(miPrint = new JMenuItem("Print"));
        miPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
        miPrint.addActionListener(this);
        fileMenu.add(miPageSetup = new JMenuItem("Page Setup"));
        miPageSetup.addActionListener(this);
        fileMenu.add(miRefresh = new JMenuItem("Refresh"));
        actionRefreshPage = new RefreshPageAction("Refresh", "graphics/Arrow (Refresh)_24x24.png");
        toolBar.add(actionRefreshPage).setToolTipText("Refresh");
        miRefresh.addActionListener(this);
        miPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
        miPrint.addActionListener(this);
        fileMenu.add(miQuit = new JMenuItem("Close"));
        miQuit.addActionListener(this);
        miQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK));

        addWindowListener(c4.getViewMenu());

        mb = new JMenuBar();
        mb.add(fileMenu);
        mb.add(ViewMenu);
        setJMenuBar(mb);
        rPane = new RenderingPane(rend);
        scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //  imageIcon = new ImageIcon();
        //  label =new JLabel(imageIcon);
        getContentPane().add(scrollPane);
        getContentPane().add(toolBar, BorderLayout.NORTH);
        scrollPane.getViewport().add(rPane);
        
        //addWindowListener(this);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //addComponentListener(this);
        addKeyListener(this);
        setBackground(Color.white);
        setSize(650, (11+1)*72);
    }
    class NextPageAction extends AbstractAction {
        public NextPageAction(String name, String icon) {
            super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
        }
        public void actionPerformed(ActionEvent event) {
        	setCurrentPage(Math.min(cpage + 1, rend.getPageSet().size()-1));
            return;
         }
     }
    class PrevPageAction extends AbstractAction {
        public PrevPageAction(String name, String icon) {
            super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
        }
        public void actionPerformed(ActionEvent event) {
        	setCurrentPage(Math.max(0, cpage - 1));
            return;
         }
     }
    class RefreshPageAction extends AbstractAction {
        public RefreshPageAction(String name, String icon) {
            super(name, new ImageIcon(ClassLoader.getSystemResource(icon)));
        }
        public void actionPerformed(ActionEvent event) {
        	rend.invalidateRendering();
        	setCurrentPage(0);
            return;
         }
     }
    /* --------------------------------------------------------------------------------*/
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            doMenu((JMenuItem) e.getSource());
        }
    }
    /* --------------------------------------------------------------------------------*/
    public void doMenu(JMenuItem item) {
        if (item == miNextPage) {
            setCurrentPage(Math.min(cpage + 1, rend.getPageSet().size()-1));
            return;
        }
        if (item == miPreviousPage) {
            setCurrentPage(Math.max(0, cpage - 1));
            return;
        }
        if (item == miGotoPage) {
            int reqPage;
            String input = JOptionPane.showInputDialog("Enter Page Number [1-" + (rend.getPageSet().size()) +"]");
            try {
                reqPage = Integer.parseInt(input);
                if (reqPage > 0 && reqPage <= rend.getPageSet().size()) {
                    setCurrentPage(reqPage - 1);
                }
                else
                {
                  JOptionPane.showMessageDialog(null, "Value out out range:"+input, "Alert", JOptionPane.ERROR_MESSAGE); 
                }
            }
            catch (NumberFormatException nfe)
            {
                JOptionPane.showMessageDialog(null, "Bad Numerical value: "+input, "Alert", JOptionPane.ERROR_MESSAGE); 
            }
            return;
        }
        if (item == miPrint) { // printer setup.
            //		PrinterJob pj = PrinterJob.getPrinterJob();
            PrinterJob pj = rend.getPrintJob();
            
            pj.setJobName("Chord Print");
            pj.setPageable(rend);
            if (!pj.printDialog()) {
                return;
            }
            pj.setPrintable(rend, rend.getUserPageFormat());
            try {
                pj.print();
            }
            catch (PrinterException pe) {
                pe.printStackTrace();
            }
            return;
        }
        if (item == miPageSetup) {
            PageFormat pf = rend.getPrintJob().pageDialog(rend.getUserPageFormat());
            rend.setUserPageFormat(pf);
            setCurrentPage(0);
            return;
        }
        if (item == miRefresh) {
        	rend.invalidateRendering();
        	setCurrentPage(0);
        	return;
        }
        if (item == miQuit) {
            setVisible(false);
            return;
        }
    }
    /* --------------------------------------------------------------------------------*/
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_PAGE_UP :
                if (cpage == 0) {
                    Toolkit.getDefaultToolkit().beep();
                }
                else {
                    setCurrentPage(cpage - 1);
                }
                break;
            case KeyEvent.VK_PAGE_DOWN :
                if (cpage == rend.getPageSet().size() - 1) {
                    Toolkit.getDefaultToolkit().beep();
                }
                else {
                    setCurrentPage(cpage + 1);
                }
                break;
        }
    }
    /* --------------------------------------------------------------------------------*/
    public void keyReleased(KeyEvent e) {
    }
    /* --------------------------------------------------------------------------------*/
    public void keyTyped(KeyEvent e) {
    }
    /* --------------------------------------------------------------------------------*/
    public void setCurrentPage(int p) {
        rPane.setCurrentPage(p); // side effect: rendering gets done if necessary
        PageSet ps =  rend.getPageSet();
        if (ps.size() == 0) {
            setTitle("Chord Renderer: Nothing to render");
        }
        else {
            cpage = p;
            setTitle("Chord Renderer (page " + (p + 1) + " of " + ps.size() + ")");
            miNextPage.setEnabled(!(cpage == ps.size() - 1));
            miPreviousPage.setEnabled(!(cpage == 0));
        }
    }
    
    public RenderingPane getRenderingPane() {
        return rPane;
    }
    
}
