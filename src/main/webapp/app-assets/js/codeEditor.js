// var isRunning = false;

var tabButtonx = document.getElementById('tab1button');
tabButtonx.classList.add('highlightedButton');

function openTab(tabName) {
    // Hide all tab contents
    var tabContents = document.getElementsByClassName('tab-content');
    var tabButtons = document.getElementsByClassName('tab');
    var tabButton = document.getElementById(tabName + 'button');


    // highlightedButton
    for (var i = 0; i < tabContents.length; i++) {
        tabContents[i].style.display = 'none';
        tabButtons[i].classList.remove('highlightedButton');
    }

    // Show the selected tab content
    var tabContent = document.getElementById(tabName);
    tabContent.style.display = 'block';

    tabButton.classList.add('highlightedButton');

    if (tabName == 'tab2') {
        if (customDbId == ''){
            getUserDBInfo();
        } else {
            getCustomDBInfo(customDbId);
            getCustomDBSchema(customDbId);
        }

    }

    if (tabName == 'tab3') {
        getTaskStatInfo();
    }

}

function addTextToTerminal(text) {
    const paragraph = document.getElementById('secondaryStatusDisplay');
    var beforeAddingNewContent = paragraph.innerText;
    // console.log("Inside addTextToTerminal, before adding others: " + beforeAddingNewContent);

    var adding = "Hackathonx> " + text + "\n";
    var afterAdding = beforeAddingNewContent + adding;
    paragraph.innerText = afterAdding
    // console.log("Inside addTextToTerminal, after adding others: " +afterAdding);
}

function showLoadingModal() {
    // $('#loadingModal').modal('show');
}

// Hide loading modal
function hideLoadingModal() {
    // $('#loadingModal').modal('hide');
}




function hideCursor() {
    const terminalCursor = document.getElementById('terminalCursor');
    terminalCursor.style.display = 'none';
}

function showCursor() {
    const terminalCursor = document.getElementById('terminalCursor');
    terminalCursor.style.display = 'block';
}


function disableButtons() {
    const submitButton = document.getElementById('submitButton');
    const clearButton = document.getElementById('clearButton');
    hideCursor();
    submitButton.disabled = true;
    clearButton.disabled = true;

    addTextToTerminal("Code submitted. Please wait for results ...");
    // isRunning = true;
    // setInterval(addDot, 1000);


}

function enableButtons() {
    const submitButton = document.getElementById('submitButton');
    const clearButton = document.getElementById('clearButton');

    submitButton.disabled = false;
    clearButton.disabled = false;

    showCursor();
}

function postDataUsingjQuery(url, codeSubmitDto) {

    console.log(codeSubmitDto);
    $.ajax({
        url: url,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(codeSubmitDto),
        success: function (response) {
            console.log(response.message);
            addTextToTerminal(response.message);
            // isRunning = false;
            enableButtons();
            hideLoadingModal();
            getTaskStatInfo();

            var textToFind = "This task is now complete."

            if (response.message.includes(textToFind)) {
                // Redirect to the "/tasks" URL
                console.log("I show redirect you to /tasks now");

                setTimeout(function () {
                    window.location.href = "/tasks";
                }, 2000);
            }
        },
        error: function (response) {
            console.error('Error xxx:', response.message);
            addTextToTerminal(response.message);
            // isRunning = false;
            enableButtons();
            hideLoadingModal();
            getTaskStatInfo();
        }
    });


}

function submitCode() {
    showLoadingModal();
    disableButtons();

    const codeEditorContent = document.getElementById("code-editor");
    const content = codeEditorContent.value;
    const codeSubmitDto = {"task": taskId, "sql": content, "hackathonId": hackID};

    postDataUsingjQuery("/task/editor/submit", codeSubmitDto);

}

function submitStartTime(){
    const codeSubmitStartDto = {"task": taskId, "hackathonId": hackID};
    // postDataUsingjQuery("/task/editor/submit/start", codeSubmitStartDto);
    console.log("submitting start time");
    $.ajax({
        url: "/task/editor/submit/start",
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(codeSubmitStartDto),
        success: function (response) {
            console.log(response.message);
            addTextToTerminal(response.message);

        },
        error: function (response) {
            console.error('Error xxx:', response.message);
            addTextToTerminal(response.message);
            window.location.href = "/tasks";

        }
    });

}


// JavaScript code goes here
document.addEventListener('DOMContentLoaded', function () {
    // Get the input element and the clear button
    const textInput = document.getElementById('code-editor');
    const clearButton = document.getElementById('clearButton');

    // Add an event listener to the button
    clearButton.addEventListener('click', function () {
        // Clear the text input
        textInput.value = '';

        addTextToTerminal("Code editor cleared.");
    });
});


