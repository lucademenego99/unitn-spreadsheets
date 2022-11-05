package it.unitn.disi.webarch.lucademenego.unitnspreadsheets;

import it.unitn.disi.webarch.lucademenego.unitnspreadsheets.core.SSEngine;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

/**
 * Get information about the state of the spreadsheet, i.e. rows and columns
 */
@WebServlet(name = "SpreadsheetsOptionsServlet", value = "/spreadsheets-options")
public class SpreadsheetsOptionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Construct the JSON for the columns array
        StringBuilder columns = new StringBuilder("[");
        SSEngine engine = SSEngine.getSSEngine();
        for (int i = 0; i < engine.getColumns().length; i++) {
            columns.append("\"").append(engine.getColumns()[i]).append("\"");
            if (i < engine.getColumns().length - 1) {
                columns.append(",");
            }
        }
        columns.append("]");

        String json = "{" + "\"columns\": " + columns + ",\"rows\":" + engine.getNrows() + "}";

        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().print(json);
    }
}
