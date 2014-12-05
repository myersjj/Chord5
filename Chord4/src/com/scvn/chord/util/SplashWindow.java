package com.scvn.chord.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SplashWindow extends JWindow {
    public SplashWindow(String filename, Frame f, int waitTime) {
        super(f);
        InputStream is = getClass().getResourceAsStream(filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c;
        try {
            while ( (c = is.read()) >=0) {
                baos.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
        JLabel l = new JLabel(new ImageIcon(baos.toByteArray()));
        getContentPane().add(l, BorderLayout.CENTER);
        pack();
        Dimension screenSize =
        Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();
        setLocation(screenSize.width/2 - (labelSize.width/2),
        screenSize.height/2 - (labelSize.height/2));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                setVisible(false);
                dispose();
            }
        });
        final int pause = waitTime;
        final Runnable closerRunner = new Runnable() {
            public void run() {
                setVisible(false);
                dispose();
            }
        };
        Runnable waitRunner = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(pause);
                    SwingUtilities.invokeAndWait(closerRunner);
                }
                catch(Exception e) {
                    e.printStackTrace();
                    // can catch InvocationTargetException
                    // can catch InterruptedException
                }
            }
        };
        setVisible(true);
        Thread splashThread = new Thread(waitRunner, "SplashThread");
        splashThread.start();
    }
    
    public static void main(String args[]) {
        new SplashWindow("/graphics/splash.gif", null, 3000);
    }
}


