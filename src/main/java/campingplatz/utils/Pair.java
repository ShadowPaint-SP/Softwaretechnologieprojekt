package campingplatz.utils;

import lombok.Getter;
import lombok.Setter;

public class Pair<T1, T2> {

	@Getter @Setter
	public T1 first;

	@Getter @Setter
	public T2 second;

	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

}
