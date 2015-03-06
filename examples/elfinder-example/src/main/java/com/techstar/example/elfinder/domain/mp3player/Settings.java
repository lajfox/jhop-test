package com.techstar.example.elfinder.domain.mp3player;

public class Settings {

	private int width = 120;
	private int height = 110;
	private boolean autoLoad = true;
	private boolean autoPlay = true;
	private boolean continuousPlay = true;
	private boolean onCompleteJumpToNext = true;
	private boolean repeat = true;
	private boolean shuffleSongs = false;
	private boolean showSongNumber = false;
	private int initialVolume = 70;
	private int bufferTime = 5;
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isAutoLoad() {
		return autoLoad;
	}
	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
	}
	public boolean isAutoPlay() {
		return autoPlay;
	}
	public void setAutoPlay(boolean autoPlay) {
		this.autoPlay = autoPlay;
	}
	public boolean isContinuousPlay() {
		return continuousPlay;
	}
	public void setContinuousPlay(boolean continuousPlay) {
		this.continuousPlay = continuousPlay;
	}
	public boolean isOnCompleteJumpToNext() {
		return onCompleteJumpToNext;
	}
	public void setOnCompleteJumpToNext(boolean onCompleteJumpToNext) {
		this.onCompleteJumpToNext = onCompleteJumpToNext;
	}
	public boolean isRepeat() {
		return repeat;
	}
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	public boolean isShuffleSongs() {
		return shuffleSongs;
	}
	public void setShuffleSongs(boolean shuffleSongs) {
		this.shuffleSongs = shuffleSongs;
	}
	public boolean isShowSongNumber() {
		return showSongNumber;
	}
	public void setShowSongNumber(boolean showSongNumber) {
		this.showSongNumber = showSongNumber;
	}
	public int getInitialVolume() {
		return initialVolume;
	}
	public void setInitialVolume(int initialVolume) {
		this.initialVolume = initialVolume;
	}
	public int getBufferTime() {
		return bufferTime;
	}
	public void setBufferTime(int bufferTime) {
		this.bufferTime = bufferTime;
	}
	
	
}
