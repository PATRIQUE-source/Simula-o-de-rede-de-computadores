/* ***************************************************************
* Autor............: patrique Rodrigues Nascimento
* Matricula........: 202310197
* Inicio...........: 26/08/2025
* Ultima alteracao.: xx/xx/xxxx
* Nome.............: CamadaFisicaReceptora
* Funcao...........: Recebe do meio de comunicacao a mensagem codificada
*************************************************************** */
package model;

import controller.PrincipalController;

public class CamadaFisicaReceptoraClasse {
	private CamadaDeAplicacaoReceptoraClasse camadaDeAplicacaoReceptoraClasse;

  public CamadaFisicaReceptoraClasse(PrincipalController principalController)
  {
    camadaDeAplicacaoReceptoraClasse = new CamadaDeAplicacaoReceptoraClasse(principalController);
  }

/* ***************************************************************
* Metodo: CamadaFisicaReceptora
* Funcao: selecionar o tipo de decodificacao e mandar o fluxo de bits para a camada de aplicacao receptora
* Parametros: array com os inteiros empacotados
* Retorno: array decodificado
*************************************************************** */
	public void CamadaFisicaReceptora(int [] quadro)
	{
		int tipoDeCodificacao = PrincipalController.tipoDeCodificacao;

		int [] fluxoBrutoDeBits = new int[quadro.length];

		switch (tipoDeCodificacao)
		{
			case 0: // Decodificacao Binaria
				fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoBinaria(quadro);
				break;

			case 1: // Decodificacao Manchester
				fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchester(quadro);
				break;

			case 2: // Decodificacao Manchester Diferencial
				fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(quadro);
				break;

			default:
				break;
		}

		camadaDeAplicacaoReceptoraClasse.CamadaDeAplicacaoReceptora(fluxoBrutoDeBits);
	} 

/* ***************************************************************
* Metodo: CamadaFisicaReceptoraDecodificacaoBinaria
* Funcao: Decodifica a mensagem binaria
* Parametros: array com os inteiros empacotados
* Retorno: array decodificado
*************************************************************** */
	int[] CamadaFisicaReceptoraDecodificacaoBinaria(int[] quadro) 
	{
    int mensagemDecodificada[] = new int[quadro.length];
    int vereficacao = 1; 
    int indice = 0; 
    int mascara = 1; 
    int contador = 1; 
    int mascara2 = 1; 

    while (Math.abs(quadro[indice]) >= vereficacao) // enquanto ainda houver bits no array
    {
      if((mascara & quadro[indice]) == 0) // se o bit for 0
      {
        mensagemDecodificada[indice] <<= 1;
      } else { // se o bit for 1
        mensagemDecodificada[indice] <<= 1; 

        mensagemDecodificada[indice] = (mascara2 | mensagemDecodificada[indice]); // adiciona 1 no final
      }

      mascara <<= 1;
      
      if(contador % 32 == 0) // a cada 32 bits ele troca de numero do array
      {
        indice ++;

        mascara = 1;
      }

      if(contador % 8 == 0) // a cada 8 bits ele troca de posicao do numero
      {
        vereficacao = (int) Math.pow(2, contador);

        if(contador % 32 == 0) // se for o ultimo bit do numero, reseta a verificacao
        {
          vereficacao = 1;

          contador = 0;
        }
      }

      contador ++;
    }

    return mensagemDecodificada;
	}

/* ***************************************************************
* Metodo: CamadaFisicaReceptoraDecodificacaoManchester
* Funcao: decodifica e desempacota a mensagem manchester
* Parametros: array com os inteiros empacotados
* Retorno: array decodificado e desempacotado
*************************************************************** */
	int[] CamadaFisicaReceptoraDecodificacaoManchester(int[] quadro) 
	{
    int mensagemDecodificada[] = new int[quadro.length];
    int potencia = 1; // indica a potencia que a verificacao deve estar
    int vereficacao = 1;
    int indiceQuadro = 0;
    int indiceCodificacao = 0; //indice para o array codificado em manchester
    int mascara = 1;
    int contador = 1;
    int mascara2 = 1;

    while (Math.abs(quadro[indiceCodificacao]) >= vereficacao) // enquanto ainda houver bits no array 
    {
      if((mascara & quadro[indiceCodificacao]) == 0) // se for 0
      {
        mensagemDecodificada[indiceQuadro] <<= 1;
      } else { // se for 1
        mensagemDecodificada[indiceQuadro] <<= 1;

        mensagemDecodificada[indiceQuadro] = (mascara2 | mensagemDecodificada[indiceQuadro]); // adiciona 1 no final
      }

      mascara <<= 2;

      if(contador % 16 == 0) // a cada 16 bits troca de posicao do numero codificado
      {
        indiceCodificacao ++;

        mascara = 1;
      }

      if(contador % 32 == 0) // a cada 32 bits troca de numero do quadro
      {
        indiceQuadro ++;
      }

      if(contador % 8 == 0) // a cada 8 bits atualiza a verificacao
      {
        vereficacao = (int) Math.pow(4, potencia);

        if(contador % 16 == 0) // se for o ultimo bit do numero, reseta a verificacao
        {
          vereficacao = 1;

          potencia = 0;
        }
      }

      contador ++;

      potencia ++;
    }

    return mensagemDecodificada;
	}

/* ***************************************************************
* Metodo: CamadaFisicaReceptoraDecodificacaoManchesterDiferencial
* Funcao: dcodifica e desempacota a mensagem manchester diferencial
* Parametros: array com os inteiros empacotados
* Retorno: array decodificado e desempacotado
*************************************************************** */
	int[] CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(int[] quadro) 
	{
    char anterior;
    int mensagemDecodificada[] = new int[quadro.length];
    int potencia = 1;
    int vereficacao = 1;
    int indice = 0;
    int indiceCodificacao = 0;
    int mascara = 1;
    int contador = 1;
    int mascara2 = 1;

    if((mascara & quadro[0]) == 0) // se o primeiro bit for 0
    {
      mensagemDecodificada[indice] <<= 1;

      anterior = '1';
    } else { // se o primeiro bit for 1
      mensagemDecodificada[indice] <<= 1;

      mensagemDecodificada[indice] = (mascara2 | mensagemDecodificada[indice]); // adiciona 1 no final

      anterior = '0';
    }

    contador ++;

    mascara <<= 2;

    while (Math.abs(quadro[indiceCodificacao]) >= vereficacao) // enquanto ainda houver bits no array
    {
      if((mascara & quadro[indiceCodificacao]) == 0) // se for 0
      {
        if(anterior == '1') // se o anterior for 1
        {
          mensagemDecodificada[indice] <<= 1;
        } else { // se o anterior for 0
          mensagemDecodificada[indice] <<= 1;

          mensagemDecodificada[indice] = (mascara2 | mensagemDecodificada[indice]); // adiciona 1 no final
        }

        anterior = '1';
      } else { // se for 1
        if(anterior == '1')
        {
          mensagemDecodificada[indice] <<= 1;

          mensagemDecodificada[indice] = (mascara2 | mensagemDecodificada[indice]); // adiciona 1 no final
        } else {
          mensagemDecodificada[indice] <<= 1;
        }

        anterior = '0';
      }
      mascara <<= 2;

      if(contador % 16 == 0) // a cada 16 bits troca de posicao do numero codificado
      {
        indiceCodificacao ++;

        mascara = 1;
      }
      
      if(contador % 32 == 0) // a cada 32 bits troca de numero do quadro
      {
        indice ++;
      }

      if(contador % 8 == 0) // a cada 8 bits atualiza a verificacao
      {
        vereficacao = (int) Math.pow(4, potencia);

        if(contador % 16 == 0) // se for o ultimo bit do numero, reseta a verificacao
        {
          vereficacao = 1;

          potencia = 0;
        }
      }

      contador ++;
    }

    return mensagemDecodificada;
	}
}
