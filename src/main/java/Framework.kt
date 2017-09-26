// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser

fun json(path: String): JsonArray<JsonObject> =
    Parser::class.java.getResourceAsStream(path)?.let { Parser().parse(it) } as JsonArray<JsonObject>

val databases: List<JsonArray<JsonObject>> =
    listOf(json("/Database1.json"), json("/Database2.json"), json("/Database3.json"))