package campingplatz.utils;

import java.io.Serializable;
import java.time.LocalDateTime;

import campingplatz.firstName.firstName;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
public class Comment implements Serializable {

	@Getter
	private @Id @GeneratedValue long id;

	@Getter
	private String text;

	@Getter
	private int rating;

	@Getter
	private LocalDateTime time;

	@Getter
	private String firstName;

	@Getter
	private String lastName;

	public Comment(String text, int rating, LocalDateTime time, String firstName, String lastName) {
		this.text = text;
		this.rating = rating;
		this.time = time;
		this.firstName = firstName;
		this.lastName = lastName;

	}

	public Comment() {
	}

}
