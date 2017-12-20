/*
*
*  DebugFrame.java
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
* Window that displays the full output text from various components.
* Consists of a static TextArea which components will write to if it
* exists.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class DebugFrame extends Frame {

  public static TextArea text = new TextArea(10,60);

  GridBagLayout gridbag = new GridBagLayout();
  Button cycle_button = null;

  public DebugFrame () {
    super("Debug Frame");
    add(text, "Center");
    show();
    resize(preferredSize());
    paintAll(getGraphics());
  }

  public boolean handleEvent(Event event) {
    if (event.id == Event.WINDOW_DESTROY) {
      dispose();
      mic1sim.debug = false;
    }
    return true;
  }

}
