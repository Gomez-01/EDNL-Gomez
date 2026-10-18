package arvore_generica;

import java.util.List;
import java.util.Scanner;

public class Simulador {
	private static final Scanner in = new Scanner(System.in);
	private static final Arvore<Item> arvore = new Arvore<>();
	private static No<Item> atual;

	public static void main(String[] args) {
		arvore.criarRaiz(new No<>(Item.diretorio("")));
		atual = arvore.getRaiz();

		System.out.println("=== SIMULADOR DE GERENCIAMENTO DE ARQUIVOS ===");
		int opcao;
		do {
			exibirMenu();
			opcao = lerInteiro("Opção: ");
			System.out.println();
			switch (opcao) {
				case 1:  criarDiretorio();      break;
				case 2:  criarArquivo();        break;
				case 3:  selecionarDiretorio(); break;
				case 4:  removerArquivo();      break;
				case 5:  removerDiretorio();    break;
				case 6:  listarConteudo();      break;
				case 7:  exibirArvore();        break;
				case 8:  localizarArquivo();    break;
				case 9:  caminhoDoArquivo();    break;
				case 10: tamanhoDoDiretorio();  break;
				case 11: exportarMD();          break;
				case 0:  System.out.println("Encerrando o simulador."); break;
				default: System.out.println("Opção inválida!");
			}
		} while (opcao != 0);
	}

	private static void exibirMenu() {
		System.out.println("\n--------------------------------------------");
		System.out.println("Diretório atual: " + caminho(atual));
		System.out.println("--------------------------------------------");
		System.out.println(" 1 - Criar diretório");
		System.out.println(" 2 - Criar arquivo");
		System.out.println(" 3 - Selecionar diretório atual");
		System.out.println(" 4 - Remover arquivo");
		System.out.println(" 5 - Remover diretório");
		System.out.println(" 6 - Listar conteúdo do diretório atual");
		System.out.println(" 7 - Exibir árvore completa");
		System.out.println(" 8 - Localizar arquivo pelo nome");
		System.out.println(" 9 - Informar caminho completo de um arquivo");
		System.out.println("10 - Calcular tamanho total de um diretório");
		System.out.println("11 - Exportar árvore para Markdown (.md)");
		System.out.println(" 0 - Sair");
	}

	private static void criarDiretorio() {
		String nome = lerTexto("Nome do diretório (será criado em " + caminho(atual) + "): ");
		if (!nomeValido(nome)) {
			System.out.println("Nome inválido.");
			return;
		}
		if (existeFilho(atual, nome)) {
			System.out.println("Já existe um item com esse nome neste diretório.");
			return;
		}
		arvore.inserirNo(atual, new No<>(Item.diretorio(nome)));
		System.out.println("Diretório criado com sucesso.");
	}

	private static void criarArquivo() {
		String nome = lerTexto("Nome do arquivo: ");
		if (!nomeValido(nome)) {
			System.out.println("Nome inválido.");
			return;
		}
		No<Item> destino = resolver(lerTexto("Diretório de destino (Enter = diretório atual): "));
		if (destino == null || !destino.dado.isDiretorio()) {
			System.out.println("Diretório não encontrado.");
			return;
		}
		long tamanho = lerLong("Tamanho do arquivo (em bytes): ");
		if (tamanho < 0) {
			System.out.println("Tamanho inválido.");
			return;
		}
		if (existeFilho(destino, nome)) {
			System.out.println("Já existe um item com esse nome nesse diretório.");
			return;
		}
		arvore.inserirNo(destino, new No<>(Item.arquivo(nome, tamanho)));
		System.out.println("Arquivo criado em " + caminho(destino));
	}

	private static void selecionarDiretorio() {
		No<Item> alvo = resolver(lerTexto("Caminho do diretório (.. sobe, / vai à raiz): "));
		if (alvo == null || !alvo.dado.isDiretorio()) {
			System.out.println("Diretório não encontrado.");
			return;
		}
		atual = alvo;
		System.out.println("Diretório atual: " + caminho(atual));
	}

	private static void removerArquivo() {
		String entrada = lerTexto("Arquivo a remover (nome ou caminho): ");
		No<Item> alvo = resolver(entrada);
		if (entrada.isEmpty() || alvo == null || alvo.dado.isDiretorio()) {
			System.out.println("Arquivo não encontrado.");
			return;
		}
		arvore.remover(alvo);
		System.out.println("Arquivo removido.");
	}

