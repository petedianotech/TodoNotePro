#pragma once

#include <string>
#include <vector>

class SearchEngine {
public:
    SearchEngine() = default;
    ~SearchEngine() = default;

    // Returns JSON array of matching indices for ultra-fast filtering
    std::string search(const std::vector<std::string>& titles,
                       const std::vector<std::string>& contents,
                       const std::string& query);

private:
    static bool containsIgnoreCase(const std::string& haystack, const std::string& needle);
};
