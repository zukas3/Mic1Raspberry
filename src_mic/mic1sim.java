/*
*
*  mic1sim.java
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
import java.io.*;
import java.util.*;

/**
* Main component of the mic1 simulator.  All components are
* created in this class, and displayed in the mic1sim frame.
* mic1sim also handles all keyboard and mouse events, loads
* micro and macro programs, and coordinates the sequence of 
* activities for each clock cycle.
*
* @author 
*   Dan Stone (<a href="mailto:dans@ontko.com"><i>dans@ontko.com</i></a>),
*   Ray Ontko & Co,
*   Richmond, Indiana, US
*/
public class mic1sim extends Frame implements Mic1Constants {

  public static TextArea stdout = new TextArea(5, 50);
  public static boolean debug = false;
  public static boolean run = false;
  public static boolean halt = false;
  public static Vector key_buffer = new Vector();
  
  DebugFrame debug_frame = null;
  MenuBar menubar = null;
  Menu file = null;
  GridBagLayout gridbag = new GridBagLayout();
  RunThread run_thread = null;

  private Button reset_button = null;
  private Button run_button = null;
  private Button stop_button = null;
  private Button step_button = null;
  private Label mic1_status = null;
  private Label ijvm_status = null;
  private TextField next_micro = null;

  private int cycle_count;
  private boolean null_array[] = {false, false, false, false, false, false};
  private boolean true_array[] = {true};
  private boolean mp_loaded = false;

  // Components 
  private ControlStore control_store = null;
  private MainMemory main_memory = null;
  private ALU alu = null;
  private Shifter shifter = null;
  private Decoder decoder = null;
  private O o = null;
  private HighBit high_bit = null;

  // Registers
  public Register
    sp = null,
    lv = null,
    cpp = null,
    tos = null,
    opc = null,
    h = null;
  private MAR mar = null;
  private PC pc = null;
  private MDR mdr = null;
  private MBR mbr = null;
  private MPC mpc = null;
  private MIR mir = null;

  // Busses
  private Bus 
    byte_address_bus = null, 
    word_address_bus = null,
    byte_data_bus = null,
    word_data_bus = null,
    a_bus = null,
    b_bus = null,
    c_bus = null,
    alu_bus = null;
  private ObjectBus mir_bus = null;

  // Component control lines
  private IntControlLine
    control_store_cl = null,
    decoder_cl = null,
    o_addr_cl = null,  // Carries next microinstruction address to O
    o_mbr_cl = null,   // Carries MBR value to O
    mpc_cl = null;     // Carries O value to MPC
  private ControlLine 
    memory_cl = null,
    alu_cl = null,
    shifter_cl = null,
    o_jmpc_cl = null,    // Carries JMPC from MIR to O
    jam_cl = null,       // Carries JAMN/JAMZ from MIR to High bit
    high_bit_cl = null,  // Carries bit from High bit to MPC
    n_cl = null,         // Negative flag from ALU to High bit
    z_cl = null;         // Zero flag from ALU to High bit

  // Register control lines
  //   Each register has 2 control lines, one to indicate whether to store the value
  //   on the IN bus, the other to indicate whether to put its value onto the OUT
  //   bus.  Two control lines are used instead of one because the control signals
  //   come from two different sources--STORE from the MIR, and PUT from the decoder--
  //   and the two operations occur at different times in the clock cycle
  private ControlLine
    mar_store_cl = null,
    mar_put_cl = null,
    mdr_store_cl = null,
    mdr_put_cl = null,
    mdr_mem_cl = null,
    pc_store_cl = null,
    pc_put_cl = null,
    mbr_store_cl = null,
    mbr_put_cl = null,  // MBR stores only on a memory fetch, so it does not have a 
    mbru_put_cl = null, // store control line.  It has 2 put control lines, signed and unsigned
    sp_store_cl = null,
    sp_put_cl = null,
    lv_store_cl = null,
    lv_put_cl = null,
    cpp_store_cl = null,
    cpp_put_cl = null,
    tos_store_cl = null,
    tos_put_cl = null,
    opc_store_cl = null,
    opc_put_cl = null,
    h_store_cl = null,
    h_put_cl = null;  // In the dual-bus data path, this control line will always be asserted


  //**************************************************************************
  /**   Constructor.  Creates all components, busses, and control lines
  */
  public mic1sim() {
    super("Mic-1 Simulator");
    init();
  }

