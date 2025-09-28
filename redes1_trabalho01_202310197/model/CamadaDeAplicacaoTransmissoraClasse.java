/* ***************************************************************
* Autor............: patrique Rodrigues Nascimento
* Matricula........: 202310197
* Inicio...........: 26/08/2025
* Ultima alteracao.: 30/08/2025
* Nome.............: CamadaDeAplicacaoTransmissora
* Funcao...........: Recebe a mensagem da aplicacao transmissora e armazena num array de bits
*************************************************************** */
package model;

import controller.PrincipalController;

public class CamadaDeAplicacaoTransmissoraClasse {
  private CamadaFisicaTransmissoraClasse camadaFisicaTransmissoraClasse;

  public CamadaDeAplicacaoTransmissoraClasse(PrincipalController principalController) 
  {
    camadaFisicaTransmissoraClasse = new CamadaFisicaTransmissoraClasse(principalController);
  }

  int[] quadro = new int[0];

  /* ***************************************************************
  * Metodo: CamadaDeAplicacaoTransmissora
  * Funcao: atribui a mensagem convertida em bits ao quadro e manda para a camada fisica transmissora
  * Parametros: String
  * Retorno: void
  *************************************************************** */
  public void CamadaDeAplicacaoTransmissora(String mensagem) 
  {
    quadro = ConversaoEmBits(mensagem);

    camadaFisicaTransmissoraClasse.CamadaFisicaTransmissora(quadro);
  }

  /* ***************************************************************
  * Metodo: ConversaoEmBits
  * Funcao: coloca os bits dos caracteres ASCII da mensagem em um array de inteiros compactados
  * Parametros: String
  * Retorno: array de inteiros que eh a mensagem em bits
  *************************************************************** */
  private int[] ConversaoEmBits(String mensagem)
  {
    int[] mensagemEmBits = new int[(int) Math.ceil((mensagem.length()/2.0)) + 1]; //dimensiona a saida para comportar 4 caracteres por inteiro

    char[] caracteres = mensagem.toCharArray();

    int indice = 0;

    for(int i = 0; i < caracteres.length; i++) // percorre o array enquanto houver caracteres
    {
      mensagemEmBits[indice] = TransformaEmBits(mensagemEmBits[indice], caracteres[i]);

      if(mensagemEmBits[indice] >= 33554431) // verifica se o inteiro ja esta cheio
      {
        indice++;
      }
    }

    return mensagemEmBits;
  }

  /* ***************************************************************
  * Metodo: TransformaEmBits
  * Funcao: transforma o caractere da mensagem em um array de bits
  * Parametros: char
  * Retorno: int[]
  *************************************************************** */
  private int TransformaEmBits(int numero, int correspondente) 
  {
    if(numero != 0) // verifica se o numero ja esta com bits preenchidos
    {
      numero <<= 8; // desloca os bits 8 posicoes para a esquerda para abrir espaco para o novo caractere
    }

    int mascara = 1;
    
    for(int i = 0; i < 8; i++) // percorre os 8 bits do caractere
    {
      if(correspondente % 2 == 0)
      {
        mascara <<= 1;
      } else {
        numero = numero | mascara;

        mascara <<= 1;
      }

      correspondente /= 2; // desloca o caractere para a direita para analisar o proximo bit
    }

    return numero;
  }
}