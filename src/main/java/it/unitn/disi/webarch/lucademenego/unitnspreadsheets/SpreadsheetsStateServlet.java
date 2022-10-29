package it.unitn.disi.webarch.lucademenego.unitnspreadsheets;

import it.unitn.disi.webarch.lucademenego.unitnspreadsheets.core.Cell;
import it.unitn.disi.webarch.lucademenego.unitnspreadsheets.core.SSEngine;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebServlet(name = "SpreadsheetsStateServlet", value = "/spreadsheets-state")
public class SpreadsheetsStateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the client's current timestamp
        String parameterTimestamp = request.getParameter("timestamp");

        Instant clientInstant = null;
        if (parameterTimestamp != null) {
            try {
                clientInstant = Instant.ofEpochSecond(Long.parseLong(parameterTimestamp));
            } catch (NumberFormatException | DateTimeException e) {
                response.setStatus(400);
                response.setContentType("application/json");
                response.getWriter().print("{\"message\": \"Error parsing the provided timestamp\"}");
                return;
            }
        }

        // Get the current engine
        SSEngine engine = SSEngine.getSSEngine();

        long engineSeconds = engine.getLatestUpdateTimestamp().toInstant().getEpochSecond();

        // Build the json from the set of cells
        StringBuilder json = new StringBuilder("{ \"timestamp\": \"" + engineSeconds + "\", \"cells\": [");

        if (clientInstant == null || (clientInstant.getEpochSecond() < engineSeconds)) {
            // Get the current state of the engine
            HashMap<String, Cell> map = engine.getState();

            // Iterate through the map values
            Iterator<Map.Entry<String, Cell>> cellsIterator = map.entrySet().iterator();

            while(cellsIterator.hasNext())
            {
                Map.Entry<String, Cell> entry = cellsIterator.next();
                Cell cell = entry.getValue();
                json.append(cell.toJson()).append(cellsIterator.hasNext() ? ",\n" : "");
            }

            json.append("]}");
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().print(json);
        } else {
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().print("{\"message\": \"No update available\", \"timestamp\": \"" + engineSeconds + "\"}");
        }
    }
}