document.addEventListener('DOMContentLoaded', function () {
    const buttonToClick = document.getElementById('sidebarToggle');
    buttonToClick.click();

    var textarea = document.getElementById('code-editor');
    var submitButton = document.getElementById('submitButton');
    submitButton.disabled = true;
// Add an input event listener to the textarea
    textarea.addEventListener('input', function () {
        // Check if the textarea content is empty or contains only spaces and newlines
        if (textarea.value.trim() === '') {
            submitButton.disabled = true;
        } else {
            submitButton.disabled = false;
        }
    });

});


function customDbSchema(data) {
    var dbNamePlace = document.getElementById('table-container-schema-name');
    dbNamePlace.textContent = 'Database Schema: ';

    // Clear the existing table rows
    var userDbTableBody = document.getElementById('table-container-schema');

    while (userDbTableBody.firstChild) {
        userDbTableBody.removeChild(userDbTableBody.firstChild);
    }

    if (data.tables.length == 0) {
        var tableHeading = document.createElement('h5');
        tableHeading.setAttribute("style", "font-family: 'Courier New', monospace; font-size: 13px;");
        tableHeading.textContent = "No tables found!";
        userDbTableBody.appendChild(tableHeading);
        console.log("No tables found.");

    } else {

        // Rebuild the table with the updated data
        console.log("Rebuilding tables with updated data.");
        data.tables.forEach(function (item) {

            console.log("Rebuilding tables with updated data. Inside: data.tables.forEach");

            var tableHeading = document.createElement('h5');
            tableHeading.setAttribute("style", "font-family: 'Courier New', monospace; font-size: 13px;");

            var spaceLine = document.createElement('hr');
            userDbTableBody.appendChild(spaceLine);


            tableHeading.textContent = 'Table Name:' + item.tableName;
            userDbTableBody.appendChild(tableHeading);

            var table = document.createElement('table');
            table.setAttribute("style", "font-family: 'Courier New', monospace; font-size: 12px;");
            table.setAttribute('class', 'table table-striped table-hover');

            var thead = document.createElement('thead');

            var tr = document.createElement('tr');

            var th1 = document.createElement('th');
            var th2 = document.createElement('th');
            var th3 = document.createElement('th');
            var th4 = document.createElement('th');

            th1.textContent = "Column Name";
            th2.textContent = "Data Type";
            th3.textContent = "Is Nullable?";
            th4.textContent = "Is Primary Key?";
            tr.appendChild(th1);
            tr.appendChild(th2);
            tr.appendChild(th3);
            tr.appendChild(th4);

            thead.appendChild(tr);
            table.appendChild(thead);

            var tbody = document.createElement('tbody');


            if (item.columns.length == 0) {

                var td = document.createElement('td');
                td.setAttribute("colspan", "4");
                td.setAttribute("style", "text-align:center");
                td.textContent = "No Columns to show";
                tbody.appendChild(td);

            } else {

                item.columns.forEach(function (item_col) {
                    var row = document.createElement('tr');
                    var col1 = document.createElement('td');
                    var col2 = document.createElement('td');
                    var col3 = document.createElement('td');
                    var col4 = document.createElement('td');
                    col1.textContent = item_col.columnName;
                    col2.textContent = item_col.dataType;
                    col3.textContent = item_col.isNullable;
                    col4.textContent = item_col.isPrimaryKey;
                    row.appendChild(col1);
                    row.appendChild(col2);
                    row.appendChild(col3);
                    row.appendChild(col4);

                    tbody.appendChild(row);
                });
                table.appendChild(tbody);

            }

            userDbTableBody.appendChild(table);
            console.log("Appended table child named: " + item.tableName);
            console.log(item);

        });
    }
}


