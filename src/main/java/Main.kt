// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import com.beust.klaxon.JsonObject

fun main(args: Array<String>) {
    metrics()
}

private val JsonObject.host: String?
    get() = (this["email"] as? String)?.substringAfter("@")

fun metrics() {
    val userDatabase = databases.flatten()

    val hosts: List<String> = userDatabase
        .mapNotNull(JsonObject::host)
        .distinct()

    val hostsInfo = hosts.map(hostInfo(userDatabase))

    val result = hosts zip hostsInfo

    for ((host, info) in result) {
        println("Host: $host")
        println("  - Count: ${info.count} users")
        println("  - Average age: ${info.age} years old")
    }
}

data class HostInfo(val count: Int, val age: Int)

fun hostInfo(database: List<JsonObject>): (String) -> HostInfo = { host ->
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