	private static void removerDiretorio() {
		String entrada = lerTexto("Diretório a remover (nome ou caminho): ");
		No<Item> alvo = resolver(entrada);
		if (entrada.isEmpty() || alvo == null || !alvo.dado.isDiretorio()) {
			System.out.println("Diretório não encontrado.");
			return;
		}
		if (alvo == arvore.getRaiz()) {
			System.out.println("O diretório raiz não pode ser removido.");
			return;
		}
		if (!alvo.filhos.isEmpty()) {
			String r = lerTexto("O diretório não está vazio; todo o conteúdo será removido. Confirmar (s/n)? ");
			if (!r.equalsIgnoreCase("s")) {
				System.out.println("Operação cancelada.");
				return;
			}
		}
		if (estaContido(atual, alvo)) atual = alvo.pai;
		arvore.remover(alvo);
		System.out.println("Diretório removido.");
	}

	private static void listarConteudo() {
		System.out.println("Conteúdo de " + caminho(atual) + ":");
		if (atual.filhos.isEmpty()) {
			System.out.println("  (vazio)");
			return;
		}
		for (No<Item> f : atual.filhos) {
			System.out.println("  " + (f.dado.isDiretorio() ? "[DIR]  " : "[ARQ]  ") + f.dado);
		}
	}

	private static void exibirArvore() {
		System.out.println("Árvore completa:");
		arvore.imprimir();
		System.out.println("Altura: " + arvore.altura());
	}

	private static void localizarArquivo() {
		String nome = lerTexto("Nome do arquivo: ");
		List<No<Item>> achados = arvore.buscarTodos(i -> !i.isDiretorio() && i.getNome().equals(nome));
		if (achados.isEmpty()) {
			System.out.println("Arquivo não encontrado.");
			return;
		}
		System.out.println(achados.size() + " ocorrência(s):");
		for (No<Item> n : achados) System.out.println("  " + caminho(n) + "  (" + Item.formatar(n.dado.getTamanho()) + ")");
	}

	private static void caminhoDoArquivo() {
		String nome = lerTexto("Nome do arquivo: ");
		List<No<Item>> achados = arvore.buscarTodos(i -> !i.isDiretorio() && i.getNome().equals(nome));
		if (achados.isEmpty()) {
			System.out.println("Arquivo não encontrado.");
			return;
		}
		for (No<Item> n : achados) System.out.println("Caminho completo: " + caminho(n));
	}

	private static void tamanhoDoDiretorio() {
		No<Item> alvo = resolver(lerTexto("Diretório (Enter = diretório atual): "));
		if (alvo == null || !alvo.dado.isDiretorio()) {
			System.out.println("Diretório não encontrado.");
			return;
		}
		long total = arvore.somar(alvo, Item::getTamanho);
		System.out.println("Tamanho total de " + caminho(alvo) + ": " + total + " bytes (" + Item.formatar(total) + ")");
	}

	private static void exportarMD() {
		String arquivo = lerTexto("Nome do arquivo de saída (ex.: arvore.md): ");
		if (arquivo.isEmpty()) arquivo = "arvore.md";
		arvore.imprimirMD(arquivo);
		System.out.println("Árvore exportada para " + arquivo);
	}

	private static boolean existeFilho(No<Item> dir, String nome) {
		return filho(dir, nome) != null;
	}

	private static No<Item> filho(No<Item> dir, String nome) {
		for (No<Item> f : dir.filhos) if (f.dado.getNome().equals(nome)) return f;
		return null;
	}

	private static No<Item> resolver(String caminho) {
		if (caminho == null || caminho.trim().isEmpty()) return atual;
		caminho = caminho.trim();
		No<Item> no = caminho.startsWith("/") ? arvore.getRaiz() : atual;
		for (String parte : caminho.split("/")) {
			if (parte.isEmpty() || parte.equals(".")) continue;
			if (!no.dado.isDiretorio()) return null;
			if (parte.equals("..")) {
				if (no.pai != null) no = no.pai;
			} else {
				no = filho(no, parte);
				if (no == null) return null;
			}
		}
		return no;
	}

	private static String caminho(No<Item> no) {
		if (no == arvore.getRaiz()) return "/";
		StringBuilder sb = new StringBuilder();
		for (No<Item> n = no; n != null && n != arvore.getRaiz(); n = n.pai) {
			sb.insert(0, "/" + n.dado.getNome());
		}
		return sb.toString();
	}

	private static boolean estaContido(No<Item> no, No<Item> ancestral) {
		for (No<Item> n = no; n != null; n = n.pai) if (n == ancestral) return true;
		return false;
	}

	private static boolean nomeValido(String nome) {
		return !nome.isEmpty() && !nome.contains("/") && !nome.equals(".") && !nome.equals("..");
	}

	private static String lerTexto(String msg) {
		System.out.print(msg);
		return in.hasNextLine() ? in.nextLine().trim() : "";
	}

	private static int lerInteiro(String msg) {
		try { return Integer.parseInt(lerTexto(msg)); } catch (NumberFormatException e) { return -1; }
	}

	private static long lerLong(String msg) {
		try { return Long.parseLong(lerTexto(msg)); } catch (NumberFormatException e) { return -1; }
	}
}