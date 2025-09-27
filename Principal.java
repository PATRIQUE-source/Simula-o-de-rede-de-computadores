/* ***************************************************************
* Autor............: Patrique Rodrigues Nascimento
* Matricula........: 202310197
* Inicio...........: 26/08/2025
* Ultima alteracao.: xx/xx/xxxx
* Nome.............: Principal
* Funcao...........: ponto central do projeto, inicia a aplicacao
*************************************************************** */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import controller.TitleController;
import controller.PrincipalController;


public class Principal extends Application {
/* ***************************************************************
* Metodo: start
* Funcao: inicia o painel e configura a cena
* Parametros: stage
* Retorno: void
*************************************************************** */
  @Override
	public void start(Stage stage) {
		try {
			stage.setResizable(false);

			try {
        Font.loadFont(getClass().getResourceAsStream("/util/Minecraft.ttf"), 12);

        System.out.println("Fonte Minecraft carregada com sucesso!");
      } catch (Exception e) {
        System.err.println("Erro ao carregar a fonte Minecraft.ttf.");
				
        e.printStackTrace();
      }
			
			AnchorPane anchorPane = new AnchorPane();
			anchorPane = FXMLLoader.load(getClass().getResource("/view/Title_screen.fxml"));
			anchorPane.getStylesheets().add(getClass().getResource("/view/Main_screen_style.css").toExternalForm());

			Scene scene = new Scene(anchorPane);
			
			stage.setScene(scene);
			stage.setTitle("Transmission");
			stage.getIcons().add(new Image(getClass().getResource("/img/T1_icon.png").toExternalForm()));
			stage.show();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
/* ***************************************************************
* Metodo: stop
* Funcao: testa os valores para transmissao
* Parametros: xxx
* Retorno: void
*************************************************************** */
	@Override
	public void stop() throws Exception {
		super.stop();
		System.exit(0);
	}
    
/* ***************************************************************
* Metodo: main
* Funcao: iniciar a aplicacao
* Parametros: vetor de strings "args"
* Retorno: void
*************************************************************** */
  public static void main (String[] args)
  {
    launch(args);
  }
}
