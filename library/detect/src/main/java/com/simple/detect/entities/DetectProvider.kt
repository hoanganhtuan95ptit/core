package com.simple.detect.entities

enum class DetectState(val value: Int) {

    READY(1),
    NOT_READY(0),
    NO_SUPPORT(-1)
}

enum class DetectProvider(val value: String) {

    ONLINE("ONLINE"),
    OFFLINE("OFFLINE");
}
