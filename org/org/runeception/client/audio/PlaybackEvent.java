package org.runeception.client.audio;

public class PlaybackEvent {

	public PlaybackEvent(AdvancedPlayer source, int id, int frame) {
		this.id = id;
		this.source = source;
		this.frame = frame;
	}

	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}

	public int getFrame(){
		return frame;
	}
	
	public void setFrame(int frame){
		this.frame = frame;
	}

	public AdvancedPlayer getSource(){
		return source;
	}
	
	public void setSource(AdvancedPlayer source){
		this.source = source;
	}

	public static int STOPPED = 1;
	
	public static int STARTED = 2;
	
	public static int PAUSED = 3;

	private AdvancedPlayer source;
	
	private int frame;
	
	private int id;

}
