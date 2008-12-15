/*
 * Copyright 1997-2004 Unidata Program Center/University Corporation for
 * Atmospheric Research, P.O. Box 3000, Boulder, CO 80307,
 * support@unidata.ucar.edu.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

// $Id: StringUtil.java,v 1.53 2007/06/01 17:02:44 jeffmc Exp $


package ucar.unidata.util;


import ucar.unidata.xml.XmlUtil;


import java.lang.reflect.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;




/**
 * @version $Id: StringUtil.java,v 1.53 2007/06/01 17:02:44 jeffmc Exp $
 */

public class HtmlUtil {

    public static final String SIZE_3 = "  size=\"3\" ";


    public static final String SIZE_5 = "  size=\"5\" ";

    public static final String SIZE_8 = "  size=\"8\" ";

    /** _more_ */
    public static final String SIZE_10 = "  size=\"10\" ";

    /** _more_ */
    public static final String SIZE_20 = "  size=\"20\" ";

    /** _more_ */
    public static final String SIZE_15 = "  size=\"15\" ";

    /** _more_ */
    public static final String SIZE_25 = "  size=\"25\" ";

    /** _more_ */
    public static final String SIZE_30 = "  size=\"30\" ";

    /** _more_ */
    public static final String SIZE_40 = "  size=\"40\" ";

    /** _more_ */
    public static final String SIZE_50 = "  size=\"50\" ";

    /** _more_ */
    public static final String SIZE_60 = "  size=\"60\" ";

    /** _more_ */
    public static final String SIZE_70 = "  size=\"70\" ";

    public static final String SIZE_80 = "  size=\"80\" ";
    public static final String SIZE_90 = "  size=\"90\" ";


    /**
     * _more_
     *
     * @param value _more_
     * @param name _more_
     *
     * @return _more_
     */
    public static String hidden(String name, Object value) {
        return "<input type=\"hidden\" name=\"" + name + "\" value=\""
               + value + "\">";
    }


    /**
     * _more_
     *
     * @param s1 _more_
     * @param s2 _more_
     *
     * @return _more_
     */
    public static String hbox(String s1, String s2) {
        return "<table cellspacing=\"0\" cellpadding=\"0\">"
               + HtmlUtil.rowTop(HtmlUtil.cols(s1, s2)) + "</table>";
    }

    /**
     * _more_
     *
     * @return _more_
     */
    public static String br() {
        return "<br>";
    }

    public static String br(String line) {
        return line +"<br>";
    }
    /**
     * _more_
     *
     * @return _more_
     */
    public static String hr() {
        return "<hr>";
    }

    /**
     * _more_
     *
     * @return _more_
     */
    public static String p() {
        return "<p>";
    }

    public static String nobr(String inner) {
        return wrap("nobr",inner);
    }


    public static String b(String inner) {
        return wrap("b",inner);
    }

    public static String li(String inner, String extra) {
        return wrap("li",extra, inner);
    }


    public static String center(String inner) {
        return wrap("center",inner);
    }

    public static String wrap(String tag, String inner) {
        return "<" + tag +">" + inner +"</" + tag +">";
    }


    public static String wrap(String tag, String extra, String inner) {
        //        System.err.println ("<" + tag +" " + extra +">" + inner +"</" + tag +">");

        return "<" + tag +" " + extra +">" + inner +"</" + tag +">";
    }


    /**
     * _more_
     *
     * @param s _more_
     *
     * @return _more_
     */
    public static String pad(String s) {
        return space(1) + s + space(1);
    }

    /**
     * _more_
     *
     * @param s _more_
     *
     * @return _more_
     */
    public static String padLeft(String s) {
        return space(1) + s;
    }

    /**
     * _more_
     *
     * @param s _more_
     *
     * @return _more_
     */
    public static String padRight(String s) {
        return s + space(1);
    }


    /**
     * _more_
     *
     * @param cnt _more_
     *
     * @return _more_
     */
    public static String space(int cnt) {
        String s = "";
        while (cnt-- > 0) {
            s = s + "&nbsp;";
        }
        return s;
    }

    /**
     * _more_
     *
     * @param s _more_
     *
     * @return _more_
     */
    public static String quote(String s) {
        return "\"" + s + "\"";
    }

    public static String squote(String s) {
        return "'" + s + "'";
    }

    /**
     * _more_
     *
     * @param path _more_
     *
     * @return _more_
     */
    public static String img(String path) {
        return img(path, "");
    }

    /**
     * _more_
     *
     * @param path _more_
     * @param title _more_
     *
     * @return _more_
     */
    public static String img(String path, String title) {
        return img(path, title, "");
    }

