/*
*
*  ijvmasm.java
*
*  mic1 microarchitecture simulator 
*  Copyright (C) 1999, Prentice-Hall, Inc. 
* 
*  This program is free software; you can redistribute it and/or modify 
*  it under the terms of the GNU General Public License as published by 
*  the Free Software Foundation; either version 2 of the License, or 
*  (at your option) any later version. 
* 
*  This program is distributed in the hope that it will be useful, but 
*  WITHOUT ANY WARRANTY; without even the implied warranty of 
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
*  Public License for more details. 
* 
*  You should have received a copy of the GNU General Public License along with 
*  this program; if not, write to: 
* 
*    Free Software Foundation, Inc. 
*    59 Temple Place - Suite 330 
*    Boston, MA 02111-1307, USA. 
* 
*  A copy of the GPL is available online the GNU web site: 
* 
*    http://www.gnu.org/copyleft/gpl.html
* 
*/ 

/**
* Command line front end for IJVMAssembler.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/

import java.io.FileOutputStream;
import java.io.PrintStream;

public final class ijvmasm {

    public static void main(String args[]) {
        IJVMAssembler ia = null;
        if (args.length == 0 || args[0].equals("help")) {
            System.out.println("usage: java ijvmasm <jas source file> " +
                "[ijvm file]");
        }
        else {
            String outfile = null;
            String infile = args[0];
            if (!infile.endsWith(".jas")) {
                System.out.println("Source file must be .jas file");
                System.exit(0);
            }
            if (args.length >= 2) {
                outfile = args[1];
                if (!outfile.endsWith(".ijvm")) {
                    System.out.println("Destination file must be .ijvm file.");
                    System.exit(0);
                }
            }
            else {
                outfile = infile.substring(0, infile.length() - 4) + ".ijvm";
            }
            ia = new IJVMAssembler(infile, outfile);
    
            if (args.length >= 3) {
                try {
                    ia.writeSymbols(new PrintStream(
                        new FileOutputStream(args[2])));
                }  
                catch (Exception e) {
                    System.out.println("Error opening file " + args[2]);
                }
            }
        }
    }
}
