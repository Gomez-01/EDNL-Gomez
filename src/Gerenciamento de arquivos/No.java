package arvore_generica;

import java.util.ArrayList;
import java.util.List;

public class No<T> {
	public T dado;
	public No<T> pai;
	public List<No<T>> filhos = new ArrayList<>();

	public No(T dado) {
		this.dado = dado;
	}
}