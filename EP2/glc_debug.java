//Guilherme Rodrigues Pisni - NºUSP:11270851 e Mark Poll Herrmann - NºUSP:11208291
import java.util.Scanner;
import java.io.*;
import java.util.*;


public class glc_debug {
	public static void main (String [] args) throws IOException {
		@SuppressWarnings("unchecked")
		String path = System.getProperty("user.dir");
		File grams = new File(path + "\\inp-glc.txt");							//arquivo contendo as gramáticas
		File chains = new File(path + "\\inp-cadeias.txt");						//arquivo contendo as cadeias
		FileWriter fw = new FileWriter(path + "\\out-status.txt");				//escreve no arquivo de saída
		//PrintWriter saida = new PrintWriter(fw);

		LinkedList<String []> regras = new LinkedList<String []>();				//lista de regras da gramática
		LinkedList<String []> cadeias = new LinkedList<String []>();

		try {
			String [] linha, term, vars;										//linha atual do arquivo de entrada, lista de símbolos terminais, lista de variáveis
			Scanner ler_e = new Scanner(grams);									//scanner para ler as gramáticas
			Scanner ler_c = new Scanner(chains);								//scanner para ler as cadeias
			int num_grams, num_chains, q, t, s; 									//inicializa variáveis
			num_grams = Integer.parseInt(ler_e.nextLine());

			for (int i = 0; i < num_grams; i++) {
				q = ler_e.nextInt();											//numero de variaveis
				t = ler_e.nextInt();											//numero de terminais
				s = ler_e.nextInt();											//numero de regras
				System.out.print("q = " + q + " t = " + t + " s = " + s + "\n");
				ler_e.nextLine();
				vars = ler_e.nextLine().split(" ");								//armazena as variaveis
				print_arr(vars);
				term = ler_e.nextLine().split(" ");								//armazena os terminais
				print_arr(term);

				for (int j = 0; j < s; j++) {									//"for" para carregar as regras na lista de regras
					linha = ler_e.nextLine().split(" ");
					if (linha.length <= 3) regras.add(linha);					//se o resultado da regra for uma variável isolada adiciona normalmente
					else regras.add(ajusta_linha(linha));						//se não, concatena as saídas em uma string só e adiciona
					linha = regras.get(regras.size() - 1);
					//print_arr(linha);
				}
				for (String [] dam : regras) print_arr(dam);
				num_chains = Integer.parseInt(ler_c.nextLine());
				for (int j = 0; j < num_chains; j++) {							//carrega as cadeias para a lista de cadeias
					cadeias.add(ler_c.nextLine().split(" "));
					print_arr(cadeias.get(cadeias.size() - 1));
				}
				fw.write(cyk(regras,cadeias,vars[0], q, t, s));					//escreve no arquivo de saída o resultado retornado pela função cyk()

				
				regras.clear();															//limpa as listas para a próxima gramática usar
				cadeias.clear();	
			}
			fw.close();
			//for (int i = 0; i < regras.size(); i++) System.out.println(regras.get(i)[0]);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String cyk (LinkedList<String []> regras, LinkedList<String []> cadeias,String inicial, int q, int t, int s) {					//função contendo o algoritmo CYK, recebe a lista de regras e de cadeias, a variável inicial, o número de variáveis,terminais e de regras
		LinkedList<LinkedList<LinkedList<String>>>  tri = new LinkedList<LinkedList<LinkedList<String>>> ();						//matriz triangular
		String resp = "";																											//string para as respostas
		boolean foi = false;																										//boolean para controlar a saída
		int l,bx;
		for (String [] cad : cadeias) {																								//"for" para iterar para cada cadeia
			inicializa_tri(tri, cad.length);
			for (int yon = 0; yon < cad.length; yon++) {																			//inicializa a primeira diagonal				
				for (String [] reg : regras) {
					for (int yin = 2; yin < reg.length; yin++) {
						if (cad[yon].equals(reg[yin])) tri.get(yon).get(yon).add(reg[0]);
						//System.out.println("simbolo da cadeia: " + cad[yon] + " simbolo da regra: " + reg[yin] + " reg[0]: " + reg[0] + " ???: " + tri.get(yon).get(yon));
					}
				}
			}
			for (int d = 1; d < cad.length; d++) {																					//"for" para iterar pelas diagonais > 1
				l = 0;
				for (int col = d; col < cad.length; col++) {																		//"for" para iterar pelos campos da diagonal atual, tri[l][col]
					bx = l + 1;																										//índice de início para as comparações de baixo
					for (int es = l; es < col; es++) {																				//"for" para a comparação do algoritmo CYK, tri[l][(l + es)], tri[l + 1][col]
						if (tri.get(l).get(es).size() != 0 && tri.get(bx).get(col).size() != 0) {									//só irá ser efetuada a comparação se nenhum dos campos estiver vazio
							for (String esq : tri.get(l).get(es)) {																	//para todas as entradas em tri[l][l + es], vindo da diagonal até o elemento atual
								for (String bxo : tri.get(bx).get(col)) {															//para todas as entradas em tri[bx][col]
									for (String [] reg : regras) {																	//verifica se tem regras contendo a concatenação das duas strings atuais
										//System.out.println("chegou aquiii/ reg[2]: " + reg[2] + " esq + bxo: " + (esq + bxo) + " esq: " + esq + " bxo: " + bxo);
										if (reg[2].equals(esq + bxo)) tri.get(l).get(col).add(reg[0]);

									}
								}
							}
						}
						bx++;
					}
					l++;
				}

			}
			for (String fim : tri.get(0).get(cad.length - 1)) {																//passa por todas as variáveis na posição mais interna da matriz													
				if (fim.equals(inicial)) {																					//se a variável inicial estiver presente na posição mais interna da matriz
					resp += "1 ";																							//a cadeia foi aceita então, adicione 1 às respostas
					foi = true;
					break;
				}
			}
			if (foi == false) resp += "0 ";																					//se não, adicione 0 às respostas
			foi = false;
			print_mat(tri);
			tri.clear();
		}
		return (resp.trim() + "\n");																						//retorna a String formatada para escrever no arquivo 
	}
	public static String[] ajusta_linha (String [] linha) {
		String [] linha_ajusatada = new String [3];
		linha_ajusatada[0] = linha[0];
		linha_ajusatada[1] = linha[1];
		linha_ajusatada[2] = linha[2] + linha[3];
		return linha_ajusatada;
	}
	public static void inicializa_tri(LinkedList<LinkedList<LinkedList<String>>> tri, int tam) {
		int temp = tam;
		LinkedList<LinkedList<String>> te;
		for (int i = 0; i < tam; i++){
			tri.add(new LinkedList<LinkedList<String>>());
			for (int j = 0; j < tam; j++) tri.get(i).add(new LinkedList<String>());
			temp--;
		}
	}
	public static void print_arr(String [] linha) {
		for (String ch : linha) System.out.print("[" + ch + "] ");
		System.out.println();

	}
	public static void print_mat(LinkedList<LinkedList<LinkedList<String>>>  mat) {
		for (int i = 0; i < mat.size(); i++) {
			for (int j = 0; j < mat.get(i).size(); j ++) {
				if (mat.get(i).get(j).size() == 0) System.out.print("[]");
				else {
					System.out.print("["); 
					for (int k = 0; k < mat.get(i).get(j).size(); k++) System.out.print(mat.get(i).get(j).get(k));
					System.out.print("]");
				}
			}
			System.out.println();
		}
	}	
}