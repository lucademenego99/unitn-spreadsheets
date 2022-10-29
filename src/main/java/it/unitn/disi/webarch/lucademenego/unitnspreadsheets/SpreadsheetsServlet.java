package it.unitn.disi.webarch.lucademenego.unitnspreadsheets;

import it.unitn.disi.webarch.lucademenego.unitnspreadsheets.core.Cell;
import it.unitn.disi.webarch.lucademenego.unitnspreadsheets.core.SSEngine;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

@WebServlet(name = "SpreadsheetsServlet", value = "/spreadsheets")
public class SpreadsheetsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the parameters provided by the user
        String identifier = request.getParameter("id");
        String formula = request.getParameter("formula");

        // Evaluate the new formula
        SSEngine engine = SSEngine.getSSEngine();
        Set<Cell> result = engine.modifyCell(identifier, formula);

        // Build the json from the set of cells
        StringBuilder json = new StringBuilder("{ \"cells\": [");

        Iterator<Cell> cellsIterator = result.iterator();

        while (cellsIterator.hasNext()) {
            Cell cell = cellsIterator.next();
            json.append("{\n");
            json.append("\"value\": ").append(cell.getValue()).append(",\n");
            json.append("\"id\": \"").append(cell.getId()).append("\",\n");
            json.append("\"formula\": \"").append(cell.getFormula()).append("\"\n");
            json.append("}").append(cellsIterator.hasNext() ? ",\n" : "\n");
        }

        json.append("]}");

        // Return it
        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().print(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the parameters provided by the user
        String identifier = request.getParameter("id");
        String formula = request.getParameter("formula");

        // Evaluate the new formula
        SSEngine engine = SSEngine.getSSEngine();
        Set<Cell> result = engine.modifyCell(identifier, formula);

        if (result == null) {
            response.setStatus(400);
            response.setContentType("application/json");
            response.getWriter().print("{\"message\": \"Error - probable circular dependency\"}");
        }

        // Build the json from the set of cells
        StringBuilder json = new StringBuilder("{ \"cells\": [");

        Iterator<Cell> cellsIterator = result.iterator();

        while (cellsIterator.hasNext()) {
            Cell cell = cellsIterator.next();
            json.append("{\n");
            json.append("\"value\": ").append(cell.getValue()).append(",\n");
            json.append("\"id\": \"").append(cell.getId()).append("\",\n");
            json.append("\"formula\": \"").append(cell.getFormula()).append("\"\n");
            json.append("}").append(cellsIterator.hasNext() ? ",\n" : "\n");
        }

        json.append("]}");

        // Return it
        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().print(json);
    }
}
