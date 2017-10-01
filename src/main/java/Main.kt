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

    val hosts: List<String> =
        userDatabase.mapNotNull { (it["email"] as? String)?.substringAfter("@") }

    var uniqueHosts = mutableListOf<String>()

    for (host in hosts) {
        if (!uniqueHosts.contains(host)) {
            uniqueHosts.add(host)
        }
    }

    val hostsInfo = uniqueHosts.map(hostInfo(userDatabase))

    for (i in 0 until uniqueHosts.count()) {
        println("Host: ${uniqueHosts[i]}")
        println("  - Count: ${hostsInfo[i].count} users")
        println("  - Average age: ${hostsInfo[i].age} years old")
    }
}

data class HostInfo(val count: Int, val age: Int)

fun hostInfo(database: JsonArray<JsonObject>): (String) -> HostInfo = { host ->
    database.fold(HostInfo(0, 0)) { (count, age), user ->
        (user["email"] as? String)?.substringAfter("@")?.let { userHost ->
            (user["age"] as? Int)
                ?.takeIf { userHost == host }
                ?.let { HostInfo(count + 1, age + it) }
        } ?: HostInfo(count, age)
    }.let { (count, age) ->
        HostInfo(count, age / count)
    }
}