// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject

fun main(args: Array<String>) {
    metrics()
}

private val JsonObject.host: String?
    get() = (this["email"] as? String)?.substringAfter("@")

fun metrics() {
    var userDatabase = JsonArray<JsonObject>()

    for (database in databases) {
        userDatabase.addAll(database)
    }

    val hosts: List<String> = userDatabase
        .mapNotNull(JsonObject::host)
        .fold(emptyList()) { acc, host ->
            if (acc.contains(host)) acc else acc + host
        }

    val hostsInfo = hosts.map(hostInfo(userDatabase))

    for (i in 0 until hosts.count()) {
        println("Host: ${hosts[i]}")
        println("  - Count: ${hostsInfo[i].count} users")
        println("  - Average age: ${hostsInfo[i].age} years old")
    }
}

data class HostInfo(val count: Int, val age: Int)

fun hostInfo(database: JsonArray<JsonObject>): (String) -> HostInfo = { host ->
    database.fold(HostInfo(0, 0)) { (count, age), user ->
        user.host?.let { userHost ->
            (user["age"] as? Int)
                ?.takeIf { userHost == host }
                ?.let { HostInfo(count + 1, age + it) }
        } ?: HostInfo(count, age)
    }.let { (count, age) ->
        HostInfo(count, age / count)
    }
}