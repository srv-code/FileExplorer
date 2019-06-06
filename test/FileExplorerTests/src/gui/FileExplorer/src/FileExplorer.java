package clg.project.summer.others.FileExplorer.src;


import javax.swing.*;

public class FileExplorer {
    public static void main(String args[]) throws Exception  {
        System.out.println("FileExplorer  Copyright (C) 2009  Morgan Prior");
        System.out.println("This program comes with ABSOLUTELY NO WARRANTY; ");
        System.out.println("   This is free software, and you are welcome to redistribute it");
        System.out.println("   under certain conditions;");
        clg.project.summer.others.FileExplorer.src.fileexplorer.FileExplorer window
                = new clg.project.summer.others.FileExplorer.src.fileexplorer.FileExplorer();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
   }
}