package com.ssavice.model

enum class Category(
    val value: String,
    val index: Int,
    val shownInSeller: Boolean = true
) {
    ALL("전체",0, shownInSeller = false),
    HEALTH("건강", 1),
    SHOPPING("쇼핑", 2),
    HOBBY("취미", 3),
    SPORTS("스포츠", 4),
    CULTURE("문화", 5),
    FOOD("식품", 6),
    LIFE("생활", 7),
}
