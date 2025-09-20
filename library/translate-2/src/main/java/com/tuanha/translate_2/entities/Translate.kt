package com.tuanha.translate_2.entities

import com.simple.state.ResultState

class Translate {

    data class Request(
        val text: String,
        val languageCode: String
    )

    data class Response(
        val text: String,
        val state: ResultState<String>
    )
//
//    data class Info(
//        val name: String,
//        val State: State,
//        val Status: Status
//    )
//
//    enum class State {
//
//        READY,
//        NOT_READY,
//        NO_SUPPORT;
//    }
//
//    enum class Status {
//
//        ONLINE,
//        OFFLINE;
//    }
}
