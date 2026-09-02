package com.todonotepro.app.native

/**
 * Thin, safe wrapper around the high-performance C++ core.
 * All heavy lifting (search, sorting) happens in native code for speed and low battery impact.
 */
object NativeCore {

    init {
        System.loadLibrary("todonotepro_core")
        init()
    }

    @JvmStatic
    external fun init()

    @JvmStatic
    external fun shutdown()

    /**
     * Ultra-fast full-text search.
     * @return JSON array of matching indices, e.g. "[0,3,7]"
     */
    @JvmStatic
    external fun fastSearch(titles: Array<String>, contents: Array<String>, query: String): String

    @JvmStatic
    external fun sortByPriority(priorities: IntArray, timestamps: LongArray): Long
}
