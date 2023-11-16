package MRXS;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author aritchie
 */
public class MRXSLevel {

    private LinkedHashMap<Integer, int[]> levelTileData;
    private HashMap<Point, Integer> indexCoordinateMap;
    public Integer dataFileNumber;
    public File dataFile;
    private int[] tileDimensions;
    private int levelNumber;
    private int channelNumber;
    private MRXSSlide masterSlide;
    private int bgColor;
    private int overlapX;
    private int overlapY;
    private boolean nocropping;

    public MRXSLevel(MRXSSlide mirax, int zoomLevel, int channelId) throws Exception {
        masterSlide = mirax;
        levelNumber = zoomLevel;
        channelNumber = channelId;
        levelTileData = mirax.readOffsets(zoomLevel, channelId);
        indexCoordinateMap = getIndexCoordinateMap();
        dataFileNumber = levelTileData.entrySet().iterator().next().getValue()[2];
        dataFile = mirax.folderPath.resolve(MRXSSlide.numberToFileName(dataFileNumber)).toFile();
        nocropping = true;
        getBasicValuesFromIni();
    }

    public String[] getInfo() {
        Color bg = new Color(bgColor);
        int[] res = getResolution();
        String[] info = new String[]{
            "Level " + levelNumber + " has " + levelTileData.size() + " tiles in a file " + MRXSSlide.numberToFileName(dataFileNumber),
            "The tile area is " + (tileDimensions[1] - tileDimensions[0] + 1) + "x" + (tileDimensions[3] - tileDimensions[2] + 1)
            + " with resolution of " + res[0] + "x" + res[1],
            "The background colour is \"" + (bg.getRed() + "," + bg.getGreen() + "," + bg.getBlue()) + "\" and tile overlap is " + overlapX + "x" + overlapY + " px"};
        // tile number and tile area are not always the same, because empty tiles are excluded, the former might therefore be smaller than the latter
        return info;
    }

    private void printTiles() {
        // print all the tiles
        System.out.println("##############################################");
        System.out.println("ID: tile_index [offset, length, file_number, x, y]");
        System.out.println("##############################################");
        levelTileData.entrySet().forEach(es -> {
            System.out.println("ID: " + es.getKey() + " " + Arrays.toString(es.getValue()));
        });
    }

    public int[] getResolution() {
        return new int[]{
            masterSlide.tileWidth * (tileDimensions[1] - tileDimensions[0] + 1),
            masterSlide.tileHeight * (tileDimensions[3] - tileDimensions[2] + 1)};
    }

    public void setCropping(boolean crop) {
        nocropping = crop;
    }

    private HashMap<Point, Integer> getIndexCoordinateMap() {
        HashMap<Point, Integer> indexes = new HashMap<>();
        levelTileData.entrySet().forEach(es -> {
            indexes.put(new Point(es.getValue()[3], es.getValue()[4]), es.getKey());
        });
        return indexes;
    }

    public ExtractThread extractSingleTiles(int id, String destination) throws Exception {
        ExtractThread worker = new ExtractThread(this, id) {
            @Override
            protected void core() {
                try {
                    // handle folder stuff etc
                    String outFolder = destination;
                    File dir = new File(outFolder + masterSlide.slideName);
                    dir.mkdirs();
                    // extract and save the tiles
                    byte[] sourceDataFile = readDataFile();
                    int[] done = new int[]{0};
                    String ff = masterSlide.fileFormat.toLowerCase();
                    for (Entry<Integer, int[]> es : levelTileData.entrySet()) {
                        try {
                            byte[] firstTile = readASingleTile(sourceDataFile, es.getKey());
                            Files.write(new File(dir.getPath() + "/0" + es.getValue()[4] + "_0" + es.getValue()[3] + "." + ff).toPath(), firstTile);
                            done[0]++;
                            if (done[0] % 10 == 0) {
                                publish(1000 * done[0] / levelTileData.size());
                            }
                        } catch (Exception ex) {
                            exceptionList.add(ex);
                            return;
                        }
                    }
                } catch (Exception ex) {
                    exceptionList.add(ex);
                    return;
                }
            }
        };
        worker.execute();
        return worker;
    }

