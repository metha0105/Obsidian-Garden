#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <array>
#include <string>
#include <cstdlib>
#include <cstdio>
#include "nlohmann/json.hpp"

using json = nlohmann::json;

// Writes user's code to a file
void writeToFile(const std::string& path, const std::string& code) {
    std::ofstream file(path);
    file << code;
    file.close();
}

// Executes shell commands
std::string runCommand(const std::string& cmd) {
    // Buffer reads small portions of the output from the shell command (128 chars at a time)
    std::array<char, 128> buffer{};
    std::string result;
    // Opens a process by creating a pipe to the shell and running the command
    FILE* pipe = popen(cmd.c_str(), "r");

    if (!pipe) return "ERROR";

    while (fgets(buffer.data(), buffer.size(), pipe) != nullptr) {
        result += buffer.data();
    }

    pclose(pipe);
    return result;
}

int main() {
    // Loads and parses the input file
    std::ifstream inputFile("/app/input.json");
    json input;
    inputFile >> input;

    // Extracts JSON values
    std::string userCode = input.value("userCode", "");
    std::string judgeHarness = input.value("judgeHarness", "");
    std::string language = input.value("language", "");
    auto testCases = input["testCases"];

    // Combine user's code with judge harness
    std::string fullCode = userCode + "\n" + judgeHarness;

    // Compilation + interpretation
    std::string filename, runCmd;
    bool isCpp = (language == "CPP");

    if (isCpp) {
        // CASE 1: Language is C++
        filename = "/app/submission.cpp";
        writeToFile(filename, fullCode);

        // Saves user's C++ code to a file and then compiles
        std::string compileCmd = "g++ -std=c++17 " + filename + " -o /app/a.out 2>&1";
        std::string compileOutput = runCommand(compileCmd);

        // Returns error if compilation fails
        if (!compileOutput.empty()) {
            json result;
            result["passed"] = false;
            result["failedTestCaseIndices"] = {};
            result["stdout"] = "";
            result["stderr"] = compileOutput;
            std::cout << result.dump();
            return 1;
        }

        // Uses the following binary to run if compilation succeeds
        runCmd = "/app/a.out";
    } else {
        // CASE 2: Language is Python 
        // *** Pls note that more languages will be added ***
        // Saves user's code to a python file and then runs it directly
        filename = "/app/submission.py";
        writeToFile(filename, fullCode);
        runCmd = "python3 " + filename;
    }

    // Runs test cases !!
    bool allPassed = true;
    std::vector<int> failedIndices;
    std::stringstream stdoutStream;

    for (size_t i = 0; i < testCases.size(); i++) {
        std::string inputStr = testCases[i].value("input", "");
        std::string expectedOutput = testCases[i].value("expected", "");

        // Creates a shell command to feed input(s) to the program using 'echo', runs it, and captures the output
        std::string command = "echo \"" + inputStr + "\" | " + runCmd;
        std::string actualOutput = runCommand(command);
        // Trims trailing whitespace
        actualOutput.erase(actualOutput.find_last_not_of(" \n\r\t") + 1);

        stdoutStream << "Test Case " << i << ": " << actualOutput << "\n";

        // If test case fails !!
        if (actualOutput != expectedOutput) {
            allPassed = false;
            failedIndices.push_back(i);
        }
    }

    json result;
    result["passed"] = allPassed;
    result["failedTestCaseIndices"] = failedIndices;
    result["stdout"] = stdoutStream.str();
    result["stderr"] = "";

    std::cout << result.dump();
    return 0;
}