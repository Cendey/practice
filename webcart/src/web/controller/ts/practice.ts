///<reference path="../../../../lib/typings/jquery/jquery.d.ts"/>
///<reference path="../../../../lib/typings/backbone/backbone.d.ts"/>
///<reference path="../../../../lib/typings/angularjs/angular.d.ts"/>
///<reference path="../../../../lib/typings/node/node.d.ts"/>
interface INote {
    initialize(): void;
    author(): string;
    coordinates(): number;
    allowedToEdit(account: string): boolean;
}
class Note extends Backbone.Model implements INote {
    initialize(): void {
        alert("Note Model JavaScript Initialize");
    }

    author(): string {
        return "developer";
    }

    coordinates(): number {
        return 0;
    }

    allowedToEdit(account: string): boolean {
        return true;
    }
}

$(document).ready(()=> {
    var note = new Note();
});
var phonecatService = angular.module("phonecatServices", ["ngResource"]);
phonecatService.factory("Phone", [
    "$resource",
    ($resource)=> {
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
interface IPhone {
    age: number;
    id: string;
    imageUrl: string;
    name: string;
    snippet: string;
}

interface IScope {
    phones: IPhone[];
}

class PhoneListCtrl {
    myScope: IScope;

    constructor($scope, $http: ng.IHttpService, Phone) {
        this.myScope = $scope;
        this.myScope.phones = Phone.query();
        $scope.orderProb = "age";
        _.bindAll(this, "getPhoneSuccess");
    }

    getPhoneSuccess(data: any): void {
        this.myScope.phones = data;
    }
}

import http = require("http");
import server = require("./server");
var port = process.env.port || 8080;
http.createServer(server.processRequest).listen(port);
http.createServer(server.processRequestReadFromFileAnonymous).listen(port);
