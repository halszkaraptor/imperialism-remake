/*
 * Copyright (C) 2011 Trilarion
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
package org.tools.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import org.tools.common.Loader;
import org.tools.ui.common.GradientPanel;

/**
 * Simple Tip of the Day dialog.
 *
 * Hints The icon fits the layout best, if it is size 40x40. The preferred size
 * of the Text area is hard coded (300x150). The content is capsuled in a
 * TipOfTheDayProvider object.
 */
public class TipOfTheDayDialolg extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final Color BGColor = new Color(230, 235, 245);
    private TipOfTheDayProvider provider;
    private JCheckBox showStartupCheckBox;
    private JLabel headingLabel;
    private JTextPane textPane;

    /**
     *
     * @param parent
     * @param modal
     * @param icon
     */
    public TipOfTheDayDialolg(Frame parent, boolean modal, Icon icon) {
        super(parent, modal);
        initComponents(icon);
    }

    /**
     *
     */
    private void initComponents(Icon icon) {

        // the whole frame is filled with a panel with BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // at the top is a panel with BorderLayout and no gaps (and gradient in the background)
        GradientPanel top = new GradientPanel();
        top.setGradientColors(Color.white, BGColor);
        top.setLayout(new BorderLayout(0, 0));

        // inside is a label with the heading text (bold)
        headingLabel = new JLabel("Heading");
        // distance 25 pixels from left, effectively 30 because of JLabel
        headingLabel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        Font font = headingLabel.getFont();
        headingLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        top.add(headingLabel);

        // then there is an icon with 5 pixel border on the right side
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(iconLabel, BorderLayout.EAST);

        // and there is a separator below
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);
        top.add(separator, BorderLayout.SOUTH);

        // new panel for the center, again without gaps
        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setBackground(BGColor);
        textPane = new JTextPane();
        // center.setBorder(Utils.createRoundedEmptyBorder(18, 18, 18, 18, 10, textPane.getBackground()));
        textPane.setPreferredSize(new Dimension(350, 150));
        textPane.setContentType("text/html");
        textPane.setText("<b>Content</b>");
        textPane.setEditable(false);
        center.add(textPane);

        // new panel for the "Show Tips at startup" line
        JPanel below = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        below.setBackground(BGColor);
        showStartupCheckBox = new JCheckBox("Show Tips at startup");
        showStartupCheckBox.setSelected(true);
        showStartupCheckBox.setMnemonic(KeyEvent.VK_S);
        showStartupCheckBox.setBackground(BGColor);
        below.add(showStartupCheckBox);

        // bottom panel is for showing the forward, backward, close buttons in right aligned FlowLayout
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        bottom.setBackground(BGColor);
        JButton previousButton = new JButton();
        previousButton.setToolTipText("Previous tip");
        previousButton.setIcon(Loader.getAsIcon(Loader.IconPath + "resultset_previous.png"));
        previousButton.setMargin(new Insets(1, 1, 1, 1));
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                provider.previous();
                update();
            }
        });
        JButton nextButton = new JButton();
        nextButton.setToolTipText("Next tip");
        nextButton.setIcon(Loader.getAsIcon(Loader.IconPath + "resultset_next.png"));
        nextButton.setMargin(new Insets(1, 1, 1, 1));
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                provider.next();
                update();
            }
        });
        JButton closeButton = new JButton("Close");
        closeButton.setMnemonic(KeyEvent.VK_C);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        bottom.add(previousButton);
        bottom.add(nextButton);
        bottom.add(closeButton);

        // add all sub panels to panel and add it to the dialog
        panel.add(top);
        panel.add(center);
        panel.add(below);
        panel.add(bottom);
        add(panel);

        // we also dispose on close and the modality is of type APPLICATION_MODAL
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setResizable(false);
        pack();
    }

    /**
     *
     */
    public void update() {
        setTitle(provider.getTitle());
        headingLabel.setText(provider.getHeading());
        textPane.setText(provider.getContent());
    }

    /**
     *
     * @param provider
     */
    public void setTipsProvider(TipOfTheDayProvider provider) {
        this.provider = provider;
    }

    /**
     *
     */
    public void save() {
        provider.setShowingOnStartup(showStartupCheckBox.isSelected());
    }

    /**
     *
     * @param parent
     * @param provider
     * @param icon
     */
    public static void start(JFrame parent, TipOfTheDayProvider provider, Icon icon) {
        // create new modal dialog
        TipOfTheDayDialolg dlg = new TipOfTheDayDialolg(parent, true, icon);

        // set provide
        dlg.setTipsProvider(provider);

        // update
        dlg.update();

        // start dialog
        dlg.setVisible(true);

        // read final state out
        dlg.save();
    }
}
