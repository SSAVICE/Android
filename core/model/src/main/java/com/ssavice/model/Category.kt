package com.ssavice.model

enum class Category(
    val value: String,
    val index: Int,
) {
    HEALTH("건강", 0),
    SHOPPING("쇼핑", 1),
    HOBBY("취미", 2),
    SPORTS("스포츠", 3),
    CULTURE("문화", 4),
    FOOD("식품", 5),
    LIFE("생활", 6),
}
