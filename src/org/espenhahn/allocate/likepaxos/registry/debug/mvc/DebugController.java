package org.espenhahn.allocate.likepaxos.registry.debug.mvc;

import org.espenhahn.allocate.likepaxos.registry.debug.mvc.objects.Circle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class DebugController {
	private Queue<String> commands;

    private Map<String, PaxosNodeModel> nodes;
    private DebugRenderer renderer;

	public DebugController(DebugRenderer renderer) {
		this.commands = new LinkedList<>();
		this.nodes = new HashMap<>();
		this.renderer = renderer;

		renderer.nextButton.addActionListener((event) -> processNext());
	}

	public void appendCommand(String src, String cmd) {
		this.commands.add(src + ":" + cmd);
	}

	private boolean processNext() {
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

		if (cmds == null)
			return false;

		String src = cmds[0];
		String cmd = cmds[1];
		switch (cmd) {
		    case "register":
                register(Short.parseShort(cmds[2]), src);
                break;
            case "connect":
                connect(src, cmds[2]);
                break;
            case "accept":
                accept(src, Double.parseDouble(cmds[2]), cmds[3]);
                break;
            case "propose":
                propose(src, Double.parseDouble(cmds[2]), cmds[3]);
                break;
            case "reject":
                reject(src, Double.parseDouble(cmds[2]));
                break;
        }

		return true;
	}

	private void register(short clientID, String clientName) {
		PaxosNodeModel node = new PaxosNodeModel(clientName, clientID);

		Circle c = new Circle(0, 0, 30);
		node.setCircle(c);
		renderer.addCircleToRing(c);

		nodes.put(clientName, node);
	}

	private void connect(String src, String dest) {
		PaxosNodeModel m1 = nodes.get(src);
		PaxosNodeModel m2 = nodes.get(dest);

		renderer.addLineBetween(m1.getCircle(), m2.getCircle(), (l) -> renderer.removeShape(l));
	}

	private void propose(String src, double proposalNumber, String dest) {
		PaxosNodeModel m1 = nodes.get(src);
		PaxosNodeModel m2 = nodes.get(dest);

		renderer.addTextBetween("" + proposalNumber, m1.getCircle(), m2.getCircle(), (t) -> {
			renderer.removeShape(t);
			m2.setProposalNumber(proposalNumber);
		});
	}

	private void accept(String src, double proposalNumber, String dest) {
		PaxosNodeModel m1 = nodes.get(src);
		renderer.invoke(() -> {
			m1.setProposalNumber(null);
			m1.setAcceptedProposalNumber(proposalNumber);
		});

		PaxosNodeModel m2 = nodes.get(dest);
		if (m2 != null) renderer.addTextBetween("" + proposalNumber, m1.getCircle(), m2.getCircle(), (t) -> renderer.removeShape(t));
	}
	
	private void reject(String src, double proposalNumber) {
		PaxosNodeModel m1 = nodes.get(src);
		renderer.invoke(() -> m1.setProposalNumber(null));
	}
}
