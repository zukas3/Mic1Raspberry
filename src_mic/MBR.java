/*
*  MBR.java
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
* Register that stores bytes fetched from memory.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class MBR extends TextField{

  private int value, next_val;
  private Bus
    in = null,
    out = null;
  private ControlLine 
    memory_cl = null,
    store_cl = null,
    mbr_cl = null,
    mbru_cl = null;
  private IntControlLine o_cl = null;
  private boolean fetch = false;

  public MBR(Bus in, Bus out, ControlLine memory_cl, ControlLine mbr_cl,
	     ControlLine mbru_cl, IntControlLine o_cl) {
    super(10);
    this.in = in;
    this.out = out;
    this.store_cl = store_cl;
    this.mbr_cl = mbr_cl;
    this.mbru_cl = mbru_cl;
    this.o_cl = o_cl;
    this.memory_cl = memory_cl;
    setEditable(false);
    setText("0x" + Integer.toHexString(value).toUpperCase());
  }

  public void store() {
    if (memory_cl.getValue()[0]) {
      value = in.getValue();
      o_cl.setValue(value & 255);
      if (mic1sim.debug)
	DebugFrame.text.appendText("MBR: Store 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run)
	setText("0x" + Integer.toHexString(value).toUpperCase());
    }
  }

  public void put() {
    if (mbr_cl.getValue()[0]) {
      if (mic1sim.debug)
	DebugFrame.text.appendText("MBR: Put 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      if ((value & 128) == 0)
	out.setValue(value & 255);
      else 
	out.setValue(value | -256);
    }
    else if (mbru_cl.getValue()[0]) {
      if (mic1sim.debug)
	DebugFrame.text.appendText("MBR: Put 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      out.setValue(value & 255);
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
