package campingplatz.plots;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import campingplatz.utils.Utils;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

/**
 * Represents a comment in the campingplatz management system.
 * Each comment has an ID, text, rating, timestamp, and the first and last names
 * of the person who made the comment.
 */
@Entity
public class Comment implements Serializable {

	/**
	 * The unique identifier for the comment.
	 */
	@Getter
	private @Id @GeneratedValue UUID id;

	/**
	 * The text content of the comment.
	 */
	@Getter
	private String text;

	/**
	 * The rating given by the commenter.
	 */
	@Getter
	private int rating;

	/**
	 * The timestamp indicating when the comment was made.
	 */
	@Getter
	private LocalDateTime time;

	/**
	 * The first name of the person who made the comment.
	 */
	@Getter
	private String firstName;

	/**
	 * The last name of the person who made the comment.
	 */
	@Getter
	private String lastName;

	/**
	 * Constructs a new Comment with the specified text, rating, timestamp, and the
	 * first and last names of the commenter.
	 *
	 * @param text      the text content of the comment
	 * @param rating    the rating given by the commenter
	 * @param time      the timestamp indicating when the comment was made
	 * @param firstName the first name of the person who made the comment
	 * @param lastName  the last name of the person who made the comment
	 */
	public Comment(String text, int rating, LocalDateTime time, String firstName, String lastName) {
		this.text = Utils.clampLength(text);
		this.rating = Math.min(5, Math.max(0, rating));
		this.time = time;
		this.firstName = Utils.clampLength(firstName);
		this.lastName = Utils.clampLength(lastName);
	}

	/**
	 * Default constructor for the Comment class.
	 */
	public Comment() {
	}
}
