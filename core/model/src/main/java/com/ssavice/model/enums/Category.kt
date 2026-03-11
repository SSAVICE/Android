package com.ssavice.model.enums

enum class Category(
    val value: String,
    val index: Int,
    val shownInSeller: Boolean = true,
    val showInUser: Boolean = true,
) {
    ALL("전체", 0, shownInSeller = false),
    HEALTH("건강", 1),
    SHOPPING("쇼핑", 2),
    HOBBY("취미", 3),
    SPORTS("스포츠", 4),
    CULTURE("문화", 5),
    FOOD("식품", 6),
    LIFE("생활", 7),
    UNKNOWN("알 수 없음", 99, shownInSeller = false, showInUser = false),
    ;

    companion object
}

fun Category.Companion.mapCategoryByValue(value: String): Category = Category.entries.find { it.value == value } ?: Category.UNKNOWN

fun Category.Companion.mapCategoryByName(name: String): Category = Category.entries.find { it.name == name } ?: Category.UNKNOWN
