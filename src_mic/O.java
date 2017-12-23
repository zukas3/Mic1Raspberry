/*
*
*  O.java
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
* Depending on the value of the JMPC control line, will set the MPC
* to point to either the next microinstruction, or the microinstruction
* addressed by MBR.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class O {

  private IntControlLine
    addr_cl = null,
    mbr_cl = null,
    mpc_cl = null;
  private ControlLine jmpc_cl = null;

  public O(IntControlLine addr_cl, IntControlLine mbr_cl,
	   ControlLine jmpc_cl, IntControlLine mpc_cl) {
    this.addr_cl = addr_cl;
    this.mbr_cl = mbr_cl;
    this.jmpc_cl = jmpc_cl;
    this.mpc_cl = mpc_cl;
  }

  public void poke() {
    if (jmpc_cl.getValue()[0]) {
      mpc_cl.setValue(mbr_cl.getValue());
      if (mic1sim.debug) 
	DebugFrame.text.appendText("Goto MBR: 0x" + Integer.toHexString(mbr_cl.getValue()).toUpperCase() + "\n");
    }
    else {
      mpc_cl.setValue(addr_cl.getValue());
      if (mic1sim.debug)
	DebugFrame.text.appendText("Goto ADDR: 0x" + Integer.toHexString(addr_cl.getValue()).toUpperCase() + "\n");
    }
  }
}
