package com.ssavice.common

object DomainFormatter {
    fun formatPrice(price: Int): String = "₩%,d".format(price)

    fun formatPrice(price: Long): String = "₩%,d".format(price)
}
