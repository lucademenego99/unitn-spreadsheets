import SpreadsheetsService from "./spreadsheets-service.js";

/**
 * Class exposing the unitn-spreadsheets, which constructs the spreadsheets table while getting all relevant
 * information from the API.
 *
 * The class is self-contained, and it can be integrated in any HTML page just by importing it in a module script
 * and instantiating it.
 */
export default class UnitnSpreadsheets {

    /**
     * Class constructor
     * @param parentHTMLElement element that will become the parent of the unitn-spreadsheets
     */
    constructor(parentHTMLElement) {
        // Update interval in milliseconds: every UPDATE_INTERVAL milliseconds the UI will be updated with the most recent changes
        this.UPDATE_INTERVAL = 1000;

        // Create the HTML fragment from the unitn-spreadsheets template
        this.document = document.getElementById("unitn-spreadsheets-template").content.cloneNode(true);

        // Get the spreadsheets options from the API (rows and columns)
        SpreadsheetsService.getSpreadsheetsOptions().then((spreadsheetsOptions) => {
            // Set default timestamp
            this.timestamp = 0;

            // Save information about rows and columns
            this.columns = spreadsheetsOptions.columns;
            this.rows = spreadsheetsOptions.rows;

            // Generate the table representing the spreadsheets
            const table = this.generateSpreadsheetsTable();

            // Append the constructed table to the template
            this.document.getElementById("spreadsheets-container").appendChild(table);

            // Append the template into the document
            parentHTMLElement.appendChild(this.document);

            // Get the current global state of the spreadsheet, and update the cells if necessary
            // Do it every UPDATE_INTERVAL seconds
            setInterval(() => {
                SpreadsheetsService.getSpreadsheetsState(this.timestamp).then(result => {
                    if (this.timestamp !== result.timestamp) {
                        this.timestamp = result.timestamp;
                        this.updateUI(result.cells, false);
                    }
                });
            }, this.UPDATE_INTERVAL);

            // Add event listeners on the formula text field
            this.setFormulaEventListeners();
        });
    }

    /**
     * Given a list of cells, update the UI based on their value
     * @param cells list of cells to update
     * @param updateCurrentCell whether the currently selected cell should be updated or not
     *                          this is especially useful to prevent selected cells to be automatically updated
     */
    updateUI(cells, updateCurrentCell = true) {
        for (let cell of cells) {
            const el = document.getElementById(cell.id);
            el.setAttribute("formula", cell.formula);
            // Update the value only if the user is not modifying the cell
            if (this.currentCell == null || cell.id !== this.currentCell.id || (cell.id === this.currentCell.id && updateCurrentCell)) {
                el.firstChild.value = cell.value;
            }
        }
    }

    /**
     * Dynamically generate the spreadsheets table based on the gotten information about rows and columns
     * @returns {HTMLTableElement} Table representing the spreadsheets
     */
    generateSpreadsheetsTable() {
        // Create main table
        const table = document.createElement("table");
        table.id = "main-table";

        /**
         * HEADER TR GENERATION
         */

        const header = document.createElement("tr");
        header.id = "table-header";

        // Create TDs
        const emptyTd = document.createElement("td");
        emptyTd.id = "empty-td";

        header.appendChild(emptyTd);

        for (let i = 0; i < this.columns.length; i++) {
            const td = document.createElement("td");
            td.classList.add("headertd");
            td.innerText = this.columns[i];
            header.appendChild(td);
        }

        table.appendChild(header);

        /**
         * BODY TR GENERATION
         */

        for (let i = 0; i < this.rows; i++) {
            const tr = document.createElement("tr");
            tr.classList.add("bodytr");

            // Generate ID TD
            const td = document.createElement("td");
            td.classList.add("idtd");
            td.innerText = "i+1";
            tr.appendChild(td);

            // Generate main TDs
            for (let j = 0; j < this.columns.length; j++) {
                const td = document.createElement("td");
                console.log("Generating cell " + (this.columns[j] + (i+1)));
                td.tabIndex = 0;
                td.id = this.columns[j] + (i+1);
                td.setAttribute("formula", "");
                td.classList.add(..."bodytd cell drawn-border".split(" "));

                const input = document.createElement("input");
                input.type = "text";
                input.classList.add("table-input");
                td.appendChild(input);

                this.setEventListeners(td);

                tr.appendChild(td);
            }

            table.appendChild(tr);
        }

        return table;
    }

    /**
     * If there is a currently selected cell, deactivate it, evaluate its result and update the spreadsheets
     * @param event formula focusout or cell focusin event
     */
    async updateCells(event) {
        if (this.currentCell != null) {
            this.currentCell.classList.remove("active");
            const cellsUpdate = await SpreadsheetsService.evaluateCell(this.currentCell);
            if (cellsUpdate.cells != null)
                this.updateUI(cellsUpdate.cells);
            else
                this.currentCell.firstChild.value = "ERROR";
            document.getElementById("formula").value = "";
            this.currentCell = null;
        }
    }

    /**
     * Set event listeners for the formula text field: focusout, input and Enter keypress
     */
    setFormulaEventListeners() {
        // Get the formula text field
        const formulaElement = document.getElementById("formula");

        // On focus out, update the cells based on this.currentCell
        formulaElement.addEventListener("focusout", async (event) => {
            document.getElementById("input-cell-name").innerText = "xx";
            await this.updateCells(event);
        });

        // On input, update the corresponding current cell
        formulaElement.addEventListener('input', (event) => {
            if (this.currentCell != null) {
                this.currentCell.firstChild.value = event.target.value;
            } else {
                // Do not allow the user to write inside the formula if no cell is selected
                formulaElement.value = "";
            }
        })

        // On enter, focus out
        formulaElement.addEventListener('keypress', async (event) => {
            if (event.key === "Enter") {
                formulaElement.blur();
            }
        })
    }

    /**
     * Set the event listeners of a specific cell
     * @param cell cell in which we need to add the event listener
     */
    setEventListeners(cell) {
            cell.addEventListener('focusin', async (event) => {
                // Update the cells if this.currentCell is not null
                await this.updateCells(event);

                // Update the current cell with the newly selected one
                this.currentCell = cell;

                // Set its ID on the formula text field
                document.getElementById("input-cell-name").innerText = event.target.parentElement.getAttribute("id");

                // Set the current cell to active
                event.target.parentElement.classList.add("active");

                // Set the formula in the formula field and in the cell
                const formulaField = document.getElementById("formula");
                const formulaValue = event.target.parentElement.getAttribute("formula");
                formulaField.value = formulaValue;
                event.target.value = formulaValue;

                // Focus the formula text field
                document.getElementById("formula").focus();
            });
    }
}