package controller;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TitleController implements Initializable
{
	@FXML
	private Button comecar;

	@FXML
	private Button tutorial;

	@FXML
	private MediaPlayer intro;

	private AudioClip somDoClique;
	private AudioClip somDoHover;
	private AudioClip cliquePadrao;

	private static final String CLICK_SOUND_PATH = "/util/call_sound_effect.mp3";
	private static final String HOVER_SOUND_PATH = "/util/selection_sound_effect.mp3";
	private static final String INTRO_SOUND_PATH = "/util/01-Metal-Gear-Solid-Main-Theme.mp3";
	private static final String DEFAULT_SOUND_PATH = "/util/Default_button_sound_efffect.mp3";

/* ***************************************************************
* Metodo: initialize
* Funcao: inicializa o controlador do FXML
* Parametros: url e recursos
* Retorno: void
*************************************************************** */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		// carrega o som de clique
		URL somDoCliqueURL = getClass().getResource(CLICK_SOUND_PATH);
		if(somDoCliqueURL != null) 
		{
			somDoClique = new AudioClip(somDoCliqueURL.toExternalForm());
			somDoClique.setVolume(1.0);
		} 
		else 
		{
			System.err.println("Erro ao carregar o arquivo de som: " + CLICK_SOUND_PATH);
		}

		// carrega o som do botao padrao
		URL somBotaoPadraoURL = getClass().getResource(DEFAULT_SOUND_PATH);
		if(somBotaoPadraoURL != null) 
		{
			cliquePadrao = new AudioClip(somBotaoPadraoURL.toExternalForm());
			cliquePadrao.setVolume(1.0);
		} 
		else 
		{
			System.err.println("Erro ao carregar o arquivo de som: " + DEFAULT_SOUND_PATH);
		}

		// carrega o som de hover
		URL somDoHoverURL = getClass().getResource(HOVER_SOUND_PATH);
		if(somDoHoverURL != null) 
		{
			somDoHover = new AudioClip(somDoHoverURL.toExternalForm());
			somDoHover.setVolume(1.0);
		} 
		else 
		{
			System.err.println("Erro ao carregar o arquivo de som: " + HOVER_SOUND_PATH);
		}

		// carrega o som de intro
		URL introURL = getClass().getResource(INTRO_SOUND_PATH);
		if(introURL != null)
		{
			Media media = new Media(introURL.toExternalForm());
			intro = new MediaPlayer(media);
			intro.setCycleCount(MediaPlayer.INDEFINITE); // loop infinito
			intro.setVolume(0.1);

			// Seta o volume quando pronto
    		intro.setOnReady(() -> {
        	intro.play();
			});
		}else
		{
			System.err.println("Erro ao carregar o arquivo de som: " + INTRO_SOUND_PATH);
		}

		// atribui as funcoes do botao comecar
		comecar.setOnAction(this::HandleBotaoComecar);
		comecar.setOnMouseEntered(this::HandleBotaoHover);

		// atribui as funcoes do botao tutorial
		tutorial.setOnAction(this::handleBotaoTutorial);
		tutorial.setOnMouseEntered(this::HandleBotaoHover);
	}

/* ***************************************************************
* Metodo: HandleBotaoComecar
* Funcao: Configura as funcoes do botao de start
* Parametros: evento de interacao com o botao
* Retorno: void
*************************************************************** */
	public void HandleBotaoComecar(ActionEvent event)
	{
		// toca o som do clique
		if(somDoClique != null) 
		{
			somDoClique.play();
		} 
		else 
		{
			System.err.println("Som nao carregado, nao pode tocar o som.");
		}
		
		// para a intro se estiver tocando
		if(intro != null) 
		{
			intro.stop();
		} 
		else 
		{
			System.err.println("Som nao carregado, nao pode parar o som.");
		}

		// Lógica para iniciar
		try
		{
			AnchorPane telaPrincipal = new AnchorPane();
			telaPrincipal = FXMLLoader.load(getClass().getResource("/view/Main_screen.fxml"));
			telaPrincipal.getStylesheets().add(getClass().getResource("/view/Main_screen_style.css").toExternalForm());

			Scene cenaPrincipal = new Scene(telaPrincipal);

			currentStage(event).setScene(cenaPrincipal);
		}	catch (IOException e)
		{
			e.printStackTrace();
		}
	}

/* ***************************************************************
* Metodo: handleBotaoTutorial
* Funcao: Configura as funcoes do botao de tutorial
* Parametros: evento de interacao com o botao
* Retorno: void
*************************************************************** */
	public void handleBotaoTutorial(ActionEvent event)
	{
		// toca o som do clique padrao
		if(cliquePadrao != null) 
		{
			cliquePadrao.play();
		} 
		else 
		{
			System.err.println("Som nao carregado, nao pode tocar o som.");
		}

		// Lógica para tutorial
		try
		{
			// Carrega o FXML para a janela pop-up do tutorial
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Tutorial_screen.fxml"));
      Parent tutorialRoot = loader.load();

      // Cria um novo Stage para o tutorial
      Stage tutorialStage = new Stage();
      tutorialStage.initModality(Modality.APPLICATION_MODAL); // faz com que o pop-up seja modal
      tutorialStage.setTitle("Tutorial");
      tutorialStage.setScene(new Scene(tutorialRoot));
      tutorialStage.setResizable(false); // impede o redimensionamento

      // abre o tutorial
      tutorialStage.show();
		}	catch (IOException e)
		{
			e.printStackTrace();
		}
	}

/* ***************************************************************
* Metodo: HandleBotaoHover
* Funcao: Configura a reacao quando o mouse passa em cima do botao
* Parametros: evento de interacao do mouse com o botao
* Retorno: void
*************************************************************** */
	public void HandleBotaoHover(MouseEvent event)
	{
		if(somDoHover != null) 
		{
			somDoHover.play();
		} 
		else 
		{
			System.err.println("Som nao carregado, nao pode tocar o som.");
		}
	}

/* ***************************************************************
* Metodo: currentStage
* Funcao: retorna o stage atual
* Parametros: evento
* Retorno: stage atual
*************************************************************** */
	public Stage currentStage(Event event) {
    //Ele retorna o Stage atual de onde ocorreu o evento
    return (Stage) ((Node) event.getSource()).getScene().getWindow();
  }
}

