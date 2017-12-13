/*
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
* Register that contains word values for writing or reading from main
* memory
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class MDR extends TextField {

  private int value;
  private Bus 
    in = null,
    out = null,
    data_bus = null;
  private ControlLine 
    put_cl = null,
    store_cl = null,
    read_cl = null,
    mem_cl = null;

  public MDR(Bus in, Bus out, Bus data_bus, ControlLine store_cl, 
	     ControlLine put_cl, ControlLine read_cl, ControlLine mem_cl) {
    super(10);
    this.in = in;
    this.out = out;
    this.store_cl = store_cl;
    this.put_cl = put_cl;
    this.data_bus = data_bus;
    this.read_cl = read_cl;
    this.mem_cl = mem_cl;
    setEditable(false);
    refresh();
  }

  public void store() {
    if (store_cl.getValue()[0]) {
      value = in.getValue();
      if (mic1sim.debug) 
	DebugFrame.text.appendText("MDR: Store 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run)
	refresh();
    }
    if (read_cl.getValue()[0]) {
      value = data_bus.getValue();
      if (mic1sim.debug) 
	DebugFrame.text.appendText("MDR: Read 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run)
	refresh();
    }
  }

  public void put() {
    if (put_cl.getValue()[0]) {
      out.setValue(value);
      if (mic1sim.debug) 
	DebugFrame.text.appendText("MDR: Put 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run)
	refresh();
    }
  }

  public void mem() {
    if (mem_cl.getValue()[MIR.WRITE]) {
      data_bus.setValue(value);
      if (mic1sim.debug) 
	DebugFrame.text.appendText("MDR: Write 0x" + Integer.toHexString(value).toUpperCase() + "\n");
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
