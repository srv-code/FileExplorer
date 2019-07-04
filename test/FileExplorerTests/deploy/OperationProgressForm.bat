@echo on
jar cvfe0 progress.jar gui.mytests.OperationProgressForm -C ../build/classes/ gui/
copy /Y progress.jar "C:\Users\soura\Documents\Virtual machine\Shared"
move /Y progress.jar ..\build\artifacts
REM pause 