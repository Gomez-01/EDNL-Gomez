import No;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;

public class Arvore<T> {
	private No<T> raiz;

	public No<T> getRaiz() {
		return raiz;
	}

	public boolean estaVazia() {
		return raiz == null;
	}

	public void criarRaiz(No<T> no) {
		no.pai = null;
		raiz = no;
	}

	public void inserirNo(No<T> pai, No<T> filho) {
		if (pai == null || filho == null) return;
		filho.pai = pai;
		pai.filhos.add(filho);
	}

	public No<T> buscar(T dado) {
		return buscar(raiz, dado);
	}

	private No<T> buscar(No<T> no, T dado) {
		if (no == null) return null;
		if (Objects.equals(no.dado, dado)) return no;
		for (No<T> f : no.filhos) {
			No<T> r = buscar(f, dado);
			if (r != null) return r;
		}
		return null;
	}

	public List<No<T>> buscarTodos(Predicate<T> criterio) {
		List<No<T>> resultado = new ArrayList<>();
		buscarTodos(raiz, criterio, resultado);
		return resultado;
	}

	private void buscarTodos(No<T> no, Predicate<T> criterio, List<No<T>> resultado) {
		if (no == null) return;
		if (criterio.test(no.dado)) resultado.add(no);
		for (No<T> f : no.filhos) buscarTodos(f, criterio, resultado);
	}

	public No<T> retornarPai(T dado) {
		No<T> no = buscar(dado);
		return no == null ? null : no.pai;
	}

	public boolean removerNo(T dado) {
		return remover(buscar(dado));
	}

	public boolean remover(No<T> no) {
		if (no == null) return false;
		if (no == raiz) {
			raiz = null;
		} else {
			no.pai.filhos.remove(no);
			no.pai = null;
		}
		return true;
	}

	public int altura() {
		return altura(raiz);
	}

	private int altura(No<T> no) {
		if (no == null) return -1;
		int maior = -1;
		for (No<T> f : no.filhos) maior = Math.max(maior, altura(f));
		return maior + 1;
	}

	public long somar(No<T> no, ToLongFunction<T> valor) {
		if (no == null) return 0;
		long total = valor.applyAsLong(no.dado);
		for (No<T> f : no.filhos) total += somar(f, valor);
		return total;
	}

	public void imprimir() {
		if (raiz == null) {
			System.out.println("Árvore vazia");
			return;
		}
		System.out.println(raiz.dado);
		imprimirFilhos(raiz, "");
	}

	public void imprimir(No<T> no) {
		System.out.println(no.dado);
		imprimirFilhos(no, "");
	}

	private void imprimirFilhos(No<T> no, String prefixo) {
		for (int i = 0; i < no.filhos.size(); i++) {
			No<T> f = no.filhos.get(i);
			boolean ultimo = (i == no.filhos.size() - 1);
			System.out.println(prefixo + (ultimo ? "`-- " : "|-- ") + f.dado);
			imprimirFilhos(f, prefixo + (ultimo ? "    " : "|   "));
		}
	}

	public void imprimirMD(String arquivo) {
		try (BufferedWriter w = Files.newBufferedWriter(Paths.get(arquivo), StandardCharsets.UTF_8)) {
			if (raiz == null) {
				w.write("_Árvore vazia_\n");
			} else {
				escreverMD(w, raiz, 0);
			}
		} catch (IOException e) {
			System.out.println("Erro ao gravar " + arquivo + ": " + e.getMessage());
		}
	}

	private void escreverMD(BufferedWriter w, No<T> no, int nivel) throws IOException {
		w.write("  ".repeat(nivel) + "- `" + no.dado + "`\n");
		for (No<T> f : no.filhos) escreverMD(w, f, nivel + 1);
	}
}

class No<T> {
	public T dado;
	public No<T> pai;
	public List<No<T>> filhos = new ArrayList<>();

	public No(T dado) {
		this.dado = dado;
	}
}