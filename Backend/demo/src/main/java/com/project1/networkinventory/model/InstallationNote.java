package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "installation_note")
public class InstallationNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long noteId;

    private String author; // technician name or id string

    @Column(columnDefinition = "TEXT")
    private String note;

    public Long getNoteId() {
		return noteId;
	}

	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	private LocalDateTime timestamp = LocalDateTime.now();
}
