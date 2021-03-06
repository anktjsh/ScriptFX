/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

/**
 *
 * @author Aniket
 */
public class AngularTab extends SamplesTab {

    public AngularTab() {
        super("AngularJS");
        add("AngularJS Directives", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\" ng-init=\"firstName='John'\">\n"
                + "\n"
                + "<p>The name is <span ng-bind=\"firstName\"></span></p>\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("AngularJS Directives with HTML5", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div data-ng-app=\"\" data-ng-init=\"firstName='John'\">\n"
                + "\n"
                + "<p>The name is <span data-ng-bind=\"firstName\"></span></p>\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("AngularJS Expressions", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\">\n"
                + "<p>My first expression: {{ 5 + 5 }}</p>\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("AngularJS Expressions with Variables", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\">\n"
                + " \n"
                + "<p>Input something in the input box:</p>\n"
                + "<p>Name: <input type=\"text\" ng-model=\"name\"></p>\n"
                + "<p>{{name}}</p>\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("AngularJS Controller", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<p>Try to change the names.</p>\n"
                + "\n"
                + "<div ng-app=\"myApp\" ng-controller=\"myCtrl\">\n"
                + "\n"
                + "First Name: <input type=\"text\" ng-model=\"firstName\"><br>\n"
                + "Last Name: <input type=\"text\" ng-model=\"lastName\"><br>\n"
                + "<br>\n"
                + "Full Name: {{firstName + \" \" + lastName}}\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "<script>\n"
                + "var app = angular.module('myApp', []);\n"
                + "app.controller('myCtrl', function($scope) {\n"
                + "    $scope.firstName= \"John\";\n"
                + "    $scope.lastName= \"Doe\";\n"
                + "});\n"
                + "</script>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Simple Expression", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app>\n"
                + "<p>My first expression: {{ 5 + 5 }}</p>\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Expression without ng-app", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<p>Without the ng-app directive, HTML will display the expression as it is, without solving it.</p>\n"
                + "\n"
                + "<div>\n"
                + "<p>My first expression: {{ 5 + 5 }}</p>\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Expression with Numbers", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\" ng-init=\"quantity=1;cost=5\">\n"
                + "<p>Total in dollar: {{ quantity * cost }}</p>\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Using ng-bind with Numbers", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\" ng-init=\"quantity=1;cost=5\">\n"
                + "<p>Total in dollar: <span ng-bind=\"quantity * cost\"></span></p>\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Expression with Strings", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\" ng-init=\"firstName='John';lastName='Doe'\">\n"
                + "\n"
                + "<p>The full name is: {{ firstName + \" \" + lastName }}</p>\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Using ng-bind with Strings", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\" ng-init=\"firstName='John';lastName='Doe'\">\n"
                + "\n"
                + "<p>The full name is: <span ng-bind=\"firstName + ' ' + lastName\"></span></p>\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Expression with Objects", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\" ng-init=\"person={firstName:'John',lastName:'Doe'}\">\n"
                + "\n"
                + "<p>The name is {{ person.lastName }}</p>\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Using ng-bind with Objects", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\" ng-init=\"person={firstName:'John',lastName:'Doe'}\">\n"
                + "\n"
                + "<p>The name is <span ng-bind=\"person.lastName\"></span></p>\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Expression with Arrays", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\" ng-init=\"points=[1,15,19,2,40]\">\n"
                + "\n"
                + "<p>The third result is {{ points[2] }}</p>\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
        add("Using ng-bind with Arrays", "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js\"></script>\n"
                + "<body>\n"
                + "\n"
                + "<div ng-app=\"\" ng-init=\"points=[1,15,19,2,40]\">\n"
                + "\n"
                + "<p>The third result is <span ng-bind=\"points[2]\"></span></p>\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>\n"
                + "");
//        add("", "");
//        add("", "");
//        add("", "");
//        add("", "");
//        add("", "");
//        add("", "");
//        add("", "");
//        add("", "");
//        add("", "");
//        add("", "");
//        add("", "");

    }

}
