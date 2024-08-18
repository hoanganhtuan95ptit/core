package com.simple.translate.entities

enum class TranslateState(val value: Int) {

    READY(1),
    NOT_READY(0),
    NO_SUPPORT(-1)
}

enum class TranslateProvider(val value: String) {

    ONLINE("ONLINE"),
    OFFLINE("OFFLINE");
}
