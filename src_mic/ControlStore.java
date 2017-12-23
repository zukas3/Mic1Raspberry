/*
*
*  ControlStore.java
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
* Stores the microprogram.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class ControlStore {

  private static final int INSTR_COUNT = 512;
  private Mic1Instruction store[];
  private IntControlLine store_cl = null;
  private ObjectBus out = null;

  public ControlStore(IntControlLine store_cl, ObjectBus out) {
    this.store_cl = store_cl;
    store = new Mic1Instruction[INSTR_COUNT];
    this.out = out;
  }

  public Mic1Instruction[] getStore() {
    return store;
  }

  public void setStore(Mic1Instruction store[]) {
    this.store = store;
  }

  public void poke() {
    int value = store_cl.getValue();
    if (value < INSTR_COUNT) ;
      out.setValue(store[value]);
  }

  public Mic1Instruction getInstruction(int index) {
    return store[index];
  }
}
