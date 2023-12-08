package campingplatz.plots;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
public class Issue {

	@Getter
	@Setter
	private @Id UUID id;

}
