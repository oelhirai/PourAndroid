package com.github.oelhirai.pour.data.model

data class PourOver(
    val pours: List<Pour>
) {
    val numberOfPours = pours.size
}