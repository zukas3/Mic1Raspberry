/*
*
*  Decoder.java
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
* Decodes a 4-bit control line from the MIR and enables the proper register 
* control line. 
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class Decoder {

  private IntControlLine decoder_cl = null;

  private ControlLine 
    mdr_cl = null,
    pc_cl = null,
    mbr_cl = null,
    mbru_cl = null,
    sp_cl = null,
    lv_cl = null,
    cpp_cl = null,
    tos_cl = null,
    opc_cl = null;

  public Decoder(){}
  
  public Decoder(IntControlLine decoder_cl, ControlLine mdr_cl, ControlLine pc_cl,
		 ControlLine mbr_cl, ControlLine mbru_cl, ControlLine sp_cl, 
		 ControlLine lv_cl, ControlLine cpp_cl, ControlLine tos_cl, 
		 ControlLine opc_cl) {
    this.decoder_cl = decoder_cl;
    this.mdr_cl = mdr_cl;
    this.pc_cl = pc_cl;
    this.mbr_cl = mbr_cl;
    this.mbru_cl = mbru_cl;
    this.sp_cl = sp_cl;
    this.lv_cl = lv_cl;
    this.cpp_cl = cpp_cl;
    this.tos_cl = tos_cl;
    this.opc_cl = opc_cl;
  }

  public void poke() {
    int value = decoder_cl.getValue();

    boolean mdr_ar[]  = {value == 0};
    boolean pc_ar[]   = {value == 1};
    boolean mbr_ar[]  = {value == 2};
    boolean mbru_ar[] = {value == 3};
    boolean sp_ar[]   = {value == 4};
    boolean lv_ar[]   = {value == 5};
    boolean cpp_ar[]  = {value == 6};
    boolean tos_ar[]  = {value == 7};
    boolean opc_ar[]  = {value == 8};

    mdr_cl.setValue(mdr_ar);
    pc_cl.setValue(pc_ar);
    mbr_cl.setValue(mbr_ar);
    mbru_cl.setValue(mbru_ar);
    sp_cl.setValue(sp_ar);
    lv_cl.setValue(lv_ar);
    cpp_cl.setValue(cpp_ar);
    tos_cl.setValue(tos_ar);
    opc_cl.setValue(opc_ar);
  }
}
