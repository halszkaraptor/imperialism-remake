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
package org.iremake.client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import org.iremake.client.ui.StartScreenBuilder;
import org.tools.ui.LookAndFeel;

/**
 *
 * @author Trilarion 2012
 */
public class Main {
    
    private Main() {}    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // set look and feel
        LookAndFeel.setSystemLookAndFeel();
        
        
        // fire up start frame
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = StartScreenBuilder.makeFrame();
                frame.setVisible(true);
            }
        });        

    }
    
    public static void shutDown() {
        
    }

}
