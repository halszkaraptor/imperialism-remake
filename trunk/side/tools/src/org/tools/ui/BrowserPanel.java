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

import java.awt.Dimension;
import java.awt.Image;
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
public class BrowserPanel extends PanelWithBackground {

    private static final Logger LOG = Logger.getLogger(BrowserPanel.class.getName());
    private static final long serialVersionUID = 1L;
    /* size of the buttons including margin, border and everything */
    private static final Dimension PREF_BUTTON_SIZE = new Dimension(23, 23);
    /* "previous in the list" button */
    private JButton pButton;
    /* "next in the list" button */
    private JButton nButton;
    /* ui element where the html is displayed */
    private JEditorPane contentPane;
    /* index url where we go back */
    private URL indexURL;
    /* provider of button icon images */
    private IconLoader loader;
    /* holds the visited URLs */
    private Deque<URL> historyList = new LinkedList<>();
    /* holds the visited and gone back URLs */
    private Deque<URL> forwardList = new LinkedList<>();

    /**
     * Constructs a new browser panel and sets index and start page.
     *
     * @param index index URL
     * @param start start URL
     * @param loader loader of icon resources
     */
    public BrowserPanel(URL index, URL start, IconLoader loader, Image background) {
        super(background);
        // check, should not give null
        if (index == null || start == null) {
            LOG.log(Level.SEVERE, "BrowserDialog init failed");
            throw new IllegalArgumentException("index or start URL are null");
        }

        // store variables
        indexURL = index;
        this.loader = loader;

        // setup of the panel
        initComponents();

        // initially set start page
        setPage(start);
    }

    /**
     * Setup of the components of the panel.
     */
    private void initComponents() {

        // "previous in the list" - button
        pButton = ButtonFactory.create(loader.getAsIcon("browser.button.previous.png"), "Previous page");
        pButton.setPreferredSize(PREF_BUTTON_SIZE);
        pButton.setEnabled(false);
        pButton.addActionListener(new ActionListener() {
            /**
             * Previous action.
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

                updateButtonStatus();
            }
        });

        // "next in the list" - button
        nButton = ButtonFactory.create(loader.getAsIcon("browser.button.next.png"), "Next page");
        nButton.setPreferredSize(PREF_BUTTON_SIZE);
        nButton.addActionListener(new ActionListener() {
            /**
             * Forward action.
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

                updateButtonStatus();
            }
        });

        updateButtonStatus();

        // "back to index" - button
        JButton iButton = ButtonFactory.create(loader.getAsIcon("browser.button.home.png"), "Back to index");
        iButton.setPreferredSize(PREF_BUTTON_SIZE);
        iButton.addActionListener(new ActionListener() {
            /**
             * Index button action.
             */
            @Override
            public void actionPerformed(ActionEvent evt) {
                // clear forward and backward lists
                forwardList.clear();
                historyList.clear();

                // set to index page
                setPage(indexURL);

                updateButtonStatus();
            }
        });

        // content pane
        contentPane = new JEditorPane();
        contentPane.setContentType("text/html");
        contentPane.setEditable(false);
        contentPane.addHyperlinkListener(new HyperlinkListener() {
            /**
             * Hyperlink clicked action.
             */
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

                    updateButtonStatus();
                }
            }
        });

        // scroll pane (never scrolls horizontally)
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(contentPane);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // menubar
        ButtonBar bar = new ButtonBar();
        bar.add(pButton, nButton, iButton);

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

    /**
     * Enables or disables the previous/next buttons according to the emptiness
     * of the queues.
     */
    private void updateButtonStatus() {
        pButton.setEnabled(!historyList.isEmpty());
        nButton.setEnabled(!forwardList.isEmpty());
    }
}
