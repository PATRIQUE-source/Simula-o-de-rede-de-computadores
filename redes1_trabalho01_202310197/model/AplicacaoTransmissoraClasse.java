/* ***************************************************************
* Autor............: patrique Rodrigues Nascimento
* Matricula........: 202310197
* Inicio...........: 26/08/2025
* Ultima alteracao.: xx/xx/xxxx
* Nome.............: AplicacaoTransmissora
* Funcao...........: Recebe as entradas do usuario e manda para a camada de aplicacao transmissora
*************************************************************** */
package model;

import controller.PrincipalController;

public class AplicacaoTransmissoraClasse
{
	private CamadaDeAplicacaoTransmissoraClasse camadaDeAplicacaoTransmissoraClasse;

	public AplicacaoTransmissoraClasse(PrincipalController principalController)
	{
		camadaDeAplicacaoTransmissoraClasse = new CamadaDeAplicacaoTransmissoraClasse(principalController);
	}
	
/* ***************************************************************
* Metodo: AplicacaoTransmissora
* Funcao: manda a mensagem para a camada de aplicacao transmissora
* Parametros: String
* Retorno: void
*************************************************************** */
	public void AplicacaoTransmissora(String mensagem)
	{  
		camadaDeAplicacaoTransmissoraClasse.CamadaDeAplicacaoTransmissora(mensagem);
	}
}