  public mic1sim(String filename) {
    super("Mic-1 Simulator");
    init();
    loadMicroprogram(filename);
  }

  public mic1sim(String file1, String file2) {
    super("Mic-1 Simulator");
    init();
    loadMicroprogram(file1);
    loadProgram(file2);
  }

  private void init() {
    stdout.setFont(new Font("Courier",Font.PLAIN,12));
    menubar = new MenuBar();
    setMenuBar(menubar);
    file = new Menu("File");
    file.add(new MenuItem("Load Microprogram"));
    file.add(new MenuItem("Load Macroprogram"));
    file.add(new MenuItem("Open Debug Window"));
    file.add(new MenuItem("Exit"));
    menubar.add(file);
    next_micro = new TextField();
    next_micro.setEditable(false);

    // First, create busses and control lines

    // Busses    
    byte_address_bus = new Bus();
    word_address_bus = new Bus();
    byte_data_bus = new Bus();
    word_data_bus = new Bus();
    a_bus = new Bus();
    b_bus = new Bus();
    c_bus = new Bus();
    alu_bus = new Bus();
    mir_bus = new ObjectBus();

    // Control lines
    control_store_cl = new IntControlLine();
    memory_cl = new ControlLine();
    alu_cl = new ControlLine();
    shifter_cl = new ControlLine();
    decoder_cl = new IntControlLine();
    o_mbr_cl = new IntControlLine();
    o_addr_cl = new IntControlLine();
    o_jmpc_cl = new ControlLine();
    mpc_cl = new IntControlLine();
    jam_cl = new ControlLine();
    high_bit_cl = new ControlLine();
    n_cl = new ControlLine();
    z_cl = new ControlLine();
    
    mar_store_cl = new ControlLine();
    mar_put_cl = new ControlLine();
    mdr_store_cl = new ControlLine();
    mdr_put_cl = new ControlLine();
    mdr_mem_cl = new ControlLine();
    pc_store_cl = new ControlLine();
    pc_put_cl = new ControlLine();
    mbr_store_cl = new ControlLine();
    mbr_put_cl = new ControlLine();
    mbru_put_cl = new ControlLine();
    sp_store_cl = new ControlLine();
    sp_put_cl = new ControlLine();
    lv_store_cl = new ControlLine();
    lv_put_cl = new ControlLine();
    cpp_store_cl = new ControlLine();
    cpp_put_cl = new ControlLine();
    tos_store_cl = new ControlLine();
    tos_put_cl = new ControlLine();
    opc_store_cl = new ControlLine();
    opc_put_cl = new ControlLine();
    h_store_cl = new ControlLine();
    h_put_cl = new ControlLine();
    h_put_cl.setValue(true_array);

    // Next, create components and registers

  // Registers
    mar = new MAR(c_bus, word_address_bus, mar_store_cl, memory_cl);
    sp = new Register(c_bus, b_bus, "SP", sp_store_cl, sp_put_cl);
    lv = new Register(c_bus, b_bus, "LV", lv_store_cl, lv_put_cl);
    cpp = new Register(c_bus, b_bus, "CPP", cpp_store_cl, cpp_put_cl);
  
    tos = new Register(c_bus, b_bus, "TOS", tos_store_cl, tos_put_cl);
    opc = new Register(c_bus, b_bus, "OPC", opc_store_cl, opc_put_cl);
    h = new Register(c_bus, a_bus, "H", h_store_cl, h_put_cl);
    pc = new PC(c_bus, b_bus, byte_address_bus, pc_store_cl, pc_put_cl, memory_cl);
    mdr = new MDR(c_bus, b_bus, word_data_bus, mdr_store_cl, mdr_put_cl, mdr_mem_cl, memory_cl);
    mbr = new MBR(byte_data_bus, b_bus, mbr_store_cl, mbr_put_cl, mbru_put_cl, o_mbr_cl);
    mir = new MIR(o_addr_cl, o_jmpc_cl, jam_cl, shifter_cl, alu_cl, mar_store_cl, mdr_store_cl,
		  pc_store_cl, sp_store_cl, lv_store_cl, cpp_store_cl, tos_store_cl, opc_store_cl, 
		  h_store_cl, memory_cl, decoder_cl, mir_bus);

    // Components
    control_store = new ControlStore(control_store_cl, mir_bus);
    mpc = new MPC(mpc_cl, high_bit_cl, control_store_cl, control_store, next_micro);
    main_memory = new MainMemory(byte_address_bus, word_address_bus, 
				 byte_data_bus, word_data_bus, memory_cl, mbr_store_cl, mdr_mem_cl, mdr);
    alu = new ALU(a_bus, b_bus, alu_bus, alu_cl, n_cl, z_cl);
    shifter = new Shifter(alu_bus, c_bus, shifter_cl);
    decoder = new Decoder(decoder_cl, mdr_put_cl, pc_put_cl, mbr_put_cl, mbru_put_cl,
			  sp_put_cl, lv_put_cl, cpp_put_cl, tos_put_cl, opc_put_cl);
    o = new O(o_addr_cl, o_mbr_cl, o_jmpc_cl, mpc_cl);
    high_bit = new HighBit(n_cl, z_cl, jam_cl, high_bit_cl);
    
    initializeFrame();

  }

