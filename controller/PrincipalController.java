package controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.scene.media.AudioClip;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.AplicacaoTransmissoraClasse;

public class PrincipalController implements Initializable
{
	@FXML
	private Button voltar;
	@FXML
	private Button mutar;
	@FXML
	private Button paraTras;
	@FXML
	private Button paraFrente;
	@FXML
	private Button enviar;

	@FXML
	private ImageView desmutado;
	@FXML
	private ImageView mutado;

	@FXML
	private MediaPlayer musicaDeFundo;

	@FXML
	private TextArea textoTransmmissor;
	@FXML
	private TextArea textoReceptor;
	@FXML
	private TextArea codigoDeTransmissao;
	@FXML
	private TextArea codigoDeRecepcao;
	@FXML
	private TextArea selecionadorDeCodificacao;

	@FXML
	private Canvas canvas;

	private AudioClip somDeSwitch;
	private AudioClip somDeEnviar;
	private AudioClip somDeMutar;
	private AudioClip cliquePadrao;

	public static int tipoDeCodificacao;
	private String[] textoCodificacao = {"Binario", "Manchester", "Manchester Diferencial"};
	private String codigo;
	private static StringBuilder codigoExibido;

	private GraphicsContext pincel;

	private AnimationTimer desenhaOnda;

	public static void Mostrar (String codigo)
	{
		codigoExibido.setLength(0);
		codigoExibido.append(codigo);
	}

	private static final String SWITCH_SOUND_PATH = "/util/change_mode_sound_effect.mp3";
	private static final String BACKGROUND_SOUND_PATH = "/util/Metal_Gear_Solid_Menu_Theme.mp3";
	private static final String SEND_SOUND_PATH = "/util/Send_sound_effect.mp3";
	private static final String MUTE_SOUND_PATH = "/util/Mute_sound_effect.mp3";
	private static final String DEFAULT_SOUND_PATH = "/util/Default_button_sound_efffect.mp3";

