/*
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
package org.iremake.ui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

/**
 *
 * @author Trilarion 2011
 */
public class StartFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    public StartFrame() {
        initComponents();
    }
    
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Title");
        setUndecorated(true);
        setIconImage(new ImageIcon(getClass().getResource("/data/images/gui/icons/app_icon.png")).getImage());        
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setAlwaysOnTop(true);
        
        JToolBar toolBar = new JToolBar();
        JButton buttonNewWorld = new JButton(new ImageIcon(getClass().getResource("/data/images/gui/icons/start_new_world.png")));
        toolBar.add(buttonNewWorld);
        
        add(toolBar);
    }
    
}