  //*********************************************************************************************

  private void clearControlLines() {
    mbr_store_cl.setValue(null_array);
    mdr_mem_cl.setValue(null_array);
    memory_cl.setValue(null_array);
    alu_cl.setValue(null_array);
    shifter_cl.setValue(null_array);
    o_jmpc_cl.setValue(null_array);
    jam_cl.setValue(null_array);
    high_bit_cl.setValue(null_array);
    n_cl.setValue(null_array);
    z_cl.setValue(null_array);
    mar_store_cl.setValue(null_array);
    mar_put_cl.setValue(null_array); 
    mdr_store_cl.setValue(null_array);
    mdr_put_cl.setValue(null_array);
    pc_store_cl.setValue(null_array);
    pc_put_cl.setValue(null_array);
    mbr_put_cl.setValue(null_array); 
    mbru_put_cl.setValue(null_array);
    sp_store_cl.setValue(null_array);
    sp_put_cl.setValue(null_array);
    lv_store_cl.setValue(null_array);
    lv_put_cl.setValue(null_array); 
    cpp_store_cl.setValue(null_array);
    cpp_put_cl.setValue(null_array); 
    tos_store_cl.setValue(null_array);
    tos_put_cl.setValue(null_array);
    opc_store_cl.setValue(null_array);
    opc_put_cl.setValue(null_array);
    h_store_cl.setValue(null_array);



  }


  //************************************************************************************************


  public void run() {
    run = true;
    halt = false;
    run_button.disable();
    step_button.disable();
    reset_button.disable();
    stop_button.enable();
    run_thread = new RunThread(this);
    run_thread.start();
  }

  public void stop() {
    run = false;
    if (halt)
      run_button.disable();
    else
      run_button.enable();
    step_button.enable();
    reset_button.enable();
    stop_button.disable();
    refresh();
  }

  public void step() {
    cycle();
  }

  public void reset() {
    if (mp_loaded) {
      halt = false;
      if (debug)
        debug_frame.text.setText("");
      stdout.setText("");
      cycle_count = 0;
      control_store_cl.setValue(0);
      mir.reset();
      clearControlLines();
      mpc.forceValue(0);
      pc.forceValue(-1);
      mbr.forceValue(0);
      mar.forceValue(0);
      mdr.forceValue(0);
      opc.forceValue(0);
      tos.forceValue(0);
      h.forceValue(0);
      sp.forceValue(SP);
      lv.forceValue(LV);
      cpp.forceValue(CPP);
      key_buffer.removeAllElements();
      run_button.enable();
      step_button.enable();
      reset_button.enable();
      stop_button.disable();
      main_memory.reset();
  
      control_store_cl.setValue(0);
      decoder_cl.setValue(0);
      o_mbr_cl.setValue(0); 
      o_addr_cl.setValue(0); 
      mpc_cl.setValue(0); 
  
      byte_address_bus.setValue(0);
      word_address_bus.setValue(0);
      byte_data_bus.setValue(0);
      word_data_bus.setValue(0); 
      a_bus.setValue(0);
      b_bus.setValue(0);
      c_bus.setValue(0);
      alu_bus.setValue(0);
    }
    else {
      run_button.disable();
      step_button.disable();
      reset_button.disable();
      stop_button.disable();
   }
  }

