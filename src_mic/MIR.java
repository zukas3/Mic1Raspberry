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
* Register that stores the current microinstruction and enables the
* appropriate control lines.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class MIR extends TextField{

  public static final int // Constants which define the order of control signals
    JMPC = 0,
    JAMN = 0,
    JAMZ = 1,
    SLL8 = 0,
    SRA1 = 1,
    F0 = 0,
    F1 = 1,
    ENA = 2,
    ENB = 3,
    INVA = 4,
    INC = 5,
    WRITE = 0,
    READ = 1,
    FETCH = 2;

  private Mic1Instruction value = null;
  private IntControlLine
    addr_cl = null,
    decoder_cl = null;
    
  private ControlLine
    memory_cl = null,
    alu_cl = null,
    shifter_cl = null,
    jam_cl = null,
    jmpc_cl = null,
    mar_cl = null,
    mdr_cl = null,
    pc_cl = null,
    sp_cl = null,
    lv_cl = null,
    cpp_cl = null,
    tos_cl = null,
    opc_cl = null,
    h_cl = null;
  private ObjectBus in = null;

  public MIR(IntControlLine addr_cl, ControlLine jmpc_cl, ControlLine jam_cl,
	     ControlLine shifter_cl, ControlLine alu_cl, ControlLine mar_cl,
	     ControlLine mdr_cl, ControlLine pc_cl, ControlLine sp_cl,
	     ControlLine lv_cl, ControlLine cpp_cl, ControlLine tos_cl,
	     ControlLine opc_cl, ControlLine h_cl, ControlLine memory_cl, 
	     IntControlLine decoder_cl, ObjectBus in) 
    {
      super(30);
      this.addr_cl = addr_cl;
      this.jmpc_cl = jmpc_cl;
      this.jam_cl = jam_cl;
      this.shifter_cl = shifter_cl;
      this.alu_cl = alu_cl;
      this.mar_cl = mar_cl;
      this.mdr_cl = mdr_cl;
      this.pc_cl = pc_cl;
      this.sp_cl = sp_cl;
      this.lv_cl = lv_cl;
      this.cpp_cl = cpp_cl;
      this.tos_cl = tos_cl;
      this.opc_cl = opc_cl;
      this.h_cl = h_cl;
      this.memory_cl = memory_cl;
      this.decoder_cl = decoder_cl;
      this.in = in;
      setEditable(false);
    }


  public void poke() {
    value = (Mic1Instruction)in.getValue();
    if (mic1sim.debug) 
      DebugFrame.text.appendText(value.toString() + "\n");
    if (!mic1sim.run)
      setText(value.toString());
    addr_cl.setValue(value.NEXT_ADDRESS);
    boolean jmpc_ar[] = {value.JMPC};
    boolean jam_ar[] = {value.JAMN, value.JAMZ};
    boolean shifter_ar[] = {value.SLL8, value.SRA1};
    boolean alu_ar[] = {value.F0, value.F1, value.ENA, value.ENB,
                        value.INVA, value.INC};
    boolean h_ar[] = {value.H};
    boolean opc_ar[] = {value.OPC};
    boolean tos_ar[] = {value.TOS};
    boolean cpp_ar[] = {value.CPP};
    boolean lv_ar[] = {value.LV};
    boolean sp_ar[] = {value.SP};
    boolean pc_ar[] = {value.PC};
    boolean mdr_ar[] = {value.MDR};
    boolean mar_ar[] = {value.MAR};
    boolean memory_ar[] = {value.WRITE, value.READ, value.FETCH};
    jmpc_cl.setValue(jmpc_ar);
    jam_cl.setValue(jam_ar);
    shifter_cl.setValue(shifter_ar);
    alu_cl.setValue(alu_ar);
    h_cl.setValue(h_ar);
    opc_cl.setValue(opc_ar);
    tos_cl.setValue(tos_ar);
    cpp_cl.setValue(cpp_ar);
    lv_cl.setValue(lv_ar);
    sp_cl.setValue(sp_ar);
    pc_cl.setValue(pc_ar);
    mdr_cl.setValue(mdr_ar);
    mar_cl.setValue(mar_ar);
    memory_cl.setValue(memory_ar);
    decoder_cl.setValue(value.B);
  }

  public void reset() {
    setText("reset");
  }
}
