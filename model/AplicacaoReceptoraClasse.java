/* ***************************************************************
* Autor............: patrique Rodrigues Nascimento
* Matricula........: 202310197
* Inicio...........: 26/08/2025
* Ultima alteracao.: xx/xx/xxxx
* Nome.............: AplicacaoReceptora
* Funcao...........: Exibe a mensagem recebida decodificada
*************************************************************** */
package model;

import controller.PrincipalController;

public class AplicacaoReceptoraClasse {
	private PrincipalController principalController;

	public AplicacaoReceptoraClasse(PrincipalController principalController)
	{
		this.principalController = principalController;
	}

/* ***************************************************************
* Metodo: AplicacaoReceptora
* Funcao: manda a mensagem final para o controller
* Parametros: mansagem final decodificada em string
* Retorno: void
*************************************************************** */
	public void AplicacaoReceptora(String mensagem)
	{
		principalController.RecebeMensagem(mensagem);
	}
}
