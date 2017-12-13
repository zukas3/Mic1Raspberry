/*
*
*  MainMemory.java
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
* Main memory module.  Stores program, constants, stack, constants.
* Responds to all write/read/fetch requests.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class MainMemory implements Mic1Constants {

  private static final int STDIN = 0xffffff00;
  private static final int STDOUT = 0xffffff00;
  private byte memory[];
  private Bus byte_address_bus = null;
  private Bus word_address_bus = null;
  private Bus byte_data_bus = null;
  private Bus word_data_bus = null;
  private MDR mdr = null;
  private ControlLine 
    main_memory_cl = null,
    mbr_cl = null,
    mdr_cl = null;
  private boolean
    fetch = false,
    read = false,
    write = false,
    true_array[] = {true};

    int address;
    int read_address = 0;
    int write_address = 0;
    int byte_address = 0;
    int data;
    int read_data = 0;
    int write_data = 0;

  public MainMemory(Bus byte_address_bus, Bus word_address_bus ,
		    Bus byte_data_bus, Bus word_data_bus,
		    ControlLine main_memory_cl, ControlLine mbr_cl, ControlLine mdr_cl, MDR mdr) {
    this.byte_address_bus = byte_address_bus;
    this.word_address_bus = word_address_bus;
    this.byte_data_bus = byte_data_bus;
    this.word_data_bus = word_data_bus;
    this.main_memory_cl = main_memory_cl;
    this.mbr_cl = mbr_cl;
    this.mdr_cl = mdr_cl;
    this.mdr = mdr;
    memory = new byte[MEM_MAX];
  }

  public void poke() {
    if (write) {
      address = write_address; // Word addressed, not byte
      data = write_data;
      if (address  < 0 ) {
	  mic1sim.stdout.appendText(String.valueOf((char)data));
          //System.out.println("Key written: " + data + ", " + (char)data);
	if (mic1sim.debug)
	  DebugFrame.text.appendText("MEM: Out character " + (char)data + "\n");
	write = false;
      }
      else {
	if (mic1sim.debug)
	  DebugFrame.text.appendText("MEM: Write value 0x" + Integer.toHexString(data).toUpperCase()
				+ " to address 0x" + Integer.toHexString(address).toUpperCase() + "\n");
	for (int i = 3; i >= 0; i--) {
	  if (address + i < MEM_MAX) {
	    memory[address + i] = (byte)data;
	  }
          else
            System.out.println("Write index too large: 0x" + Integer.toHexString(address).toUpperCase());
	  data = data >> 8;
	}
	write = false;
      }
      write = false;
    }
    if ((main_memory_cl.getValue())[MIR.WRITE]) {
      write_address = word_address_bus.getValue() << 2;
      write_data = word_data_bus.getValue();
      //if (write_data > 0) System.out.println("greater");
      if (mic1sim.debug)
	DebugFrame.text.appendText("MEM: Write value 0x" + Integer.toHexString(write_data).toUpperCase() +
			  " to word# 0x" + Integer.toHexString(write_address).toUpperCase() + 
			  " requested. Processing...\n");
      write = true;
    }

    if (read) {
      address = read_address; // Word addressed, not byte
      if (address  < 0 ) {
	if (mic1sim.debug)
	  DebugFrame.text.appendText("Memory waiting for key press...\n");
	if (mic1sim.key_buffer.size() == 0) {
	  if (mic1sim.debug)
	    DebugFrame.text.appendText("Key not pressed\n");
	  data = 0;
        }
	else {
	  data = ((Character)mic1sim.key_buffer.elementAt(0)).charValue();
	  mic1sim.key_buffer.removeElementAt(0);
	  if (mic1sim.debug)
	    DebugFrame.text.appendText("Key pressed: " + (char)data + "\n");
	}
	word_data_bus.setValue(data);
	mdr_cl.setValue(true_array);
	mdr.store();
      }
      else {
        if (address + 3 < MEM_MAX) {
           data = (((int)memory[address] & 0xff)  << 24) + 
	     (((int)memory[address+1] & 0xff) << 16) +
	     (((int)memory[address+2] & 0xff) << 8) + 
	     ((int)memory[address+3] & 0xff);
	}
        else
          System.out.println("Read index too large: 0x" + Integer.toHexString(address).toUpperCase());
	word_data_bus.setValue(data);
	mdr_cl.setValue(true_array);
	mdr.store();
	if (mic1sim.debug)
	  DebugFrame.text.appendText("MEM: Read value 0x" + Integer.toHexString(data).toUpperCase()
				+ " from address 0x" + Integer.toHexString(address).toUpperCase() + "\n");
      }
      read = false;
    }
    if ((main_memory_cl.getValue())[MIR.READ]) {
      read_address = word_address_bus.getValue() << 2;
      if (mic1sim.debug)
	DebugFrame.text.appendText("MEM: Read from word# 0x" + 
			 Integer.toHexString(read_address).toUpperCase() + " requested. Processing...\n");
      read = true;
    }

    if (fetch) {
      address = byte_address;
      if (address < MEM_MAX && address >= 0) {
	data = memory[address];
	data = data & 255;
	byte_data_bus.setValue(data);
	mbr_cl.setValue(true_array);
	if (mic1sim.debug)
	  DebugFrame.text.appendText("MEM: Fetch value 0x" + Integer.toHexString(data).toUpperCase()
			   + " from address 0x" + Integer.toHexString(address).toUpperCase() + "\n");
      }
      else
        System.out.println("Invalid fetch index: 0x" + Integer.toHexString(address).toUpperCase());
      fetch = false;
    }
    if ((main_memory_cl.getValue())[MIR.FETCH]) {
      byte_address = byte_address_bus.getValue();
      if (mic1sim.debug)
	DebugFrame.text.appendText("MEM: Fetch from byte# 0x" +
			 Integer.toHexString(byte_address).toUpperCase() + " requested. Processing...\n");
      fetch = true;
    }
  }

  public void reset() {
    read = false;
    write = false;
    fetch = false;
  }

  public byte[] getMemory() {
    return memory;
  }
  
  public void setMemory(byte[] bytes) {
    memory = bytes;
  }
}