    public ExtractThread extractMergedTiles(int id, String destination) throws Exception {
        ExtractThread worker = new ExtractThread(this, id) {
            @Override
            protected void core() {
                try {
                    // handle folder stuff etc
                    String outFolder = destination;
                    File dir = new File(outFolder);
                    dir.mkdirs();
                    //
                    byte[] sourceDataFile = Files.readAllBytes(dataFile.toPath());
                    int width = masterSlide.tileWidth - overlapX, height = masterSlide.tileHeight - overlapY;
                    BufferedImage result = new BufferedImage(width * (tileDimensions[1] - tileDimensions[0] + 1), height * (tileDimensions[3] - tileDimensions[2] + 1), BufferedImage.TYPE_INT_RGB);
                    Graphics g = result.getGraphics();
                    g.setColor(new Color(bgColor));
                    g.fillRect(0, 0, result.getWidth(), result.getHeight());
                    int[] newDims = new int[]{tileDimensions[1] - tileDimensions[0] + 1, 0, tileDimensions[3] - tileDimensions[2] + 1, 0};
                    int[] done = new int[]{0};
                    for (Entry<Integer, int[]> es : levelTileData.entrySet()) {
                        try {
                            byte[] firstTile = readASingleTile(sourceDataFile, es.getKey());
                            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(firstTile));
                            if (bi.getRGB(0, 0) == bi.getRGB(width - 1, 0) && bi.getRGB(width - 1, 0) == bi.getRGB(width - 1, height - 1)
                                    && bi.getRGB(width - 1, height - 1) == bi.getRGB(0, height - 1) && bi.getRGB(0, height - 1) == bgColor && !nocropping) {
                                //tile is completely grey
                            } else {
                                newDims[0] = Math.min(newDims[0], es.getValue()[3] - tileDimensions[0]);
                                newDims[1] = Math.max(newDims[1], es.getValue()[3] - tileDimensions[0]);
                                newDims[2] = Math.min(newDims[2], es.getValue()[4] - tileDimensions[2]);
                                newDims[3] = Math.max(newDims[3], es.getValue()[4] - tileDimensions[2]);
                                g.drawImage(bi, width * (es.getValue()[3] - tileDimensions[0]), height * (es.getValue()[4] - tileDimensions[2]), null);
                            }
                            done[0]++;
                            if (done[0] % 10 == 0) {
                                publish(200 * done[0] / levelTileData.size());
                            }
                        } catch (IOException ex) {
                            g.setColor(Color.RED);
                            g.fillRect(width * (es.getValue()[3] - tileDimensions[0]), height * (es.getValue()[4] - tileDimensions[2]), masterSlide.tileWidth, masterSlide.tileHeight);
                            //Some of the tiles are corrupted.
                            exceptionList.add(ex);
                            return;
                        }
                    }

                    int[] lastProg = new int[]{0};

                    ImageOutputStream ios = ImageIO.createImageOutputStream(new File(outFolder + masterSlide.slideName + "_level" + level.levelNumber + "_channel" + level.channelNumber + ".png"));
                    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
                    if (writers.hasNext()) {
                        ImageWriter writer = writers.next();
                        writer.addIIOWriteProgressListener(new IIOWriteProgressListener() {
                            @Override
                            public void imageStarted(ImageWriter source, int imageIndex) {
                            }

                            @Override
                            public void imageProgress(ImageWriter source, float percentageDone) {
                                int newProg = (int) (percentageDone * 5);
                                if (lastProg[0] != newProg) {
                                    publish(200 + ((int) percentageDone) * 8);
                                }
                                lastProg[0] = newProg;
                            }

                            @Override
                            public void imageComplete(ImageWriter source) {
                            }

                            @Override
                            public void thumbnailStarted(ImageWriter source, int imageIndex, int thumbnailIndex) {
                            }

                            @Override
                            public void thumbnailProgress(ImageWriter source, float percentageDone) {
                            }

                            @Override
                            public void thumbnailComplete(ImageWriter source) {
                            }

                            @Override
                            public void writeAborted(ImageWriter source) {
                            }
                        });

                        writer.setOutput(ios);
                        try {
                            writer.write(result.getSubimage(
                                    newDims[0] * width, newDims[2] * height, (newDims[1] - newDims[0] + 1) * width, (newDims[3] - newDims[2] + 1) * height));
                        } catch (Exception ex) {
                            exceptionList.add(ex);
                            return;
                        } finally {
                            writer.removeAllIIOWriteProgressListeners();
                            ios.close();
                        }
                    }
                } catch (Exception ex) {
                    exceptionList.add(ex);
                    return;
                }
            }
        };
        worker.execute();
        return worker;
    }

    public byte[] readDataFile() throws IOException {
        return Files.readAllBytes(dataFile.toPath());
    }

    public byte[] readASingleTile(byte[] fileBytes, int x, int y) {
        return readASingleTile(fileBytes, indexCoordinateMap.get(new Point(x, y)));
    }

    public byte[] readASingleTile(byte[] fileBytes, int tileIndex) {
        int[] tileData = levelTileData.get(tileIndex);
        int offset = tileData[0];
        int length = tileData[1];
        return Arrays.copyOfRange(fileBytes, offset, offset + length);
    }

    public int[] getTileDimensions() {
        int[] dims = new int[]{Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0};
        levelTileData.entrySet().forEach(es -> {
            int[] data = es.getValue();
            dims[0] = Math.min(dims[0], data[3]);
            dims[1] = Math.max(dims[1], data[3]);
            dims[2] = Math.min(dims[2], data[4]);
            dims[3] = Math.max(dims[3], data[4]);
        });
        return dims;
    }

    public int getBgColor() {
        int colorBGR = Integer.parseInt(masterSlide.INI.get("LAYER_0_LEVEL_" + levelNumber + "_SECTION", "IMAGE_FILL_COLOR_BGR"));
        int colorRGB = (colorBGR & 0xFF << 16) | (colorBGR >> 8 & 0xFF << 8) | (colorBGR >> 16 & 0xFF) | 0xFF000000;
        return colorRGB;
    }

    public final void getBasicValuesFromIni() {
        bgColor = getBgColor();
        tileDimensions = getTileDimensions();
        int[] overlaps = getOverLaps();
        overlapX = overlaps[0];
        overlapY = overlaps[1];
    }

    public int[] getOverLaps() {
        return new int[]{(int) Double.parseDouble(masterSlide.INI.get("LAYER_0_LEVEL_" + levelNumber + "_SECTION", "OVERLAP_X")),
            (int) Double.parseDouble(masterSlide.INI.get("LAYER_0_LEVEL_" + levelNumber + "_SECTION", "OVERLAP_Y"))};
    }
}
