# TodoNotePro

**Professional all-in-one Todo List + Notes app** built with **Material Design 3**, minimalist aesthetic, and high-performance native code.

## Why TodoNotePro?

- **Unified UI**: Seamless switch between Todos and Notes in one clean interface
- **Material Design 3**: Dynamic color, adaptive theming, modern components
- **Minimalist & Professional**: Clean spacing, subtle elevation, focus on content
- **Blazing Fast**: C++ core engine for data operations, search, and sorting
- **Battery Efficient**: Optimized background work, efficient Room + native storage
- **Crash Resistant**: Strict null-safety, defensive coding, robust error handling
- **Offline First**: Fully functional without internet

## Tech Stack (chosen for performance & reliability)

| Layer              | Technology                          | Reason                                      |
|--------------------|-------------------------------------|---------------------------------------------|
| UI                 | Jetpack Compose + Material 3        | Modern, declarative, excellent performance  |
| Language (App)     | Kotlin                              | Null-safety, coroutines, concise            |
| Native Core        | C++ (Android NDK)                   | Maximum speed & low battery impact          |
| Local Database     | Room (SQLite)                       | Efficient, reactive, well-tested            |
| Architecture       | MVVM + Clean Architecture           | Testable, maintainable, scalable            |
| Background         | WorkManager                         | Battery-aware scheduling                    |
| Build & CI         | Gradle + GitHub Actions             | Automated debug APK builds                  |

## Features (v1 roadmap)

- [x] Material 3 dynamic theming (light/dark/system)
- [x] Unified Todo + Note list with smart filtering
- [x] Rich text notes with markdown support
- [x] Priority, due dates, tags, subtasks
- [x] Fast full-text search powered by C++
- [x] Offline-first with optional cloud sync (future)
- [x] Widget support (coming)
- [x] Biometric lock (coming)

## Project Structure

```
TodoNotePro/
├── app/
│   ├── src/main/
│   │   ├── java/com/todonotepro/app/     # Kotlin UI + ViewModels
│   │   ├── cpp/                         # High-performance C++ core
│   │   └── res/                         # Themes, icons, strings
│   └── build.gradle.kts
├── .github/workflows/
│   └── build-debug-apk.yml              # CI: build & upload debug APK
└── README.md
```

## Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 17+
- Android NDK (side-by-side)

### Build
```bash
git clone https://github.com/petedianotech/TodoNotePro.git
cd TodoNotePro
./gradlew assembleDebug
```

The debug APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

### GitHub Actions
Every push to `main` or `develop` automatically builds a debug APK and uploads it as an artifact.

## Performance Philosophy

- Heavy lifting (search, bulk operations, sorting) runs in C++ via JNI
- UI stays on main thread only for rendering
- Coroutines + Flow for reactive updates without blocking
- Minimal allocations and careful memory management in native layer

## Contributing

Pull requests are welcome. Please keep the minimalist Material 3 design language and performance focus.

## License

MIT

---

Built with care for speed, battery life, and a delightful user experience.
