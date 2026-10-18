package arvore_generica;

public class Item {
	private final String nome;
	private final boolean diretorio;
	private final long tamanho; // em bytes

	public Item(String nome, boolean diretorio, long tamanho) {
		this.nome = nome;
		this.diretorio = diretorio;
		this.tamanho = tamanho;
	}

	public static Item diretorio(String nome) {
		return new Item(nome, true, 0);
	}

	public static Item arquivo(String nome, long tamanho) {
		return new Item(nome, false, tamanho);
	}

	public String getNome()      { return nome; }
	public boolean isDiretorio() { return diretorio; }
	public long getTamanho()     { return tamanho; }

	public static String formatar(long bytes) {
		if (bytes < 1024) return bytes + " B";
		double kb = bytes / 1024.0;
		if (kb < 1024) return String.format("%.2f KB", kb);
		double mb = kb / 1024.0;
		if (mb < 1024) return String.format("%.2f MB", mb);
		return String.format("%.2f GB", mb / 1024.0);
	}

	@Override
	public String toString() {
		if (diretorio) return nome.isEmpty() ? "/" : nome + "/";
		return nome + " (" + formatar(tamanho) + ")";
	}
}