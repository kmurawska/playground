package com.kmurawska.playground.nashornandmustache;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PageContentCreator {
    private static final String MUSTACHE_SCRIPT_PATH = "/js/mustache.js";
    private static final String HTML_TEMPLATE_PATH = "/html/content-template.html";
    private ScriptEngine engine;

    PageContentCreator() {
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
    }

    String create(List<TimeSeries> timeSeries) throws ScriptException, NoSuchMethodException {
        String json = new Gson().toJson(timeSeries);

        return renderData(loadHtmlTemplate(), parseToJsObject(json));
    }

    private String loadHtmlTemplate() {
        InputStream inputStream = this.getClass().getResourceAsStream(HTML_TEMPLATE_PATH);
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining());
    }

    private Object parseToJsObject(String jsonObject) throws ScriptException, NoSuchMethodException {
        Object json = engine.eval("JSON");
        return ((Invocable) (engine)).invokeMethod(json, "parse", jsonObject);
    }

    private String renderData(String template, Object data) throws ScriptException, NoSuchMethodException {
        engine.eval(loadMustacheScript());
        Object mustache = engine.eval("Mustache");
        return ((Invocable) engine).invokeMethod(mustache, "render", template, data).toString();
    }

    private BufferedReader loadMustacheScript() {
        InputStream inputStream = this.getClass().getResourceAsStream(MUSTACHE_SCRIPT_PATH);
        InputStreamReader in = new InputStreamReader(inputStream);
        return new BufferedReader(in);
    }
}
