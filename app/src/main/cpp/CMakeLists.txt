cmake_minimum_required(VERSION 3.22.1)

project("todonotepro_core")

add_library(
    todonotepro_core
    SHARED
    native-lib.cpp
    core/TodoEngine.cpp
    core/SearchEngine.cpp
)

find_library(log-lib log)

target_include_directories(todonotepro_core PRIVATE ${CMAKE_SOURCE_DIR}/core)

target_link_libraries(
    todonotepro_core
    ${log-lib}
)

# Aggressive optimization for release
target_compile_options(todonotepro_core PRIVATE
    $<$<CONFIG:Release>:-O3 -DNDEBUG -ffast-math>
    $<$<CONFIG:Debug>:-O0 -g>
)
