/*
 * This file is a part of the SchemaSpy project (http://schemaspy.sourceforge.net).
 * Copyright (C) 2004, 2005, 2006, 2007, 2008, 2009, 2010 John Currier
 *
 * SchemaSpy is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * SchemaSpy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sourceforge.schemaspy.view;

import java.io.File;
import java.io.IOException;
import net.sourceforge.schemaspy.model.Table;
import net.sourceforge.schemaspy.util.Dot;
import net.sourceforge.schemaspy.util.LineWriter;

public class HtmlTableDiagrammer extends HtmlDiagramFormatter {
    private static HtmlTableDiagrammer instance = new HtmlTableDiagrammer();

    private HtmlTableDiagrammer() {
    }

    public static HtmlTableDiagrammer getInstance() {
        return instance;
    }

    public boolean write(Table table, File diagramDir, LineWriter html) {
        try {
            Dot dot = getDot();
            if (dot == null)
                return false;

            File oneDegreeDotFile = new File(diagramDir, table.getName() + ".1degree.dot");
            File oneDegreeDiagramFile = new File(diagramDir, table.getName() + ".1degree." + dot.getBitmapFormat());
            File oneDegreeVectorFile = new File(diagramDir, table.getName() + ".1degree." + dot.getVectorFormat());
            File twoDegreesDotFile = new File(diagramDir, table.getName() + ".2degrees.dot");
            File twoDegreesDiagramFile = new File(diagramDir, table.getName() + ".2degrees." + dot.getBitmapFormat());
            File twoDegreesVectorFile = new File(diagramDir, table.getName() + ".2degrees." + dot.getVectorFormat());
            File impliedDotFile = new File(diagramDir, table.getName() + ".implied2degrees.dot");
            File impliedDiagramFile = new File(diagramDir, table.getName() + ".implied2degrees." + dot.getBitmapFormat());
            File impliedVectorFile = new File(diagramDir, table.getName() + ".implied2degrees." + dot.getVectorFormat());

            String map = dot.generateDiagram(oneDegreeDotFile, oneDegreeDiagramFile, oneDegreeVectorFile);

            html.write("<br><form action='get'><b>Close relationships");
            if (twoDegreesDotFile.exists()) {
                html.writeln("</b><span class='degrees' id='degrees' title='Detail diminishes with increased separation from " + table.getName() + "'>");
                html.write("&nbsp;within <label for='oneDegree'><input type='radio' name='degrees' id='oneDegree' checked>one</label>");
                html.write("  <label for='twoDegrees'><input type='radio' name='degrees' id='twoDegrees'>two degrees</label> of separation");
                html.write("</span><b>:</b>");
                html.writeln("</form>");
            } else {
                html.write(":</b></form>");
            }
            html.write(map);
            map = null;
            html.writeln("  <div class='diagram'>");
            html.writeln("    <object id='oneDegreeImg' data='../diagrams/" + urlEncode(oneDegreeVectorFile.getName()) + "' type='image/svg+xml'>");
            html.writeln("      <img src='../diagrams/" + urlEncode(oneDegreeDiagramFile.getName()) + "' usemap='#oneDegreeRelationshipsDiagram'>");
            html.writeln("    </object>");
            html.writeln("  </div>");

            if (impliedDotFile.exists()) {
                html.writeln(dot.generateDiagram(impliedDotFile, impliedDiagramFile, impliedVectorFile));
                html.writeln("  <div class='diagram'>");
                html.writeln("    <object id='impliedTwoDegreesImg' data='../diagrams/" + urlEncode(impliedVectorFile.getName()) + "' type='image/svg+xml'>");
                html.writeln("      <img src='../diagrams/" + urlEncode(impliedDiagramFile.getName()) + "' usemap='#impliedTwoDegreesRelationshipsDiagram'>");
                html.writeln("    </object>");
                html.writeln("  </div>");
            } else {
                impliedDotFile.delete();
                impliedDiagramFile.delete();
            }
            if (twoDegreesDotFile.exists()) {
                html.writeln(dot.generateDiagram(twoDegreesDotFile, twoDegreesDiagramFile, twoDegreesVectorFile));
                html.writeln("  <div class='diagram'>");
                html.writeln("    <object id='twoDegreesImg' data='../diagrams/" + urlEncode(twoDegreesVectorFile.getName()) + "' type='image/svg+xml'>");
                html.writeln("      <img src='../diagrams/" + urlEncode(twoDegreesDiagramFile.getName()) + "' usemap='#twoDegreesRelationshipsDiagram'>");
                html.writeln("    </object>");
                html.writeln("  </div>");
            } else {
                twoDegreesDotFile.delete();
                twoDegreesDiagramFile.delete();
            }
        } catch (Dot.DotFailure dotFailure) {
            System.err.println(dotFailure);
            return false;
        } catch (IOException ioExc) {
            ioExc.printStackTrace();
            return false;
        }

        return true;
    }
}
