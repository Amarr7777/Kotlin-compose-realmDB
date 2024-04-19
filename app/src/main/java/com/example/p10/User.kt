package com.example.p10

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class User:RealmObject {
    @PrimaryKey var id: ObjectId = ObjectId()
    var name: String = ""
    var email: String= ""
    var phone: String= ""
}