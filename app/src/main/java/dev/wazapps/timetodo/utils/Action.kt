package dev.wazapps.timetodo.utils

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION;

    companion object {
        fun fromString(string: String?) = string?.let { possibleAction ->
            runCatching {
                valueOf(possibleAction)
            }.getOrNull() ?: NO_ACTION
        } ?: NO_ACTION
    }
}