  public void cycle() {
    cycle_count++;
    if (debug)
      debug_frame.text.appendText("-----------------Start cycle " + cycle_count + "----------------------\n");
    clearControlLines();
    // Drive next microinstruction onto control lines
    control_store.poke();
    mir.poke();

    // Data path cycle
    decoder.poke();
    mdr.put();
    pc.put();
    mbr.put();
    sp.put();
    lv.put();
    cpp.put();
    tos.put();
    opc.put();
    h.put();
    alu.poke();
    shifter.poke();

    mar.store();
    mdr.store();
    pc.store();
    sp.store();
    lv.store();
    cpp.store();
    tos.store();
    opc.store();
    h.store();

    pc.mem();
    mar.mem();
    mdr.mem();
    main_memory.poke();
    mbr.store();

    // Control path cycle
    high_bit.poke();
    o.poke();
    mpc.poke();
    if (!run)
      refresh();
  }

  public static void halt() {
    run = false;
    halt = true;
    stdout.appendText("\nEnd of run.\n");
  }

  public void refresh() {
    mdr.refresh();
    pc.refresh();
    mbr.refresh();
    sp.refresh();
    lv.refresh();
    cpp.refresh();
    tos.refresh();
    opc.refresh();
    h.refresh();
    mar.refresh();
  }

  public void setControlStore(Mic1Instruction instructions[]) {
    control_store.setStore(instructions);
  }

  private void setMemory(byte[] bytes) {
    main_memory.setMemory(bytes);
  }

  private void programDialog() {
    FileDialog fd = new FileDialog(this, "Load Macroprogram", FileDialog.LOAD);
    fd.setFile("*.ijvm");
    fd.show();
    // fd.paintAll(fd.getGraphics());
    if (fd.getFile() != null) {
      loadProgram(fd.getDirectory() + fd.getFile());
    }
  }

  public void loadProgram(String filename) {
    ErrorDialog err = null;
    try {
      IJVMLoader loader = new IJVMLoader(filename);
      if (loader.isValid() == null) {
        setMemory(loader.getProgram());
        reset();
      }
      else
        err = new ErrorDialog("Error loading program", loader.isValid());
    }
    catch (FileNotFoundException fnfe) {
      err = new ErrorDialog("Error opening program", "File not found: " + filename);
    }
    catch (IOException ioe) {
      System.out.println("Error loading macroprogram");
    }
  }
   
  private void microprogramDialog() {
    FileDialog fd = new FileDialog(this, "Load Microprogram", FileDialog.LOAD);
    fd.setFile("*.mic1");
    fd.show();
    // fd.paintAll(fd.getGraphics());
    if (fd.getFile() != null) {
      loadMicroprogram(fd.getDirectory() + fd.getFile());
    } 
  }
  
  public void loadMicroprogram(String filename) {
    ErrorDialog err = null;
    Mic1Instruction microprogram[] = new Mic1Instruction[612];
    try {
      InputStream in = new FileInputStream(filename);
      boolean eof = false;
      if (in.read() != mic1_magic1 ||
          in.read() != mic1_magic2 ||
          in.read() != mic1_magic3 ||
          in.read() != mic1_magic4) {
        err = new ErrorDialog("Error opening file", 
          "Error loading Microprogram: invalid file format: " + filename);
        mp_loaded = false;
      }
      else {
        int i = 0;
        while (!eof) {
	  microprogram[i] = new Mic1Instruction();
	  eof = (microprogram[i].read(in) == -1);
	  i++;
        }
        setControlStore(microprogram);
        mp_loaded = true;
      }
    }
    catch (FileNotFoundException fnfe) {
      err = new ErrorDialog("Error opening file", 
        "Error loading Microprogram: file not found: " + filename);
      mp_loaded = false;
    }
    catch (IOException ioe) { 
      err = new ErrorDialog("Error reading file",
        "Error loading Microprogram: error reading file: " + filename);
      mp_loaded = false;
    }
    reset();
  }

