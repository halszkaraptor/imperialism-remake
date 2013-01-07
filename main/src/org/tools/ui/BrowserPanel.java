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
package org.tools.ui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.utils.IconLoader;

/**
 * A panel consisting of a few buttons and a JEditorPane to display
 * (non-editable) text/html content.
 */
// TODO use composition instead
public class BrowserPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(BrowserPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private JButton previousButton;
    private JButton nextButton;
    private JButton indexButton;
    private JScrollPane scrollPane;
    private JEditorPane contentPane;
    private URL indexURL;
    private Deque<URL> historyList = new LinkedList<>();
    private Deque<URL> forwardList = new LinkedList<>();
    private IconLoader loader;

    public BrowserPanel(URL index, URL start, IconLoader loader) {
        if (index == null || start == null) {
            LOG.log(Level.SEVERE, "BrowserDialog init failed");
            throw new IllegalArgumentException("index or start URL are null");
        }
        indexURL = index;
        this.loader = loader;
        initComponents();
        setPage(start);
    }

    /**
     * Internal initialization.
     */
    private void initComponents() {

        // TODO generic button maker

        // previous in the list button
        previousButton = new JButton();
        previousButton.setToolTipText("Previous page");
        previousButton.setIcon(loader.getAsIcon("browser.button.previous.png"));
        previousButton.setMargin(new Insets(1, 1, 1, 1));
        previousButton.setFocusable(false);
        previousButton.addActionListener(new ActionListener() {

            /*
             * previous action
             */
            @Override
            public void actionPerformed(ActionEvent evt) {
                // check if there is some
                if (historyList.isEmpty() == true) {
                    return;
                }
                // add current to forward list
                forwardList.add(contentPane.getPage());
                // take one out and set
                setPage(historyList.removeLast());
            }
        });

        // next in the list button
        nextButton = new JButton();
        nextButton.setToolTipText("Next page");
        nextButton.setIcon(loader.getAsIcon("browser.button.next.png"));
        nextButton.setMargin(new Insets(1, 1, 1, 1));
        nextButton.setFocusable(false);
        nextButton.addActionListener(new ActionListener() {

            /*
             * forward action
             */
            @Override
            public void actionPerformed(ActionEvent evt) {
                // check if there is some
                if (forwardList.isEmpty() == true) {
                    return;
                }
                // store current in history list
                historyList.addLast(contentPane.getPage());
                // take one out and set
                setPage(forwardList.removeLast());
            }
        });

        // index button
        indexButton = new JButton();
        indexButton.setIcon(loader.getAsIcon("browser.button.home.png"));
        indexButton.setMargin(new Insets(1, 1, 1, 1));
        indexButton.setFocusable(false);
        indexButton.addActionListener(new ActionListener() {
            // content button clicked
            @Override
            public void actionPerformed(ActionEvent evt) {
                // clear forward and backward lists
                forwardList.clear();
                historyList.clear();
                // set to index page
                setPage(indexURL);
            }
        });

        // content pane
        contentPane = new JEditorPane();
        contentPane.setContentType("text/html");
        contentPane.setEditable(false);
        contentPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    URL url = e.getURL();
                    // delete forwardList
                    forwardList.clear();
                    // add current to history List
                    historyList.addLast(contentPane.getPage());
                    // set new one
                    setPage(url);
                }
            }
        });

        // scroll pane (never scrolls horizontally)
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(contentPane);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // menubar
        ButtonBar bar = new ButtonBar();
        bar.add(previousButton, nextButton, indexButton);

        // layout
        setLayout(new MigLayout("wrap 1, fill", "", "[][grow]"));
        add(bar.get());
        add(scrollPane, "grow, hmin 300, wmin 400");
    }

    /**
     * Just for not having the exception handling all the time
     *
     * @param url
     */
    private void setPage(URL url) {
        try {
            contentPane.setPage(url);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}
