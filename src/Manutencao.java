import javax.swing.*;
import java.util.Scanner;
import static java.lang.System.*;

public class Manutencao {
    enum Ordens {porRa, porNome, porCurso, porMedia}
    private static Ordens ordemAtual = Ordens.porRa;
    private static ManterEstudantes estud;
    static Scanner leitor = new Scanner(in);
    static boolean continuarPrograma = true;

    public static void main(String[] args) throws Exception {
        estud = new ManterEstudantes();
        estud.dados = new Estudante[3];
        for (int i = 0; i < 3; i++)
            estud.dados[i] = new Estudante();
        estud.qtosDados = 0;
        estud.leituraDosDados("DadosEstudantes.txt");
        if (continuarPrograma) {
            seletorDeOpcoes();
            estud.gravarDados("DadosEstudantes.txt");
        }
        out.println("\nPrograma encerrado.");
    }

    public static void seletorDeOpcoes() throws Exception {
        int opcao;
        do {
            out.println("Opções:\n");
            out.println("0 - Terminar programa");
            out.println("1 - Incluir estudante");
            out.println("2 - Listar estudantes");
            out.println("3 - Excluir estudante");
            out.println("4 - Listar situações");
            out.println("5 - Digitar notas de estudante");
            out.println("6 - Ordenar por curso");
            out.println("7 - Ordenar por nome");
            out.println("8 - Ordenar por média");
            out.print("\nSua opção: ");
            opcao = leitor.nextInt();
            leitor.nextLine();
            switch(opcao) {
                case 1 : incluirEstudante(); break;
                case 2 : listarEstudantes(); break;
                case 3 : excluirEstudante(); break;
                case 4 : listarSituacoes();  break;
                case 5 : digitarNotas(); break;
                case 6 : ordenarPorCurso(); break;
                case 7 : ordenarPorNome(); break;
                case 8 : ordenarPorMedia(); break;
            }
        }
        while (opcao != 0);
    }

    public static void incluirEstudante() throws Exception {
        if (ordemAtual != Ordens.porRa)
            ordenarPorRa();

        out.println("Incluir Estudante\n");
        out.print("Curso : ");
        String curso = leitor.nextLine();
        out.print("RA    : ");
        String ra = leitor.nextLine();
        out.print("Nome  : ");
        String nome = leitor.nextLine();
        Estudante umEstudante = new Estudante(curso, ra, nome);
        if (estud.existe(umEstudante))
          JOptionPane.showMessageDialog(null,"Estudante repetido!");
        else
        {
            incluirEmOrdem(umEstudante);
        }
    }

    private static void expandirVetor() {
        Estudante[] novoVetor = new Estudante[estud.dados.length * 2];
        for (int i = 0; i <estud.qtosDados; i++)
            novoVetor[i] = estud.dados[i];
        estud.dados = novoVetor;
    }

    private static void incluirEmOrdem(Estudante novo) {
        if (estud.qtosDados >= estud.dados.length)
            expandirVetor();
        estud.incluirEm(novo, estud.posicaoAtual);
        estud.qtosDados++;
    }

    public static void excluirEstudante() throws Exception {
        if (ordemAtual != Ordens.porRa)
            ordenarPorRa();
        out.println("Excluir Estudante\n");
        out.print("RA    : ");
        String ra = leitor.nextLine();
        Estudante umEstudante = new Estudante(" ", ra, " ");
        if (!estud.existe(umEstudante))
            JOptionPane.showMessageDialog(null,"Estudante não encontrado!");
        else {
            estud.excluir(estud.posicaoAtual);
        }
    }
    public static void listarEstudantes() {
        out.println("\n\nListagem de Estudantes\n");
        int contLinha = 0;
        for (int i = 0; i < estud.qtosDados; i++)
        {
            out.println(estud.dados[i]);

            if (++contLinha >= 20) {
                out.print("\n\nTecle [Enter] para prosseguir: ");
                leitor.nextLine();
                contLinha = 0;
            }
        }
        out.print("\n\nTecle [Enter] para prosseguir: ");
        leitor.nextLine();
    }

    public static void listarSituacoes() {
        out.println("\n\nSituação estudantil\n");
        String situacao;
        for (int i = 0; i < estud.qtosDados; i++)
        {
            double mediaDesseEstudante = estud.dados[i].mediaDasNotas();
            if (mediaDesseEstudante < 5)
                situacao = "Não promovido(a)";
            else
                situacao = "Promovido(a)    ";

            out.printf(
                    "%4.1f %16s "+estud.dados[i]+"\n", mediaDesseEstudante,
                    situacao);
        }
        out.print("\n\nTecle [Enter] para prosseguir: ");
        leitor.nextLine();
    }

    private static void ordenarPorCurso() {
        for (int i = 0; i < estud.qtosDados; i++)
            for (int j = i + 1; j < estud.qtosDados; j++)
                if (estud.dados[i].getCurso().compareTo(estud.dados[j].getCurso()) > 0)
                    estud.trocar(i, j);
        ordemAtual = Ordens.porCurso;
    }

    private static void ordenarPorRa() {
        for (int i = 0; i < estud.qtosDados; i++)
            for (int j = i + 1; j < estud.qtosDados; j++)
                if (estud.dados[i].getRa().compareTo(estud.dados[j].getRa()) > 0)
                    estud.trocar(i, j);
        ordemAtual = Ordens.porRa;
    }

    private static void ordenarPorNome() {
        for (int i = 0; i < estud.qtosDados; i++)
            for (int j = i + 1; j < estud.qtosDados; j++)
                if (estud.dados[i].getNome().compareTo(estud.dados[j].getNome()) > 0)
                    estud.trocar(i, j);
        ordemAtual = Ordens.porNome;
    }

    private static void ordenarPorMedia() {
        for (int i = 0; i < estud.qtosDados; i++) {
            double mediaAtual = estud.dados[i].mediaDasNotas();
            for (int j = i + 1; j < estud.qtosDados; j++)
                if (mediaAtual > estud.dados[j].mediaDasNotas())
                    estud.trocar(i, j);
            ordemAtual = Ordens.porMedia;
        }
    }
    private static void digitarNotas() {
        out.println("Digitação de notas de estudante:\n");
        out.print("Digite o RA do(a) estudante desejado(a): ");
        String raEstudante = leitor.nextLine();
        try {
            Estudante estProc = new Estudante("00", raEstudante, "A");
            if (!estud.existe(estProc))
                out.println("Não há um(a) estudante com esse RA!");
            else {
                out.print("Quantidade de notas a serem digitadas: ");
                int quant = leitor.nextInt();
                leitor.nextLine();

                estud.dados[estud.posicaoAtual].setQuantasNotas(quant);
                double nota;
                for (int indNota = 0; indNota < quant; indNota++) {
                    do {
                        out.printf("Digite a %da. nota:", indNota + 1);
                        nota = leitor.nextDouble();
                        if (nota >= 0 && nota <= 10)
                            break;
                        out.println("Nota inválida. Digite novamente:");
                    } while (true);
                    estud.dados[estud.posicaoAtual].setNota(nota, indNota);
                }
            }
        }
        catch (Exception erro) {
            out.println("Não foi possivel criar objeto Estudante.");
            out.println(erro.getMessage());
        }
    }
}
