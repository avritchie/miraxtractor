package MRXS;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import org.ini4j.Ini;

/**
 *
 * @author kluopaja
 *
 * Extracts locations of the image tiles in the files. input: the location of
 * .mrxs file channel: index of the extracted jpg (0 or 1)
 *
 *
 * Returns an Arraylist<int[]> in the format of: [[tile_index, offset, length,
 * file_number], ...]
 *
 * tile_index: the location of the tile in the final image offset: the starting
 * position of the tile jpg in the data file length: the length of the jpg
 * file_name: the .dat file in which the jpg is saved to
 */
public class MRXSSlide {

    public final Ini INI;
    public final int zoomLevelCount;
    public final int channelCount;
    private final int tilesXAmount;
    private final int tilesYAmount;
    public final String fileFormat;
    ///
    public final int tileWidth;
    public final int tileHeight;
    public File mrxsFile;
    public String slideName;
    public Path folderPath;

    public MRXSSlide(String path) throws Exception {
        resolveFile(path);
        INI = new Ini(folderPath.resolve("Slidedat.ini").toFile());
        int[] res = getLevelResolution(0);
        tileWidth = res[0];
        tileHeight = res[1];
        int[] tiles = getTiles();
        tilesXAmount = tiles[0];
        tilesYAmount = tiles[1];
        fileFormat = getFileFormat();
        zoomLevelCount = getZoomLevelCount();
        channelCount = getChannelCount();
    }

    private void resolveFile(String path) {
        if (path.toLowerCase().endsWith("ini")) {
            // folder name as an input (use for annotations)
            mrxsFile = null;
            slideName = new File(path).getParentFile().getName();
            folderPath = new File(path).getParentFile().toPath();
        } else if (path.toLowerCase().endsWith("mrxs")) {
            // mirax file as an input (complete slides)
            mrxsFile = new File(path);
            slideName = mrxsFile.getName().replaceFirst("[.][^.]+$", "");
            folderPath = mrxsFile.toPath().getParent().resolve(slideName);
        }
    }

    public LinkedHashMap readOffsets(int zoomLevel, int channel) throws Exception {
        if (zoomLevel >= zoomLevelCount) {
            throw new Exception("Trying to access a zoom level that does not exist");
        }
        //read Index.dat
        ByteBuffer indexBuffer = ByteBuffer.wrap(Files.readAllBytes(folderPath.resolve("Index.dat")));
        indexBuffer.order(ByteOrder.LITTLE_ENDIAN);
        //len(version) + len(UUID) = 5 + 32
        //position of the hierarchical (image-containing) offset table
        int offsetTablePos = indexBuffer.getInt(5 + 32);
        /*        Zoom levels are in order, first highest res at offsetTablePos, second highest level at offsetTablePos+4 etc.
        (multiply positions by 4 because 4-byte integer lol)
        It seems that the subsequent channels are at offsetTablePos+(channel*levels*4) so that
        if there are 10 levels, channel 0 of level 2 would be at offsetTablePos+2, but channel 0 of level 2
        would be at offsetTablePos+10+2 and so on...        */
        int levelChannelPos = indexBuffer.getInt(offsetTablePos + 4 * (zoomLevel + zoomLevelCount * channel));
        LinkedHashMap<Integer, int[]> list = new LinkedHashMap<>();
        //readLinkedList(indexBuffer, levelChannelPos);
        readLinkedList(list, indexBuffer, levelChannelPos, zoomLevel);
        //list.sort((int[] f, int[] n) -> f[0] - n[0]);
        return list;
    }

    public String[] getInfo() {
        String[] info = new String[]{
            "Slide " + slideName + " has " + zoomLevelCount + " zoom levels and " + channelCount + " channels",
            "Total resolution is " + tileWidth * tilesXAmount + "x" + tileHeight * tilesYAmount + " px",
            "There are " + tilesXAmount + "x" + tilesYAmount + " " + fileFormat + " tiles with the resolution of " + tileWidth + "x" + tileHeight + " px"};
        return info;
    }

    public int getZoomLevelCount() {
        return Integer.parseInt(INI.get("HIERARCHICAL", "HIER_0_COUNT"));
    }

    public String getFileFormat() {
        if (INI.get("GENERAL").containsValue("COMPRESSION")) {
            return INI.get("GENERAL", "COMPRESSION");
        } else {
            return INI.get("LAYER_0_LEVEL_0_SECTION", "IMAGE_FORMAT");
        }
    }

    public int[] getLevelResolution(int level) { //width, height
        return new int[]{
            Integer.parseInt(INI.get("LAYER_0_LEVEL_" + level + "_SECTION", "DIGITIZER_WIDTH")),
            Integer.parseInt(INI.get("LAYER_0_LEVEL_" + level + "_SECTION", "DIGITIZER_HEIGHT"))
        };
    }

    public int[] getTiles() { //x,y
        return new int[]{
            Integer.parseInt(INI.get("GENERAL", "IMAGENUMBER_X")),
            Integer.parseInt(INI.get("GENERAL", "IMAGENUMBER_Y"))
        };
    }

    //The linked list has the format
    //[size, next_element, data_1, data_2, data_3, ..., data_(size-1)]
    private void readLinkedList(LinkedHashMap list, ByteBuffer index_file, int pos, int zoom) {
        if (pos == 0) {
            //no more elements
            return;
        }
        //size of the data array
        int size = index_file.getInt(pos);
        //pointer to the next list element
        int next_element = index_file.getInt(pos + 4);
        for (int i = 0; i < size; ++i) {
            int tilePos = pos + 8 + i * 16;
            int[] result = new int[5];
            int tileID = index_file.getInt(tilePos);
            for (int j = 1; j < 4; ++j) {
                result[j - 1] = index_file.getInt(tilePos + j * 4);
            }
            result[3] = (tileID % tilesXAmount) >> zoom; //x
            result[4] = (tileID / tilesXAmount) >> zoom; //y
            list.put(tileID, result);
        }
        readLinkedList(list, index_file, next_element, zoom);
    }

    public int[] getImagePos(LinkedHashMap offsets, int x, int y, int zoom) {
        int index = (y << zoom) * tilesXAmount + (x << zoom);
        return (int[]) offsets.get(index);
    }

    //produces the .dat file name based on the file_number
    //e.g. 5 -> Data0005.dat 
    public static String numberToFileName(int file_number) {
        String result = "Data0000.dat";
        return result.replace("0000", String.format("%04d", file_number));
    }

    public int getChannelCount() throws Exception {
        int channels = 0;
        LinkedHashMap<Integer, int[]> data;
        Integer numb = 0;
        while (true) {
            try {
                data = readOffsets(0, channels);
                numb = data.entrySet().iterator().next().getValue()[2];
                channels++;
            } catch (IOException ex) {
                throw ex;
            } catch (NoSuchElementException ex) {
                //finished
                break;
            }
        }
        return channels;
    }
}