function dbInfo(data) {
    var dbNamePlace = document.getElementById('dbNamePlace');
    dbNamePlace.textContent = 'Database Name: ' + data.dbName;

    // Clear the existing table rows
    var userDbTableBody = document.getElementById('user-db-table-body');

    while (userDbTableBody.firstChild) {
        userDbTableBody.removeChild(userDbTableBody.firstChild);
    }

    if (data.tables.length == 0) {
        var tableHeading = document.createElement('h5');
        tableHeading.setAttribute("style", "font-family: 'Courier New', monospace; font-size: 13px;");
        tableHeading.textContent = "No tables found!";
        userDbTableBody.appendChild(tableHeading);
        console.log("No tables found.");

    } else {

        // Rebuild the table with the updated data
        console.log("Rebuilding tables with updated data.");
        data.tables.forEach(function (item) {

            console.log("Rebuilding tables with updated data. Inside: data.tables.forEach");

            var tableHeading = document.createElement('h5');
            tableHeading.setAttribute("style", "font-family: 'Courier New', monospace; font-size: 13px;");

            var spaceLine = document.createElement('hr');
            userDbTableBody.appendChild(spaceLine);


            tableHeading.textContent = 'Table Name:' + item.tableName;
            userDbTableBody.appendChild(tableHeading);

            var table = document.createElement('table');
            table.setAttribute("style", "font-family: 'Courier New', monospace; font-size: 12px;");
            table.setAttribute('class', 'table table-striped table-hover');

            var thead = document.createElement('thead');

            var tr = document.createElement('tr');

            var th1 = document.createElement('th');
            var th2 = document.createElement('th');
            var th3 = document.createElement('th');
            var th4 = document.createElement('th');

            th1.textContent = "Column Name";
            th2.textContent = "Data Type";
            th3.textContent = "Is Nullable?";
            th4.textContent = "Is Primary Key?";
            tr.appendChild(th1);
            tr.appendChild(th2);
            tr.appendChild(th3);
            tr.appendChild(th4);

            thead.appendChild(tr);
            table.appendChild(thead);

            var tbody = document.createElement('tbody');


            if (item.columns.length == 0) {

                var td = document.createElement('td');
                td.setAttribute("colspan", "4");
                td.setAttribute("style", "text-align:center");
                td.textContent = "No Columns to show";
                tbody.appendChild(td);

            } else {

                item.columns.forEach(function (item_col) {
                    var row = document.createElement('tr');
                    var col1 = document.createElement('td');
                    var col2 = document.createElement('td');
                    var col3 = document.createElement('td');
                    var col4 = document.createElement('td');
                    col1.textContent = item_col.columnName;
                    col2.textContent = item_col.dataType;
                    col3.textContent = item_col.isNullable;
                    col4.textContent = item_col.isPrimaryKey;
                    row.appendChild(col1);
                    row.appendChild(col2);
                    row.appendChild(col3);
                    row.appendChild(col4);

                    tbody.appendChild(row);
                });
                table.appendChild(tbody);

            }

            userDbTableBody.appendChild(table);
            console.log("Appended table child named: " + item.tableName);
            console.log(item);

        });
    }
}


function getUserDBInfo() {
    console.log("Getting user database info");
    $.ajax({
        url: "/task/database-status",
        type: "GET",
        dataType: "json",
        success: function (data) {
            // console.log(data);
            dbInfo(data);
        }
    });
    getTaskStatInfo();
}

function getCustomDBInfo(customDbId) {
    console.log("Getting task database info");
    $.ajax({
        url: "/task/database-status/"+customDbId,
        type: "GET",
        dataType: "json",
        success: function (data) {
            console.log(data);
            createTableFromJSON(data);
        }
    });
    getTaskStatInfo();
}
function getCustomDBSchema(customDbId) {
    console.log("Getting custom database info schema");
    $.ajax({
        url: "/task/database-status/schema"+customDbId,
        type: "GET",
        dataType: "json",
        success: function (data) {
            console.log(data);
            customDbSchema(data);
        }
    });
}


function setTaskStatInfo(data){


    var taskStat = "";
    if (data.taskStatus){
        taskStat = "This task is complete.";
    } else {
        taskStat = "This task is incomplete.";
    }

    var taskStatusBottom = document.getElementById('taskStatusBottom');
    taskStatusBottom.textContent = taskStat;


    var taskLeftBottom = document.getElementById('taskLeftBottom');
    taskLeftBottom.textContent = 'You have ' + data.attemptsLeft + '/' + data.maxAllowedAttempts + ' attempts left.';

    var hackathonUserComplete = document.getElementById('hackathonUserComplete');
    hackathonUserComplete.textContent = 'You have completed '+ data.overAllProgress +'% problems of the hackathon.' ;

    var averageAttempts = document.getElementById('averageAttempts');
    averageAttempts.textContent = 'On an average everyone is taking ' + data.averageAttemptsAllUsers + '/' + data.maxAllowedAttempts + ' attempts.';


}
function getTaskStatInfo() {
    console.log("Getting user task info");
    $.ajax({
        url: "/task/task-status/" + taskId,
        type: "GET",
        dataType: "json",
        success: function (data) {
            console.log(data);
            setTaskStatInfo(data);
        }
    });
}

document.getElementById('pane1').style.width = "50%";
document.getElementById('pane2').style.width = "50%";


