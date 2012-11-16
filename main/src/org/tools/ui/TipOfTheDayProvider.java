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

import java.util.List;
import org.tools.common.Pair;

/**
 * A really, simple provider of a certain number of String pairs (headings and
 * content) that e.g. can be displayed in the TipOfTheDay dialog.
 */
public class TipOfTheDayProvider {

    private int index;
    private int number;
    private boolean show;
    private String title;
    private List<Pair<String, String>> tips;

    /**
     *
     * @param title
     * @param tips
     */
    public TipOfTheDayProvider(String title, List<Pair<String, String>> tips) {
        if (tips.isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        this.title = title;
        this.tips = tips;
        number = tips.size();
    }

    /**
     *
     * @param i
     * @return
     */
    public boolean setIndex(int i) {
        if (i < 0 || i >= number) {
            return false;
        }
        index = i;
        return true;
    }

    /**
     *
     */
    public void next() {
        index = (index + 1) % number;
    }

    /**
     *
     */
    public void previous() {
        index = (index - 1 + number) % number;
    }

    /**
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     *
     * @param flag
     */
    public void setShowingOnStartup(boolean flag) {
        show = flag;
    }

    /**
     *
     * @return
     */
    public boolean isShowingOnStartup() {
        return show;
    }

    /**
     *
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return String.format("%s (%d/%d)", title, index + 1, number);
    }

    /**
     *
     * @return
     */
    public String getHeading() {
        return tips.get(index).getA();
    }

    /**
     *
     * @return
     */
    public String getContent() {
        return tips.get(index).getB();
    }
}
