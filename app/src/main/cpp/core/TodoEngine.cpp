#include "TodoEngine.h"
#include <algorithm>

void TodoEngine::sortByPriority(std::vector<TodoItem>& items) {
    std::stable_sort(items.begin(), items.end(),
        [](const TodoItem& a, const TodoItem& b) {
            if (a.completed != b.completed) return !a.completed; // incomplete first
            if (a.priority != b.priority) return a.priority > b.priority;
            return a.updatedAt > b.updatedAt;
        });
}

void TodoEngine::sortByDueDate(std::vector<TodoItem>& items) {
    std::stable_sort(items.begin(), items.end(),
        [](const TodoItem& a, const TodoItem& b) {
            if (a.completed != b.completed) return !a.completed;
            if (a.dueDate == 0 && b.dueDate == 0) return a.updatedAt > b.updatedAt;
            if (a.dueDate == 0) return false;
            if (b.dueDate == 0) return true;
            return a.dueDate < b.dueDate;
        });
}

size_t TodoEngine::filterCompleted(const std::vector<TodoItem>& items, bool completed,
                                   std::vector<TodoItem>& out) {
    out.clear();
    out.reserve(items.size());
    for (const auto& item : items) {
        if (item.completed == completed) {
            out.push_back(item);
        }
    }
    return out.size();
}
