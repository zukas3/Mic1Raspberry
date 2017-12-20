/* 
*  ALU.java
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

import java.awt.*;

/**
* Arithmetic logic unit.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class ALU extends TextField{

  private Bus a_bus = null,
              b_bus = null,
              out = null;
  
  private ControlLine alu_cl = null,
    n_cl = null,
    z_cl = null;

  int result;

  public ALU(Bus a_bus, Bus b_bus, Bus out, 
	     ControlLine alu_cl, ControlLine n_cl,
	     ControlLine z_cl) {
    super(30);
    this.a_bus = a_bus;
    this.b_bus = b_bus;
    this.out = out;
    this.alu_cl = alu_cl;
    this.n_cl = n_cl;
    this.z_cl = z_cl;
    setEditable(false);
  }

  public void poke() {
    //    System.out.print("ALU poked: ");

    int a = 0; 
    int b = 0; 
    result = 0;
    boolean value[] = alu_cl.getValue();
    if (value[MIR.ENA])
      a = a_bus.getValue();
    if (value[MIR.ENB])
      b = b_bus.getValue();
    if (value[MIR.INVA])
      a = ~ a;
    if (value[MIR.F0]) {
      if (value[MIR.F1])
	result = a + b;
      else
	result = ~ b;
    }
    else {
      if (value[MIR.F1])
	result = (a | b);
      else
	result = (a & b);    
    }
    if (value[MIR.INC])
      result++;
    if (mic1sim.debug) 
      DebugFrame.text.appendText("ALU: " + toString() + "\n");
    if (!mic1sim.run)
      setText(toString());
    out.setValue(result);
    boolean z_ar[] = {result == 0};
    boolean n_ar[] = {result < 0};
    z_cl.setValue(z_ar);
    n_cl.setValue(n_ar);
  }

  public String toString() {
    boolean value[] = alu_cl.getValue();
    String s = new String();
    if (value[MIR.INVA])
      s += "NOT ";
    if (value[MIR.ENA])
      s += "A ";
    else
      s += "0 ";
    if (value[MIR.F0]) {
      if (value[MIR.F1])
	s += "+ ";
      else 
	s = "NOT ";
    }
    else {
      if (value[MIR.F1])
	s += "OR ";
      else 
	s += "AND ";
    }
    if (value[MIR.ENB])
      s += "B ";
    else
      s += "0 ";
    if (value[MIR.INC])
      s += "+ 1 ";
    s += "= 0x" + Integer.toHexString(result).toUpperCase();
    return s;
  }
}