    /**
     * _more_
     *
     * @param path _more_
     * @param title _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String img(String path, String title, String extra) {
        if (title.length() > 0) {
            return "<img "
                   + attrs("border", "0", "src", path, "title", title, "alt",
                           title) + " " + extra + ">";
        }
        return "<img " + attrs("border", "0", "src", path) + " " + extra
               + ">";
    }

    /**
     * _more_
     *
     * @param c _more_
     *
     * @return _more_
     */
    public static String cssClass(String c) {
        return " class=\"" + c + "\" ";
    }

    public static String title(String c) {
        return " title=" + quote(c) +" " ;
    }

    public static String id(String c) {
        return " id=\"" + c +"\" ";
    }

    public static String style(String c) {
        return " style=\"" + c +"\" ";
    }

    /**
     * _more_
     *
     * @param v1 _more_
     *
     * @return _more_
     */
    public static String bold(String v1) {
        return "<b>" + v1 + "</b>";
    }

    /**
     * _more_
     *
     * @param v1 _more_
     *
     * @return _more_
     */
    public static String col(String v1) {
        return "<td>" + v1 + "</td>";
    }


    /**
     * _more_
     *
     * @param content _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String span(String content, String extra) {
        return "<span " + extra + ">" + content + "</span>";
    }

    /**
     * _more_
     *
     * @param content _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String div(String content, String extra) {
        return "<div " + extra + ">" + content + "</div>";
    }


    /**
     * _more_
     *
     * @param path _more_
     * @param n1 _more_
     * @param v1 _more_
     *
     * @return _more_
     */
    public static String url(String path, String n1, Object v1) {
        return url(path, new String[] { n1, v1.toString() });
    }


    /**
     * _more_
     *
     * @param path _more_
     * @param n1 _more_
     * @param v1 _more_
     * @param n2 _more_
     * @param v2 _more_
     *
     * @return _more_
     */
    public static String url(String path, String n1, Object v1, String n2,
                             Object v2) {
        return url(path, new String[] { n1, v1.toString(), n2, v2.toString() });
    }

    /**
     * _more_
     *
     * @param path _more_
     * @param n1 _more_
     * @param v1 _more_
     * @param n2 _more_
     * @param v2 _more_
     * @param n3 _more_
     * @param v3 _more_
     *
     * @return _more_
     */
    public static String url(String path, String n1, Object v1, String n2,
                             Object v2, String n3, Object v3) {
        return url(path, new String[] {
            n1, v1.toString(), n2, v2.toString(), n3, v3.toString()
        });
    }

    /**
     * _more_
     *
     * @param path _more_
     * @param n1 _more_
     * @param v1 _more_
     * @param n2 _more_
     * @param v2 _more_
     * @param n3 _more_
     * @param v3 _more_
     * @param n4 _more_
     * @param v4 _more_
     *
     * @return _more_
     */
    public static String url(String path, String n1, Object v1, String n2,
                             Object v2, String n3, Object v3, String n4,
                             Object v4) {
        return url(path, new String[] {
            n1, v1.toString(), n2, v2.toString(), n3, v3.toString(), n4, v4.toString()
        });
    }

    public static String url(String path, List  args) {
        return url(path,Misc.listToStringArray(args));
    }


    /**
     * _more_
     *
     * @param path _more_
     * @param args _more_
     *
     * @return _more_
     */
    public static String url(String path, String[] args) {
        if (args.length == 0) {
            return path.toString();
        }
        boolean addAmpersand = false;
        String  url          = path.toString();
        if (url.indexOf("?") >= 0) {
            if ( !url.endsWith("?")) {
                addAmpersand = true;
            }
        } else {
            url = url + "?";
        }

        for (int i = 0; i < args.length; i += 2) {
            if (addAmpersand) {
                url = url + "&";
            }
            try {
                url          = url + args[i] + "=" + java.net.URLEncoder.encode(args[i + 1], "UTF-8");
            } catch(Exception exc) {
                System.err.println("error encoding arg:" + args[i+1] + " " + exc);
            }
            addAmpersand = true;
        }
        return url;
    }


    /**
     * _more_
     *
     * @param row _more_
     *
     * @return _more_
     */
    public static String row(String row) {
        return "<tr>" + row + "</tr>";
    }

    /**
     * _more_
     *
     * @param row _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String row(String row, String extra) {
        return "<tr " + extra + ">" + row + "</tr>";
    }

    /**
     * _more_
     *
     * @param row _more_
     *
     * @return _more_
     */
    public static String rowTop(String row) {
        return "<tr valign=\"top\">" + row + "</tr>";
    }

