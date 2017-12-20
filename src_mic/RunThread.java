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
* Thread which allows mic1 to continuously cycle.  Without running
* the cycles in a background thread, the simulator would be unable to 
* respond to mouse or keyboard events (such as clicking the "Stop" button).
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class RunThread extends Thread {

  mic1sim sim = null;

  public RunThread(mic1sim sim) {
    super("Run");
    this.sim = sim;
  }

  public void run() {
    while (sim.run) {
      try{
      sleep(1);
      }
      catch(Exception e) {}
      sim.cycle();
    }
    sim.stop();
  }
}
