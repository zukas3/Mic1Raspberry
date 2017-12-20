/*
*
*  Register.java
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
* Basic register.  Used for "normal" registers, those thaT are not involved
* with main memory access.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class Register extends TextField /*Canvas*/ {

  private String name = null;
  protected int value;
  protected Bus in = null;
  protected Bus out = null;
  protected ControlLine store_cl = null;
  protected ControlLine put_cl = null;

  public Register(Bus in, Bus out, String name,
		  ControlLine store_cl, ControlLine put_cl) {
    super(10);
    this.in = in;
    this.out = out;
    this.name = name;
    this.store_cl = store_cl;
    this.put_cl = put_cl;
    setEditable(false);
    setText("0x" + Integer.toHexString(value).toUpperCase());
  }

  public void store() {
    if (store_cl.getValue()[0]) {
      value = in.getValue();
      if (mic1sim.debug) 
	DebugFrame.text.appendText(name + ": Store 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run)
	setText("0x" + Integer.toHexString(value).toUpperCase());
    }
  }

  public void put() {
    if (put_cl.getValue()[0]) {
      if (mic1sim.debug) 
	DebugFrame.text.appendText(name + ": Put 0x" + Integer.toHexString(value).toUpperCase() + "\n");
      if (!mic1sim.run)
	setText("0x" + Integer.toHexString(value).toUpperCase());
      out.setValue(value);
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
