rem @echo off
rem zips the data folder in the dropbox account for easy access of the data without dropbox

rem can be automatized on windows, see:
rem http://www.ehow.com/how_7458580_set-folder-based-scheduled-task.html
rem http://www.dotnetperls.com/7-zip-examples

rem only used by Trilarion, therefore featuring fixed paths

cd /d "C:\Users\Jan\Dropbox\remake\current version"

del data.zip

"C:\Programme\7-Zip\7z.exe" a -tzip -mx9 -r data.zip "data/*.*"

pause