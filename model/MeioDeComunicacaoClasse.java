/* ***************************************************************
* Autor............: patrique Rodrigues Nascimento
* Matricula........: 202310197
* Inicio...........: 26/08/2025
* Ultima alteracao.: xx/xx/xxxx
* Nome.............: MeioDeComunicacaoClasse
* Funcao...........: manda a mensagem codificada de um ponto a outro
*************************************************************** */
package model;

import controller.PrincipalController;

public class MeioDeComunicacaoClasse {
	private CamadaFisicaReceptoraClasse camadaFisicaReceptoraClasse;

  public MeioDeComunicacaoClasse(PrincipalController principalController)
  {
    camadaFisicaReceptoraClasse = new CamadaFisicaReceptoraClasse(principalController);
  }
	
/* ***************************************************************
* Metodo: MeioDeComunicacao
* Funcao: manda o fluxo de bits de um ponto a outro e chama a proxima camada
* Parametros: array de inteiros codificado
* Retorno: array de inteiros copiado
*************************************************************** */
	public void MeioDeComunicacao(int [] fluxoBrutoDeBits)
	{
		int [] fluxoBrutoDeBitsPontoA, fluxoBrutoDeBitsPontoB = new int[fluxoBrutoDeBits.length];

		fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;

		fluxoBrutoDeBitsPontoB = TransfereBits(fluxoBrutoDeBitsPontoA);

		camadaFisicaReceptoraClasse.CamadaFisicaReceptora(fluxoBrutoDeBitsPontoB);
	}  

/* ***************************************************************
* Metodo: TransfereBits
* Funcao: copia bit a bit de um array para outro
* Parametros: array de inteiros codificado
* Retorno: array de inteiros copiado
*************************************************************** */
	private int[] TransfereBits(int[] bits) {
    int transferidorDeBits[] = new int[bits.length];
    int indice = 0;
    int mascara = 1;
    int contador = 1;
    int vereficacao = 1;

    while (Math.abs(bits[indice]) >= vereficacao) // enquanto ainda houver bits no array
    {
      if((bits[indice] & mascara) == 0) // se o bit for 0
      {
        mascara <<= 1;
      } else { // se o bit for 1
        transferidorDeBits[indice] = (transferidorDeBits[indice] | mascara); // adiciona 1 no final

        mascara <<= 1;
      }

      if(contador % 8 == 0) // a cada 8 bits atualiza a verificacao
      {
        vereficacao = (int) Math.pow(2, contador);

        if(contador % 32 == 0) // se for o ultimo bit do numero, reseta a verificacao
        {
          vereficacao = 0;

          contador = 0;
        }
      }

      if(contador % 32 == 0) // reseta a mascara e aumenta 1 no indice
      {
        mascara = 1;

        indice ++;
      }

      contador ++;
    }

    return transferidorDeBits;
  }

/****************************************************************
  * Metodo: Reverso
  * Funcao: reverte o array de inteiros
  * Parametros: array de inteiros enviado
  * Retorno: array de inteiros reverso
****************************************************************/
  public static int[] Reverso(int bits[]){
    int transferidorDeBits[] = new int[bits.length];
    int indice = 0;
    int mascara = 1;
    int mascaraNumber1 = 1;
    int verificacao = 1;
    int contador = 1;

    while (Math.abs(bits[indice]) >= verificacao) // enquanto ainda houver bits no array
    {
      if((mascara & bits[indice]) == 0) // se o bit for 0
      {
        transferidorDeBits[indice] <<= 1; 
      } else { // se o bit for 1
        transferidorDeBits[indice] <<= 1; 

        transferidorDeBits[indice] = (transferidorDeBits[indice] | mascaraNumber1); // adiciona 1 no final
      }

      mascara <<= 1;

      if(contador % 8 == 0) // a cada 8 bits atualiza a verificacao
      {
        verificacao = (int) Math.pow(2, contador);
          
        if(contador % 32 == 0) // se for o ultimo bit do numero, reseta a verificacao
        {
          verificacao = 1;

          contador = 0;
        }
      }
            
      if(contador % 32 == 0) // reseta a mascara e aumenta 1 no indice
      {   
        mascara = 1;

        indice ++;
      }

      contador ++;
    }

    return transferidorDeBits;
  }

/* ***************************************************************
* Metodo: DisplayDeCodigoBinaro
* Funcao: exibir o código em bits na interface
* Parametros: array de bits a serem exibidos
* Retorno: void
*************************************************************** */
  public static void DisplayDeCodigoBinario(int[] mensagemCodificada)
  {
    mensagemCodificada = Reverso(mensagemCodificada);
    StringBuilder codigo = new StringBuilder();

    int indice = 0;
    int mascara = 1;
    int contador = 1;
    int verificacao = 1;

    while (Math.abs(mensagemCodificada[indice]) >= verificacao) // enquanto houver bits no array
    {
      if((mensagemCodificada[indice] & mascara) == 0) // se o bit for 0
      {
        codigo.append('0');

        mascara <<= 1;
      } else { // se for 1
        codigo.append('1');

        mascara <<= 1;
      }
            
      if(contador % 8 == 0) // a cada 8 bits separa com espaco e atualiza a verificacao
      {
        codigo.append(' ');

        verificacao = (int) Math.pow(2, contador);
        
        if(contador % 32 == 0) // se for o ultimo bit do numero, reseta a verificacao
        {
          verificacao = 1;

          contador = 0;
        }
      }
            
      if(contador % 32 == 0) // reseta a mascara e aumenta 1 no indice
      {
        mascara = 1;

        indice ++;
      }

      contador ++;
    }

    PrincipalController.Mostrar(codigo.toString());
  }

/* ***************************************************************
* Metodo: DisplayDeCodigoManchester
* Funcao: exibir o codigo em bits na interface
* Parametros: darray de bits a serem exibidos
* Retorno: void
*************************************************************** */
  public static void DisplayDeCodigoManchester(int[] mensagemCodificada)
  {
    mensagemCodificada = Reverso(mensagemCodificada);
    StringBuilder codigo = new StringBuilder();

    int indice = 0;
    int mascara = 1;
    int contador = 1;
    int verificacao = 1;

    while (Math.abs(mensagemCodificada[indice]) >= verificacao) // enquanto houver bits no array
    {
      if((mensagemCodificada[indice] & mascara) == 0) // se o bit for 0
      {
        codigo.append("01"); // 0 = 01

        mascara <<= 1;
      } else { // se o bit for 1
        codigo.append("10"); // 1 = 10

        mascara <<= 1;
      }
            
      if(contador % 8 == 0) // a cada 8 bits separa com espaco e atualiza a verificacao
      {
        codigo.append(' ');

        verificacao = (int) Math.pow(2, contador);

        if(contador % 32 == 0) // se for o ultimo bit do numero, reseta a verificacao
        {
          verificacao = 1;
          
          contador = 0;
        }
      }
            
      if(contador % 32 == 0) // reseta a mascara e aumenta 1 no indice
      {
        mascara = 1;

        indice ++;
      }

      contador ++;
    }

    PrincipalController.Mostrar(codigo.toString());
  }

/* ***************************************************************
* Metodo: DisplayDeCodigoManchesterDiferencial
* Funcao: exibir o codigo em bits na interface
* Parametros: darray de bits a serem exibidos
* Retorno: void
*************************************************************** */
  public static void DisplayDeCodigomanchesterDiferencial(int[] mensagemCodificada)
  {
    mensagemCodificada = Reverso(mensagemCodificada);
    StringBuilder codigo = new StringBuilder();

    int indice = 0;
    int mascara = 1;
    int contador = 1;
    int verificacao = 1;

    char anterior; // lembra o ultimo numero no array
    
    if((mensagemCodificada[indice] & mascara) == 0){ // se o primeiro bit for 0
      codigo.append("01");

      anterior = '1';
    } else { // se for 1
      codigo.append("10");

      anterior = '0';
    }

    contador ++;

    mascara <<= 1;

    while (Math.abs(mensagemCodificada[indice]) >= verificacao) //enquanto houver bits no array
    {
      if((mensagemCodificada[indice] & mascara) == 0) //Se for pra inserir um 0
      {
        if(anterior == '1') // se o ultimo bit inserido foi 1, insere 0
        {
          codigo.append("01");

          anterior = '1';
        } else { // se o ultimo bit inserido foi 0, insere 1
          codigo.append("10");

          anterior = '0';
        }
      } else { //Se for pra inserir um 1
        if(anterior == '1') // se o ultimo bit inserido foi 1, insere 1
        {
          codigo.append("10");

          anterior = '0';
        } else { // se o ultimo bit inserido foi 0, insere 0
          codigo.append("01");

          anterior = '1';
        }
      }
            
      mascara <<= 1;

      if(contador % 8 == 0) // a cada 8 bits separa com espaco e atualiza a verificacao
      {
        codigo.append(' ');

        verificacao = (int) Math.pow(2, contador);

        if(contador % 32 == 0) // se for o ultimo bit do numero, reseta a verificacao
        {
          verificacao = 1;

          contador = 0;
        }
      }
            
      if(contador % 32 == 0) // reseta a mascara e aumenta 1 no indice
      {
        mascara = 1;

        indice ++;
      }

      contador ++;
    }

    PrincipalController.Mostrar(codigo.toString());
  }
}
