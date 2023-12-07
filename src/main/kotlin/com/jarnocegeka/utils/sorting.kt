package com.jarnocegeka.utils

object QuickSort {
    fun <T : Comparable<T>> List<T>.quickSort(): List<T> {
        return quickSort { current, first -> current < first }
    }

    fun <T> List<T>.quickSort(partitionCompare: (current: T, first: T) -> Boolean): List<T> {
        return if (size < 2) {
            this
        } else {
            val (less, high) = subList(1, size).partition { partitionCompare(it, first()) }
            less.quickSort(partitionCompare) + first() + high.quickSort(partitionCompare)
        }
    }
}