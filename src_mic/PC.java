/*
*
*  PC.java
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
* Program counter register.  Used for fetching operators and operands
* from main memory.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
import java.awt.*;

public class PC extends TextField {

  private int value;
  private int cycle_count;
  private static final int COUNT_MAX = 100;
  private Bus 
    address_bus = null,
    in = null,
    out = null;
  private ControlLine 
    mem_cl = null,
    store_cl = null,
    put_cl = null;
  
  public PC(Bus in, Bus out, Bus address_bus,
	    ControlLine store_cl, ControlLine put_cl, ControlLine mem_cl) {
    super(10);
    this.in = in;
    this.out = out;
    this.address_bus = address_bus;
    this.store_cl = store_cl;
    this.put_cl = put_cl;
    this.mem_cl = mem_cl;
    setEditable(false);
    setText("0x0");
    cycle_count = 0;
  }

  public void store() {
    if (store_cl.getValue()[0]) {
      value = in.getValue();
      if (mic1sim.debug) 
	DebugFrame.text.appendText("PC: Store " + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run || cycle_count == COUNT_MAX) {
	setText("0x" + Integer.toHexString(value).toUpperCase());
        cycle_count = 0;
      }
      else cycle_count++;
    }
  }

  public void put() {
    if (put_cl.getValue()[0]) {
      out.setValue(value);
      if (mic1sim.debug) 
	DebugFrame.text.appendText("PC: Put " + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run || cycle_count == COUNT_MAX) {
	setText("0x" + Integer.toHexString(value).toUpperCase());
        cycle_count = 0;
      }
      else cycle_count++;
    }
  }

  public void mem() {
    if (mem_cl.getValue()[MIR.FETCH]) {
      address_bus.setValue(value);
      if (mic1sim.debug) 
	DebugFrame.text.appendText("PC: Fetch byte " + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run || cycle_count == COUNT_MAX) {
	setText("0x" + Integer.toHexString(value).toUpperCase());
        cycle_count = 0;
      }
      else cycle_count++;
    }
  }

  public void forceValue(int value) {
    this.value = value;
    setText("0x" + Integer.toHexString(value).toUpperCase());
  }

  public void refresh() {
    setText("0x" + Integer.toHexString(value).toUpperCase());
  }
}
