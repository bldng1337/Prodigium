package me.engine.Entity;

enum Animation {
	INTERACTION(1), DEATH(2), ATTACKING(3), IDLE(4), RUNNING(5);
	byte txtindex;

	private Animation(int b) {
		txtindex = (byte) b;
	}

	public int gettextureindex() {
		return txtindex;
	}
}