package com.hadi.searchablespinner.model

data class User(
    var name:String,
    var email:String,
    var age:String,
    var score:String
) {
    override fun toString(): String {
        return name;
    }
}