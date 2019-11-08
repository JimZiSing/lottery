package org.javatribe.lottery.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.QRCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import static org.apache.catalina.manager.Constants.CHARSET;

/**
 * @Author Jimzising
 * @Date 2019/10/21
 * @Desc 生成二维码工具类
 */
public class QRCodeUtils {

    //定义宽和高
    private static int WIDTH = 300;
    private static int HEIGHT = 300;
    /**
     *
     * 把文本转化成二维码图片对象
     * @param text 二维码内容
     * @param width 二维码高度
     * @param height 二位宽度
     * @return BufferedImage 返回类型
     * @throws Exception
     */
    public static BufferedImage toBufferedImage(String text, int width, int height) throws Exception {
        int BLACK = 0xFF000000;
        int WHITE = 0xFFFFFFFF;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        // 内容所使用字符集编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     *
     * 把文本转化成二维码图片对象
     * @param text 二维码内容
     * @return BufferedImage 返回类型
     * @throws Exception
     */
    public static BufferedImage toBufferedImage(String text) throws Exception {
        int black = 0xFF000000;
        int white = 0xFFFFFFFF;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        // 内容所使用字符集编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? black : white);
            }
        }
        return image;
    }

    /**
     * 解析二维码内容
     * @param file 二维码
     * @return 二维码包含的信息
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        HashMap hints = new HashMap<DecodeHintType, Object>(1);
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

}
