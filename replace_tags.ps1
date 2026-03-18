$files = Get-ChildItem -Path "c:\Github Clones\cen206-homework-eren-koc-java\projectmanager-app\src\main\java" -Recurse -Filter "*.java"
foreach ($file in $files) {
    # Read entire file
    $content = Get-Content $file.FullName -Raw

    # Remove @class and @enum lines
    $content = $content -replace '(?m)^[ \t]*\*[ \t]*@class[ \t]+[A-Za-z0-9_]+\s*\r?\n', ''
    $content = $content -replace '(?m)^[ \t]*\*[ \t]*@enum[ \t]+[A-Za-z0-9_]+\s*\r?\n', ''

    # Remove @brief keyword but keep the text following it
    $content = $content -replace '@brief\s+', ''

    # Replace @details with <p>
    $content = $content -replace '@details\s+', '<p> '

    # Save back to file
    [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.Encoding]::UTF8)
}
