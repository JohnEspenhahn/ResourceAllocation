package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class DebugController {
	Queue<String> commands;
	
	Map<String, PaxosNodeModel> nodes;
	DebugRenderer renderer;
	
	public DebugController(DebugRenderer renderer) {
		this.commands = new LinkedList<String>();
		this.nodes = new HashMap<String, PaxosNodeModel>();
		this.renderer = renderer;
		
		renderer.nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				processNext();
			}
			
		});
	}
	
	public void appendCommand(String src, String cmd) {
		this.commands.add(src + ":" + cmd);
	}
	
	public boolean processNext() {	
		String[] cmds = null;
		while (!commands.isEmpty()) {
			String text_cmd = commands.remove();
			System.out.println("Rendering: " + text_cmd);
			
			cmds = text_cmd.split(":");
			if (cmds.length < 2) {
				cmds = null;
				continue;
			} else {
				break;
			}
		}
		
		if (cmds == null) return false;
		
		String src = cmds[0];
		String cmd = cmds[1];
		if (cmd.equals("register"))
			register(Short.parseShort(cmds[2]), src);
		else if (cmd.equals("connect"))
			connect(src, cmds[2]);
		else if (cmd.equals("accept"))
			accept(src, Double.parseDouble(cmds[2]));
		
		return true;
	}

	private void register(short clientID, String clientName) {
		PaxosNodeModel node = new PaxosNodeModel(clientName, clientID);
		
		Circle c = new Circle(0,0,20);
		node.setCircle(c);
		renderer.addCircleToRing(c);
		
		nodes.put(clientName, node);
	}
	
	private void connect(String src, String dest) {
		PaxosNodeModel m1 = nodes.get(src);
		PaxosNodeModel m2 = nodes.get(dest);
		
		renderer.addLineBetween(m1.getCircle(), m2.getCircle(), l -> renderer.removeShape(l));
	}
	
	private void accept(String src, double proposalNumber) {
		PaxosNodeModel m1 = nodes.get(src);
		renderer.invoke(() -> m1.setAcceptedProposalNumber(proposalNumber));
		
	}
}
