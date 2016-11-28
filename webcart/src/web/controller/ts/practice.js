"use strict";
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
///<reference path="../../../../lib/typings/jquery/jquery.d.ts"/>
///<reference path="../../../../lib/typings/backbone/backbone.d.ts"/>
///<reference path="../../../../lib/typings/angularjs/angular.d.ts"/>
///<reference path="../../../../lib/typings/node/node.d.ts"/>
var Note = (function (_super) {
    __extends(Note, _super);
    function Note() {
        _super.apply(this, arguments);
    }
    Note.prototype.initialize = function () {
        alert("Note Model JavaScript Initialize");
    };
    Note.prototype.author = function () {
        return "developer";
    };
    Note.prototype.coordinates = function () {
        return 0;
    };
    Note.prototype.allowedToEdit = function (account) {
        return true;
    };
    return Note;
}(Backbone.Model));
$(document).ready(function () {
    var note = new Note();
});
var phonecatService = angular.module("phonecatServices", ["ngResource"]);
phonecatService.factory("Phone", [
    "$resource",
    function ($resource) {
        return $resource("phones/:phoneId.json", {}, {
            query: {
                method: "Get",
                params: {
                    phoneId: "phones"
                },
                isArray: true
            }
        });
    }
]);
var phonecatApp = angular.module("phonecatApp", []);
var PhoneListCtrl = (function () {
    function PhoneListCtrl($scope, $http, Phone) {
        this.myScope = $scope;
        this.myScope.phones = Phone.query();
        $scope.orderProb = "age";
        _.bindAll(this, "getPhoneSuccess");
    }
    PhoneListCtrl.prototype.getPhoneSuccess = function (data) {
        this.myScope.phones = data;
    };
    return PhoneListCtrl;
}());
var http = require("http");
var server = require("./server");
var port = process.env.port || 8080;
http.createServer(server.processRequest).listen(port);
http.createServer(server.processRequestReadFromFileAnonymous).listen(port);
