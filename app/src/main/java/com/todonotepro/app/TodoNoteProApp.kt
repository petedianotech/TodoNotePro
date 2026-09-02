package com.todonotepro.app

import android.app.Application
import com.todonotepro.app.native.NativeCore

class TodoNoteProApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Native core is initialized via object init block
        NativeCore // force load
    }

    override fun onTerminate() {
        NativeCore.shutdown()
        super.onTerminate()
    }
}
