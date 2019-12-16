package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import models.MixTape;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by hshankar on 12/12/19.
 */
public class PlaylistEditorClientShell {
    private String mixTapeFile;
    private String changeListFile;
    private String outFile;

    private int parseInput(List<String> cmdList, OptionParser parser) throws IOException {
        if (cmdList.size() > 3) {
            System.err.println("Usage: bin/playlistmgr.sh inp changes out");
            parser.printHelpOn(System.err);
            return -1;
        }
        return 0;
    }

    private int applyChanges(MixTape mt) throws IOException {
        PlaylistChangeMgr plMgr = new PlaylistChangeMgr(mt, changeListFile, outFile);
        plMgr.applyChanges();
        return 0;
    }

    private int processClientInp(List<String> inpList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        mixTapeFile = inpList.get(0);
        outFile = inpList.get(2);
        changeListFile = inpList.get(1);

        File file = new File(mixTapeFile);
        MixTape p = objectMapper.readValue(file, MixTape.class);
        applyChanges(p);
        return 0;
    }

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();
        OptionSet options = parser.parse(args);
        List<String> inpList = (List<String>) options.nonOptionArguments();
        PlaylistEditorClientShell clientShell = new PlaylistEditorClientShell();
        if (clientShell.parseInput(inpList, parser) < 0) {
            System.exit(-1);
        }
        clientShell.processClientInp(inpList);
    }
}
