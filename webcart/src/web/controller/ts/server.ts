///<reference path="../../../../lib/typings/node/node.d.ts"/>
/**
 * <p>Project: MIT Liberal Project</p>
 * <p>Description: server</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Liberal Co., Ltd.</p>
 *
 * @author <cendey@126.com>
 * @since 9/2/2016
 * @version 1.0
 */
import http = require("http");
export function processRequest(request: http.IncomingMessage, response: http.ServerResponse): void {
    console.log("Request URL: " + request.url.toString());
    console.log("Request Header Information: " + request.headers.toString());
    response.writeHead(200, {"Content-Type": "text/plain"});
    response.end("Hello, World!");
}

import fs = require("fs");
import ErrnoException = NodeJS.ErrnoException;
export function processRequestReadFromFileAnonymous(request: http.IncomingMessage, response: http.ServerResponse): void {
    console.log("Request URL: " + request.url.toString());
    console.log("Request Header Information: " + request.headers.toString());
    fs.readFile("server.js", "utf8", (error, data) => {
        response.writeHead(200, {"Content-Type": "text/plain"});
        if (error) {
            response.write("Could not open file for reading!");
        } else {
            response.write(data);
            response.end();
        }
    });
}

export function processRequestReadFileError(request: http.IncomingMessage, response: http.ServerResponse): void {
    fs.readFile("server.js", "utf8", (error, data): void => {
        writeFileToStreamError(error, data, response);
    });
}

function writeFileToStreamError(error: ErrnoException, data: any, response: http.ServerResponse): void {
    response.writeHead(200, {"Content-Type": "text/plain"});
    if (error) {
        response.write("Could not open file for reading!");
    } else {
        response.write(data);
        response.end();
    }
}
