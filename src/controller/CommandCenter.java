package controller;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.DesignMode;
import java.util.ArrayList;

import javax.swing.DefaultSingleSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;
import simulation.Simulator;
import view.Chat;
import view.checkGameover;
import view.game;
import view.yarab;

public class CommandCenter implements SOSListener ,ActionListener {
	private checkGameover g;
	private int currentcycle;
	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private yarab design ;
	private JButton start;
	private JButton exit;
	private game play;
	private JLabel background ;
	private JButton notres;
	private JButton green;
	private JButton yellow;
	private JButton red;
	
	private JLabel back;

	@SuppressWarnings("unused")
	private ArrayList<Unit> emergencyUnits;
    
	private JPanel grid;
	private JPanel unitpan;
	private JTextArea infopan;
	private JPanel control ;
	private JButton[][] a;
	private JLabel curcycle , numdead ;
	private JButton nextcycle , end;
	private JButton respond ;
	private JPanel rescuepanal;
	private JButton retry;
	private JButton cancel;
	private ArrayList<JButton> unitbutton;
	private JButton[] ub;
	private JLabel mes;
	private JScrollPane s ;
	private JButton chat;
	
	public CommandCenter() throws Exception  {
		
		engine = new Simulator(this);
		a=new JButton[10][10];
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		////////////////////
		design =new yarab();
		start =new JButton("Start new game");
		Font sf = new Font("Arial", Font.BOLD, 16); 
		start.setFont(sf);
		currentcycle=0;
		// 	start.setBackground();
		start.setBounds(800,500,200,60);
		start.setBorderPainted(true);
//		start.setBackground(Color.WHITE);
		start.addActionListener(this);
		JButton exit =new JButton("Exit game");
		//	exit.setBackground(Color.red);
		exit.setBounds(800,600,200,60);
		exit.addActionListener(this);
		exit.setFont(sf);
		design.add(start);
		design.add(exit);
		background=new JLabel();
		background.setBounds(0,0,design.getWidth(),design.getHeight());
		design.add(background);
		
		Image img=new ImageIcon(design.getClass().getResource("/back.jpg")).getImage();
		background.setIcon(new ImageIcon(img));
		
        ///////////////
		
		
	}

	public void addunits(Unit b) {
		
	}
	
