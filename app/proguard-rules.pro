# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Keep data classes used by Room
-keep class com.todonotepro.app.data.** { *; }
