/*
*
*  HighBit.java
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
* Memory address register.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class MAR extends TextField{

  private int value;
  private Bus
    in = null,
    address_bus = null;
  private ControlLine
    store_cl = null,
    mem_cl = null;

  public MAR(Bus in, Bus address_bus, ControlLine store_cl, ControlLine mem_cl) {
    super(10);
    this.in = in;
    this.address_bus = address_bus;
    this.store_cl = store_cl;
    this.mem_cl = mem_cl;
    setEditable(false);
    refresh();
  }

  public void store() {
    if (store_cl.getValue()[0]) {
      value = in.getValue();
      if (mic1sim.debug) 
	DebugFrame.text.appendText("MAR: Store 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run)
	refresh();
    }
  }

  public void mem() {
    if (mem_cl.getValue()[MIR.READ]){
      address_bus.setValue(value);
      if (mic1sim.debug) 
	DebugFrame.text.appendText("MAR: Read from word 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run)
	refresh();
    }

    if (mem_cl.getValue()[MIR.WRITE]) {
      address_bus.setValue(value);
      if (mic1sim.debug) 
	DebugFrame.text.appendText("MAR: Write to word 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run)
	refresh();
    }
  }

  public void refresh() {
    setText("0x" + Integer.toHexString(value).toUpperCase());
  }

  public void forceValue(int value) {
    this.value = value;
    refresh();
  }
}
