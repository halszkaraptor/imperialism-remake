@echo off
rem zips the data folder in my dropbox account automatically, just schedule it (on windows)
rem http://www.ehow.com/how_7458580_set-folder-based-scheduled-task.html
rem http://www.dotnetperls.com/7-zip-examples
cd "C:\Users\Admin\Dropbox\remake"
del data.zip
"C:\Program Files\7-Zip\7z.exe" a -tzip -mx9 -r data.zip "data/*.*"
rem pause