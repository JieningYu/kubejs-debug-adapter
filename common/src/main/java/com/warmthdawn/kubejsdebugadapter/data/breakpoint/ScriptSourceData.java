package com.warmthdawn.kubejsdebugadapter.data.breakpoint;

import com.ibm.icu.impl.Pair;
import com.warmthdawn.kubejsdebugadapter.data.ScriptLocation;
import com.warmthdawn.kubejsdebugadapter.utils.BreakpointUtils;
import com.warmthdawn.kubejsdebugadapter.utils.LocationParser;
import dev.latvian.mods.rhino.Kit;
import dev.latvian.mods.rhino.ast.ScriptNode;

import java.util.ArrayList;
import java.util.List;

public class ScriptSourceData {
    private String id;
    private String sourceString;
    private LocationParser parser;

    private boolean finished = false;
    private List<Pair<ScriptLocation, ScriptLocation>> locationList = null;


    private final List<FunctionSourceData> functions = new ArrayList<>();

    public ScriptSourceData(String sourceId, String sourceString) {
        this.id = sourceId;
        this.sourceString = sourceString;
        this.parser = LocationParser.resolve(sourceId, sourceString);
    }

    public FunctionSourceData addFunction(ScriptNode scriptNode) {
        if (finished) {
            throw Kit.codeBug("Cannot add functions after finished compile");
        }
        int id = functions.size();
        FunctionSourceData functionSourceData = new FunctionSourceData(scriptNode, id);
        functions.add(functionSourceData);
        return functionSourceData;
    }

    public List<FunctionSourceData> getFunctions() {
        return functions;
    }

    public FunctionSourceData getFunction(int functionId) {
        return functions.get(functionId);
    }

    public LocationParser getLocationParser() {
        return parser;
    }

    public void finishCompile() {
        this.finished = true;
        this.locationList = BreakpointUtils.collectBreakpoints(this);
    }

    public String getId() {
        return id;
    }

    public boolean isFinished() {
        return finished;
    }

    public List<Pair<ScriptLocation, ScriptLocation>> getLocationList() {
        return locationList;
    }


    public List<Pair<ScriptLocation, ScriptLocation>> getLocationList(int lineStart, int lineEnd, int columnStart, int columnEnd) {

        int startIndex = -1;
        int endIndex = locationList.size();
        int i = 0;
        for (; i < locationList.size(); i++) {
            ScriptLocation loc = locationList.get(i).first;
            if (loc.getLineNumber() > lineStart || (loc.getLineNumber() == lineStart && loc.getColumnNumber() >= columnStart)) {
                startIndex = i;
                break;
            }

        }
        for (; i < locationList.size(); i++) {
            ScriptLocation loc = locationList.get(i).first;
            if (loc.getLineNumber() > lineEnd || (loc.getLineNumber() == lineEnd && (columnEnd >= 0 && loc.getColumnNumber() > columnEnd))) {
                endIndex = i;
                break;
            }
        }

        return locationList.subList(startIndex, endIndex);

    }
}