  public boolean handleEvent(Event event) {
    switch (event.id) {
    case Event.ACTION_EVENT :
      if (event.target instanceof MenuItem) {
	if (((String)event.arg).equals("Load Microprogram"))
	  microprogramDialog();
	if (((String)event.arg).equals("Load Macroprogram"))
	  programDialog();
	if (((String)event.arg).equals("Open Debug Window")) {
	  debug_frame = new DebugFrame();
	  debug = true;
	}
	if (((String)event.arg).equals("Exit")) {
	  System.exit(0);
	}
      }
      else if (event.target == run_button)
	run();
      else if (event.target == stop_button)
	stop();
      else if (event.target == step_button)
	step();
      else if (event.target == reset_button)
	reset();
      break;
    case Event.KEY_ACTION:
    case Event.KEY_PRESS :
      key_buffer.addElement(new Character((char)event.key));
      //System.out.println("Key pressed: " + (char)event.key);
      break;
    case Event.WINDOW_DESTROY:
      System.exit(0);
      break;
    }
    return true;
  }

  private void initializeFrame() {
    setLayout(gridbag);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.insets = new Insets(2,5,1,5);

    c.weightx = 1.0; c.weighty = 0.0;

    c.gridx = 0; c.gridy = 0; c.gridwidth = 2; c.gridheight = 1;
    c.gridwidth=2; constrain(new Label("Registers"),c);
    c.gridwidth=1;
    c.gridy=1; constrain(new Label("MAR"),c);
    c.gridy=2; constrain(new Label("MDR"),c);
    c.gridy=3; constrain(new Label("PC"),c);
    c.gridy=4; constrain(new Label("MBR"),c);
    c.gridy=5; constrain(new Label("SP"),c);
    c.gridy=6; constrain(new Label("LV"),c);
    c.gridy=7; constrain(new Label("CPP"),c);
    c.gridy=8; constrain(new Label("TOS"),c);
    c.gridy=9; constrain(new Label("OPC"),c);
    c.gridy=10; constrain(new Label("H"),c);
    c.gridx=1;
    c.gridy=1; constrain(mar,c);
    c.gridy=2; constrain(mdr,c);
    c.gridy=3; constrain(pc,c);
    c.gridy=4; constrain(mbr,c);
    c.gridy=5; constrain(sp,c);
    c.gridy=6; constrain(lv,c);
    c.gridy=7; constrain(cpp,c);
    c.gridy=8; constrain(tos,c);
    c.gridy=9; constrain(opc,c);
    c.gridy=10; constrain(h,c);

    c.gridx=2; c.gridy=0; c.gridwidth=2;
    constrain(new Label("Components"),c);
    c.gridwidth=1;
    c.gridy=1; constrain(new Label("Microinstruction"),c);
    c.gridy=2; constrain(new Label("NextMicroinstruction"),c);
    c.gridy=3; constrain(new Label("MPC"),c);
    c.gridy=4; constrain(new Label("ALU"),c);
    c.gridwidth=2;
    c.gridy=5; constrain(new Label("Standard out"),c);

    c.gridwidth=1; c.gridx=3;
    c.gridy=1; constrain(mir,c);
    c.gridy=2; constrain(next_micro,c);
    c.gridy=3; constrain(mpc,c);
    c.gridy=4; constrain(alu,c);
    c.gridwidth=2;
    c.gridx=2;
    c.gridheight=5;
    c.gridy=6; constrain(stdout,c);

    Panel button_panel = new Panel();
    button_panel.setLayout(new GridLayout(1,4,5,5));

    run_button = new Button("Run");
    run_button.enable(false);
    stop_button = new Button("Stop");
    stop_button.enable(false);
    step_button = new Button("Step");
    step_button.enable(false);
    reset_button = new Button("Reset");
    reset_button.enable(false);

    button_panel.add(run_button);
    button_panel.add(stop_button);
    button_panel.add(step_button);
    button_panel.add(reset_button);

    c.gridx = 0; c.gridy = 11; c.gridwidth = 5; c.gridheight = 1;
    c.weightx = 1.0;
    c.weighty = 0.0;
    constrain(button_panel, c);

    show();
    resize(preferredSize());
    paintAll(getGraphics());
  }

  private void constrain(Component component, GridBagConstraints constraints) {
    ((GridBagLayout)getLayout()).setConstraints(component, constraints);
    add(component);
  }

  public static void main(String args[]) {
    mic1sim s = null;
    stdout.setEditable(false);
    if (args.length == 1)
      s = new mic1sim(args[0]);
    else if (args.length == 2)
      s = new mic1sim(args[0], args[1]);
    else 
      s = new mic1sim();
  }
}
