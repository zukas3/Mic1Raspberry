/*
*
*  RunThread.java
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
*
* Modified thread to work with new Mic1 wrapper
*
*
*/

public class ThreadWrapper extends Thread {

  Mic1Wrapper sim = null;

  public ThreadWrapper(Mic1Wrapper sim) {
    super("Run");
    this.sim = sim;
  }

  public void run() {
    while (sim.run) {
      try{
      sleep(5); //We use 5 miliseconds instead of 1, because on stronger devices it just works a bit tad too fast
      }
      catch(Exception e) {}
      sim.cycle();
    }

    ServerManager.main.SendStatusMessage("MIC-1 has stopped");
    sim.stop();
  }
}
