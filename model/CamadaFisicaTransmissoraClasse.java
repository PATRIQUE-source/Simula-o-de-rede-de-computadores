/* ***************************************************************
* Autor............: patrique Rodrigues Nascimento
* Matricula........: 202310197
* Inicio...........: 26/08/2025
* Ultima alteracao.: xx/xx/xxxx
* Nome.............: CamadaFisicaTransmissora
* Funcao...........: codifica a mensagem com o metodo selecionado e manda para o meio de comunicacao
*************************************************************** */
package model;

import controller.PrincipalController;

public class CamadaFisicaTransmissoraClasse {
	private MeioDeComunicacaoClasse meioDeComunicacaoClasse;

  public CamadaFisicaTransmissoraClasse(PrincipalController principalController)
  {
    meioDeComunicacaoClasse = new MeioDeComunicacaoClasse(principalController);
  }

/* ***************************************************************
* Metodo: CamadaFisicaTransmissora
* Funcao: selecionar o tipo de codificacao e mandar o fluxo de bits para o meio de comunicacao
* Parametros: int[]
* Retorno: void
*************************************************************** */
	public void CamadaFisicaTransmissora(int [] quadro)
	{
		int tipoDeCodificacao = PrincipalController.tipoDeCodificacao;
		int fluxoBrutoDeBits [] = new int[quadro.length];

		switch (tipoDeCodificacao)
		{
			case 0: //codificacao binaria
				fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoBinaria(quadro);
				break;

			case 1: //codificacao manchester
				fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchester(quadro);
				break;

			case 2: //codificacao manchester diferencial
				fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
				break;

			default:
				System.out.println("ERRO: Tipo de codificacao invalido");
				break;
		}

		meioDeComunicacaoClasse.MeioDeComunicacao(MeioDeComunicacaoClasse.Reverso(fluxoBrutoDeBits));
	}

/* ***************************************************************
* Metodo: CamadaFisicaTransmissoraCodificacaoBinaria
* Funcao: faz a codificacao binaria
* Parametros: int[]
* Retorno: int[]
*************************************************************** */
	int[] CamadaFisicaTransmissoraCodificacaoBinaria(int [] quadro)
  {
    MeioDeComunicacaoClasse.DisplayDeCodigoBinario(quadro);

    //this.controller.Sinais(quadro);

    int mensagemCodificada[] = new int[quadro.length];
    int vereficacao = 1;
    int indice = 0;
    int mascara = 1;
    int contador = 1;
    int mascara2 = 1;

    while (Math.abs(quadro[indice]) >= vereficacao) 
    {
      if((mascara & quadro[indice]) == 0)
      {
        mensagemCodificada[indice] <<= 1;
      } else {
        mensagemCodificada[indice] <<= 1;

        mensagemCodificada[indice] = (mascara2 | mensagemCodificada[indice]); //Coloca o bit 1 na posicao correta
      }

      mascara <<= 1;
    
      if(contador % 32 == 0) //A cada 32 bits ele troca de numero do array
      {
        indice ++;
        mascara = 1;
      }

      if(contador % 8 == 0) //A cada 8 bits ele troca de posicao do numero
      {
        vereficacao = (int) Math.pow(2, contador);

        if(contador % 32 == 0) // Se for o ultimo bit do numero, ele reseta a verificacao
        {
          vereficacao = 1;
          contador = 0;
        }
      }

      contador ++;
    }

    return mensagemCodificada;
  }

/* ***************************************************************
* Metodo: CamadaFisicaTransmissoraCodificacaoManchester
* Funcao: faz a codificacao manchester
* Parametros: int[]
* Retorno: int[]
*************************************************************** */
	int[] CamadaFisicaTransmissoraCodificacaoManchester(int [] quadro)
  {
    MeioDeComunicacaoClasse.DisplayDeCodigoManchester(quadro);

    //this.controller.Sinais(quadro);

    int manchester[] = new int[quadro.length];
    int vereficacao = 1;
    int indiceQuadro = 0;
    int indiceCodificacao = 0; // diferente do indiceQuadro, esse serve para saber em qual posicao do array manchester esta
    int mascara = 1;
    int contador = 1;
    int mascaraNumber1 = 1;

    while (Math.abs(quadro[indiceQuadro]) >= vereficacao) 
    {
      if((mascara & quadro[indiceQuadro]) == 0) // se for pra inserir um 0
      {
        manchester[indiceCodificacao] <<= 1;

        manchester[indiceCodificacao] <<= 1;

        manchester[indiceCodificacao] = (mascaraNumber1 | manchester[indiceCodificacao]);
      } else { // se for pra inserir um 1
        manchester[indiceCodificacao] <<= 1;

        manchester[indiceCodificacao] = (mascaraNumber1 | manchester[indiceCodificacao]);

        manchester[indiceCodificacao] <<= 1;
      }

      mascara <<= 1;

      if(contador % 32 == 0){ // a cada 32 bits ele troca de numero do quadro
        indiceQuadro ++;

        mascara = 1;
      }

      if(contador % 16 == 0) // a cada 16 bits ele troca de posicao do numero codificado
      {
        indiceCodificacao ++;
      }

      if(contador % 8 == 0) // a cada 8 bits ele atualiza a verificacao
      {
        vereficacao = (int) Math.pow(2, contador);

        if(contador % 32 == 0) // se for o ultimo bit do numero, ele reseta a verificacao
        {
          vereficacao = 1;

          contador = 0;
        }
      }

      contador ++;
    }

    return manchester;
  }

/* ***************************************************************
* Metodo: CamadaFisicaTransmissoraCodificacaoManchesterDiferencial
* Funcao: faz a codificacao manchester diferencial
* Parametros: int[]
* Retorno: int[]
*************************************************************** */
	int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(int [] quadro)
  {
    MeioDeComunicacaoClasse.DisplayDeCodigomanchesterDiferencial(quadro);

    //this.controller.Sinais(quadro);

    int manchesterDiferencial[] = new int[quadro.length];
    int vereficacao = 1;
    char anterior; // armazena o ultimo bit inserido na codificacao
    int indiceQuadro = 0;
    int indiceCodificacao = 0;
    int mascara = 1;
    int contador = 1;
    int mascaraNumber1 = 1;
    
    if((mascara & quadro[0]) == 0){ // se o primeiro bit for 0
      manchesterDiferencial[indiceCodificacao] <<= 1;

      manchesterDiferencial[indiceCodificacao] <<= 1;

      manchesterDiferencial[indiceCodificacao] = (mascaraNumber1 | manchesterDiferencial[indiceCodificacao]);

      anterior = '1';
    } else { // se o primeiro bit for 1
      manchesterDiferencial[indiceCodificacao] <<= 1;

      manchesterDiferencial[indiceCodificacao] = (mascaraNumber1 | manchesterDiferencial[indiceCodificacao]);

      manchesterDiferencial[indiceCodificacao] <<= 1;

      anterior = '0';
    }

    contador ++;

    mascara <<= 1;

    while (Math.abs(quadro[indiceQuadro]) >= vereficacao) // enquanto houver bits para ler
    {
      if((mascara & quadro[indiceQuadro]) == 0) // se for pra inserir um 0
      {
        if(anterior == '1') // se o ultimo bit inserido foi 1, insere 0
        {
          manchesterDiferencial[indiceCodificacao] <<= 1;

          manchesterDiferencial[indiceCodificacao] <<= 1;

          manchesterDiferencial[indiceCodificacao] = (mascaraNumber1 | manchesterDiferencial[indiceCodificacao]);

          anterior = '1';
        } else { // se o ultimo bit inserido foi 0, insere 1
          manchesterDiferencial[indiceCodificacao] <<= 1;

          manchesterDiferencial[indiceCodificacao] = (mascaraNumber1 | manchesterDiferencial[indiceCodificacao]);

          manchesterDiferencial[indiceCodificacao] <<= 1;

          anterior = '0';
        }
      } else { // se for pra inserir um 1
        if(anterior == '1') // se o ultimo bit inserido foi 1, insere 1
        {
          manchesterDiferencial[indiceCodificacao] <<= 1;

          manchesterDiferencial[indiceCodificacao] = (mascaraNumber1 | manchesterDiferencial[indiceCodificacao]);

          manchesterDiferencial[indiceCodificacao] <<= 1;

          anterior = '0';
        } else { // se o ultimo bit inserido foi 0, insere 0
          manchesterDiferencial[indiceCodificacao] <<= 1;

          manchesterDiferencial[indiceCodificacao] <<= 1;

          manchesterDiferencial[indiceCodificacao] = (mascaraNumber1 | manchesterDiferencial[indiceCodificacao]);

          anterior = '1';
        }
      }

      mascara <<= 1;

      if(contador % 32 == 0) // a cada 32 bits ele troca de numero do quadro
      {
        indiceQuadro ++;

        mascara = 1;
      }

      if(contador % 16 == 0) // a cada 16 bits ele troca de posicao do numero codificado
      {
        indiceCodificacao ++;
      }

      if(contador % 8 == 0) // a cada 8 bits ele atualiza a verificacao
      {
        vereficacao = (int) Math.pow(2, contador);

        if(contador % 32 == 0) // se for o ultimo bit do numero, ele reseta a verificacao
        {
          vereficacao = 1;

          contador = 0;
        }
      }

      contador++;
    }

    return manchesterDiferencial;
  }
}
