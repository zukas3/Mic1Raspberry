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

/**
* Sets the high bit for instructions prefixed by WIDE
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class HighBit {

  private ControlLine mpc_cl = null,
    n_cl = null,
    z_cl = null,
    jam_cl = null;

  public HighBit(ControlLine n_cl, ControlLine z_cl, 
                 ControlLine jam_cl, ControlLine mpc_cl) {
    this.mpc_cl = mpc_cl;
    this.n_cl = n_cl;
    this.z_cl = z_cl;
    this.jam_cl = jam_cl;
  }

  public void poke() {
    boolean cl_array[] = 
                    {((jam_cl.getValue()[MIR.JAMN] && n_cl.getValue()[0]) ||
                    (jam_cl.getValue()[MIR.JAMZ] && z_cl.getValue()[0]))};
    mpc_cl.setValue(cl_array);
         //new boolean[] 
                    //{((jam_cl.getValue()[MIR.JAMN] && n_cl.getValue()[0]) ||
                    //(jam_cl.getValue()[MIR.JAMZ] && z_cl.getValue()[0]))});
  }
}