	public void updateinfo(Simulatable s) {
		String a ="";
		if(s instanceof Citizen) {
			a=((Citizen)s).toString();
		}
		else if(s instanceof ResidentialBuilding) {
			a=((ResidentialBuilding)s).toString();
		}
		else if(s instanceof Unit) {
			a=((Unit)s).toString();
		}
		else{
			a=((Disaster)s).toString();
		}
		infopan.setText(a);
}

	
	@Override
	public void receiveSOSCall(Rescuable r) {
		
		if (r instanceof ResidentialBuilding) {
			
			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);
			
		} else {
			
			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}

	}
	int w=0;
	Unit x=null;
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand()=="Cancel Respond") {
			mes.setVisible(false);
			w=0;
		}
		else if(w==1) {
			x=findu((JButton) e.getSource());
			if(x==null) {
				mes.setText("...WARNING... Please choose a unit");				
			}
			else {
				mes.setText("Now enter citizen or building");
				w++;
			}
		}
		else if(w==2) {
			boolean b=loopboth((JButton) e.getSource());
			if(b==false) {
				mes.setText("...WARNING... Please choose citizen or building");
			}
			else {
				w=0;
				mes.setVisible(false);
			}
		}
		
		else if (e.getActionCommand()=="Respond") {
			w=1;
			mes.setVisible(true);
			mes.setText("NOW enter unit");
			
		}
		else if(w==0) {
			if (e.getActionCommand()=="Start new game") {
				design.setVisible(false);
				creatnew();
			}
			
			else if (e.getActionCommand()=="Exit game") {
				System.exit(0);
			}
			
			else if(e.getActionCommand()=="Back") {
				play.dispose();
				try {
					new CommandCenter();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			
			else if(e.getActionCommand()=="Next Cycle") {
				rescuepanal.removeAll();
				callnextcycle();
			}
			
			else if(e.getActionCommand()=="Retry") {
				play.dispose();
				design.dispose();
				g.dispose();
				try {
					CommandCenter c=new CommandCenter();
					c.design.setVisible(false);
					c.creatnew();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					
					e1.printStackTrace();
				}
			}
			else if(e.getActionCommand()=="Cancel") {
				g.dispose();
				design.dispose();
				play.dispose();
				try {
					new CommandCenter();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(e.getSource()==chat) {
				Chat c=new Chat();
			}
			outer:for(int i=0;i<10;i++) {
				for(int j=0;j<10;j++) {
					if(e.getSource()==(a[i][j])) {
						rescuepanal.removeAll();
						rescuepanal.updateUI();
						boolean f=true;
						for(ResidentialBuilding x:visibleBuildings) {
							if(x.getLocation().getX()==i &&x.getLocation().getY()==j) {
								updateinfo(x);
								JButton b=new JButton();
								b.setOpaque(false);
								b.setContentAreaFilled(false);
								b.setBorderPainted(false);
								addimg(b,"/building.png");
								rescuepanal.add(b);
								for(int u=0;u<x.getOccupants().size();u++) {
									JButton a=new JButton();
									a.setOpaque(false);
									a.setContentAreaFilled(false);
									a.setBorderPainted(false);
									rescuepanal.add(a);
									addimg(a,"/citezen.png");
								}
								f=false;
							}
						}
						if(f) {
							for(Citizen x:visibleCitizens) {
								if(x.getLocation().getX()==i &&x.getLocation().getY()==j) {
									updateinfo(x);
									JButton a=new JButton();
									a.setOpaque(false);
									a.setContentAreaFilled(false);
									a.setBorderPainted(false);
									rescuepanal.add(a);
									addimg(a,"/citezen.png");
								}
							}
						}
						for(Unit u:emergencyUnits) {
							
							if(u.getLocation().getX()==i && u.getLocation().getY()==j) {
								JButton a=new JButton();
								a.setOpaque(false);
								a.setContentAreaFilled(false);
								a.setBorderPainted(false);
								rescuepanal.add(a);
								
								
								if(u instanceof Ambulance) {
										addimg(a,"/ambulance.jpg");
									
								}
								else if(u instanceof FireTruck) {
										addimg(a,"/firetruck.jpg");
								}
								else if(u instanceof GasControlUnit) {
									addimg(a,"/gascontrol.jpg");
										
								}
								else if(u instanceof DiseaseControlUnit) {
									addimg(a,"/infcon.jpg");
								}
								else if(u instanceof Evacuator) {
									addimg(a,"/evacuator.jpg");
				
								}
							}
						}
						rescuepanal.updateUI();
						break outer;
					}
						
				}
			}
			int i=0;
			boolean bool=false;
			for(;i<ub.length;i++) {
				if(e.getSource()==ub[i]) {
					bool=true;
					break;
				}
			}
			if(bool)
				updateinfo(engine.getEmergencyUnits().get(i));
		}
		
	}

	public void creatnew() {
		play=new game();
		GridLayout g=new GridLayout(10 , 10,10,10);
		grid=new JPanel(g);
		grid.setBounds(300,0,1200,800);
		grid.setOpaque(false);
		grid.setBackground(new Color(0, 0, 0,0));
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				a[i][j]=new JButton();
				grid.add(a[i][j]);
//				findimg(i, j, a[i][j]);
				a[i][j].addActionListener(this);
			}
		}
		play.add(grid);
		/////////////////////////////
		infopan =new JTextArea("INFO");
		Font newFont = new Font("Arial", Font.BOLD, 16); 
		infopan.setFont(newFont);
		infopan.setOpaque(false);
		infopan.setLineWrap(true);
		infopan.setText("          INFO");
		s=new JScrollPane(infopan);
		s.setBounds(0,0,300,800);
		infopan.setBounds(0,0,300,800);
		
		s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		infopan.setEditable(false);
		s.setOpaque(false);
		s.getViewport().setOpaque(false);s.setBorder(null);
		s.setViewportBorder(null);
		infopan.setBorder(null);
		infopan.setBackground(new Color(0,0,0,0));
		play.add(s);
		////////////////////////////
		unitbutton=new ArrayList<>();
		unitpan =new JPanel();
		unitpan.setBounds(1500,0,play.getWidth()-1500,720);
		unitpan.setBackground(new Color(0, 0, 0,0));
		infopan.setForeground(Color.WHITE);
		Font f=new Font("Arial", Font.BOLD, 24);
		infopan.setFont(f);
		GridLayout lay=new GridLayout(10, 3,20,20);
		unitpan.setLayout(lay);
		int k=0;
		ub=new JButton[engine.getEmergencyUnits().size()];
		for(Unit x:engine.getEmergencyUnits()) {
			JButton b=new JButton();
			String s="";
			b.setBackground(Color.GREEN);
			if(x instanceof Ambulance) {
				s="Ambulance "+"Loc :- ("+x.getLocation().getX()+" , "+x.getLocation().getY()+")";
				b.setText(s);
			}
			else if(x instanceof DiseaseControlUnit) {
				s="DiseaseControlUnit "+"Loc :- ("+x.getLocation().getX()+" , "+x.getLocation().getY()+")";
				b.setText(s);
			}
			else if(x instanceof Evacuator) {
				s="Evacuator "+"Loc :- ("+x.getLocation().getX()+" , "+x.getLocation().getY()+")";
				b.setText(s);
			}
			else if(x instanceof FireTruck) {
				s="FireTruck "+"Loc :- ("+x.getLocation().getX()+" , "+x.getLocation().getY()+")";
				b.setText(s);
			}
			else if(x instanceof GasControlUnit) {
				s="GasControlUnit "+"Loc :- ("+x.getLocation().getX()+" , "+x.getLocation().getY()+")";
				b.setText(s);
			}
			b.setFont(f);
			b.addActionListener(this);
			unitbutton.add(b);
			ub[k]=b;
			k++;
			
			unitpan.add(b);
			
		}
		unitpan.setOpaque(false);
		play.add(unitpan); 
		
		respond =new JButton("Respond");
		respond.setFont(f);
		respond.addActionListener(this);
		respond.setBounds(320,70,140,40);
		
		notres =new JButton("Cancel Respond");
		notres.setFont(f);
		notres.addActionListener(this);
		notres.setBounds(460,70,280,40);
		
		
		control=new JPanel();
		control.setBounds(1200,800,play.getWidth()-1200,play.getHeight()-800);
		control.setLayout(null);
		end=new JButton("Back");
		end.addActionListener(this);
		end.setBounds(320,160,control.getWidth()-320,40);
		control.add(end);
		nextcycle=new JButton("Next Cycle");
		nextcycle.setFont(f);
		nextcycle.addActionListener(this);
		nextcycle.setBounds(320,120,control.getWidth()-320,40);
		control.add(nextcycle);
		end.setFont(f);
		numdead=new JLabel("Num Of Deadpeople    0");	
		curcycle=new JLabel("The Current Cycle    0");
		numdead.setFont(f);
		curcycle.setFont(f);
		numdead.setBounds(0,0,300,100);
		curcycle.setBounds(0,60,300,100);
		numdead.setFont(f);
		curcycle.setFont(f);
		numdead.setForeground(Color.WHITE);
		curcycle.setForeground(Color.WHITE);
		control.add(curcycle);
		control.add(numdead);
		control.add(respond);
		control.add(notres);
		control.setOpaque(false);
		control.setBackground(new Color(0, 0, 0,0));
		
		mes=new JLabel("WELCOME TO MY GAME");
		mes.setBounds(320,1,control.getWidth()-320,100);
		mes.setFont(new Font("Arial", Font.BOLD, 20));
		mes.setForeground(Color.RED);
		
		chat=new JButton("Chat");
		chat.addActionListener(
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						Chat c=new Chat();
					}
				}		
		);
		chat.setBounds(0,140,200,60);
		control.add(chat);
		chat.setFont(f);
		
		control.add(mes);
		play.add(control);
		green=new JButton("Idle");
		yellow=new JButton("Responding");
		red=new JButton("Treating");
		green.setBounds(1500,720,(play.getWidth()-1500)/3,80);
		yellow.setBounds(1500+(play.getWidth()-1500)/3,720,(play.getWidth()-1500)/3,80);
		red.setBounds(1500+2*(play.getWidth()-1500)/3,720,(play.getWidth()-1500)/3,80);
		
		Font q=(new Font("Arial", Font.BOLD,15));
		
		green.setBackground(Color.GREEN);
		red.setBackground(Color.RED);
		yellow.setBackground(Color.YELLOW);
		green.setFont(f);
		yellow.setFont(q);
		red.setFont(f);
		play.add(green);
		play.add(red);
		play.add(yellow);
		
		rescuepanal=new JPanel();
		rescuepanal.setBounds(0,800,1200,play.getHeight()-800);
		rescuepanal.setOpaque(false);
		rescuepanal.setBackground(new Color(0, 0, 0,0));
		rescuepanal.setLayout(new FlowLayout());
		
		play.add(rescuepanal);
		
		back=new JLabel();
		back.setBounds(0,0,play.getWidth(),play.getHeight());
		play.add(back);
		Image img=new ImageIcon(design.getClass().getResource("/ash.jpg")).getImage();
		back.setIcon(new ImageIcon(img));
		
	}
	public void creatgameover(int score) {
		g=new checkGameover(score);
		retry =new JButton("Retry");
		retry.setBounds(250,260,120,50);
		retry.addActionListener(this);
		g.add(retry);
		
		cancel =new JButton("Cancel");
		cancel.setBounds(400,260,120,50);
		cancel.addActionListener(this);
		g.add(cancel);
	}
	public Unit findu (JButton but) {
		if(unitbutton.contains(but)) {
			int ind=unitbutton.indexOf(but);
			return (engine.getEmergencyUnits().get(ind));
		}
		return null;

	}
	
	
	
	public boolean loopboth(JButton e) {
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				if(e==(a[i][j])) {
					
					boolean f=true;
					for(ResidentialBuilding a:visibleBuildings) {
						if(a.getLocation().getX()==i &&a.getLocation().getY()==j) {
							try {
								x.respond(a);
							} catch (IncompatibleTargetException e1) {
								JOptionPane.showMessageDialog(play,e1.getMessage());
							}catch (CannotTreatException e2) {
								JOptionPane.showMessageDialog(play,e2.getMessage());							}
							return true;
						}
					}
					if(f) {
						for(Citizen a:visibleCitizens) {
							if(a.getLocation().getX()==i &&a.getLocation().getY()==j) {
								
									try {
										x.respond(a);
									} catch (IncompatibleTargetException e1) {
										JOptionPane.showMessageDialog(play,e1.getMessage());
									}catch (CannotTreatException e2) {
										JOptionPane.showMessageDialog(play,e2.getMessage());	
									}
								return true;
							}
						}
					}
				}
					
			}
		}
		return false;
	}
	public void findimg(int i,int j,JButton b) {
		
		
		boolean f=true;
		for(ResidentialBuilding x:visibleBuildings) {
			if(x.getLocation().getX()==i &&x.getLocation().getY()==j) {
				addimg(b,"/building.png");
				f=false;
			}
		}
		if(f) {
			for(Citizen x:visibleCitizens) {
				if(x.getLocation().getX()==i &&x.getLocation().getY()==j) {
					addimg(b,"/citezen.png");
				}
			}
		}
	}
	public void addimg(JButton b,String s) {
		Image img=new ImageIcon(this.getClass().getResource(s)).getImage().getScaledInstance(100, 80, Image.SCALE_DEFAULT);
		b.setIcon(new ImageIcon(img));		
	}
	public void callnextcycle() {
		currentcycle++;
		try {
			engine.nextCycle();
		} catch (CitizenAlreadyDeadException e1) {
			JOptionPane.showMessageDialog(play,e1.getMessage());
		} catch (BuildingAlreadyCollapsedException e1) {
			JOptionPane.showMessageDialog(play,e1.getMessage());
		}
		curcycle.setText("The Current Cycle    "+currentcycle);
		numdead.setText("Num Of Deadpeople  "+engine.calculateCasualties());
		///////////		
		infopan.setText("Current Disasters \n");
			for(Citizen cit:visibleCitizens) {
				int x=cit.getLocation().getX();
				int y=cit.getLocation().getY();
				Disaster d=cit.getDisaster();
				if(cit.getState()==CitizenState.DECEASED ) {
					addimg(getbutton(x,y),"/dead.jpg");
				}
				else if(cit.getState()==CitizenState.IN_TROUBLE) {
					if(d instanceof Injury ) {
						addimg(getbutton(x, y), "/injury.png");
					}
					else if(d instanceof Infection) {
						addimg(getbutton(x, y), "/infcon.jpg");
					}
					infopan.append("\n"+d.toString()+"\n");
				}
				else {
					addimg(getbutton(x, y), "/citezen.png");
				}
			}
			for(ResidentialBuilding bulid:visibleBuildings) {
				
				int x=bulid.getLocation().getX();
				int y=bulid.getLocation().getY();
				Disaster d=bulid.getDisaster();
				if(d!=null && d.isActive()) {
					if(d instanceof Fire) {
						addimg(getbutton(x, y), "/fire.jpg");
					}
						
					else if(d instanceof GasLeak) {
						addimg(getbutton(x, y), "/gas leak.jpg");
					}
						
					else if(d instanceof Collapse) {
						addimg(getbutton(x, y), "/collapse.png");
					}
					infopan.append("\n"+d.toString()+"\n");
				}
				else {
					addimg(getbutton(x, y), "/building.png");
				}
				
			}
			
			for(Unit u :emergencyUnits) {
				int i=engine.getEmergencyUnits().indexOf(u);
				if(u instanceof Ambulance) {
					if(u.getLocation().getX()!=0 && u.getLocation().getY()!=0) {
						
						ub[i].setText("Ambulance  /"+"Loc :- ("+u.getLocation().getX()+" , "+u.getLocation().getY()+")");
						addimg(getbutton(u.getLocation().getX(),u.getLocation().getY()),"/ambulance.jpg");
					}
				}
				if(u instanceof FireTruck) {
					if(u.getLocation().getX()!=0 && u.getLocation().getY()!=0) {
						ub[i].setText("FireTruck  /"+"Loc :- ("+u.getLocation().getX()+" , "+u.getLocation().getY()+")");
						addimg(getbutton(u.getLocation().getX(),u.getLocation().getY()),"/firetruck.jpg");
					}
						
				}
				if(u instanceof GasControlUnit) {
					if(u.getLocation().getX()!=0 && u.getLocation().getY()!=0) {
						ub[i].setText("GasControlUnit  /"+"Loc :- ("+u.getLocation().getX()+" , "+u.getLocation().getY()+")");
						addimg(getbutton(u.getLocation().getX(),u.getLocation().getY()),"/gascontrol.jpg");
					}
						
				}
				if(u instanceof DiseaseControlUnit) {
					if(u.getLocation().getX()!=0 && u.getLocation().getY()!=0) {
						ub[i].setText("DiseaseControlUnit  /"+"Loc :- ("+u.getLocation().getX()+" , "+u.getLocation().getY()+")");
						addimg(getbutton(u.getLocation().getX(),u.getLocation().getY()),"/infcon.jpg");
					}
						
				}
				if(u instanceof Evacuator) {
					if(u.getLocation().getX()!=0 && u.getLocation().getY()!=0) {
						ub[i].setText("Evacuator  /"+"Loc :- ("+u.getLocation().getX()+"  ,  "+u.getLocation().getY()+")");
						addimg(getbutton(u.getLocation().getX(),u.getLocation().getY()),"/evacuator.jpg");
					}
						
				}
				if(u.getState()==UnitState.IDLE)
					ub[i].setBackground(Color.GREEN);
				else if(u.getState()==UnitState.RESPONDING)
					ub[i].setBackground(Color.yellow);
				else
					ub[i].setBackground(Color.RED);
			}
		if(engine.checkGameOver()) {
			creatgameover(engine.calculateCasualties());
			play.dispose();
		}
	}
	public JButton getbutton(int x,int y) {
		return a[x][y];
	}
	
	public static void main(String[] args) throws Exception {
		CommandCenter ahmed =new CommandCenter();
		
	}

}
