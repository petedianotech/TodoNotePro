#pragma once

#include <string>
#include <vector>
#include <cstdint>

struct TodoItem {
    int64_t id;
    std::string title;
    std::string content;
    int priority;       // 0=low, 1=medium, 2=high
    int64_t dueDate;    // epoch millis, 0 = none
    bool completed;
    int64_t updatedAt;
};

class TodoEngine {
public:
    TodoEngine() = default;
    ~TodoEngine() = default;

    // Fast in-memory operations (called from JNI)
    void sortByPriority(std::vector<TodoItem>& items);
    void sortByDueDate(std::vector<TodoItem>& items);
    size_t filterCompleted(const std::vector<TodoItem>& items, bool completed, std::vector<TodoItem>& out);
};
