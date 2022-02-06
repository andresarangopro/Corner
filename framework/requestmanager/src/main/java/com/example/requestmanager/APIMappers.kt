package com.example.requestmanager

fun String.toTitleJson():JsonTitleServer = JsonTitleServer(this)

fun String.toIdJson():JsonIdServer = JsonIdServer(this)