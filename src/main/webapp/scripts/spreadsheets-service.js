/**
 * Class used to interact with the spreadsheets API
 * It provides static functions to retrieve the current state/configuration of the spreadsheets, or to evaluate cells
 */
export default class SpreadsheetsService {
    /**
     * Get the current global state of the spreadsheet
     * @returns {Promise<any>} Promise to teh spreadsheet's state
     */
    static getSpreadsheetsState(timestamp) {
        const request = "spreadsheets-state" + (timestamp !== 0 ? ("?timestamp=" + timestamp) : "")
        return fetch(request).then(response => response.json());
    }

    /**
     * Get information about the spreadsheets options by calling the API
     * @returns {Promise<any>} a promise to an object containing rows and columns information
     */
    static getSpreadsheetsOptions() {
        return fetch('spreadsheets-options')
            .then((response) => response.json());
    }

    /**
     * Evaluate the current cell by calling the API
     * @returns {Promise<any>} Promise to information about updated cells
     */
    static evaluateCell(currentCell) {
        // Prepare the parameters to send to the API
        const parameters = {
            id: currentCell.getAttribute("id"),
            formula: currentCell.firstChild.value
        };

        // Generate the body in www-form-urlencoded form
        let formBody = [];
        for (let property in parameters) {
            formBody.push(encodeURIComponent(property) + "=" + encodeURIComponent(parameters[property]));
        }
        formBody = formBody.join("&");

        // Perform the POST call - the result is in JSON
        return fetch("spreadsheets", {
            method: "POST",
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
            },
            body: formBody
        }).then(response => response.json());
    }
}