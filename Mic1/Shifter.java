/*
*
*  Shifter.java
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
* This shifter has three operations, shift left 8 bits, shift right 1 bit,
* and no shift.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class Shifter {

  private Bus in = null,
              out = null;
  private ControlLine shifter_cl = null;

  public Shifter(Bus in, Bus out, ControlLine shifter_cl) {
    this.in = in;
    this.out = out;
    this.shifter_cl = shifter_cl;
  }

  public void poke() {
    boolean value[] = shifter_cl.getValue();
    int result = in.getValue();
    if (value[MIR.SLL8])
      result = result << 8;
    if (value[MIR.SRA1])
      result = result >> 1; // Issue: should shifter extend sign, as it does here?
    out.setValue(result);
  }

}
