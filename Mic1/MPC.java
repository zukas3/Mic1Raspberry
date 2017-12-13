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
* Contains the address of the next microinstruction.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class MPC extends TextField {

  private int value;
  private int cycle_count;
  private static final int COUNT_MAX = 100;
  private IntControlLine control_store_cl = null,
    o_cl = null;
  private ControlLine high_bit_cl = null;
  private ControlStore control_store = null;
  private TextField next_micro = null;

  public MPC(IntControlLine o_cl, ControlLine high_bit_cl, 
	     IntControlLine control_store_cl, ControlStore control_store,
             TextField next_micro) {
    super(10);
    this.o_cl = o_cl;
    this.high_bit_cl = high_bit_cl;
    this.control_store_cl = control_store_cl;
    this.control_store = control_store;
    this.next_micro = next_micro;
    cycle_count = 0;
    setEditable(false);
  }

  /** Set 9th bit of MPC as value of HighBit by shifting the value
      of HighBit (either 1 or 0) left 8 places and then performing 
      a bitwise OR with the value from O */
  public void poke() {
    value = o_cl.getValue();
    if (high_bit_cl.getValue()[0])
      value = value | 0x100;
    control_store_cl.setValue(value);
    Mic1Instruction inst = control_store.getInstruction(value);
    if (!mic1sim.run || cycle_count == COUNT_MAX) {
      setText("0x" + Integer.toHexString(value).toUpperCase());
      cycle_count = 0;
    }
    else cycle_count++;
    if (!mic1sim.run) {
      next_micro.setText(inst.toString());
    }
    if (value == 0xFF) 
      mic1sim.halt();
  }

  public void forceValue(int value) {
    this.value = value;
    Mic1Instruction inst = control_store.getInstruction(value);
    setText("0x" + Integer.toHexString(value).toUpperCase());
    next_micro.setText(inst.toString());
  }
}
