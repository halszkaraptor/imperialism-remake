package org.iremake.ctt;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.utils.LookAndFeel;

/**
 *
 */
public class MainFrame {
    
    public MainFrame() {
        JFrame frame = new JFrame("Imperialism Continuous Tiles Test");
        frame.setResizable(true);
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);        
        
        frame.setLayout(new MigLayout("wrap 2, fill", "[grow][]", "[][grow]"));
        frame.add(makeA(), "wmin 200, hmin 200, grow");
        frame.add(makeB());
        frame.add(makeC(), "span 2, hmin 200, grow");
        
        frame.pack();
        frame.setVisible(true);
    }
    
    private JComponent makeA() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("A"));
        
        return panel;
    }
    
    private JComponent makeB() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("B"));        
        
        return panel;
    }
    
    private JComponent makeC() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("C"));        
        
        return panel;
    }    
    
    
    

    public static void main(String[] args) {
        LookAndFeel.setSystemLookAndFeel();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }    
    
}
