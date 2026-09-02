#include "SearchEngine.h"
#include <sstream>
#include <cctype>
#include <algorithm>

bool SearchEngine::containsIgnoreCase(const std::string& haystack, const std::string& needle) {
    if (needle.empty()) return true;
    auto it = std::search(
        haystack.begin(), haystack.end(),
        needle.begin(), needle.end(),
        [](char ch1, char ch2) {
            return std::tolower(static_cast<unsigned char>(ch1)) ==
                   std::tolower(static_cast<unsigned char>(ch2));
        });
    return it != haystack.end();
}

std::string SearchEngine::search(const std::vector<std::string>& titles,
                                 const std::vector<std::string>& contents,
                                 const std::string& query) {
    std::ostringstream oss;
    oss << "[";
    bool first = true;

    const size_t n = std::min(titles.size(), contents.size());
    for (size_t i = 0; i < n; ++i) {
        if (containsIgnoreCase(titles[i], query) || containsIgnoreCase(contents[i], query)) {
            if (!first) oss << ",";
            oss << i;
            first = false;
        }
    }
    oss << "]";
    return oss.str();
}
