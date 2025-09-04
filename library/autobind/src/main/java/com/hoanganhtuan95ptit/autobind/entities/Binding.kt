package com.hoanganhtuan95ptit.autobind.entities

internal data class Binding(
    val type: String,
    val impl: String
)

internal data class BindingsWrapper(
    val bindings: List<Binding>
)
