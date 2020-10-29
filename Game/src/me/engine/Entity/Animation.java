package me.engine.Entity;

public enum Animation {
	INTERACTION(0), DEATH(1), ATTACKING(2), IDLE(3), RUNNING(4);
	byte txtindex;

	private Animation(int b) {
		txtindex = (byte) b;
	}

	public int gettextureindex() {
		return txtindex;
	}
}