import javax.swing.*;
import javax.swing.border.Border;
import java.io.*;
import java.util.Scanner;

import static java.lang.System.out;

public class ManterEstudantes implements ManterDados {
    int qtosDados, posicaoAtual;
    Estudante[] dados;
    Situacao situacao;
    Scanner leitor = new Scanner(System.in);

    public void leituraDosDados(String nomeArquivo) {
        try {
            BufferedReader arquivoDeEntrada = new BufferedReader(
                    new FileReader(nomeArquivo)
            );
            String linhaDoArquivo = "";
            try {
                boolean parar = false;
                while (! parar) {
                    Estudante novoEstudante = new Estudante();
                    try {
                        if (novoEstudante.leuLinhaDoArquivo(arquivoDeEntrada) ) {
                            incluirNoFinal(novoEstudante);
                            qtosDados++;
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

    public Boolean existe(Estudante dadoProcurado) {
        int inicio = 0;
        int fim = qtosDados - 1;
        boolean achou = false;
        while (! achou && inicio <= fim) {
            posicaoAtual = (inicio + fim) / 2;
            String raDoMeioDoTrechoDoVetor = dados[posicaoAtual].getRa();
            String raDoProcurado = dadoProcurado.getRa();
            if (raDoMeioDoTrechoDoVetor.compareTo(raDoProcurado) == 0)
                achou = true;
            else
            if (raDoProcurado.compareTo(raDoMeioDoTrechoDoVetor) < 0)
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
            incluirEm(novoDado, qtosDados-1);  // última posição usada
        }
    }

    public void incluirEm(Estudante novoDado, int posicaoDeInclusao) {
        if (existe(novoDado)) {
            JOptionPane.showMessageDialog(null,"Estudante repetido!");
        } else {
            for (int i = qtosDados; i > posicaoDeInclusao; i--) {
                dados[i] = dados[i-1];
            }
            dados[posicaoDeInclusao] = novoDado;
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
                if (dados[i].getRa().compareTo(dados[j].getRa()) > 0)
                    trocar(i, j);
    }

    public Boolean estaVazio() {
        return null;
    }

    public Boolean estaNoInicio() {
        return null;
    }

    public Boolean estaNoFim() {
        return null;
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

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao novaSituacao) {
        situacao = novaSituacao;
    }
}
