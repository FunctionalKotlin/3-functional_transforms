// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject

fun main(args: Array<String>) {
    metrics()
}

fun metrics() {
    var userDatabase = JsonArray<JsonObject>()

    for (database in databases) {
        userDatabase.addAll(database)
    }

    val hosts: List<String?> = userDatabase
        .map { (it["email"] as? String)?.substringAfter("@") }
        .filter { it != null }

    var hostsInfo = mutableListOf<HostInfo>()

    for (host in hosts) {
        hostsInfo.add(hostInfo(userDatabase, host))
    }

    for (i in 0 until hosts.count()) {
        println("Host: ${hosts[i]}")
        println("  - Count: ${hostsInfo[i].count} users")
        println("  - Average age: ${hostsInfo[i].age} years old")
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