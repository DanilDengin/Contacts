package com.example.lessons

class Contact(
    _name: String?,
    _number1: String?,
    _id: String
) {
    var name = _name
    var number1 = _number1
    val id = _id
    var number2: String? = null
    var email1: String? = null
    var email2: String? = null
    var description: String? = null
    var birthday: String? = null

    constructor(
        _name: String?,
        _number1: String?,
        _number2: String?,
        _email1: String?,
        _email2: String?,
        _description: String?,
        _birthday: String?,
        _id: String
    ) : this(_name, _number1, _id) {
        number2 = _number2
        email1 = _email1
        email2 = _email2
        description = _description
        birthday = _birthday

    }
}

