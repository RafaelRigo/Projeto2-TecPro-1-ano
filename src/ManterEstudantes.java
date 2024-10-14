import javax.swing.*;
import java.io.*;
import java.util.Scanner;

import static java.lang.System.out;

public class ManterEstudantes {
    int qtosDados, posicaoAtual;
    Estudante[] dados;
    Scanner leitor = new Scanner(System.in);

    public void leituraDosDados(String nomeArquivo) {
        int tamanhoInicial = 3;

        dados = new Estudante[tamanhoInicial];
        for (int i = 0; i < tamanhoInicial; i++)
            dados[i] = new Estudante();

        try {
            BufferedReader arquivoDeEntrada = new BufferedReader(
                    new FileReader(nomeArquivo)
            );
            try {
                boolean parar = false;
                while (!parar) {
                    Estudante novoEstudante = new Estudante();
                    try {
                        if (novoEstudante.leuLinhaDoArquivo(arquivoDeEntrada) ) {
                            incluirNoFinal(novoEstudante);
                        }
                        else
                            parar = true;
                    }
                    catch (Exception erroDeLeitura) {
                        out.println(erroDeLeitura.getMessage());
                        parar = true;
                    }
                }
                arquivoDeEntrada.close();
            }
            catch (IOException erroDeIO)
            {
                out.println(erroDeIO.getMessage());
            }
        }
        catch (FileNotFoundException erro) {
            out.println(erro.getMessage());
        }
    }

    public void gravarDados(String nomeArquivo) throws IOException {
        BufferedWriter arquivoDeSaida = new BufferedWriter(
                new FileWriter(nomeArquivo));

        for (int i=0; i < qtosDados; i++)
            arquivoDeSaida.write(dados[i].formatoDeArquivo());
        arquivoDeSaida.close();
    }

    public void expandir() {
        Estudante[] novoVetor = new Estudante[dados.length * 2];
        for (int i = 0; i <qtosDados; i++)
            novoVetor[i] = valorDe(i);
        dados = novoVetor;
    }

    public Boolean existe(Estudante dadoProcurado) {
        int inicio = 0;
        int fim = qtosDados - 1;
        boolean achou = false;
        while (! achou && inicio <= fim) {
            posicaoAtual = (inicio + fim) / 2;
            String raDoMeioDoTrechoDoVetor = valorDe(posicaoAtual).getRa();
            String raDoProcurado = dadoProcurado.getRa();
            if (raDoMeioDoTrechoDoVetor.compareTo(raDoProcurado) == 0)
                achou = true;
            else if (raDoProcurado.compareTo(raDoMeioDoTrechoDoVetor) < 0)
                fim = posicaoAtual - 1;
            else
                inicio = posicaoAtual + 1;
        }
        if (!achou)
            posicaoAtual = inicio;   // posição de inclusao do RA em ordem crescente
        return achou;
    }

    public void incluirNoFinal(Estudante novoDado) throws Exception {
        if (existe(novoDado))  // ajusta a variável onde
            JOptionPane.showMessageDialog(null,"Estudante repetido!");
        else {
            incluirEm(novoDado, qtosDados == 0 ? 0 : qtosDados-1);  // última posição usada
        }
    }

    public void incluirEm(Estudante novoDado, int posicaoDeInclusao) {
        if (existe(novoDado)) {
            JOptionPane.showMessageDialog(null,"Estudante repetido!");
        } else {
            if (qtosDados >= dados.length) {
                expandir();
            }
            for (int i = qtosDados; i > posicaoDeInclusao; i--) {
                dados[i] = dados[i-1];
            }
            dados[posicaoDeInclusao] = novoDado;
            qtosDados++;
        }
    }

    public void excluir(int posicaoDeExclusao) {
        if (!existe(dados[posicaoDeExclusao])) {
            JOptionPane.showMessageDialog(null, "Este estudante não existe!");
        } else {
            qtosDados--;
            for (int indice = posicaoDeExclusao; indice < qtosDados; indice++)
                dados[indice] = dados[indice + 1];
        }
    }

    public Estudante valorDe(int indiceDeAcesso) {
        return dados[indiceDeAcesso];
    }

    public void alterar(int posicaoDeAlteracao, Estudante novoDado) {
        if (existe(novoDado)) {
            JOptionPane.showMessageDialog(null, "Estudante repetido!");
        } else if (!existe(dados[posicaoDeAlteracao])) {
            JOptionPane.showMessageDialog(null, "Este estudante não existe!");
        } else {
            dados[posicaoDeAlteracao] = novoDado;
        }
    }

    public void trocar(int origem, int destino) {
        Estudante auxiliar = dados[origem];
        dados[origem] = dados[destino];
        dados[destino] = auxiliar;
    }

    public void ordenar() {
        for (int i = 0; i < qtosDados; i++)
            for (int j = i + 1; j < qtosDados; j++)
                if (valorDe(i).getRa().compareTo(valorDe(j).getRa()) > 0)
                    trocar(i, j);
    }

    public Boolean estaVazio() {
        return qtosDados == 0;
    }

    public Boolean estaNoInicio() {
        return posicaoAtual == 0;
    }

    public Boolean estaNoFim() {
        return posicaoAtual == qtosDados - 1;
    }

    public void irAoInicio() {
        posicaoAtual = 0;
    }

    public void irAoFim() {
        posicaoAtual = qtosDados - 1;
    }

    public void irAoAnterior() {
        posicaoAtual--;
    }

    public void irAoProximo() {
        posicaoAtual++;
    }
}