	private static final double MAX_FONT_SIZE = 21.0;
  private static final double MIN_FONT_SIZE = 8.0;

/* ***************************************************************
* Metodo: initialize
* Funcao: inicializa o controlador do FXML
* Parametros: url e resourcers
* Retorno: void
*************************************************************** */
	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) 
	{
    selecionadorDeCodificacao.textProperty().addListener((obs, oldText, newText) -> // adiciona o listener para a propriedade de texto
		{
      AjustarFonte(selecionadorDeCodificacao);
    });

    selecionadorDeCodificacao.heightProperty().addListener((obs, oldH, newH) -> // adiciona o listener para a propriedade de altura
		{
      AjustarFonte(selecionadorDeCodificacao);
    });

    Platform.runLater(() -> AjustarFonte(selecionadorDeCodificacao)); // chamada inicial para ajustar o tamanho assim que a tela aparecer

		this.tipoDeCodificacao = 0; //codificacao padrao eh a binaria
		selecionadorDeCodificacao.setText(textoCodificacao [this.tipoDeCodificacao]); // configura o indicador para o padrao

		pincel = canvas.getGraphicsContext2D(); // inicializa o pincel

		codigoExibido = new StringBuilder(); // inicia o stringBuilder da exibicao do codigo na tela

		// seta o os icones de mutar/desmutar
		desmutado.setVisible(true);
		mutado.setVisible(false);

		// carrega o som de switch
		URL somDeSwitchURL = getClass().getResource(SWITCH_SOUND_PATH);
		if(somDeSwitchURL != null) 
		{
			somDeSwitch = new AudioClip(somDeSwitchURL.toExternalForm());
			somDeSwitch.setVolume(1.0);
		} 
		else 
		{
			System.err.println("Erro ao carregar o arquivo de som: " + SWITCH_SOUND_PATH);
		}

		// carrega a musica de fundo
		URL backgroundURL = getClass().getResource(BACKGROUND_SOUND_PATH);
		if(backgroundURL != null)
		{
			Media media = new Media(backgroundURL.toExternalForm());
			musicaDeFundo = new MediaPlayer(media);
			musicaDeFundo.setCycleCount(MediaPlayer.INDEFINITE); // loop infinito
			musicaDeFundo.setVolume(0.1);

			// da play quando estiver pronto
    	musicaDeFundo.setOnReady(() -> {
        musicaDeFundo.play();
			});
		}else
		{
			System.err.println("Erro ao carregar o arquivo de som: " + BACKGROUND_SOUND_PATH);
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

		// carrega o som de mutar
		URL somDeMutarURL = getClass().getResource(MUTE_SOUND_PATH);
		if(somDeMutarURL != null) 
		{
			somDeMutar = new AudioClip(somDeMutarURL.toExternalForm());
			somDeMutar.setVolume(0.5);
		} 
		else 
		{
			System.err.println("Erro ao carregar o arquivo de som: " + MUTE_SOUND_PATH);
		}

		// carrega o som de enviar
		URL somDeEnviarURL = getClass().getResource(SEND_SOUND_PATH);
		if(somDeEnviarURL != null) 
		{
			somDeEnviar = new AudioClip(somDeEnviarURL.toExternalForm());
			somDeEnviar.setVolume(1.0);
		} 
		else 
		{
			System.err.println("Erro ao carregar o arquivo de som: " + SEND_SOUND_PATH);
		}
		
		// atribui a funcao do botao de voltar
		voltar.setOnAction(this::HandleBotaoVoltar);

		//atribui a funcao do botao de mutar
		mutar.setOnAction(this::HandleBotaoMutar);

		// atribui a funcao do botao de enviar
		enviar.setOnMouseClicked(this::HandleBotaoEnviar);

		// atribui a funcao do botao para frente
		paraFrente.setOnMouseClicked(this::HandleBotaoParaFrente);

		// atribui a funcao do botao para tras
		paraTras.setOnMouseClicked(this::HandleBotaoParaTras);
	}

/* ***************************************************************
* Metodo: HandleBotaoVoltar
* Funcao: configura as funcoes do botao de voltar para o menu
* Parametros: evento de interacao com o botao
* Retorno: void
*************************************************************** */
	public void HandleBotaoVoltar(ActionEvent event)
	{
		// toca o som do botao padrao
		if(cliquePadrao != null) 
		{
			cliquePadrao.play();
		} 
		else 
		{
			System.err.println("Som nao carregado, nao pode tocar o som.");
		}
		
		// para a musicaDeFundo se estiver tocando
		if(musicaDeFundo != null) 
		{
			musicaDeFundo.stop();
		} 
		else 
		{
			System.err.println("Som nao carregado, nao pode parar o som.");
		}

		// Lógica para voltar
		try
		{
			AnchorPane telaInicial = new AnchorPane();
			telaInicial = FXMLLoader.load(getClass().getResource("/view/Title_screen.fxml"));
			telaInicial.getStylesheets().add(getClass().getResource("/view/Main_screen_style.css").toExternalForm());

			Scene cenaInicial = new Scene(telaInicial);

			currentStage(event).setScene(cenaInicial);
		}	catch (IOException e)
		{
			e.printStackTrace();
		}
	}

/* ***************************************************************
* Metodo: HandleBotaoMutar
* Funcao: configura as funcoes do botao de mutar e desmutar a musica de fundo
* Parametros: evento de interacao com o botao
* Retorno: void
*************************************************************** */
	public void HandleBotaoMutar(ActionEvent event)
	{
		// toca o som de mutar
		if(somDeMutar != null) 
		{
			somDeMutar.play();
		} 
		else 
		{
			System.err.println("Som nao carregado, nao pode tocar o som.");
		}

		// Lógica para mutar/desmutar a musica de fundo
		if(musicaDeFundo != null) 
		{
			if(musicaDeFundo.getVolume() > 0) // se a musica estiver tocando, muta e muda o icone
			{
				musicaDeFundo.setVolume(0);

				desmutado.setVisible(false);
				mutado.setVisible(true);
			} 
			else // se a musica estiver mutada, desmuta e muda o icone
			{
				musicaDeFundo.setVolume(0.1);

				mutado.setVisible(false);
				desmutado.setVisible(true);
			}
		} 
		else 
		{
			System.err.println("Som nao carregado, nao pode mutar/desmutar.");
		}
	}

/* ***************************************************************
* Metodo: HandleBotaoEnviar
* Funcao: configura as funcoes do botao de enviar mensagem
* Parametros: evento de interacao do mouse com o botao
* Retorno: void
*************************************************************** */
	public void HandleBotaoEnviar(MouseEvent event)
	{
		if(event.getButton().equals(MouseButton.PRIMARY))
		{
			// toca o som de enviar
			if(somDeEnviar != null) 
			{
				somDeEnviar.play();
			} 
			else 
			{
				System.err.println("Som nao carregado, nao pode tocar o som.");
			}

			// pega o texto do textArea e manda para a aplicacao transmissora
			if(!textoTransmmissor.getText().isEmpty())
			{
				String mensagem = textoTransmmissor.getText();

				AplicacaoTransmissoraClasse aplicacaoTransmissoraClasse = new AplicacaoTransmissoraClasse(this);

				aplicacaoTransmissoraClasse.AplicacaoTransmissora(mensagem);

				codigoDeTransmissao.setText(codigoExibido.toString().trim());

				Sinais(bitsParaSinais(codigoDeTransmissao.getText()));

				codigoDeRecepcao.setText(codigoExibido.toString().trim());
			}
		}
	}

/* ***************************************************************
* Metodo: HandleBotaoParaFrente
* Funcao: configura as funcoes do botao de mudar a codificacao para frente
* Parametros: evento de interacao com o botao
* Retorno: void
*************************************************************** */
	public void HandleBotaoParaFrente(MouseEvent event)
	{
		if(event.getButton().equals(MouseButton.PRIMARY))
		{
			// toca o som do switch
			if(somDeSwitch != null) 
			{
				somDeSwitch.play();
			} 
			else {
				System.err.println("Som nao carregado, nao pode tocar o som.");
			}

			if(this.tipoDeCodificacao != 2)
			{
				this.tipoDeCodificacao = this.tipoDeCodificacao += 1;

				selecionadorDeCodificacao.setText(textoCodificacao [this.tipoDeCodificacao]);
			} else{
				this.tipoDeCodificacao = 0;

				selecionadorDeCodificacao.setText(textoCodificacao [this.tipoDeCodificacao]);
			}
		}
	}

/* ***************************************************************
* Metodo: HandleBotaoParaTras
* Funcao: configura as funcoes do botao de mudar a codificacao para tras
* Parametros: evento de interacao com o botao
* Retorno: void
*************************************************************** */
	public void HandleBotaoParaTras(MouseEvent event)
	{
		if(event.getButton().equals(MouseButton.PRIMARY))
		{
			// toca o som do switch
			if(somDeSwitch != null) 
			{
				somDeSwitch.play();
			} 
			else {
				System.err.println("Som nao carregado, nao pode tocar o som.");
			}

			if(this.tipoDeCodificacao != 0)
			{
				this.tipoDeCodificacao = this.tipoDeCodificacao -= 1;

				selecionadorDeCodificacao.setText(textoCodificacao [this.tipoDeCodificacao]);
			} else{
				this.tipoDeCodificacao = 2;

				selecionadorDeCodificacao.setText(textoCodificacao [this.tipoDeCodificacao]);
			}
		}
	}

/* ***************************************************************
* Metodo: AjustarFonte
* Funcao: Ajusta o indicador de selecao de codificacao para o texto se adaptar ao textArea sem criar um scroll
* Parametros: o textArea que indica que codificacao esta selecionada
* Retorno: void
*************************************************************** */
	private void AjustarFonte(TextArea textArea) 
	{
    String conteudo = textArea.getText();
    if (conteudo.isEmpty()) 
		{
      textArea.setStyle("-fx-font-size: " + MAX_FONT_SIZE + "px;");

      return;
    }

    double availableHeight = textArea.getHeight() - 20;

    double availableWidth = textArea.getWidth() - 20;

    if (availableHeight <= 0 || availableWidth <= 0) return;

    Text helperTextNode = new Text(conteudo);

    helperTextNode.setWrappingWidth(availableWidth);

    for (double fontSize = MAX_FONT_SIZE; fontSize >= MIN_FONT_SIZE; fontSize--) 
		{
      helperTextNode.setFont(Font.font("System", fontSize));
            
      if (helperTextNode.getLayoutBounds().getHeight() <= availableHeight) 
			{
        textArea.setStyle("-fx-font-size: " + fontSize + "px;");

        return;
      }
    }

    textArea.setStyle("-fx-font-size: " + MIN_FONT_SIZE + "px;");
    }

/* ***************************************************************
* Metodo: currentStage
* Funcao: retorna o stage atual
* Parametros: evento
* Retorno: stage atual
*************************************************************** */
	public Stage currentStage(Event event) {
    return (Stage) ((Node) event.getSource()).getScene().getWindow();
  }

/* ***************************************************************
* Metodo: RecebeMensagem
* Funcao: printar o texto decodificado recebido
* Parametros: mensagem decodificada
* Retorno: void
*************************************************************** */
	public void RecebeMensagem(String mensagem)
	{
		textoReceptor.setText(mensagem);
	}

/* ***************************************************************
* Metodo: Sinais
* Funcao: desenha a onda transmitida
* Parametros: array de bits transmitidos
* Retorno: void
*************************************************************** */
	public void Sinais (int[] transmissao)
	{
		if (desenhaOnda != null)
		{
			desenhaOnda.stop();

			this.enviar.setDisable(false);
			this.paraFrente.setDisable(false);
			this.paraTras.setDisable(false);
		}

		final double ALTURA = canvas.getHeight();
		final double ALTO = ALTURA * 0.25;
		final double BAIXO = ALTURA * 0.75;
		final double VELOCIDADE = 60.0;
		final double BIT_WIDTH;

		if (this.tipoDeCodificacao == 0)
		{
			BIT_WIDTH = 40.0;
		} else
		{
			BIT_WIDTH = 20.0;
		}

		final double ONDA_LARGURA = transmissao.length * BIT_WIDTH;

		final long inicioNano = System.nanoTime();

		desenhaOnda = new AnimationTimer() {
			@Override
			public void handle(long agora)
			{
				double tempoDecorrido = (agora - inicioNano) / 1_000_000_000.0;

				double offsetHorizontal = tempoDecorrido * VELOCIDADE;

				double posicaoInicial = offsetHorizontal - ONDA_LARGURA;

				pincel.clearRect(0, 0, canvas.getWidth(), ALTURA);
				pincel.setStroke(Color.web("#a3e9c5"));
				pincel.setLineWidth(2.5);

				double nivelAnterior = BAIXO;

				for (int i = 0; i < transmissao.length; i++)
				{
					double inicio = posicaoInicial + (i * BIT_WIDTH);
					double fim = inicio + BIT_WIDTH;

					if (fim < 0 || inicio > canvas.getWidth())
					{
						nivelAnterior = (transmissao[i] == 1) ? ALTO: BAIXO;
						continue;
					}

					double nivelAtual = (transmissao[i] == 1) ? ALTO: BAIXO;

					if (nivelAtual != nivelAnterior)
					{
						pincel.strokeLine(inicio, nivelAnterior, inicio, nivelAtual);
					}

					pincel.strokeLine(inicio, nivelAtual, fim, nivelAtual);

					nivelAnterior = nivelAtual;
				}

				if (posicaoInicial > canvas.getWidth())
				{
					this.stop();

					pincel.clearRect(0, 0, canvas.getWidth(), ALTURA);

					enviar.setDisable(false);
					paraFrente.setDisable(false);
					paraTras.setDisable(false);
				}
			}
		};

		desenhaOnda.start();

		this.enviar.setDisable(true);
		this.paraFrente.setDisable(true);
		this.paraTras.setDisable(true);
	}

/* ***************************************************************
* Metodo: bitsParaSinais
* Funcao: transforma a string do codigo em bits e joga para um array de 0 e 1
* Parametros: mesma string que esta sendo exibida no app
* Retorno: array de inteiros 1 e 0
*************************************************************** */
	public int[] bitsParaSinais (String textoTransmitido)
	{
		int[] viraSinal = new int[textoTransmitido.length()];

    for (int i = 0; i < textoTransmitido.length(); i++) // Percorre cada caractere da string
		 {
      char bitChar = textoTransmitido.charAt(i);

      int bitInt = Character.getNumericValue(bitChar); // Converte o caractere para inteiro

      viraSinal[i] = bitInt; // Adiciona ao novo array
    }

		return viraSinal;
	}
}