    /**
     * _more_
     *
     * @param row _more_
     *
     * @return _more_
     */
    public static String rowBottom(String row) {
        return "<tr valign=\"bottom\">" + row + "</tr>";
    }


    /**
     * _more_
     *
     * @param s1 _more_
     *
     * @return _more_
     */
    public static String cols(String s1) {
        return "<td>" + s1 + "</td>";
    }

    /**
     * _more_
     *
     * @param s1 _more_
     * @param s2 _more_
     *
     * @return _more_
     */
    public static String cols(String s1, String s2) {
        return cols(s1) + cols(s2);
    }

    /**
     * _more_
     *
     * @param s1 _more_
     * @param s2 _more_
     * @param s3 _more_
     *
     * @return _more_
     */
    public static String cols(String s1, String s2, String s3) {
        return cols(s1) + cols(s2) + cols(s3);
    }


    /**
     * _more_
     *
     * @param s1 _more_
     * @param s2 _more_
     * @param s3 _more_
     * @param s4 _more_
     *
     * @return _more_
     */
    public static String cols(String s1, String s2, String s3, String s4) {
        return cols(s1) + cols(s2) + cols(s3) + cols(s4);
    }




    /**
     * _more_
     *
     * @param s1 _more_
     * @param s2 _more_
     * @param s3 _more_
     * @param s4 _more_
     * @param s5 _more_
     *
     * @return _more_
     */
    public static String cols(String s1, String s2, String s3, String s4,
                              String s5) {
        return cols(s1) + cols(s2) + cols(s3) + cols(s4) + cols(s5);
    }


    /**
     * _more_
     *
     * @param s1 _more_
     * @param s2 _more_
     * @param s3 _more_
     * @param s4 _more_
     * @param s5 _more_
     * @param s6 _more_
     *
     * @return _more_
     */
    public static String cols(String s1, String s2, String s3, String s4,
                              String s5, String s6) {
        return cols(s1) + cols(s2) + cols(s3) + cols(s4) + cols(s5)
               + cols(s6);
    }

    public static String cols(Object[]columns) {
        StringBuffer sb  = new StringBuffer();
        for(int i=0;i<columns.length;i++) {
            sb.append(cols(columns[i].toString()));
        }
        return sb.toString();
    }


    /**
     * _more_
     *
     * @param baseName _more_
     * @param south _more_
     * @param north _more_
     * @param east _more_
     * @param west _more_
     *
     * @return _more_
     */
    public static String makeLatLonBox(String baseName, String south,
                                       String north, String east,
                                       String west) {
        return "<table>" + "<tr><td colspan=\"2\" align=\"center\">"
               + input(baseName + "_north", north, " size=\"5\" id=\"" + baseName + "_north\"" )
               + "</td></tr>" + "<tr><td>"
               + input(baseName + "_west", west, " size=\"5\" id=\"" + baseName + "_west\"") + "</td><td>"
               + input(baseName + "_east", east, " size=\"5\"  id=\"" + baseName + "_east\"") + "</tr>"
               + "<tr><td colspan=\"2\" align=\"center\">"
               + input(baseName + "_south", south, " size=\"5\"  id=\"" + baseName + "_south\"")
               + "</table>";
    }

    /**
     * _more_
     *
     * @param baseName _more_
     * @param south _more_
     * @param north _more_
     * @param east _more_
     * @param west _more_
     *
     * @return _more_
     */
    public static String makeLatLonBox(String baseName, double south,
                                       double north, double east,
                                       double west) {
        return "<table>" + "<tr><td colspan=\"2\" align=\"center\">"
               + input(baseName + "_north", toString(north), " size=\"5\"")
               + "</td></tr>" + "<tr><td>"
               + input(baseName + "_west", toString(west), " size=\"5\"")
               + "</td><td>"
               + input(baseName + "_east", toString(east), " size=\"5\"")
               + "</tr>" + "<tr><td colspan=\"2\" align=\"center\">"
               + input(baseName + "_south", toString(south), " size=\"5\"")
               + "</table>";
    }

    /**
     * _more_
     *
     * @param v _more_
     *
     * @return _more_
     */
    private static String toString(double v) {
        if (v == v) {
            return "" + v;
        }
        return "";
    }

    /**
     * _more_
     *
     * @param south _more_
     * @param north _more_
     * @param east _more_
     * @param west _more_
     *
     * @return _more_
     */
    public static String makeAreaLabel(double south, double north,
                                       double east, double west) {
        return "<table>" + "<tr><td colspan=\"2\" align=\"center\">"
               + toString(north) + "</td></tr>" + "<tr><td>" + toString(west)
               + "</td><td>" + toString(east) + "</tr>"
               + "<tr><td colspan=\"2\" align=\"center\">" + toString(south)
               + "</table>";
    }


