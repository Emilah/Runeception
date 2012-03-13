package org.runeception.client.audio;

import java.io.InputStream;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

public class AdvancedPlayer {

	public boolean isClosed() { 
		return closed;
	}
	
	public AdvancedPlayer(InputStream stream) throws JavaLayerException {
		this(stream, null);
	}

	public AdvancedPlayer(InputStream stream, AudioDevice device) throws JavaLayerException {
		bitstream = new Bitstream(stream);
		if (device != null)
			audio = device;
		else
			audio = FactoryRegistry.systemRegistry().createAudioDevice();
		audio.open(decoder = new Decoder());
	}
	
	public void setStream(InputStream stream) {
		bitstream = new Bitstream(stream);
	}
	
	public void play() throws JavaLayerException {
		play(Integer.MAX_VALUE);
	}
	

	public boolean play(int frames) throws JavaLayerException {
		boolean ret = true;
		if(listener != null)
			listener.playbackStarted(createEvent(PlaybackEvent.STARTED));
		while((frames-- > 0 && ret) || !MusicPlayer.paused) {
			ret = decodeFrame(); 
		} 
		MusicPlayer.wasPaused = true;
		AudioDevice out = audio;
		if (out != null) {
			out.flush();
			synchronized(this) {
				close();
			}
			if(listener != null)
				listener.playbackFinished(createEvent(out, PlaybackEvent.STOPPED));
		}
		return ret;
	}

	public synchronized void close() {
		AudioDevice out = audio;
		if (out != null) {
			closed = true;
			audio = null;
			out.close();
			try {
				bitstream.close();
			} catch (BitstreamException ex) {
				
			}
		}
	}
	
	protected boolean decodeFrame() throws JavaLayerException {
		try {
			AudioDevice out = audio;
			if (out == null)
				return false;
			Header header = bitstream.readFrame();
			if (header == null)
				return false;
			SampleBuffer output = (SampleBuffer) decoder.decodeFrame(header, bitstream);
			synchronized(this) {
				out = audio;
				if(out != null) {
					MusicPlayer.lastPosition += output.getBufferLength();
					//System.out.println(MusicPlayer.lastPosition);
					out.write(output.getBuffer(), 0, output.getBufferLength());
				}
			}
			bitstream.closeFrame();
		} catch (RuntimeException ex) {
			throw new JavaLayerException("Exception decoding audio frame", ex);
		}
		return true;
	}

	protected boolean skipFrame() throws JavaLayerException {
		Header header = bitstream.readFrame();
		if (header == null)
			return false;
		bitstream.closeFrame();
		return true;
	}

	public boolean play(final int start, final int end) throws JavaLayerException {
		boolean ret = true;
		int offset = start;
		while (offset-- > 0 && ret) {
			ret = skipFrame();
		}
		return play(end - start);
	}


	private PlaybackEvent createEvent(int id) {
		return createEvent(audio, id);
	}

	private PlaybackEvent createEvent(AudioDevice dev, int id) {
		return new PlaybackEvent(this, id, dev.getPosition());
	}

	public void setPlayBackListener(PlaybackListener listener) {
		this.listener = listener;
	}

	public PlaybackListener getPlayBackListener() {
		return listener;
	}
	
	public void stop() {
		listener.playbackFinished(createEvent(PlaybackEvent.STOPPED));
		close();
	}
	
	private Bitstream bitstream;
	
	private Decoder decoder;
	
	private AudioDevice audio;
	
	private boolean closed = false;
	
	private PlaybackListener listener;
	
}
