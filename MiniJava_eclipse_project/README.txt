1. Compile MiniJavaInitialRules with JTB (use buildJTB.bat for this)
2. Open MiniJava.jj inside eclipse, compile it with javacc 
   (is done automatically by eclipse if you have the plugin and open/change the file)
3. Run tests, or do whatever you like! :)

Note:
For easier debugging we added toString method for all Ast classes.
If you run JTB however all classes will get overwritten and the toString for all Nodes is gone.
Therefore we added the file "all syntaxtree classes with toString.rar" inside the ast package.
This file is a backup of all classes extending the class Node, which implements toString().
