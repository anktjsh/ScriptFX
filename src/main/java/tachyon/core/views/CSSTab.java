/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

/**
 *
 * @author Aniket
 */
public class CSSTab extends SamplesTab {

    public CSSTab() {
        super("CSS");
        add("Element Selector", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<style>\n"
                + "p {\n"
                + "    text-align: center;\n"
                + "    color: red;\n"
                + "}\n"
                + "</style>\n"
                + "</head>\n"
                + "<body>\n"
                + "\n"
                + "<p>Every paragraph will be affected by the style.</p>\n"
                + "<p id=\"para1\">Me too!</p>\n"
                + "<p>And me!</p>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("id Selector", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<style>\n"
                + "#para1 {\n"
                + "    text-align: center;\n"
                + "    color: red;\n"
                + "}\n"
                + "</style>\n"
                + "</head>\n"
                + "<body>\n"
                + "\n"
                + "<p id=\"para1\">Hello World!</p>\n"
                + "<p>This paragraph is not affected by the style.</p>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Class selector for all elements", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<style>\n"
                + ".center {\n"
                + "    text-align: center;\n"
                + "    color: red;\n"
                + "}\n"
                + "</style>\n"
                + "</head>\n"
                + "<body>\n"
                + "\n"
                + "<h1 class=\"center\">Red and center-aligned heading</h1>\n"
                + "<p class=\"center\">Red and center-aligned paragraph.</p>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Class Selector for <p> elements", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<style>\n"
                + "p.center {\n"
                + "    text-align: center;\n"
                + "    color: red;\n"
                + "}\n"
                + "</style>\n"
                + "</head>\n"
                + "<body>\n"
                + "\n"
                + "<h1 class=\"center\">This heading will not be affected</h1>\n"
                + "<p class=\"center\">This paragraph will be red and center-aligned.</p>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Grouping Selectors", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<style>\n"
                + "h1, h2, p {\n"
                + "    text-align: center;\n"
                + "    color: red;\n"
                + "}\n"
                + "</style>\n"
                + "</head>\n"
                + "<body>\n"
                + "\n"
                + "<h1>Hello World!</h1>\n"
                + "<h2>Smaller heading!</h2>\n"
                + "<p>This is a paragraph.</p>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
    }

}
