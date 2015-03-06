package com.techstar.example.elfinder.domain.mp3player;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mp3player")
public class Mp3player {

	private Settings settings = new Settings();


	private List<Song> songs;

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	@XmlElementWrapper(name = "songs")
	@XmlElement(name = "song")	
	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

}