// document.getElementById('middivider').


$(document).ready(function () {

    document.getElementById('top-guy').style.height = "60vh";
    document.getElementById('bottom-guy').style.height = "35vh";


    let isResizing = false;
    let lastDownX;
    let leftWidth;
    let rightWidth;

    let isResizingHorizon = false;
    let lastDownY;
    let topHeight;
    let bottomHeight;

    console.log("Ready!");


    $('#middivider').mousedown(function (e) {

        console.log("Mouse down mid divider");

        e.preventDefault();
        isResizing = true;
        lastDownX = e.clientX;
        leftWidth = $('#pane1').width();
        rightWidth = $('#pane2').width();
    });

    $('#verdivider').mousedown(function (e) {

        console.log("Mouse down verdivider");

        e.preventDefault();
        isResizingHorizon = true;
        lastDownY = e.clientY;
        topHeight = $('#top-guy').height();
        bottomHeight = $('#bottom-guy').height();
    });

    $(document).mousemove(function (e) {

        if (!isResizing && !isResizingHorizon) return;

        console.log("mousemove");

        if (isResizing) {
            const diff = e.clientX - lastDownX;
            let newLeftWidth = leftWidth + diff;
            let newRightWidth = rightWidth - diff;

            if (newLeftWidth >= 100 && newRightWidth >= 100) {
                console.log(newLeftWidth, newRightWidth);
                document.getElementById('pane1').style.width = newLeftWidth + 'px';
                document.getElementById('pane2').style.width = newRightWidth + 'px';
            }
        } else if (isResizingHorizon) {
            const diff = e.clientY - lastDownY;
            let newTopHeight = topHeight + diff;
            // console.log(topHeight, diff, newTopHeight);
            let newBottomHeight = bottomHeight - diff;
            if (newTopHeight >= 50 && newBottomHeight >= 50) {
                console.log("horizaontal", newBottomHeight, newTopHeight);
                document.getElementById('top-guy').style.height = newTopHeight + 'px';
                document.getElementById('bottom-guy').style.height = newBottomHeight + 'px';
            }
        }

    }).mouseup(function () {
        isResizing = false;
        isResizingHorizon = false;
    });

    submitStartTime();

});


$(function () {

    $("#verdivider").popover('show');

    $("#verdivider").on('click', function () {
        dismissPopover();
    });
});


function dismissPopover() {
    $('#verdivider').popover('dispose');
}

// Set a timeout to dismiss the popover after 2 seconds (5000 milliseconds)
setTimeout(dismissPopover, 2000);


function createTableFromJSON(jsonData) {

    var tableContainer = document.getElementById('table-container');

    jsonData.forEach(function (tableData) {
        console.log("Inside customdb creation for each");
        var hrLine = document.createElement('hr');

        // Create an <h5> element for the table name
        var h5 = document.createElement('h5');
        h5.textContent = "Table Name: " + tableData.tableName;
        h5.setAttribute("style", "font-family: 'Courier New', monospace; font-size: 13px;");


        // Create the table element
        var table = document.createElement('table');
        table.setAttribute("style", "font-family: 'Courier New', monospace; font-size: 12px;");
        table.setAttribute('class', 'table table-striped table-hover');


        // Create the table header (thead)
        var thead = document.createElement('thead');
        var headerRow = thead.insertRow();

        console.log("Inside customdb creation for each: xx1");
        Object.keys(tableData.tableData[0]).forEach(function (key) {
            var th = document.createElement('th');
            th.textContent = key;
            headerRow.appendChild(th);
        });

        // Create the table body (tbody)
        var tbody = document.createElement('tbody');
        tableData.tableData.forEach(function (rowData) {
            var row = tbody.insertRow();
            Object.values(rowData).forEach(function (value) {
                var cell = row.insertCell();
                cell.textContent = value;
            });
        });

        // Append the thead and tbody to the table
        table.appendChild(thead);
        table.appendChild(tbody);

        // Append the <h5> and table to the container

        tableContainer.appendChild(hrLine);
        tableContainer.appendChild(h5);
        tableContainer.appendChild(table);

        console.log("end of append child");
    });

    var dbReloadButton = document.getElementById("dbReloadButton");

// Check if the element exists
    if (dbReloadButton) {
        // Hide the element by setting its display property to "none"
        dbReloadButton.style.display = "none";
    }

    var dbNamePlace = document.getElementById("dbNamePlace");

// Check if the element exists
    if (dbNamePlace) {
        // Append text to the existing content of the element
        dbNamePlace.textContent += customDbName;
    }


}

