/**
 *
 * Copyright 1997-2005 Unidata Program Center/University Corporation for
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

package ucar.unidata.repository;


import org.w3c.dom.*;


import ucar.unidata.sql.SqlUtil;
import ucar.unidata.ui.ImageUtils;
import ucar.unidata.util.DateUtil;
import ucar.unidata.util.HtmlUtil;
import ucar.unidata.util.IOUtil;
import ucar.unidata.util.Misc;

import ucar.unidata.util.StringBufferCollection;
import ucar.unidata.util.StringUtil;
import ucar.unidata.util.TwoFacedObject;
import ucar.unidata.xml.XmlUtil;


import java.util.Calendar;
import java.util.GregorianCalendar;

import java.io.*;

import java.io.File;
import java.io.InputStream;



import java.net.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;



import java.util.regex.*;

import java.util.zip.*;


/**
 * Class SqlUtil _more_
 *
 *
 * @author IDV Development Team
 * @version $Revision: 1.3 $
 */
public class DateGridOutputHandler extends OutputHandler {



    /** _more_ */
    public static final String OUTPUT_GRID = "calendar.grid";

    public static final String OUTPUT_CALENDAR = "calendar.calendar";


    /**
     * _more_
     *
     * @param repository _more_
     * @param element _more_
     * @throws Exception _more_
     */
    public DateGridOutputHandler(Repository repository, Element element)
            throws Exception {
        super(repository, element);
    }

    /**
     * _more_
     *
     *
     * @param output _more_
     *
     * @return _more_
     */
    public boolean canHandle(String output) {
        return output.equals(OUTPUT_GRID) || output.equals(OUTPUT_CALENDAR);
    }



    /**
     * _more_
     *
     * @param request _more_
     * @param entries _more_
     * @param types _more_
     *
     *
     * @throws Exception _more_
     */
    protected void getOutputTypesForEntries(Request request,
                                            List<Entry> entries, List<OutputType> types)
            throws Exception {
        types.add(new OutputType("Calendar", OUTPUT_CALENDAR));
        types.add(new OutputType("Date Grid", OUTPUT_GRID));
    }



    /**
     * _more_
     *
     * @param request _more_
     * @param entry _more_
     * @param types _more_
     *
     * @throws Exception _more_
     */
    protected void getOutputTypesForEntry(Request request, Entry entry,
                                          List<OutputType> types)
            throws Exception {}


