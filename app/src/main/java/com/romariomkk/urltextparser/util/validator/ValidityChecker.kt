package com.romariomkk.urltextparser.util.validator

@FunctionalInterface
interface ValidityChecker<T, R> {

    fun checkValidity(item: T): R
}