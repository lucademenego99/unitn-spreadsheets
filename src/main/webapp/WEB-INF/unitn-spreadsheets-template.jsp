<%--
  Created by IntelliJ IDEA.
  User: lucademenego
  Date: 10/28/22
  Time: 2:19 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!-- Include the spreadsheets css file -->
<link rel="stylesheet" href="styles/spreadsheets.css">

<!-- Define the template for unitn-spreadsheets -->
<template id="unitn-spreadsheets-template">
     <div id="spreadsheets-container" class="drawn-border">
        <div class="main-card">
            <p id="formula-title">Formula</p>
            <div id="input-container">
                <div id="pre-input">
                    <p id="input-cell-name">xx</p>
                </div>
                <input id="formula" type="text">
            </div>
            <!-- Here the spreadsheet will be appended via Javascript -->
        </div>
    </div>
</template>