    /**
     * _more_
     *
     * @param name _more_
     * @param value _more_
     *
     * @return _more_
     */
    public static String checkbox(String name, String value) {
        return checkbox(name, value, false);
    }

    /**
     * _more_
     *
     * @param name _more_
     * @param value _more_
     * @param checked _more_
     *
     * @return _more_
     */
    public static String checkbox(String name, String value,
                                  boolean checked) {
        return checkbox(name, value, checked, "");
    }

    /**
     * _more_
     *
     * @param name _more_
     * @param value _more_
     * @param checked _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String checkbox(String name, String value, boolean checked,
                                  String extra) {
        return "<input " + extra
               + attrs("type", "checkbox", "name", name, "value", value)
               + (checked
                  ? " checked "
                  : "") + ">";
    }

    /**
     * _more_
     *
     * @param url _more_
     *
     * @return _more_
     */
    public static String form(String url) {
        return form(url, "");
    }

    /**
     * _more_
     *
     * @param url _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String form(String url, String extra) {
        return "<form action=\"" + url + "\"" + " " + extra + " >";
    }

    /**
     * _more_
     *
     * @param url _more_
     *
     * @return _more_
     */
    public static String formPost(String url) {
        return "<form method=\"post\" action=\"" + url + "\">";
    }

    public static String formPost(String url, String extra) {
        return "<form method=\"post\" action=\"" + url + "\"" + " " + extra+">";
    }


    /**
     * _more_
     *
     * @param url _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String uploadForm(String url, String extra) {
        return "<form method=\"post\" enctype=\"multipart/form-data\"  action=\""
               + url + "\"" + " " + extra + " >";
    }



    /**
     * _more_
     *
     * @param url _more_
     * @param label _more_
     *
     * @return _more_
     */
    public static String href(String url, String label) {
        return "<a href=\"" + url.toString() + "\">" + label + "</a>";
    }

    /**
     * _more_
     *
     * @param url _more_
     * @param label _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String href(String url, String label, String extra) {
        return "<a href=\"" + url.toString() + "\"" + " " + extra + ">"
               + label + "</a>";
    }

    /**
     * _more_
     *
     * @param img _more_
     * @param name _more_
     *
     * @return _more_
     */
    public static String submitImage(String img, String name) {
        return "<input " + attrs("name", name, "border", "0", "src", img)
               + attrs("type", "image") + " >";

    }


    /**
     * _more_
     *
     * @param img _more_
     * @param name _more_
     * @param alt _more_
     *
     * @return _more_
     */
    public static String submitImage(String img, String name, String alt) {
        return "<input " + attrs("name", name, "border", "0", "src", img)
               + attrs("title", alt, "alt", alt, "type", "image") + " >";
    }


    /**
     * _more_
     *
     * @param label _more_
     * @param name _more_
     *
     * @return _more_
     */
    public static String submit(String label, String name) {
        return "<input  name=\"" + name + "\"   type=\"submit\" value=\""
               + label + "\" >";
    }

    /**
     * _more_
     *
     * @param label _more_
     *
     * @return _more_
     */
    public static String submit(String label) {
        return "<input  type=\"submit\" value=\"" + label + "\" >";
    }

    /**
     * _more_
     *
     * @param name _more_
     * @param value _more_
     * @param rows _more_
     * @param columns _more_
     *
     * @return _more_
     */
    public static String textArea(String name, String value, int rows,
                                  int columns) {

        return textArea(name,value,rows,columns,"");
    }

    public static String textArea(String name, String value, int rows,
                                  int columns, String extra) {
        return "<textarea " + attr("name", name) + attr("rows",""+ rows) +
            attr("cols",""+ columns) +extra + "\">" + value + "</textarea>";
    }

    /**
     * _more_
     *
     * @param name _more_
     *
     * @return _more_
     */
    public static String password(String name) {
        return "<input type=\"password\" name=\"" + name + "\" >";
    }


    /**
     * _more_
     *
     * @param name _more_
     *
     * @return _more_
     */
    public static String input(String name) {
        return input(name, null, "");
    }

    /**
     * _more_
     *
     * @param name _more_
     * @param value _more_
     *
     * @return _more_
     */
    public static String input(String name, Object value) {
        return input(name, value, "");
    }

    /**
     * _more_
     *
     * @param name _more_
     * @param value _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String input(String name, Object value, String extra) {
        return "<input name=\"" + name + "\" value=\"" + ((value == null)
                ? ""
                : value.toString()) + "\" " + extra + ">";
    }


    /**
     * _more_
     *
     * @param name _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String fileInput(String name, String extra) {
        return "<input type=\"file\" name=\"" + name + "\" " + extra + ">";
    }

    /**
     * _more_
     *
     * @param name _more_
     * @param values _more_
     *
     * @return _more_
     */
    public static String select(String name, List values) {
        return select(name, values, null);
    }

