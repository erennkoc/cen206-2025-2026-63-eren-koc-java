$files = Get-ChildItem -Path "c:\Github Clones\cen206-homework-eren-koc-java\projectmanager-app\src\main\java" -Recurse -Filter "*.java"
$utf8NoBom = New-Object System.Text.UTF8Encoding $false

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
}
