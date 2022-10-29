<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Unitn-Spreadsheets</title>

    <link rel="stylesheet" href="styles/main.css">
</head>
<body>
<!-- Include the template inside the body -->
<%@include file="unitn-spreadsheets-template.jsp" %>

<!-- Background image -->
<img src="assets/background.jpg" class="background-image" alt="background image">

<!-- App container, with a title and the spreadsheets -->
<div id="app-container">
    <h1 class="title">Unitn Spreadsheets</h1>
</div>

<!-- Footer -->
<div class="bottom-container">
    <p>University of Trento, Web Architectures assignment</p>
</div>

<script type="module">
    import UnitnSpreadsheets from "./scripts/unitn-spreadsheets.js";
    // Generate the spreadsheets
    const spreadsheets = new UnitnSpreadsheets(document.getElementById("app-container"));
</script>
</body>
</html>