package it.unitn.disi.webarch.lucademenego.unitnspreadsheets;

import it.unitn.disi.webarch.lucademenego.unitnspreadsheets.core.Cell;
import it.unitn.disi.webarch.lucademenego.unitnspreadsheets.core.SSEngine;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Update the spreadsheet by evaluating new formulas
 */
@WebServlet(name = "SpreadsheetsServlet", value = "/spreadsheets")
public class SpreadsheetsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the parameters provided by the user
        String identifier = request.getParameter("id");
        String formula = request.getParameter("formula");

        // Evaluate the new formula
        SSEngine engine = SSEngine.getSSEngine();
        Set<Cell> result = engine.modifyCell(identifier, formula);

        // If there is an error, return 400
        if (result == null) {
            response.setStatus(400);
            response.setContentType("application/json");
            response.getWriter().print("{\"message\": \"Error - probable circular dependency\"}");
            return;
        }

        // Build the json from the set of cells
        StringBuilder json = new StringBuilder("{ \"cells\": [");
        Iterator<Cell> cellsIterator = result.iterator();
        while (cellsIterator.hasNext()) {
            Cell cell = cellsIterator.next();
            json.append(cell.toJson()).append(cellsIterator.hasNext() ? ",\n" : "");
        }
        json.append("]}");

        // Return it
        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().print(json);
    }
}
