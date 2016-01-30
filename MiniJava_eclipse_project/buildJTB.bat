rmdir "src/ch/unibe/iam/scg/minijava/ast/visitor" /S /Q
rmdir "src/ch/unibe/iam/scg/minijava/ast/syntaxtree" /S /Q
del "src\ch\unibe\iam\scg\javacc\MiniJava.jj"

java -jar libs/jtb132.jar -w -jd -p "ch.unibe.iam.scg.minijava.ast" -o "src/ch/unibe/iam/scg/javacc/MiniJava.jj" "src/ch/unibe/iam/scg/javacc/MiniJavaInitialRules.jj"

xcopy "visitor" "src/ch/unibe/iam/scg/minijava/ast/visitor" /I
xcopy "syntaxtree" "src/ch/unibe/iam/scg/minijava/ast/syntaxtree" /I
rmdir "visitor" /S /Q
rmdir "syntaxtree" /S /Q