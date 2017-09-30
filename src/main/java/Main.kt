// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject

fun main(args: Array<String>) {
    println(databases)
}

fun metrics() {
    var userDatabase = JsonArray<JsonObject>()

    for (database in databases) {
        userDatabase.addAll(database)
    }

    var hosts = mutableListOf<String>()

    for (user in userDatabase) {
        var email = user["email"] as? String

        if (email != null) {
            var host = email.substringAfter("@")

            if (!hosts.contains(host)) {
                hosts.add(host)
            }
        }
    }
}

data class HostInfo(val count: Int, val age: Int)

fun hostInfo(database: JsonArray<JsonObject>, host: String): HostInfo {
    var count = 0
    var age = 0

    for (user in database) {

        val email = user["email"] as? String

        if (email != null) {
            val userHost = email.substringAfter("@")
            val userAge = user["age"] as? Int

            if (userAge != null && userHost == host) {
                count += 1
                age += userAge
            }
        }
    }

    return HostInfo(count, age / count)
}