    /**
     * _more_
     *
     * @param name _more_
     * @param values _more_
     * @param selected _more_
     *
     * @return _more_
     */
    public static String select(String name, List values, String selected) {
        return select(name, values, selected, Integer.MAX_VALUE);
    }

    /**
     * _more_
     *
     * @param name _more_
     * @param values _more_
     * @param selected _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String select(String name, List values, String selected,
                                String extra) {
        return select(name, values, selected, extra, Integer.MAX_VALUE);
    }


    public static String select(String name, List values, List<String> selected,
                                String extra) {
        return select(name, values, selected, extra, Integer.MAX_VALUE);
    }


    /**
     * _more_
     *
     * @param name _more_
     * @param values _more_
     * @param selected _more_
     * @param maxLength _more_
     *
     * @return _more_
     */
    public static String select(String name, String[] values,
                                String selected, int maxLength) {
        return select(name, Misc.toList(values), selected, maxLength);
    }


    /**
     * _more_
     *
     * @param name _more_
     * @param values _more_
     * @param selected _more_
     * @param maxLength _more_
     *
     * @return _more_
     */
    public static String select(String name, List values, String selected,
                                int maxLength) {
        return select(name, values, selected, "", maxLength);
    }


    /**
     * _more_
     *
     * @param name _more_
     * @param values _more_
     * @param selected _more_
     * @param extra _more_
     * @param maxLength _more_
     *
     * @return _more_
     */
    public static String select(String name, List values, String selected,
                                String extra, int maxLength) {
        List<String> selectedList = null;
        if(selected!=null && selected.length()>0) {
            selectedList = (List<String>)Misc.newList(selected);
        }
        return select(name, values, selectedList, extra, maxLength);
    }


    public static String select(String name, List values, List<String> selected,
                                String extra, int maxLength) {
        StringBuffer sb = new StringBuffer();
        sb.append("<select name=" + quote(name) + " " + extra + ">\n");
        for (int i = 0; i < values.size(); i++) {
            Object obj = values.get(i);
            String value;
            String label;
            if (obj instanceof TwoFacedObject) {
                TwoFacedObject tfo = (TwoFacedObject) obj;
                value = tfo.getId().toString();
                label = tfo.toString();
            } else {
                value = label = obj.toString();
            }
            String selectedAttr = "";
            if ((selected != null) && selected.contains(value)) {
                selectedAttr = " selected=\"selected\" ";
            }
            if (label.length() > maxLength) {
                label = "..." + label.substring(label.length() - maxLength);
            }

            sb.append("<option " + selectedAttr + " title=\"" + value +"\"" + " value=\"" + value + "\">"
                      + label + "</option>\n");
        }
        sb.append("</select>\n");
        return sb.toString();
    }


    public static String inset(String html, int top,int left,int bottom, int right) {
        return span(html,style((top==0?"":"margin-top:" + top +"px;")+
                               (left==0?"":"margin-left:" + left +"px;")+
                               (bottom==0?"":"margin-bottom:" + bottom +"px;") +
                               (right==0?"":"margin-right:" + top +"px;")));
    }


    public static String inset(String html, int space) {
        return div(html,style("margin:" + space +"px;"));
    }

    /**
     * _more_
     *
     * @param s _more_
     * @param cols _more_
     *
     * @return _more_
     */
    public static String colspan(String s, int cols) {
        return "<td colspan=\"" + cols + "\">" + s + "</td>";
    }

    /**
     * _more_
     *
     * @param cols _more_
     *
     * @return _more_
     */
    public static String formTableTop(String[] cols) {
        StringBuffer sb = new StringBuffer();
        sb.append(formTable());

        for (int i = 0; i < cols.length; i += 2) {
            sb.append(formEntryTop(cols[i], cols[i + 1]));
        }
        sb.append(formTableClose());
        return sb.toString();
    }


