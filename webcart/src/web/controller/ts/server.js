"use strict";
function processRequest(request, response) {
    console.log("Request URL: " + request.url.toString());
    console.log("Request Header Information: " + request.headers.toString());
    response.writeHead(200, { "Content-Type": "text/plain" });
    response.end("Hello, World!");
}
exports.processRequest = processRequest;
var fs = require("fs");
function processRequestReadFromFileAnonymous(request, response) {
    console.log("Request URL: " + request.url.toString());
    console.log("Request Header Information: " + request.headers.toString());
    fs.readFile("server.js", "utf8", function (error, data) {
        response.writeHead(200, { "Content-Type": "text/plain" });
        if (error) {
            response.write("Could not open file for reading!");
        }
        else {
            response.write(data);
            response.end();
        }
    });
}
exports.processRequestReadFromFileAnonymous = processRequestReadFromFileAnonymous;
function processRequestReadFileError(request, response) {
    fs.readFile("server.js", "utf8", function (error, data) {
        writeFileToStreamError(error, data, response);
    });
}
exports.processRequestReadFileError = processRequestReadFileError;
function writeFileToStreamError(error, data, response) {
    response.writeHead(200, { "Content-Type": "text/plain" });
    if (error) {
        response.write("Could not open file for reading!");
    }
    else {
        response.write(data);
        response.end();
    }
}
