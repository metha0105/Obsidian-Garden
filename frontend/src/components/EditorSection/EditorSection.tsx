import { type Language } from "../../types";
import { useMemo, useEffect } from "react";
import Editor from "@monaco-editor/react";
import "./EditorSection.css";

const defaultCodeMap: Record<string, string> = {
  CPP: `// C++ template
#include <iostream>
using namespace std;

int main() {
    // your code here
    return 0;
}
`,
  PYTHON: `# Python template
def main():
    # your code here
    pass

if __name__ == "__main__":
    main()
`,
};

interface EditorSectionProps {
  language: Language;
  code: string;
  onLanguageChange: (lang: Language) => void;
  onCodeChange: (code: string) => void;
  onRun: () => void;
  onSubmit: () => void;
}

const EditorSection: React.FC<EditorSectionProps> = ({
  language,
  code,
  onLanguageChange,
  onCodeChange,
  onRun,
  onSubmit,
}) => {
  const monacoLanguage = useMemo(() => {
    switch (language) {
      case "CPP":
        return "cpp";
      case "PYTHON":
        return "python";
      default:
        return "cpp";
    }
  }, [language]);

  useEffect(() => {
    if (!code) {
      onCodeChange(defaultCodeMap[language] ?? "");
    }
  }, [language, code, onCodeChange]);

  const handleLanguageChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newLang = e.target.value as Language;
    onLanguageChange(newLang);
    onCodeChange(defaultCodeMap[newLang] ?? "");
  };

  const handleReset = () => {
    onCodeChange(defaultCodeMap[language] ?? "");
  };

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(code);
      alert("Code copied to clipboard!");
    } catch (err) {
      alert("Failed to copy.");
    }
  };

  return (
    <div className="editor-wrapper">
      <div className="editor-toolbar">
        <select value={language} onChange={handleLanguageChange}>
          <option value="CPP">C++</option>
          <option value="PYTHON">Python</option>
        </select>
        <button onClick={handleReset}>RESET</button>
        <button onClick={handleCopy}>COPY</button>
        <button onClick={onRun}>RUN</button>
        <button onClick={onSubmit}>SUBMIT</button>
      </div>
      
      <Editor
        className="editor-core"
        language={monacoLanguage}
        value={code}
        onChange={(val) => onCodeChange(val ?? "")}
        theme="obsidian-garden"
        options={{
            fontSize: 14,
            fontFamily: '"Courier New", monospace',
            minimap: { enabled: false },
            scrollBeyondLastLine: false,
            automaticLayout: true
        }}
        onMount={(_, monaco) => {
            monaco.editor.defineTheme("obsidian-garden", {
                base: "vs",
                inherit: true,
                rules: [
                    { token: "", foreground: "3c2b41", background: "f3f7ff" },
                    { token: "comment", foreground: "815ba3", fontStyle: "italic" },
                    { token: "keyword", foreground: "f9679c", fontStyle: "bold" },    
                    { token: "string", foreground: "faad57" },     
                    { token: "number", foreground: "4bb59bff" },   
                    { token: "function", foreground: "62b6ff" },  
                    { token: "type", foreground: "a486ff" },   
                    { token: "constant", foreground: "ffb86b" },            
                    { token: "enum", foreground: "ffad4d" },           
                    { token: "enumMember", foreground: "ffad4d" },
                    { token: "preprocessor", foreground: "ffa94d" }   
                ],
                colors: {
                    "editor.background": "#f3f7ff",
                    "editor.foreground": "#3c2b41",
                    "editor.lineHighlightBackground": "#edf3ff",
                    "editor.selectionBackground": "#ffc3ec44",
                    "editor.inactiveSelectionBackground": "#ffc3ec22",
                    "editorLineNumber.foreground": "#a093b2",
                    "editorLineNumber.activeForeground": "#ff86b4",
                    "editorCursor.foreground": "#ff5ca8"
                }
            });
            monaco.editor.setTheme("obsidian-garden");
        }}
        />
    </div>
  );
};

export default EditorSection;