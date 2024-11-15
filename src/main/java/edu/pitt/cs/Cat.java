package edu.pitt.cs;

import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public interface Cat {
	public static Cat createInstance(InstanceType type, int id, String name) {
		switch (type) {
			case IMPL:
				return new CatImpl(id, name);
			case BUGGY:
				return new CatBuggy(id, name);
			case SOLUTION:
				return new CatSolution(id, name);
			case MOCK:
				Cat c = Mockito.mock(Cat.class);

				// the rented status of the cat. it has to be a final array because when used
				// inside the lambda expression, it has to be final or effectively final
				final boolean[] rented = { false };

				when(c.getId()).thenReturn(id);
				when(c.getName()).thenReturn(name);
				when(c.toString()).thenAnswer(x -> "ID " + id + ". " + c.getName());
				when(c.getRented()).thenAnswer(x -> rented[0]);

				doAnswer(x -> {
					rented[0] = true;
					return null;
				}).when(c).rentCat();

				doAnswer(x -> {
					rented[0] = false;
					return null;
				}).when(c).returnCat();

				doAnswer(x -> {
					String new_name = x.getArgument(0);
					when(c.getName()).thenReturn(new_name);
					return null;
				}).when(c).renameCat(anyString());

				return c;
			default:
				assert false;
				return null;
		}
	}

	// WARNING: You are not allowed to change any part of the interface.
	// That means you cannot add any method nor modify any of these methods.

	public void rentCat();

	public void returnCat();

	public void renameCat(String name);

	public String getName();

	public int getId();

	public boolean getRented();

	public String toString();
}
