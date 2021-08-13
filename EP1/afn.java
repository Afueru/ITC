//Guilherme Rodrigues Pisni - NºUSP:11270851 e Mark Poll Herrmann - NºUSP:11208291
//ATENÇÃO: AMBOS OS PROGRAMAS (afn e glc) SÃO A MESMA COISA, MAS NO INICIO DO ENUNCIADO ESTAVA ESPECIFICADO UM (afn) E NA ENTREGA ESTAVA ESPECIFICADO OUTRO (glc)
import java.util.Scanner;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

public class afn {
	String path = System.getProperty("user.dir");
	public static void main (String [] args) throws Exception {
		String path = System.getProperty("user.dir"); 		//pega caminho do usuário até o lugar de onde o arquivo está sendo executado
		String epath = path + "\\entrada.txt"; 				//caminho do arquivo de entrada
		String spath = path + "\\saida.txt"; 				//caminho do arquivo de saída

		File entrada = new File(epath);						//abre arquivo de entrada
		File saida = new File(spath);						//cria arquivo de saída

		PrintStream out = System.out;									//Salva o standard output para reportar um erro										
		System.setOut(new PrintStream(saida));							//Coloca a Printstream pra escrever no arquivo 'saída.txt' como padrão


		//inicializar variáveis
		int m, q, s, t, qo, a, ntestes;
		m = q = s = t = qo = a = ntestes = 0;
		String linha;
		ArrayList<Integer> f = new ArrayList<>(); 							//cria cadeia de aceitação
		ArrayList<ArrayList<Integer>> tr = new ArrayList<>();			//ArrayList para guardar as transições
		ArrayList<ArrayList<Integer>> testes = new ArrayList<>(); 		//Arraylist para guardar as cadeias de testes
		ArrayList<ArrayList<Integer>> resultados = new ArrayList<>(); 		//matriz contendo resultados

		try {															//começa a ler o arquivo de entrada
		Scanner ler = new Scanner(entrada);

		m = ler.nextInt();												//numero de automatos

		for (int automax = 0; automax < m; automax++){

			q = ler.nextInt();												//numero de estados
			s = ler.nextInt();												//numero de simbolos
			t = ler.nextInt();												//numero de transições
			qo = ler.nextInt();												//estado inicial
			a = ler.nextInt();												//numero de estados de aceitacao


			ler.nextLine();													//termina de ler a linha
			linha = ler.nextLine();											//pega a linha dos estados de aceitação
			converteString(linha,f);										//armazena o conjunto de estados de aceitação em f										

			for (int i = 0; i < t; i++) {									//carrega as transições e armazena em uma lista
				linha = ler.nextLine();
				tr.add(new ArrayList<Integer>());
				converteString(linha,tr.get(i));
			}

			ntestes = ler.nextInt();										//pega o numero de testes e termina a linha
			ler.nextLine();

			for (int i = 0; i < ntestes; i++) {								//carrega os conjuntos de teste e armazena em uma lista								
				linha = ler.nextLine();
				testes.add(new ArrayList<Integer>());
				converteString(linha, testes.get(i));
			}

			resultados.add(new ArrayList<Integer>());						//cria uma lista pra armazenar resultados dos testes do automato atual
			for(int i = 0; i < testes.size(); i++) {						//percorre o conjunto de testes
				if(a == 0 || faztestes(qo,0,tr,f,testes.get(i)) == false) resultados.get(automax).add(0); 			//caso não haja estados de aceitação ou o resultado do teste retorne false, adiciona '0' na lista de resultados
				else resultados.get(automax).add(1);														//caso retorne true, adiciona '1' à lista de resultados
			}


			imprimeLista(resultados.get(automax));							//imprime resultados do automato no arquivo

			testes.clear();													
			tr.clear();														//limpa as matrizes e listas para o próximo automato utilizar
			f.clear();	
		}



		ler.close();

		}catch(Exception e) { 											//erro caso o arquivo "entrada.txt" não esteja no mesmo diretório do programa
			System.setOut(out);
			System.out.println("Arquivo \"entrada.txt\" nao esta no mesmo diretorio do programa!");

		}		
	}
	public static boolean faztestes (int esatual, int iatual, ArrayList<ArrayList<Integer>> tr, ArrayList<Integer> f,ArrayList<Integer> teste) {	//método que faz os testes, recebe o estado atual, um inteiro que guarda a posição atual na lista de transições, a lista de transições, a lista de estados de aceitação e a linha de testes a ser feita
		boolean talquei = false;																					//boolean pra checar se chegou no estado de aceitação ao final do percurso
		for (int g = 0; g < tr.size(); g++) { 																		//alcança o range do estado atual e checa se há alguma solução partindo dele
			if (tr.get(g).get(0) == esatual && tr.get(g).get(1) == 0) {
				talquei = faztestes(tr.get(g).get(2),iatual,tr,f,teste);										//testará os movimentos partindo do range do estado atual
				if(talquei == true) return true;																//caso haja solução, retorna true, indicando que o automato chegou no estado de aceitação
				
			}
		}																
																					

		if(iatual == teste.size()) {																			//condição de parada: se o elemento percorrido no array de testes for o ultimo
			for (int finho = 0; finho < f.size(); finho++) if(esatual == f.get(finho)) return true;				//percorre a lista de estados de aceitação e se o estado atual for igual a um dos estados de aceitação, retorna true
			return false; 																						//se não, retorna false
		}

		for (int i = 0; i < tr.size(); i++)	{																					//percorre a lista de transições caso haja um movimento para o estado atual do automato e exista um movimento para o valor passado, então chama novamente a função
		if (tr.get(i).get(0) == esatual && tr.get(i).get(1) == teste.get(iatual)) talquei = faztestes(tr.get(i).get(2),iatual + 1,tr,f,teste);		//executará o movimento caso haja transição possível para a entrada passada 
		if (talquei == true) return true;																						//caso um teste tenha resultado em aceitação,retorna true			
	}
	return false;
}

	public static void converteString (String linha, ArrayList<Integer> a) {  //separa os números da string e coloca-os em uma ArrayList
		String s[] = linha.split(" ");
		for (int i = 0; i < s.length; i++)a.add(Integer.parseInt(s[i]));
	}
	public static void imprimeLista(ArrayList<Integer> a) {
		for (int i = 0; i < a.size(); i++) {
			System.out.print(a.get(i) + " ");
		}
		System.out.println();
	}
}