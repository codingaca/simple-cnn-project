package com.cms.cnn_project

fun translateLabelToEnglish(name: String): String {
    return when(name) {
        "당근" -> "carrot"
        "닭고기" -> "chicken"
        "마늘" -> "garlic"
        "양파" -> "onion"
        "돼지고기" -> "pork"
        "감자" -> "potato"
        else -> "unknown"
    }
}