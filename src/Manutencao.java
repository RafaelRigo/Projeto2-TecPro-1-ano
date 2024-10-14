import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import static java.lang.System.*;

public class Manutencao {
    enum Ordens {porRa, porNome, porCurso, porMedia}
    private static Ordens ordemAtual = Ordens.porRa;
    private static ManterEstudantes estud;
    private static String[] disciplinas;
    private static int qtsDisciplinas;

    static Scanner leitor = new Scanner(in);

    public static void main(String[] args) throws Exception {
        estud = new ManterEstudantes();

        lerDisciplinas();
        for (int i = 0; i < qtsDisciplinas; i++)
            out.println(disciplinas[i]);

        estud.leituraDosDados("DadosEstudantes.txt");
        seletorDeOpcoes();
        estud.gravarDados("DadosEstudantes.txt");

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
            switch (opcao) {
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

    public static void lerDisciplinas() throws FileNotFoundException {
        disciplinas = new String[15];
        try {
            BufferedReader arq = new BufferedReader(new FileReader("DadosDisciplinas.txt"));
            try {
                String linha = arq.readLine();
                int inicio = 0;
                boolean parar = false;
                if (linha != null) {
                    while (!parar && qtsDisciplinas < 15) {
                        if (linha.length() < inicio + 6)
                            parar = true;
                        else {
                            disciplinas[qtsDisciplinas] = linha.substring(inicio, inicio + 6);
                            qtsDisciplinas++;
                            inicio += 6;
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        else {
            incluirEmOrdem(umEstudante);
        }
    }

    private static void incluirEmOrdem(Estudante novo) {
        estud.incluirEm(novo, estud.posicaoAtual);
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
        String qtsSep = "+%s".repeat(5);
        String sepDisc = "--------+".repeat(qtsDisciplinas);
        String separador = String.format(
                qtsSep,
                "-".repeat(7),
                "-".repeat(7),
                "-".repeat(32),
                "-".repeat(4),
                sepDisc
        );

        out.println("\n\nListagem de Estudantes\n");
        out.println(separador);
        out.printf("| Curso | %-5s | %-30s | QN |", "RA", "Nome");
        for (int i = 0; i < qtsDisciplinas; i++)
            out.printf(" %6s |", disciplinas[i]);
        out.println();

        int contLinha = 0;
        for (int i = 0; i < estud.qtosDados; i++) {
            Estudante est = estud.valorDe(i);
            out.println(separador);
            out.printf("| %-5s | %-5s | %-30s | %-2d |",
                    est.getCurso(), est.getRa(), est.getNome(), est.getQuantasNotas());
            for (int j = 0; j < qtsDisciplinas; j++)
                out.printf(" %-6.1f |", est.getNotas()[j]);
            out.println();

            if (++contLinha >= 20) {
                out.print("\n\nTecle [Enter] para prosseguir: ");
                leitor.nextLine();
                contLinha = 0;
            }
        }
        out.println(separador);
        out.print("\n\nTecle [Enter] para prosseguir: ");
        leitor.nextLine();
    }

    public static void listarSituacoes() {
        String qtsSep = "+%s".repeat(7);
        String sepDisc = "--------+".repeat(qtsDisciplinas);
        String separador = String.format(
                qtsSep,
                "-".repeat(7),
                "-".repeat(18),
                "-".repeat(7),
                "-".repeat(7),
                "-".repeat(32),
                "-".repeat(4),
                sepDisc
        );

        out.println("\n\nSituação estudantil\n");
        out.println(separador);
        out.printf("| Média | %-16s | Curso | %-5s | %-30s | QN |", "Situacao", "RA", "Nome");
        for (int i = 0; i < qtsDisciplinas; i++)
            out.printf(" %6s |", disciplinas[i]);
        out.println();

        String situacao;
        for (int i = 0; i < estud.qtosDados; i++)
        {
            double mediaDesseEstudante = estud.dados[i].mediaDasNotas();
            if (mediaDesseEstudante < 5)
                situacao = "Não promovido(a)";
            else
                situacao = "Promovido(a)    ";

            Estudante est = estud.valorDe(i);
            out.println(separador);
            out.printf("| %-5.2f | %-16s | %-5s | %-5s | %-30s | %-2d |",
                    mediaDesseEstudante, situacao, est.getCurso(), est.getRa(), est.getNome(), est.getQuantasNotas());
            for (int j = 0; j < qtsDisciplinas; j++)
                out.printf(" %-6.1f |", est.getNotas()[j]);
            out.println();
        }
        out.println(separador);
        out.print("\n\nTecle [Enter] para prosseguir: ");
        leitor.nextLine();
    }

    private static void ordenarPorCurso() {
        for (int i = 0; i < estud.qtosDados; i++)
            for (int j = i + 1; j < estud.qtosDados; j++)
                if (estud.valorDe(i).getCurso().compareTo(estud.valorDe(j).getCurso()) > 0)
                    estud.trocar(i, j);
        ordemAtual = Ordens.porCurso;
    }

    private static void ordenarPorRa() {
        estud.ordenar();
        ordemAtual = Ordens.porRa;
    }

    private static void ordenarPorNome() {
        for (int i = 0; i < estud.qtosDados; i++)
            for (int j = i + 1; j < estud.qtosDados; j++)
                if (estud.valorDe(i).getNome().compareTo(estud.valorDe(j).getNome()) > 0)
                    estud.trocar(i, j);
        ordemAtual = Ordens.porNome;
    }

    private static void ordenarPorMedia() {
        for (int i = 0; i < estud.qtosDados; i++) {
            double mediaAtual = estud.valorDe(i).mediaDasNotas();
            for (int j = i + 1; j < estud.qtosDados; j++)
                if (mediaAtual > estud.valorDe(j).mediaDasNotas())
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
