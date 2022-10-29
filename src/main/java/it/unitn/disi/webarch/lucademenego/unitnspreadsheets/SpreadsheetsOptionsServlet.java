package it.unitn.disi.webarch.lucademenego.unitnspreadsheets;

import it.unitn.disi.webarch.lucademenego.unitnspreadsheets.core.Cell;
import it.unitn.disi.webarch.lucademenego.unitnspreadsheets.core.SSEngine;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "SpreadsheetsOptionsServlet", value = "/spreadsheets-options")
public class SpreadsheetsOptionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder columns = new StringBuilder("[");
        SSEngine engine = SSEngine.getSSEngine();
        for (int i = 0; i < engine.getColumns().length; i++) {
            columns.append("\"").append(engine.getColumns()[i]).append("\"");
            if (i < engine.getColumns().length - 1) {
                columns.append(",");
            }
        }
        columns.append("]");

        String json = "{" + "\"columns\": " + columns +
                "}";

        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().print(json);
    }
}
