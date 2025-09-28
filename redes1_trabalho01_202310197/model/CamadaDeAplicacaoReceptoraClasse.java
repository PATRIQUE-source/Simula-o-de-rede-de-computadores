/* ***************************************************************
* Autor............: patrique Rodrigues Nascimento
* Matricula........: 202310197
* Inicio...........: 26/08/2025
* Ultima alteracao.: xx/xx/xxxx
* Nome.............: CamadaDeAplicacaoReceptora
* Funcao...........: Recebe a mensagem codificada e a decodifica
*************************************************************** */
package model;

import controller.PrincipalController;

public class CamadaDeAplicacaoReceptoraClasse {
	private AplicacaoReceptoraClasse aplicacaoReceptoraClasse;

	public CamadaDeAplicacaoReceptoraClasse(PrincipalController principalController)
	{
		aplicacaoReceptoraClasse = new AplicacaoReceptoraClasse(principalController);
	}

	String mensagem;

/* ***************************************************************
* Metodo: CamadaDeAplicacaoReceptora
* Funcao: atribuir o valor da mensagem convertida em string e mandar para a aplicacao receptora
* Parametros: quadro decodificado e desempacotado
* Retorno: string com a mensagem final
*************************************************************** */
	public void CamadaDeAplicacaoReceptora(int [] quadro)
	{
		mensagem = ConverteParaString(quadro);

		aplicacaoReceptoraClasse.AplicacaoReceptora(mensagem);
	}

/* ***************************************************************
* Metodo: ConverteParaString
* Funcao: converte o array de inteiros para uma string
* Parametros: array de inteiros decodificado e desempacotado
* Retorno: string com a mensagem final
*************************************************************** */
	String ConverteParaString(int[] fluxoBrutoDeBitsPontoB)
	{
		StringBuilder mensagem = new StringBuilder();

    int contador = 1;
    int ascii = 0;
    int indice = 0;
    int mascara = 1;
    int potencia = 7; // potencia invertida para ler de tras paa frente

    while (fluxoBrutoDeBitsPontoB[indice] != 0) // enquanto ainda houver bits no array
    {
      if((mascara & fluxoBrutoDeBitsPontoB[indice]) != 0) // se o bit for 1
      {
        ascii += ((int) Math.pow(2, potencia)); // adiciona o valor do bit na soma do ascii
      }

      mascara <<= 1;

      potencia --;

      if(contador % 8 == 0) // a cada 8 bits converte o valor do ascii para char e reseta as variaveis
      {
        potencia = 7;

        if(ascii != 0) // se nao for o bit de preenchimento 00000000
        {
          mensagem.append((char) ascii); // converte o valor do ascii para char e adiciona na mensagem

          ascii = 0;
        }
      }
      if(contador % 32 == 0) // reseta a mascara e aumenta 1 no indice
      {
        indice ++;

        mascara = 1;
      }

      contador ++;
    }

    return mensagem.toString();
	}
}
