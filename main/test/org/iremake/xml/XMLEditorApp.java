/*
 * Copyright (C) 2012 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.iremake.xml;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import org.iremake.common.model.Scenario;
import org.iremake.common.ui.utils.LookAndFeel;
import org.iremake.common.xml.XMLHelper;
import org.iremake.common.xml.common.XProperty;

/**
 * Allows editing of various game XML files. Used for creating and editing them.
 */
public class XMLEditorApp extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Creates new form XMLEditor
     */
    public XMLEditorApp() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        baseTextField = new JTextField();
        mainSplitPane = new JSplitPane();
        upperPanel = new JPanel();
        terrainButton = new JButton();
        lowerPanel = new JPanel();
        mapButton = new JButton();
        toolBar = new JToolBar();
        colorButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("XML Creator Utility");
        setLocationByPlatform(true);
        setResizable(false);

        baseTextField.setText("C:\\Users\\Admin\\Dropbox\\remake\\00 actual game data\\game");

        mainSplitPane.setDividerLocation(276);
        mainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setResizeWeight(0.5);

        terrainButton.setText("Edit Terrain");
        terrainButton.setFocusable(false);
        terrainButton.setHorizontalTextPosition(SwingConstants.CENTER);
        terrainButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        terrainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                terrainButtonActionPerformed(evt);
            }
        });

        GroupLayout upperPanelLayout = new GroupLayout(upperPanel);
        upperPanel.setLayout(upperPanelLayout);
        upperPanelLayout.setHorizontalGroup(
            upperPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(terrainButton)
                .addContainerGap(681, Short.MAX_VALUE))
        );
        upperPanelLayout.setVerticalGroup(
            upperPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(terrainButton)
                .addContainerGap(241, Short.MAX_VALUE))
        );

        mainSplitPane.setTopComponent(upperPanel);

        mapButton.setText("Empty Map");
        mapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mapButtonActionPerformed(evt);
            }
        });

        GroupLayout lowerPanelLayout = new GroupLayout(lowerPanel);
        lowerPanel.setLayout(lowerPanelLayout);
        lowerPanelLayout.setHorizontalGroup(
            lowerPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(lowerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mapButton)
                .addContainerGap(683, Short.MAX_VALUE))
        );
        lowerPanelLayout.setVerticalGroup(
            lowerPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(lowerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mapButton)
                .addContainerGap(205, Short.MAX_VALUE))
        );

        mainSplitPane.setRightComponent(lowerPanel);

        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        colorButton.setText("Color");
        colorButton.setFocusable(false);
        colorButton.setHorizontalTextPosition(SwingConstants.CENTER);
        colorButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                colorButtonActionPerformed(evt);
            }
        });
        toolBar.add(colorButton);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(baseTextField)
                    .addComponent(mainSplitPane, Alignment.TRAILING)
                    .addComponent(toolBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(baseTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(mainSplitPane)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void terrainButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_terrainButtonActionPerformed

        final String target = "\\artwork\\graphics\\terrain\\terrain.xml";
        String full = baseTextField.getText() + target;
        XProperty prop = new XProperty(0);
        /*
         XMLHelper.read(full, prop);
         if (PropertyEditorDialog.createAndRun(this, target, prop) == 0) {
         XMLHelper.write(full, prop);
         }*/
    }//GEN-LAST:event_terrainButtonActionPerformed

    private void mapButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_mapButtonActionPerformed

        Scenario map = new Scenario();
        map.setEmptyMap(60, 100);
        // map.setTerrainAt(0, 0, "p1");
        XMLHelper.write("map.xml", map.toXML());
    }//GEN-LAST:event_mapButtonActionPerformed

    private void colorButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_colorButtonActionPerformed
        JDialog dialog = new JDialog(this, "Color Chooser", false);
        JColorChooser chooser = new JColorChooser();
        dialog.add(chooser);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);
    }//GEN-LAST:event_colorButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // set look and feel
        LookAndFeel.setSystemLookAndFeel();

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new XMLEditorApp().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextField baseTextField;
    private JButton colorButton;
    private JPanel lowerPanel;
    private JSplitPane mainSplitPane;
    private JButton mapButton;
    private JButton terrainButton;
    private JToolBar toolBar;
    private JPanel upperPanel;
    // End of variables declaration//GEN-END:variables
}
