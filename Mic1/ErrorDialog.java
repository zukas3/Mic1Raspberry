/*
*
*  ErrorDialog.java
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
* Window used to display error messages.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class ErrorDialog extends Frame {

  private Button button = null;
  private Label label = null;

  public ErrorDialog(String title, String msg) {
    super(title);
    init(msg);
  }

  private void init(String msg) {
    setLayout(new BorderLayout(15,15));
    Label label = new Label(msg);
    add("Center", label);
    Button button = new Button("OK");
    add("South", button);
    show();
    resize(preferredSize());
    paintAll(getGraphics());
  }

  public boolean handleEvent(Event event) {
    switch (event.id) {
    case Event.ACTION_EVENT :
      if (event.target == button)
        dispose();
      else dispose();
      break;
    case Event.WINDOW_DESTROY:
      dispose();
      break;
    }
    return true;
  }

}
