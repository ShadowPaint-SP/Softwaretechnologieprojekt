package campingplatz.utils;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
public class Comment implements Serializable {

	private @Id @GeneratedValue long id;

	@Getter
	private String text;

	@Getter
	private int rating;

	@Getter
	private LocalDateTime time;

	public Comment(String text, int rating, LocalDateTime time) {
		this.text = text;
		this.rating = rating;
		this.time = time;
	}

	public Comment() {
	}

}
