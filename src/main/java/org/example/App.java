package org.example;

import java.io.*;
import java.util.Objects;
import org.jetbrains.skia.*;
/**
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
        draw("俺の","love马が");
    }

    public static void draw(String left,String right) throws IOException {
        //加载字体文件
        InputStream resourceAsStream = App.class.getClassLoader().getResourceAsStream("RoGSanSrfStd-Bd.otf");
        assert resourceAsStream != null;
        byte[] fontBytes = toByteArray(resourceAsStream);

        //ttcIndex可以切换字体文件中的其他字体
        Font sans = new Font(FontMgr.Companion.getDefault().makeFromData(Data.Companion.makeFromBytes(fontBytes,0,fontBytes.length),0), 70F);
        Font serif = new Font(FontMgr.Companion.getDefault().makeFromData(Data.Companion.makeFromBytes(fontBytes,0,fontBytes.length),0), 70F);


        //打印字体名称
        Typeface typeface = Typeface.Companion.makeFromData(Data.Companion.makeFromBytes(fontBytes, 0, fontBytes.length), 0);
        FontFamilyName[] familyNames = typeface.getFamilyNames();
        for (FontFamilyName familyName : familyNames) {
            System.out.println(familyName);
        }

        //设置字体倾斜度
        sans.setSkewX(-0.74F);
        serif.setSkewX(-0.74F);


        TextLine leftText = TextLine.Companion.make(left, sans);
        TextLine rightText = TextLine.Companion.make(right, serif);

        //宽度
        float width = Objects.requireNonNull(leftText.getTextBlob()).getBlockBounds().getRight() +
                70 + Objects.requireNonNull(rightText.getTextBlob()).getBlockBounds().getRight();

        //创建画布
        Surface surface = Surface.Companion.makeRasterN32Premul(Float.valueOf(width).intValue(), 250);
        Canvas canvas = surface.getCanvas();


        //left
        Paint leftPaint = new Paint();
        leftPaint.setColor(Color.INSTANCE.makeRGB(18, 138, 250));
        leftPaint.setStrokeWidth(22F);
        canvas.drawTextLine(leftText, 25, 150 ,leftPaint);


        //right
        Paint rightPaint = new Paint();
        rightPaint.setColor(Color.INSTANCE.makeRGB(0,0,0));
        rightPaint.setStrokeWidth(22F);
        canvas.drawTextLine(rightText, leftText.getTextBlob().getBlockBounds().getRight() + 50, 150 ,rightPaint);


        //line
        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStrokeWidth(10F);


        Path path = new Path();
        path.moveTo(
                rightText.getTextBlob().getBlockBounds().getLeft() + 320,
                0
        );
        path.lineTo(
                rightText.getTextBlob().getBlockBounds().getLeft() + 335 ,
                0
        );
        path.lineTo(
                rightText.getTextBlob().getBlockBounds().getLeft() + 160 ,
                surface.getHeight()
        );
        path.lineTo(
                rightText.getTextBlob().getBlockBounds().getLeft() + 145 ,
                surface.getHeight()
        );

        canvas.drawPath(path,whitePaint);


        //image

        //图片缩放0.7倍
        canvas.scale(0.5f, 0.5f);
        InputStream haloStream = App.class.getClassLoader().getResourceAsStream("halo.png");
        byte[] bytes = toByteArray(Objects.requireNonNull(haloStream));

        int leftOffset = 100;
        int topOffset = 200;
        canvas.drawImage(Image.Companion.makeFromEncoded(bytes),
                rightText.getTextBlob().getBlockBounds().getLeft() + 250,
                rightText.getTextBlob().getBlockBounds().getTop() + 50);

        canvas.drawImage(Image.Companion.makeFromEncoded(
                        toByteArray(Objects.requireNonNull(App.class.getClassLoader().getResourceAsStream("cross.png")))
                ),
                rightText.getTextBlob().getBlockBounds().getLeft() + 250,
                rightText.getTextBlob().getBlockBounds().getTop() + 50);

        Image image = surface.makeImageSnapshot();
        Data data = image.encodeToData(EncodedImageFormat.PNG, 200);

        drawToImage(data,"ba-logo.png");

    }

    public static boolean drawToImage(Data data,String fileName){
        byte[] bytes = data.getBytes();
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileName);
            outputStream.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;

        while ((read = is.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }

        return baos.toByteArray();
    }
}
