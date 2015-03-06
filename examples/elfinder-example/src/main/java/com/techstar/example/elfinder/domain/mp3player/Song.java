package com.techstar.example.elfinder.domain.mp3player;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "song")
public class Song {

	private String url;
	private String songname;
	private String artist = "";

	public Song() {

	}

	public Song(String url, String songname) {
		this.url = url;
		this.songname = songname;
	}

	@XmlElement
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlElement
	public String getSongname() {
		return songname;
	}

	public void setSongname(String songname) {
		this.songname = songname;
	}

	@XmlElement
	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

}
