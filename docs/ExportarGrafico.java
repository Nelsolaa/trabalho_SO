import gui.GraficoComparativo;
import simulator.ResultadoSimulacao;
import simulator.SimuladorSubstituicao;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ExportarGrafico {
    public static void main(String[] args) throws Exception {
        int[] cadeia = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
        int quadros = 3;

        List<ResultadoSimulacao> resultados =
                new SimuladorSubstituicao().executar(cadeia, quadros);

        int largura = 760;
        int altura = 380;

        GraficoComparativo grafico = new GraficoComparativo();
        grafico.setSize(largura, altura);
        grafico.setResultados(resultados);

        BufferedImage img = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, largura, altura);
        grafico.paint(g2);
        g2.dispose();

        File destino = new File("docs/grafico-comparativo.png");
        destino.getParentFile().mkdirs();
        ImageIO.write(img, "png", destino);
        System.out.println("PNG gerado em: " + destino.getAbsolutePath());
    }
}