    /**
     * _more_
     *
     * @param request _more_
     * @param group _more_
     * @param subGroups _more_
     * @param entries _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public Result outputGroup(Request request, Group group,
                              List<Group> subGroups, List<Entry> entries)
            throws Exception {
        String       output = request.getOutput();
        StringBuffer sb     = new StringBuffer();
        showNext(request, subGroups, entries, sb);
        entries.addAll(subGroups);
        Result result;
        if(output.equals(OUTPUT_GRID)) {
            result = outputGrid(request, group, entries, sb);
        } else {
            result = outputCalendar(request, group, entries, sb);
        }
        result.putProperty(
            PROP_NAVSUBLINKS,
            getHeader(
                request, output,
                getRepository().getOutputTypesForGroup(
                    request, group, subGroups, entries)));

        return result;
    }




    private Result outputGrid(Request request, Group group,
                              List<Entry> entries, StringBuffer sb)
            throws Exception {
        String       title  = group.getFullName();
        List             types    = new ArrayList();
        List             days     = new ArrayList();
        Hashtable        dayMap   = new Hashtable();
        Hashtable        typeMap  = new Hashtable();
        Hashtable        contents = new Hashtable();

        SimpleDateFormat sdf      = new SimpleDateFormat();
        sdf.setTimeZone(DateUtil.TIMEZONE_GMT);
        sdf.applyPattern("yyyy/MM/dd");
        SimpleDateFormat timeSdf = new SimpleDateFormat();
        timeSdf.setTimeZone(DateUtil.TIMEZONE_GMT);
        timeSdf.applyPattern("HH:mm");
        SimpleDateFormat monthSdf = new SimpleDateFormat();
        monthSdf.setTimeZone(DateUtil.TIMEZONE_GMT);
        monthSdf.applyPattern("MM");
        StringBuffer header = new StringBuffer();
        header.append(HtmlUtil.cols(HtmlUtil.bold(msg("Date"))));
        for (Entry entry : entries) {
            String type = entry.getTypeHandler().getType();
            String day  = sdf.format(new Date(entry.getStartDate()));
            if (typeMap.get(type) == null) {
                types.add(entry.getTypeHandler());
                typeMap.put(type, type);
                header.append(
                    "<td>" + HtmlUtil.bold(entry.getTypeHandler().getLabel())
                    + "</td>");
            }
            if (dayMap.get(day) == null) {
                days.add(new Date(entry.getStartDate()));
                dayMap.put(day, day);
            }
            String       time =
                timeSdf.format(new Date(entry.getStartDate()));
            String       key   = type + "_" + day;
            StringBuffer colSB = (StringBuffer) contents.get(key);
            if (colSB == null) {
                colSB = new StringBuffer();
                contents.put(key, colSB);
            }
            colSB.append(getAjaxLink(request, entry, time, false));
            colSB.append(HtmlUtil.br());
        }

        sb.append(
            "<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" width=\"100%\">");
        days = Misc.sort(days);
        String currentMonth = "";
        for (int dayIdx = 0; dayIdx < days.size(); dayIdx++) {
            Date   date  = (Date) days.get(dayIdx);
            String month = monthSdf.format(date);
            //Put the header in every month
            if ( !currentMonth.equals(month)) {
                currentMonth = month;
                sb.append(
                    HtmlUtil.row(
                        header.toString(),
                        " style=\"background-color:lightblue;\""));
            }

            String day = sdf.format(date);
            sb.append("<tr valign=\"top\">");
            sb.append("<td width=\"5%\">" + day + "</td>");
            for (int i = 0; i < types.size(); i++) {
                TypeHandler  typeHandler = (TypeHandler) types.get(i);
                String       type        = typeHandler.getType();
                String       key         = type + "_" + day;
                StringBuffer cb          = (StringBuffer) contents.get(key);
                if (cb == null) {
                    sb.append("<td>" + HtmlUtil.space(1) + "</td>");
                } else {
                    sb.append("<td>" + cb + "</td>");
                }
            }
            sb.append("</tr>");
        }
        sb.append("</table>");

        Result result = new Result(title, sb);
        return result;

    }



    private Result outputCalendar(Request request, Group group,
                                  List<Entry> entries, StringBuffer sb)
            throws Exception {

        GregorianCalendar cal =
            new GregorianCalendar(DateUtil.TIMEZONE_GMT);

        GregorianCalendar tmp =
            new GregorianCalendar(DateUtil.TIMEZONE_GMT);
        
        int month = tmp.get(tmp.MONTH);
        int today = tmp.get(cal.DAY_OF_MONTH);
        int tmpDay = today;
        while(month == tmp.get(tmp.MONTH)) {
            tmpDay++;
            tmp.set(cal.DAY_OF_MONTH,tmpDay);
        }

        tmp.set(cal.DAY_OF_MONTH,tmpDay-1);
        int lastDay = tmpDay-1;

        cal.clear(cal.DAY_OF_MONTH);
        cal.set(cal.DAY_OF_MONTH,1);
        //        System.err.println("dom: " +        cal.get(cal.DAY_OF_MONTH) +
        //                           "  " + cal.get(cal.DAY_OF_WEEK));  
        sb.append(
            "<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">");

        for (Entry entry : entries) {
        }

        String[]dayNames = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        sb.append("<tr>");
        for(int colIdx=0;colIdx<7;colIdx++) {
            sb.append("<td width=\"14%\" class=\"calheader\">" + dayNames[colIdx] +"</td>");
        }
        sb.append("</tr>");
        int dayCnt = 0;
        int boxCnt = 0;
        int startDow =  cal.get(cal.DAY_OF_WEEK);  
        for(int rowIdx=0;rowIdx<6;rowIdx++) {
            sb.append("<tr>");
            for(int colIdx=0;colIdx<7;colIdx++) {
                boxCnt++;
                String content=HtmlUtil.space(1);
                if(startDow<=boxCnt) {
                    dayCnt++;
                    if(dayCnt<=lastDay) {
                        content = "<table border=0 cellspacing=\"0\" cellpadding=\"3\" width=100%><tr valign=top><td>&nbsp;</td><td align=right class=calday>" + dayCnt+"<br>&nbsp;</td></tr></table>";
                    }
                }
                sb.append("<td class=\"calentry\">" + content +"</td>");
            }
        }

        sb.append("</table>");

        Result result = new Result(group.getFullName(), sb);
        return result;

    }





}

