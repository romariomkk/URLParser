package com.romariomkk.urltextparser.util

class Resource<out T> private constructor(val status: Status, val data: T?, val exception: Throwable?) {

    enum class Status { START, LOADING, SUCCESS, ERROR, STOP, PAUSE, RESUME }

    companion object {

        fun <T> start(data: T? = null): Resource<T> {
            return Resource(Status.START, data, null)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> success(data: T? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(exception: Throwable? = null, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, exception)
        }

        fun <T> finish(data: T? = null): Resource<T> {
            return Resource(Status.STOP, data, null)
        }

        fun <T> pause(data: T? = null): Resource<T> {
            return Resource(Status.PAUSE, data, null)
        }

        fun <T> resume(data: T? = null): Resource<T> {
            return Resource(Status.RESUME, data, null)
        }


    }
}