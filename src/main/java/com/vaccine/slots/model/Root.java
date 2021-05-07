package com.vaccine.slots.model;

import java.util.List;

public class Root{
    public List<Sessions> sessions;

	public List<Sessions> getSessions() {
		return sessions;
	}

	public void setSessions(List<Sessions> sessions) {
		this.sessions = sessions;
	}

	@Override
	public String toString() {
		return "Root [sessions=" + sessions.toString() + "]";
	}
	
}

