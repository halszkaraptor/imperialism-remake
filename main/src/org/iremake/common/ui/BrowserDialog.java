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
package org.iremake.common.ui;

import java.awt.Container;
import java.awt.Frame;
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
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import net.miginfocom.swing.MigLayout;
import org.iremake.common.ui.utils.UILoader;

/**
 * Based on JEditorPane in non-editable, content text/html mode.
 *
 * Hides on close, can be reused.
 */
// TODO customize more (hide on close, dialog, frame)...
// TODO use composition instead?
public class BrowserDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(BrowserDialog.class.getName());
    private JButton previousButton;
    private JButton nextButton;
    private JButton indexButton;
    private JScrollPane scrollPane;
    private JEditorPane contentPane;
    private URL indexURL;
    private Deque<URL> historyList = new LinkedList<>();
    private Deque<URL> forwardList = new LinkedList<>();
    private UILoader loader;

    /**
     * Initializes the Browser.
     *
     * @param parent
     * @param title
     * @param modal
     * @param index
     * @param start
     */
    public BrowserDialog(Frame parent, String title, boolean modal, URL index, URL start, UILoader loader) {
        super(parent, title, modal);
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

        // previous in the list button
        previousButton = new JButton();
        previousButton.setToolTipText("Previous page");
        previousButton.setIcon(loader.getAsIcon("browser.button.previous.png"));
        previousButton.setMargin(new Insets(1, 1, 1, 1));
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
        indexButton = new JButton("Content");
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
        JToolBar menuBar = new JToolBar();
        menuBar.setFloatable(false);
        menuBar.add(previousButton);
        menuBar.add(nextButton);
        menuBar.add(indexButton);

        // layout
        Container c = getContentPane();
        c.setLayout(new MigLayout("wrap 1, fill", "", "[][grow]"));
        add(menuBar);
        add(scrollPane, "grow");

        // size and close operations
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE); // TODO do we need this

        setSize(800, 600); // TODO do we need this
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

    /**
     * After hiding we can make it visible again.
     * @param url
     */
    public void reanimate(URL url) {
        // clear forward and backward lists
        forwardList.clear();
        historyList.clear();
        // set url
        setPage(url);
        // set visible
        setVisible(true);
    }
}