    /**
     * _more_
     *
     * @param cols _more_
     *
     * @return _more_
     */
    public static String formEntryTop(String[] cols) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cols.length; i += 2) {
            sb.append(formEntryTop(cols[i], cols[i + 1]));
        }
        return sb.toString();
    }


    /**
     * _more_
     *
     * @param contents _more_
     *
     * @return _more_
     */
    public static String table(String contents) {
        return "<table cellpadding=\"5\" cellspacing=\"0\">\n" + contents
               + "</table>";
    }

    public static String table(Object[]columns) {
        return "<table cellpadding=\"5\" cellspacing=\"0\">\n" + row(cols(columns)," valign=\"top\" ")
               + "</table>";
    }


    /**
     * _more_
     *
     * @return _more_
     */
    public static String formTable() {
        return "<table  cellpadding=\"5\" cellspacing=\"0\">\n";
    }


    /**
     * _more_
     *
     * @param extra _more_
     *
     * @return _more_
     */
    public static String formTable(String extra) {
        return "<table cellpadding=\"5\" cellspacing=\"0\"" + " " + extra
               + ">\n";
    }

    /**
     * _more_
     *
     * @return _more_
     */
    public static String formTableClose() {
        return "</table>";
    }

    /**
     * _more_
     *
     * @return _more_
     */
    public static String formClose() {
        return "</form>";
    }


    /**
     * _more_
     *
     * @param left _more_
     * @param right _more_
     *
     * @return _more_
     */
    public static String formEntry(String left, String right) {
        //        if(left.length()>0) 
        return " <tr><td align=\"right\" class=\"formlabel\">" + left
               + "</td><td>" + right + "</td></tr>";

    }


    /**
     * _more_
     *
     * @param left _more_
     * @param right _more_
     *
     * @return _more_
     */
    public static String formEntryTop(String left, String right) {
        return formEntryTop(left,right,"",true);
    }


    public static String formEntryTop(String left, String right, String trExtra,boolean dummy) {
        //        if(left.length()>0) 
        return " <tr  " + trExtra+" valign=\"top\"><td align=\"right\" valign=\"top\" class=\"formlabel\">"
               + left + "</td><td>" + right + "</td></tr>";

    }

    /**
     * _more_
     *
     * @param col1 _more_
     * @param left _more_
     * @param right _more_
     *
     * @return _more_
     */
    public static String formEntryTop(String col1, String left,
                                      String right) {
        return " <tr valign=\"top\"><td>" + col1
               + "</td><td align=\"right\" valign=\"top\" class=\"formlabel\">"
               + left + "</td><td>" + right + "</td></tr>";

    }


    /**
     * _more_
     *
     * @param args _more_
     */
    public static void main(String[] args) {
        Class        c  = HtmlUtil.class;
        StringBuffer sb = new StringBuffer();
        sb.append(
            "//j-\n/** Do not change!!! This has been generated from HtmlUtil **/\n");
        Method[] methods = c.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            //            if(!Modifier.isStatic(m.getModifiers())) continue;
            if ( !m.getReturnType().equals(String.class)) {
                continue;
            }
            sb.append("public void " + m.getName() + "(");
            Class[]      params = m.getParameterTypes();
            StringBuffer implSb = new StringBuffer();
            for (int paramIdx = 0; paramIdx < params.length; paramIdx++) {
                if (paramIdx > 0) {
                    sb.append(", ");
                    implSb.append(", ");
                }
                implSb.append("param" + paramIdx);
                String type = params[paramIdx].getName();
                if (params[paramIdx].isArray()) {
                    type = params[paramIdx].getComponentType().getName()
                           + " []";
                }
                type = type.replace("java.lang.", "");
                sb.append(type + " param" + paramIdx);

            }

            sb.append(") {\n");
            sb.append("sb.append(HtmlUtil." + m.getName() + "(" + implSb
                      + "));\n");
            sb.append("}\n");
        }
        sb.append("//j+\n");
        //        System.out.println (sb);

    }


    /**
     * _more_
     *
     * @param name _more_
     * @param value _more_
     *
     * @return _more_
     */
    public static String attr(String name, String value) {
        return " " + name + "=" + quote(value) + " ";
    }

    /**
     * _more_
     *
     * @param name _more_
     * @param value _more_
     *
     * @return _more_
     */
    public static String attrs(String name, String value) {
        return " " + name + "=" + quote(value) + " ";
    }


    /**
     *  Return a String with n1=&quot;v1&quot n2=&quot;v2&quot.
     *
     *  @param n1 The first attribute name.
     *  @param v1 The first attribute value.
     *  @param n2 The second attribute name.
     *  @param v2 The second attribute value.
     *  @return The attrbiute string.
     */

    public static String attrs(String n1, String v1, String n2, String v2) {
        return attr(n1, v1) + attr(n2, v2);
    }

    /**
     *  Return a String with n1=&quot;v1&quot n2=&quot;v2&quot.  n3=&quot;v3&quot.
     *
     *  @param n1 The first attribute name.
     *  @param v1 The first attribute value.
     *  @param n2 The second attribute name.
     *  @param v2 The second attribute value.
     *  @param n3 The third attribute name.
     *  @param v3 The third attribute value.
     *  @return The attrbiute string.
     */

    public static String attrs(String n1, String v1, String n2, String v2,
                               String n3, String v3) {
        return attrs(n1, v1, n2, v2) + attr(n3, v3);
    }


    /**
     *  Return a String with n1=&quot;v1&quot n2=&quot;v2&quot.  n3=&quot;v3&quot.  n4=&quot;v4&quot.
     *
     *  @param n1 The first attribute name.
     *  @param v1 The first attribute value.
     *  @param n2 The second attribute name.
     *  @param v2 The second attribute value.
     *  @param n3 The third attribute name.
     *  @param v3 The third attribute value.
     *  @param n4 The fourth attribute name.
     *  @param v4 The fourth attribute value.
     *  @return The attrbiute string.
     */

    public static String attrs(String n1, String v1, String n2, String v2,
                               String n3, String v3, String n4, String v4) {
        return attrs(n1, v1, n2, v2, n3, v3) + attr(n4, v4);
    }



    /**
     * _more_
     *
     * @param call _more_
     *
     * @return _more_
     */
    public static String onMouseOver(String call) {
        return " onmouseover=" + quote(call) + " ";
    }

    public static String onMouseMove(String call) {
        return " onmousemove=" + quote(call) + " ";
    }


    /**
     * _more_
     *
     * @param call _more_
     *
     * @return _more_
     */
    public static String onMouseOut(String call) {
        return " onmouseout=" + quote(call) + " ";
    }

    /**
     * _more_
     *
     * @param call _more_
     *
     * @return _more_
     */
    public static String onMouseUp(String call) {
        return " onmouseup=" + quote(call) + " ";
    }

    /**
     * _more_
     *
     * @param call _more_
     *
     * @return _more_
     */
    public static String onMouseDown(String call) {
        return " onmousedown=" + quote(call) + " ";
    }


    /**
     * _more_
     *
     * @param call _more_
     *
     * @return _more_
     */
    public static String onMouseClick(String call) {
        return " onClick=" + quote(call) + " ";
    }


    public static String mouseClickHref(String call, String label) {
        return mouseClickHref(call,label,"");
    }

    public static String mouseClickHref(String call, String label, String extra) {
        //        return "<a href=\"javascript:void(0)\" " +onMouseClick(call) +">" +label +"</a>";
        return "<a href=\"javascript:void(0);\" " +onMouseClick(call) +extra+">" +label +"</a>";
    }



    /**
     * _more_
     *
     * @param events _more_
     * @param content _more_
     *
     * @return _more_
     */
    public static String jsLink(String events, String content) {
        return jsLink(events, content, "");
    }


    /**
     * _more_
     *
     * @param events _more_
     * @param content _more_
     * @param extra _more_
     *
     * @return _more_
     */
    public static String jsLink(String events, String content, String extra) {
        return "<a href=\"javascript:noop()\" " + events + " " + extra + ">"
               + content + "</a>";
    }


    /**
     * _more_
     *
     * @param s _more_
     *
     * @return _more_
     */
    public static String script(String s) {
        return "<script type=\"text/JavaScript\">" + s + "</script>\n";
    }


    public static String call(String function,String args) {
        return function+"(" + args+");\n";
    }

    /**
     * _more_
     *
     * @param jsUrl _more_
     *
     * @return _more_
     */
    public static String importJS(String jsUrl) {
        return "<script src=\"" + jsUrl + "\"></script>\n";
    }




    /** _more_ */
    static int tabCnt = 0;

    /**
     * _more_
     *
     * @param titles _more_
     * @param contents _more_
     * @param skipEmpty _more_
     *
     * @return _more_
     */
    public static String makeTabs(List titles, List contents, boolean skipEmpty) {
        return makeTabs(titles, contents, skipEmpty, "tabcontent");
    }

    /**
     * _more_
     *
     * @param titles _more_
     * @param contents _more_
     * @param skipEmpty _more_
     * @param tabContentClass _more_
     *
     * @return _more_
     */
    public static String makeTabs(List titles, List contents, boolean skipEmpty,
                           String tabContentClass) {
        return makeTabs(titles, contents, skipEmpty, tabContentClass,
                        "tabcontents");
    }

    /**
     * _more_
     *
     * @param titles _more_
     * @param contents _more_
     * @param skipEmpty _more_
     * @param tabContentClass _more_
     * @param wrapperClass _more_
     *
     * @return _more_
     */
    public static String makeTabs(List titles, List contents, boolean skipEmpty,
                           String tabContentClass, String wrapperClass) {

        String id  = "tab_" + (tabCnt++);
        String ids = "tab_" + (tabCnt++) + "_ids";
        StringBuffer titleSB =
            new StringBuffer(
                "<table cellspacing=\"0\" cellpadding=\"0\"><tr>");

        titleSB = new StringBuffer("");
        StringBuffer contentSB = new StringBuffer();
        StringBuffer jsSB      = new StringBuffer("var " + ids + "=[");
        boolean      didone    = false;
        for (int i = 0; i < titles.size(); i++) {
            String title   = titles.get(i).toString();
            String content = contents.get(i).toString();
            if (skipEmpty && (content.length() == 0)) {
                continue;
            }
            String tabId = id + "_" + i;
            contentSB.append("\n");
            contentSB.append(
                HtmlUtil.div(
                    content,
                    HtmlUtil.cssClass(tabContentClass)
                    + HtmlUtil.id("content_" + tabId)
                    + HtmlUtil.style("display:block;visibility:" + ( !didone
                    ? "visible"
                    : "hidden"))));
            String link = HtmlUtil.href("javascript:" + id + ".toggleTab("
                                        + HtmlUtil.squote(tabId)
                                        + ")", title);
            //            titleSB.append("<td>\n");
            titleSB.append(HtmlUtil.span(link,
                                         HtmlUtil.cssClass("tabtitle")
                                         + HtmlUtil.id("title_" + tabId)));
            //            titleSB.append("\n</td>\n");
            if (didone) {
                jsSB.append(",");
            }
            jsSB.append(HtmlUtil.squote(tabId));
            didone = true;
        }
        jsSB.append("];\n");

        //        titleSB.append("</tr></table>");
        return HtmlUtil.script(jsSB.toString())
               + HtmlUtil.div(
                   titleSB.toString(),
                   HtmlUtil.cssClass("tabtitles")) + HtmlUtil.div(
                       contentSB.toString(),
                       HtmlUtil.cssClass(wrapperClass)) + HtmlUtil.script(
                           "var " + id + "=new Tab(" + ids + ");\n");
    }


    private static String blockHideImageUrl;
    private static String blockShowImageUrl;

    public static void setHideShowImage(String hideImg, String showImg) {
        blockHideImageUrl = hideImg;
        blockShowImageUrl = showImg;
    }




    /**
     * _more_
     *
     * @param id _more_
     * @param label _more_
     * @param content _more_
     * @param visible _more_
     *
     * @return _more_
     */
    public static String makeShowHideBlock(String label,
                                           String content, boolean visible) {
        return makeShowHideBlock(label, content, visible,
                                 "class=\"pagesubheading\"");
    }

    /**
     * _more_
     *
     * @param label _more_
     * @param content _more_
     * @param visible _more_
     * @param headerExtra _more_
     *
     * @return _more_
     */
    public static String makeShowHideBlock(String label,
                                    String content, boolean visible,
                                    String headerExtra) {
        return HtmlUtil.makeShowHideBlock(label,content,visible, headerExtra, HtmlUtil.cssClass("block"), blockHideImageUrl,blockShowImageUrl);
    }





    public static String makeShowHideBlock(String label,
                                           String content, 
                                           boolean visible,
                                           String headerExtra,
                                           String blockExtra) {
        return HtmlUtil.makeShowHideBlock(label,content,visible, headerExtra, blockExtra,blockHideImageUrl,blockShowImageUrl);
    }





    /** _more_ */
    public static int blockCnt = 0;


    public static String makeShowHideBlock(String label,
                                    String content, boolean visible,
                                    String headerExtra,
                                           String blockExtra,
                                    String hideImg,
                                    String showImg) {
        String       id      = "block_" + (blockCnt++);
        StringBuffer sb      = new StringBuffer();
        String link =
            HtmlUtil.jsLink(HtmlUtil.onMouseClick("toggleBlockVisibility('"
                + id + "','" + id + "img','" + hideImg + "','" + showImg
                + "')"), HtmlUtil.img(visible
                                      ? hideImg
                                      : showImg, "",
                                          " id='" + id
                                          + "img' ") + HtmlUtil.space(1)
                                              + label, HtmlUtil.cssClass(
                                                  "pagesubheadinglink"));

        //        sb.append(RepositoryManager.tableSubHeader(link));
        sb.append("<div  " + blockExtra+">");
        sb.append(HtmlUtil.div(link, headerExtra));
        sb.append("<div " + HtmlUtil.cssClass("hideshowblock") + HtmlUtil.id(id)
                  + HtmlUtil.style("display:block;visibility:visible") +">");
        if ( !visible) {
            sb.append(HtmlUtil.script(HtmlUtil.call("hide",HtmlUtil.squote("id"))));
        }

        sb.append(content.toString());
        sb.append("</div>");
        sb.append("</div>");
        return sb.toString();
    }



}

