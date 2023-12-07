package com.jarnocegeka.utils

import java.lang.IllegalStateException

fun <T> Collection<T>.findFirstCommonItem(other: Collection<T>): T {
    for (item in other) {
        if (this.contains(item)) return item
    }

    throw IllegalStateException("No common item found!")
}

fun <T> List<T>.findFirstCommonItem(other: List<T>, another: List<T>): T {
    var i = 0
    var j = 0
    var k = 0

    while (i < this.size && j < other.size && k < another.size) {
        if (this[i] == other[j] && other[j] == another[k]) {
            return this[i]
        }

        if (this[i].hashCode() < other[j].hashCode()) {
            i++
        } else if (other[j].hashCode() < another[k].hashCode()) {
            j++
        } else {
            k++
        }
    }

    throw IllegalStateException("No common item found!")
}